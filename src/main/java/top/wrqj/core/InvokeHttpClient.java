package top.wrqj.core;

import top.wrqj.common.annotations.method.HttpServer;
import top.wrqj.client.HttpClient;
import top.wrqj.common.utils.HttpServiceContextHolder;
import top.wrqj.converters.ConverterFactory;
import top.wrqj.converters.HttpConverter;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.core.interceptor.InterceptorExecutionChain;
import top.wrqj.core.interceptor.InterceptorHandler;
import top.wrqj.core.parser.Parser;
import top.wrqj.core.type.ParserParams;
import top.wrqj.core.type.ParserParamsFactory;
import top.wrqj.common.utils.UrlBuilder;
import top.wrqj.exception.HttpClientNullException;
import top.wrqj.model.HttpConfig;
import top.wrqj.model.HttpServiceContext;
import top.wrqj.model.Request;
import top.wrqj.model.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class InvokeHttpClient implements InvocationHandler {

    private final HttpServiceContext httpServiceContext;

    public InvokeHttpClient(HttpServiceContext httpServiceContext) {
        this.httpServiceContext = httpServiceContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!validMethod(method)) {
            return null;
        }
        HttpServiceContextHolder.setContext(httpServiceContext);
        Request request = new Request();
        request.setHeaders(new HashMap<>());
        ParserParamsFactory parserParamsFactory = this.getParserParamsFactory();
        Parser parser = new Parser(parserParamsFactory);
        parser.parser(request, method, args);
        Map<String, String> params = request.getParams();
        if(params != null && !params.isEmpty()) {
            String finalUrl = UrlBuilder.create(request.getUrl()).addQuery(params);
            request.setUrl(finalUrl);
        }
        HttpClient httpClient = httpServiceContext.getHttpClient();
        HttpConfig httpConfig = httpServiceContext.getHttpConfig();
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
        this.after(response);
        HttpServiceContextHolder.remove();
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
        InterceptorRegister interceptorRegister = httpServiceContext.getInterceptorRegister();
        if(interceptorRegister != null) {
            InterceptorExecutionChain chain = interceptorRegister.getInterceptorExecutionChain();
            for (InterceptorHandler interceptorHandler : chain.getInterceptorList()) {
                interceptorHandler.beforeExecution(request);
            }
        }
    }

    private void after(Response response) {
        InterceptorRegister interceptorRegister = httpServiceContext.getInterceptorRegister();
        if(interceptorRegister != null) {
            InterceptorExecutionChain chain = interceptorRegister.getInterceptorExecutionChain();
            for (InterceptorHandler interceptorHandler : chain.getInterceptorList()) {
                interceptorHandler.afterExecution(response);
            }
        }
    }

    private ParserParamsFactory getParserParamsFactory() {
        ParserParamsRegister parserParamsRegister = httpServiceContext.getParserParamsRegister();
        Map<Class<?>, ParserParams> parserParamsMap = parserParamsRegister.getParserParamsMap();
        ParserParamsFactory parserParamsFactory = new ParserParamsFactory();
        parserParamsFactory.addParserParam(parserParamsMap);
        return parserParamsFactory;
    }
}
