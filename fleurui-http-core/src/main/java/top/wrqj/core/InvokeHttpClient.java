package top.wrqj.core;

import top.wrqj.annotations.method.HttpServer;
import top.wrqj.clients.HttpClient;
import top.wrqj.converters.ConverterFactory;
import top.wrqj.converters.HttpConverter;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.core.interceptor.InterceptorExecutionChain;
import top.wrqj.core.interceptor.InterceptorHandler;
import top.wrqj.core.parser.Parser;
import top.wrqj.core.type.ParserParams;
import top.wrqj.core.type.ParserParamsFactory;
import top.wrqj.core.utils.UrlBuilder;
import top.wrqj.exception.ConverterNotFoundException;
import top.wrqj.exception.HttpClientNullException;
import top.wrqj.model.HttpConfig;
import top.wrqj.model.Request;
import top.wrqj.model.Response;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InvokeHttpClient implements InvocationHandler {

    private final HttpClient httpClient;

    private final InterceptorRegister interceptorRegister;

    private final ParserParamsRegister parserParamsRegister;

    private final HttpConfig httpConfig;

    public InvokeHttpClient(HttpClient httpClient, InterceptorRegister interceptorRegister, ParserParamsRegister parserParamsRegister,HttpConfig httpConfig) {
        this.httpClient = httpClient;
        this.interceptorRegister = interceptorRegister;
        this.parserParamsRegister = parserParamsRegister;
        this.httpConfig = httpConfig;
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
        parser.methodParser(request,method,args);
        parser.parameterParser(request,method,args);
        Map<String, String> params = request.getParams();
        if(params != null && !params.isEmpty()) {
            String finalUrl = UrlBuilder.create(request.getUrl()).addQuery(params);
            request.setUrl(finalUrl);
        }
        if(httpClient == null) {
            throw new HttpClientNullException();
        }
        httpClient.configure(httpConfig);
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
