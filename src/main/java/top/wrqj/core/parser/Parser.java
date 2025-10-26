package top.wrqj.core.parser;

import top.wrqj.common.annotations.method.HttpServer;
import top.wrqj.common.annotations.request.*;
import top.wrqj.common.utils.HttpServiceContextHolder;
import top.wrqj.converters.HttpConverter;
import top.wrqj.core.type.ParserParams;
import top.wrqj.core.type.ParserParamsFactory;
import top.wrqj.common.utils.UrlBuilder;
import top.wrqj.common.utils.UrlTemplateUtils;
import top.wrqj.core.utils.InvokeClassCache;
import top.wrqj.exception.HeaderException;
import top.wrqj.model.HttpServerMeta;
import top.wrqj.model.HttpServiceContext;
import top.wrqj.model.Request;
import top.wrqj.model.RequestContext;
import top.wrqj.plugins.annotation.AnnotationHandler;
import top.wrqj.plugins.annotation.AnnotationHandlerRegister;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Parser {

    private final InvokeClassCache classCache = new InvokeClassCache();
    private final Map<Class<?>, Method> VALUE_METHOD_CACHE = new ConcurrentHashMap<>();

    public void parser(Request request, Method method, Object[] args) {
        this.classParser(request, method);
        this.methodParser(request, method, args);
        this.parameterParser(request, method, args);
    }

    public void classParser(Request request, Method method) {
        Class<?> clazz = method.getDeclaringClass();
        Request requestCache = classCache.get(clazz);
        if (requestCache == null) {
            Annotation[] annotations = clazz.getAnnotations();
            HttpServiceContext context = HttpServiceContextHolder.getContext();
            AnnotationHandlerRegister annotationHandlerRegister = context.getAnnotationHandlerRegister();
            for (Annotation annotation : annotations) {
                AnnotationHandler annotationHandler = annotationHandlerRegister.getAnnotationHandler(annotation);
                annotationHandler.process(new RequestContext(request, method, clazz, null), annotation);
            }
            classCache.put(clazz, request);
        } else {
            request.setUrl(requestCache.getUrl());
            request.setHeaders(requestCache.getHeaders());
        }
    }

    public void methodParser(Request request, Method method, Object[] args) {
        HttpServiceContext context = HttpServiceContextHolder.getContext();
        AnnotationHandlerRegister annotationHandlerRegister = context.getAnnotationHandlerRegister();
        for (Annotation annotation : method.getAnnotations()) {
            AnnotationHandler annotationHandler = annotationHandlerRegister.getAnnotationHandler(annotation);
            if (annotationHandler == null) {
                continue;
            }
            annotationHandler.process(new RequestContext(request, method, null, null), annotation);
        }
        this.parsePathParamMeta(request, method, args);
    }

    private void parsePathParamMeta(Request request, Method method, Object[] values) {
        Parameter[] parameters = method.getParameters();
        HttpServiceContext context = HttpServiceContextHolder.getContext();
        AnnotationHandlerRegister annotationHandlerRegister = context.getAnnotationHandlerRegister();
        for (int i = 0; i < parameters.length; i++) {
            Object value = values[i];
            Parameter parameter = parameters[i];
            Annotation[] annotations = parameter.getAnnotations();
            for (Annotation annotation : annotations) {
                AnnotationHandler annotationHandler = annotationHandlerRegister.getAnnotationHandler(annotation);
                if (annotationHandler == null) {
                    continue;
                }
                annotationHandler.process(new RequestContext(request, method, null, value), annotation);
            }
        }
    }

    public void parameterParser(Request request, Method method, Object[] args) {
        Map<String, String> headers = request.getHeaders();
        String contentType = headers.get("Content-Type");
        Parameter[] parameters = method.getParameters();
        if (args == null || args.length < 1) {
            return;
        }
        Map<String, String> paramsMap = new HashMap<>(args.length);
        HttpServiceContext context = HttpServiceContextHolder.getContext();
        AnnotationHandlerRegister annotationHandlerRegister = context.getAnnotationHandlerRegister();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Parameter parameter = parameters[i];
            for (Annotation annotation : parameter.getAnnotations()) {
                AnnotationHandler handler = annotationHandlerRegister.getAnnotationHandler(annotation);
                handler.process(new RequestContext(request, method, null, arg), annotation);
            }
            Params params = parameter.getAnnotation(Params.class);
            ParserParamsFactory parserParamsFactory = context.getParserParamsFactory();
            ParserParams parserParams = parserParamsFactory.getParserParams(arg.getClass());
            if (parserParams == null) {
                throw new RuntimeException("不存在当前适配类型解析器");
            }
            if (params != null) {
                String value = params.value();
                if (value == null || value.isEmpty()) {
                    value = arg.getClass().getName();
                }
                paramsMap.putAll(parserParams.parseParamType(value, arg));
            }
            Header header = parameter.getAnnotation(Header.class);
            if (header != null) {
                String[] value = header.value();
                String key = parameter.getName();
                if (value != null && value.length > 0) {
                    key = value[0];
                }
                headers.putAll(parserParams.parseParamType(key, arg));
            }
        }
        request.setParams(paramsMap);
    }

    private byte[] handleBodyParam(String contentType, Object arg) {
        HttpServiceContext context = HttpServiceContextHolder.getContext();
        HttpConverter converter = context.getAbstractConverterFactory().getConverter(contentType);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        converter.write(arg, output);
        return output.toByteArray();
    }

    public static void parserHeaders(Map<String, String> headers, String[] value) {
        for (String item : value) {
            String[] split = item.split(":");
            if (split.length != 2) {
                throw new HeaderException("header解析失败，不支持格式：" + item, item);
            }
            headers.put(split[0].trim(), split[1].trim());
        }
    }

}
