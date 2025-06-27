package com.fleurui;

import com.fleurui.builder.HttpServiceBuilder;

public class Main {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        HttpServiceBuilder builder = HttpServiceBuilder.builder();
        HttpDemo httpDemo = builder.build(HttpDemo.class);
        RandomArticle.Result random = httpDemo.getRandom();
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(random);
        Object data = random.getData();
        System.out.println(data);
    }
}