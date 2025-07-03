package top.wrqj.core;

import top.wrqj.annotations.method.GET;
import top.wrqj.annotations.request.Header;
import top.wrqj.annotations.request.Http;

@Http("https://www.api.wrqj.top/blog/web/article/1889267805298688")
public interface SendRequest {

    @GET
    @Header("Authorization:{authorization}")
    String request(@Header("Authorization") String authorization);
}
