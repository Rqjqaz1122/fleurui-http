package top.wrqj.core.parser;

import top.wrqj.common.annotations.method.HttpServer;
import top.wrqj.common.annotations.request.*;
import top.wrqj.common.utils.HttpServiceContextHolder;
import top.wrqj.converters.ConverterFactory;
import top.wrqj.converters.HttpConverter;
import top.wrqj.core.type.ParserParams;
import top.wrqj.core.type.ParserParamsFactory;
import top.wrqj.common.utils.UrlBuilder;
import top.wrqj.common.utils.UrlTemplateUtils;
import top.wrqj.exception.HeaderException;
import top.wrqj.model.HttpServerMeta;
import top.wrqj.model.HttpServiceContext;
import top.wrqj.model.Request;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Parser {

    private final Map<Class<?>, Request> CLASS_CACHE = new ConcurrentHashMap<>();
    private final Map<Class<?>, Method> VALUE_METHOD_CACHE = new ConcurrentHashMap<>();

    public Parser() {}

    public void parser(Request request, Method method, Object[] args) {
        this.classParser(request, method);
        this.methodParser(request, method, args);
        this.parameterParser(request, method, args);
    }

    public void classParser(Request request, Method method) {
        Class<?> clazz = method.getDeclaringClass();
        Request requestCache = CLASS_CACHE.get(clazz);
        if (requestCache == null) {
            Http http = clazz.getAnnotation(Http.class);
            String baseUrl = http.value();
            request.setUrl(baseUrl);
            Map<String, String> headers = request.getHeaders();
            String[] defaultHeader = http.defaultHeader();
            this.parserHeaders(headers, defaultHeader);
            CLASS_CACHE.put(clazz, request);
        } else {
            request.setUrl(requestCache.getUrl());
            request.setHeaders(requestCache.getHeaders());
        }
    }

    public void methodParser(Request request, Method method, Object[] args) {
        HttpServerMeta httpServerMeta = this.parseHttpRequestMethod(method);
        if (httpServerMeta == null) {
            return;
        }
        Map<String, String> pathParam = this.parsePathParamMeta(method.getParameters(), args);
        String url = this.buildUrl(request.getUrl(), httpServerMeta.getTemplateUrl(), pathParam);
        request.setUrl(url);
        request.setMethod(String.valueOf(httpServerMeta.getHttpMethod()));
        Header header = method.getAnnotation(Header.class);
        if (header != null) {
            this.parserHeaders(request.getHeaders(), header.value());
        }
    }

    private HttpServerMeta parseHttpRequestMethod(Method method) {
        HttpServer httpServer = method.getAnnotation(HttpServer.class);
        if (httpServer != null) {
            return new HttpServerMeta(httpServer.method(), httpServer.value());
        }
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(HttpServer.class)) {
                HttpServer parentAnno = annotation.annotationType().getAnnotation(HttpServer.class);
                return new HttpServerMeta(parentAnno.method(), getAnnoValue(annotation));
            }
        }
        return null;
    }

    private Map<String, String> parsePathParamMeta(Parameter[] parameters, Object[] values) {
        Map<String, String> map = new HashMap<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            Object value = values[i];
            PathParam pathParam = parameters[i].getAnnotation(PathParam.class);
            if (pathParam == null) {
                continue;
            }
            String pathValue = pathParam.value();
            map.put(pathValue, value.toString());
        }
        return map;
    }

    private String getAnnoValue(Annotation anno) {
        Method valueMethod = VALUE_METHOD_CACHE.computeIfAbsent(anno.annotationType(), t -> {
            try {
                return t.getMethod("value");
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            return String.valueOf(valueMethod.invoke(anno));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    private String buildUrl(String baseUrl, String pathTemplate, Map<String, String> params) {
        if (pathTemplate == null || pathTemplate.isEmpty()) return baseUrl;
        if (params.isEmpty()) {
            return UrlBuilder.create(baseUrl).addPath(pathTemplate).toString();
        }
        String matched = UrlTemplateUtils.ofTemplate(pathTemplate).matching(params);
        return UrlBuilder.create(baseUrl).addPath(matched).toString();
    }

    public void parameterParser(Request request, Method method, Object[] args) {
        Map<String, String> headers = request.getHeaders();
        String contentType = headers.get("Content-Type");
        Parameter[] parameters = method.getParameters();
        if (args == null || args.length < 1) {
            return;
        }
        Map<String, String> paramsMap = new HashMap<>(args.length);
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Parameter parameter = parameters[i];
            Body body = parameter.getAnnotation(Body.class);
            if (body != null && contentType != null && !contentType.isEmpty()) {
                byte[] bytes = this.handleBodyParam(contentType, arg);
                request.setBody(bytes);
            }
            Params params = parameter.getAnnotation(Params.class);
            HttpServiceContext context = HttpServiceContextHolder.getContext();
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

    private void parserHeaders(Map<String, String> headers, String[] value) {
        for (String item : value) {
            String[] split = item.split(":");
            if (split.length != 2) {
                throw new HeaderException("header解析失败，不支持格式：" + item, item);
            }
            headers.put(split[0].trim(), split[1].trim());
        }
    }

}
