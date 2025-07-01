package top.wrqj.core.builder;

import top.wrqj.clients.HttpClient;
import top.wrqj.core.InvokeHttpClient;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;

import java.lang.reflect.Proxy;

class HttpServiceProxy {

    private final HttpClient httpClient;

    private final InterceptorRegister interceptorRegister;

    private final ParserParamsRegister parserParamsRegister;


    public HttpServiceProxy(HttpClient httpClient, InterceptorRegister interceptorRegister, ParserParamsRegister parserParamsRegister) {
        this.httpClient = httpClient;
        this.interceptorRegister = interceptorRegister;
        this.parserParamsRegister = parserParamsRegister;
    }


    @SuppressWarnings("unchecked")
    protected <T> T createProxy(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new InvokeHttpClient(httpClient,interceptorRegister,parserParamsRegister)
        );
    }

}
