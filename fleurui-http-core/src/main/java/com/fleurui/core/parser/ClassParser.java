package com.fleurui.core.parser;

import com.fleurui.annotations.request.Http;
import com.fleurui.exception.HeaderException;
import com.fleurui.model.Request;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 类注解解析器
 */
public class ClassParser implements Parser{

    @Override
    public void parser(Request request, Method method,Object[] args) {
        Class<?> clazz = method.getDeclaringClass();
        Http http = clazz.getAnnotation(Http.class);
        String baseUrl = http.value();
        request.setUrl(baseUrl);
        Map<String, String> headers = request.getHeaders();
        String[] defaultHeader = http.defaultHeader();
        for (String header : defaultHeader) {
            String[] split = header.split(":");
            if(split.length != 2) {
                throw new HeaderException("header解析失败，不支持格式：" + header);
            }
            headers.put(split[0].trim(),split[1].trim());
        }
    }

}
