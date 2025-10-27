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

    public void parser(Request request, Method method, Object[] args) {
        this.classParser(request, method);
        this.methodParser(request, method, args);
        this.parameterParser(request, method, args);
        String uri = request.getUri();
        String baseUrl = UrlBuilder.create(request.getUrl())
                .addPath(uri)
                .toString();
        request.setUrl(baseUrl);
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
    }

    public void parameterParser(Request request, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        if (args == null || args.length < 1) {
            return;
        }
        HttpServiceContext context = HttpServiceContextHolder.getContext();
        AnnotationHandlerRegister annotationHandlerRegister = context.getAnnotationHandlerRegister();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Parameter parameter = parameters[i];
            for (Annotation annotation : parameter.getAnnotations()) {
                AnnotationHandler handler = annotationHandlerRegister.getAnnotationHandler(annotation);
                if (handler == null) {
                    return;
                }
                handler.process(new RequestContext(request, method, null, arg), annotation);
            }
        }
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
