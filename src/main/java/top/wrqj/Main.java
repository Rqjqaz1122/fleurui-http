package top.wrqj;

import top.wrqj.common.annotations.method.Get;
import top.wrqj.common.annotations.method.Post;
import top.wrqj.common.annotations.request.Http;
import top.wrqj.common.annotations.request.PathParam;
import top.wrqj.core.builder.HttpServiceBuilder;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Demo build = HttpServiceBuilder.create()
                .build(Demo.class);
        long startTime = System.currentTimeMillis();
        String demo = build.getDemo(1901674407231488L);
        long endTime = System.currentTimeMillis();
        System.out.println(demo);
        System.out.println(endTime - startTime);
        Thread.sleep(5000L);
        long startTime1 = System.currentTimeMillis();
        String demo1 = build.getDemo(1901674407231488L);
        long endTime1 = System.currentTimeMillis();
        System.out.println(demo1);
        System.out.println(endTime1 - startTime1);

    }

    @Http("https://www.api.wrqj.top/blog/web/article")
    interface Demo {
        @Get("{id}")
        String getDemo(@PathParam("id") Long id);
    }

}
