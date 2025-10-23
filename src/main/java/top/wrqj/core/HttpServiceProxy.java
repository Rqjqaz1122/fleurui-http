package top.wrqj.core;

import top.wrqj.client.HttpClient;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.model.HttpConfig;
import top.wrqj.model.HttpServiceContext;

import java.lang.reflect.Proxy;

public class HttpServiceProxy {

    private HttpServiceContext httpServiceContext;


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
