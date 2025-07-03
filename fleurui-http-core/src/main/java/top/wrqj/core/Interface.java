package top.wrqj.core;

import top.wrqj.core.interceptor.InterceptorHandler;
import top.wrqj.model.Request;
import top.wrqj.model.Response;

public class Interface implements InterceptorHandler {
    @Override
    public void beforeExecution(Request request) {
        System.out.println(request);
    }

    @Override
    public void afterExecution(Response response) {
        System.out.println(response);
    }
}
