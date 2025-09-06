package top.wrqj.client;


import top.wrqj.model.HttpConfig;
import top.wrqj.model.Request;
import top.wrqj.model.Response;

import java.io.IOException;

public interface HttpClient {

    /**
     * 执行请求
     * @param request
     * @return
     */
    Response execute(Request request) throws IOException;

    void configure(HttpConfig httpConfig);
}