package top.wrqj.core.interceptor;


import top.wrqj.model.Request;
import top.wrqj.model.Response;

public interface InterceptorHandler {

    void beforeExecution(Request request);

    void afterExecution(Response response);
}
