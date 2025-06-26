package com.fleurui;

import com.fleurui.annotations.method.GET;
import com.fleurui.annotations.request.Http;

import java.util.List;

@Http(value = "https://www.api.wrqj.top/blog/web/article/random")
public interface HttpDemo {

    @GET
    RandomArticle.Result getRandom();


}
