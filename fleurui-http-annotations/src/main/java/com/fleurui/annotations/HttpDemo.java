package com.fleurui.annotations;

import com.fleurui.annotations.method.GET;
import com.fleurui.annotations.request.Http;
import com.fleurui.annotations.request.Params;

@Http("https://www.api.wrqj.top/blog/web/article/random")
public interface HttpDemo {

    @GET
    String sendRequest(@Params("id") Long id);

}
