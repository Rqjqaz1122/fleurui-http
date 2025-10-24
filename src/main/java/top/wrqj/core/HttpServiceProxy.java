package top.wrqj.core;

import top.wrqj.model.HttpServiceContext;

import java.lang.reflect.Proxy;

public class HttpServiceProxy {

    private final HttpServiceContext httpServiceContext;


    public HttpServiceProxy(HttpServiceContext httpServiceContext) {
        this.httpServiceContext = httpServiceContext;
    }

    @SuppressWarnings("unchecked")
    public  <T> T createProxy(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new InvokeHttpClient(this.httpServiceContext)
        );
    }

}
