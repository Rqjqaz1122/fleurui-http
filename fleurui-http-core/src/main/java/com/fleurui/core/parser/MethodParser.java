package com.fleurui.core.parser;

import com.fleurui.annotations.method.HttpServer;
import com.fleurui.annotations.request.Header;
import com.fleurui.core.utils.UrlBuilder;
import com.fleurui.exception.HeaderException;
import com.fleurui.model.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MethodParser implements Parser{

    @Override
    public void parser(Request request, Method method,Object[] args) {
        HttpServer httpServer = method.getAnnotation(HttpServer.class);
        if(httpServer == null) {
            for (Annotation annotation : method.getAnnotations()) {
                if(annotation.annotationType().isAnnotationPresent(HttpServer.class)) {
                    httpServer = annotation.annotationType().getAnnotation(HttpServer.class);
                    request.setMethod(httpServer.value());
                    Method valueMethod = null;
                    try {
                        valueMethod = annotation.annotationType().getMethod("value");
                        String value = (String)valueMethod.invoke(annotation);
                        String url = UrlBuilder.create(request.getUrl())
                                .addPath(value).toString();
                        request.setUrl(url);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
        //如果为标识注解，默认GET请求
        if(httpServer == null) {
            request.setMethod("GET");
        }else {
            request.setMethod(httpServer.value());
        }
        Header header = method.getAnnotation(Header.class);
        if(header != null) {
            Map<String, String> headers = request.getHeaders();
            String[] value = header.value();
            for (String item : value) {
                String[] split = item.split(":");
                if(split.length != 2) {
                    throw new HeaderException("header解析失败，不支持格式：" + item,item);
                }
                headers.put(split[0],split[1]);
            }
        }
    }

}
