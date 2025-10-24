package top.wrqj.common.utils;

import top.wrqj.model.HttpServiceContext;

public class HttpServiceContextHolder {

    private final static ThreadLocal<HttpServiceContext> ctx = new ThreadLocal<>();

    public static void setContext(HttpServiceContext context) {
        ctx.set(context);
    }

    public static HttpServiceContext getContext() {
        return ctx.get();
    }

    public static void remove() {
        ctx.remove();
    }

}
