package com.fleurui.core.builder;

import com.fleurui.clients.HttpClient;
import com.fleurui.core.InvokeHttpClient;

import java.lang.reflect.Proxy;

class HttpServiceProxy {

    private final HttpClient httpClient;

    public HttpServiceProxy(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @SuppressWarnings("unchecked")
    protected <T> T createProxy(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new InvokeHttpClient(httpClient)
        );
    }

}
