package top.wrqj.core;

import top.wrqj.annotations.method.GET;
import top.wrqj.annotations.request.Header;
import top.wrqj.annotations.request.Http;
import top.wrqj.annotations.request.PathParam;

@Http("https://www.api.wrqj.top/blog/web")
public interface SendRequest {

    @GET("/article/{id}")
    @Header("Content-Type:application;json")
    String request(@PathParam("id") Long id, @Header("Authorization") String authorization);
}
