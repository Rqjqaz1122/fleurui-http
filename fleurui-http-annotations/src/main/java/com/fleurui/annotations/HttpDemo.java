package com.fleurui.annotations;

import com.fleurui.annotations.method.GET;
import com.fleurui.annotations.request.Body;
import com.fleurui.annotations.request.Http;
import com.fleurui.annotations.request.Params;

@Http(value = "https://www.api.wrqj.top/blog/web/article/random",defaultHeader = {"Content-Type:application/json"})
public interface HttpDemo {

    @GET
    String sendRequest(@Body @Params("id") Long id);

}
