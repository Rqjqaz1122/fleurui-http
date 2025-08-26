package top.wrqj.core.builder;

import top.wrqj.client.HttpClient;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.core.InvokeHttpClient;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.model.HttpConfig;

import java.lang.reflect.Proxy;

class HttpServiceProxy {

    private final HttpClient httpClient;

    private final InterceptorRegister interceptorRegister;

    private final ParserParamsRegister parserParamsRegister;

    private final HttpConfig httpConfig;


    public HttpServiceProxy(HttpClient httpClient, InterceptorRegister interceptorRegister, ParserParamsRegister parserParamsRegister,HttpConfig config) {
        this.httpClient = httpClient;
        this.interceptorRegister = interceptorRegister;
        this.parserParamsRegister = parserParamsRegister;
        this.httpConfig = config;
    }


    @SuppressWarnings("unchecked")
    protected <T> T createProxy(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new InvokeHttpClient(httpClient,interceptorRegister,parserParamsRegister,httpConfig)
        );
    }

}
