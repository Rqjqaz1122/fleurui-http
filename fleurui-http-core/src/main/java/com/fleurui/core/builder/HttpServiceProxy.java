package com.fleurui.core.builder;

import com.fleurui.clients.HttpClient;
import com.fleurui.core.InvokeHttpClient;
import com.fleurui.core.InterceptorRegister;
import com.fleurui.core.builder.register.ParserParamsRegister;

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
