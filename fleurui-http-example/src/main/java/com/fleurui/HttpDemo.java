package com.fleurui;

import com.fleurui.annotations.method.GET;
import com.fleurui.annotations.request.Http;

import java.util.List;

@Http(value = "http://www.baidu.com")
public interface HttpDemo {

    @GET
    String getRandom();

}
