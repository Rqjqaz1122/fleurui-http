package com.fleurui.core.builder;

import com.fleurui.clients.HttpClient;
import com.fleurui.core.InvokeHttpClient;
import com.fleurui.core.InterceptorRegister;

import java.lang.reflect.Proxy;

class HttpServiceProxy {

    private final HttpClient httpClient;

    private InterceptorRegister interceptorRegister;

    public HttpServiceProxy(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpServiceProxy(HttpClient httpClient,InterceptorRegister interceptorRegister) {
        this.httpClient = httpClient;
        this.interceptorRegister = interceptorRegister;
    }


    @SuppressWarnings("unchecked")
    protected <T> T createProxy(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new InvokeHttpClient(httpClient,interceptorRegister)
        );
    }

}
