package com.fleurui.annotations;

import com.fleurui.annotations.method.GET;
import com.fleurui.annotations.request.Body;
import com.fleurui.annotations.request.Http;
import com.fleurui.annotations.request.Params;

@Http(value = "https://www.baidu.com",defaultHeader = {"Content-Type:application/json"})
public interface HttpDemo {

    @GET
    String sendRequest(@Params("id") Long id,@Params Demo demo);

}
