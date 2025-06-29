package com.fleurui.core;

import com.fleurui.annotations.method.GET;
import com.fleurui.annotations.request.Http;

@Http("https://www.baidu.com")
public interface HttpRequest {

    @GET
    String sendRequest();

}
