package com.fleurui.core.parser;

import com.fleurui.annotations.request.Body;
import com.fleurui.model.Request;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 参数解析器
 */
public class ParameterParser implements Parser{

    @Override
    public void parser(Request request, Method method,Object[] args) {
        Map<String, String> headers = request.getHeaders();
        String contentType = headers.get("Content-Type");
        for (Object arg : args) {
            Class<?> aClass = arg.getClass();
            Body body = aClass.getAnnotation(Body.class);
            if(body != null && contentType != null && !contentType.isEmpty()) {
                System.out.println(arg);
            }
        }
    }

}
