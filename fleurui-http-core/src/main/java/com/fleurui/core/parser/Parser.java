package com.fleurui.core.parser;

import com.fleurui.annotations.method.HttpServer;
import com.fleurui.annotations.request.Body;
import com.fleurui.annotations.request.Header;
import com.fleurui.annotations.request.Http;
import com.fleurui.annotations.request.Params;
import com.fleurui.converters.ConverterFactory;
import com.fleurui.converters.HttpConverter;
import com.fleurui.core.type.ParserParams;
import com.fleurui.core.type.ParserParamsFactory;
import com.fleurui.core.utils.UrlBuilder;
import com.fleurui.exception.HeaderException;
import com.fleurui.model.Request;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private final ParserParamsFactory parserParamsFactory;

    public Parser(ParserParamsFactory parserParamsFactory) {
        this.parserParamsFactory = parserParamsFactory;
    }

    public void classParser(Request request, Method method, Object[] args) {
        Class<?> clazz = method.getDeclaringClass();
        Http http = clazz.getAnnotation(Http.class);
        String baseUrl = http.value();
        request.setUrl(baseUrl);
        Map<String, String> headers = request.getHeaders();
        String[] defaultHeader = http.defaultHeader();
        for (String header : defaultHeader) {
            String[] split = header.split(":");
            if(split.length != 2) {
                throw new HeaderException("header解析失败，不支持格式：" + header,header);
            }
            headers.put(split[0].trim(),split[1].trim());
        }
    }

    public void methodParser(Request request, Method method) {
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
        //如果未标识注解，默认GET请求
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

    public void parameterParser(Request request, Method method,Object[] args) {
        Map<String, String> headers = request.getHeaders();
        String contentType = headers.get("Content-Type");
        Parameter[] parameters = method.getParameters();
        if(args == null || args.length < 1) {
            return;
        }
        Map<String,String> paramsMap = new HashMap<>(args.length);
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Parameter parameter = parameters[i];
            Body body = parameter.getAnnotation(Body.class);
            if(body != null && contentType != null && !contentType.isEmpty()) {
                HttpConverter converter = ConverterFactory.getConverter(contentType);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                converter.write(arg,output);
                byte[] byteArray = output.toByteArray();
                request.setBody(byteArray);
            }
            Params params = parameter.getAnnotation(Params.class);
            if(params != null) {
                ParserParams parserParams = parserParamsFactory.getParserParams(arg.getClass());
                if(parserParams == null) {
                    throw new RuntimeException("不存在当前适配类型解析器");
                }
                String value = params.value();
                if(value == null || value.isEmpty()) {
                    value = arg.getClass().getName();
                }
                Map<String, String> map = parserParams.parseParamType(value,arg);
                paramsMap.putAll(map);
            }
        }
        request.setParams(paramsMap);
    }

}
