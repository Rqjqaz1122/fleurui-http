package top.wrqj.core;

import top.wrqj.core.builder.HttpServiceBuilder;

public class Main {
        public static void main(String[] args) {
        SendRequest build = HttpServiceBuilder.builder()
                .setInterceptorRegister(new InterceptorRegister().addInterceptor(new Interface()))
                .build(SendRequest.class);
        System.out.println(build.request(1L,"asdjaidadsa"));
    }
}
