package com.fleurui.clients;

import com.fleurui.model.Request;
import com.fleurui.model.Response;

import java.io.IOException;

public interface HttpClient {

    /**
     * 执行请求
     * @param request
     * @return
     */
    Response execute(Request request) throws IOException;
}
