package com.fleurui.core.interceptor;

import com.fleurui.model.Request;
import com.fleurui.model.Response;

public interface InterceptorHandler {

    void beforeExecution(Request request);

    void afterExecution(Response response);
}
