package com.fleurui.core;

import com.fleurui.annotations.method.HttpServer;
import com.fleurui.annotations.request.Http;
import com.fleurui.converters.ConverterFactory;
import com.fleurui.converters.HttpConverter;
import com.fleurui.clients.HttpClient;
import com.fleurui.core.builder.register.ParserParamsRegister;
import com.fleurui.core.interceptor.InterceptorExecutionChain;
import com.fleurui.core.interceptor.InterceptorHandler;
import com.fleurui.core.parser.*;
import com.fleurui.core.type.ArrayParserAdapter;
import com.fleurui.core.type.ParserParams;
import com.fleurui.core.type.ParserParamsFactory;
import com.fleurui.core.utils.UrlBuilder;
import com.fleurui.exception.ConverterNotFoundException;
import com.fleurui.exception.HeaderException;
import com.fleurui.exception.HttpClientNullException;
import com.fleurui.model.Request;
import com.fleurui.model.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvokeHttpClient implements InvocationHandler {

    private final HttpClient httpClient;

    private final InterceptorRegister interceptorRegister;

    private final ParserParamsRegister parserParamsRegister;

    public InvokeHttpClient(HttpClient httpClient, InterceptorRegister interceptorRegister, ParserParamsRegister parserParamsRegister) {
        this.httpClient = httpClient;
        this.interceptorRegister = interceptorRegister;
        this.parserParamsRegister = parserParamsRegister;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!validMethod(method)) {
            return null;
        }
        Request request = new Request();
        request.setHeaders(new HashMap<>());
        ParserParamsFactory parserParamsFactory = this.getParserParamsFactory();
        Parser parser = new Parser(parserParamsFactory);
        parser.classParser(request,method,args);
        parser.methodParser(request,method);
        parser.parameterParser(request,method,args);
        Map<String, String> params = request.getParams();
        if(params != null && !params.isEmpty()) {
            String finalUrl = UrlBuilder.create(request.getUrl()).addQuery(params);
            request.setUrl(finalUrl);
        }
        if(httpClient == null) {
            throw new HttpClientNullException();
        }
        this.before(request);
        Response response = httpClient.execute(request);
        byte[] body = response.getBody();
        Map<String, String> headers = response.getHeaders();
        String contentType = headers.get("Content-Type");
        HttpConverter converter = ConverterFactory.getConverter(contentType);
        if(converter == null) {
            throw new ConverterNotFoundException("找不到适配：" + contentType + "类型转换器");
        }
        this.after(response);
        return converter.read(body, method.getReturnType());
    }

    private boolean validMethod(Method method) {
        HttpServer httpServer = method.getAnnotation(HttpServer.class);
        if(httpServer == null) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                boolean isPresent = annotation.annotationType().isAnnotationPresent(HttpServer.class);
                if(isPresent) {
                    return true;
                }
            }
        }else {
            return true;
        }
        return false;
    }

    private void before(Request request) {
        if(this.interceptorRegister != null) {
            InterceptorExecutionChain chain = this.interceptorRegister.getInterceptorExecutionChain();
            for (InterceptorHandler interceptorHandler : chain.getInterceptorList()) {
                interceptorHandler.beforeExecution(request);
            }
        }
    }

    private void after(Response response) {
        if(this.interceptorRegister != null) {
            InterceptorExecutionChain chain = this.interceptorRegister.getInterceptorExecutionChain();
            for (InterceptorHandler interceptorHandler : chain.getInterceptorList()) {
                interceptorHandler.afterExecution(response);
            }
        }
    }

    private ParserParamsFactory getParserParamsFactory() {
        Map<Class<?>, ParserParams> parserParamsMap = this.parserParamsRegister.getParserParamsMap();
        ParserParamsFactory parserParamsFactory = new ParserParamsFactory();
        parserParamsFactory.addParserParam(parserParamsMap);
        return parserParamsFactory;
    }
}
