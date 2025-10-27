package top.wrqj.core.parser;

import top.wrqj.common.annotations.method.HttpServer;
import top.wrqj.common.enums.AnnotationScope;
import top.wrqj.model.HttpServerMeta;
import top.wrqj.model.RequestContext;
import top.wrqj.model.Request;
import top.wrqj.plugins.annotation.AbstractAnnotationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpServerAnnotationHandler extends AbstractAnnotationHandler<HttpServer> {

    private final Map<Class<?>, Method> VALUE_METHOD_CACHE = new ConcurrentHashMap<>();

    @Override
    protected Class<HttpServer> getSupportedAnnotation() {
        return HttpServer.class;
    }

    @Override
    public boolean supports(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        return annotationType.equals(this.getSupportedAnnotation()) || annotationType.isAnnotationPresent(this.getSupportedAnnotation());
    }

    @Override
    public void process(RequestContext context, Annotation annotation) {
        if (annotation.getClass().equals(this.getSupportedAnnotation())) {
            this.doProcess(context, (HttpServer) annotation);
        }
        Request request = context.getRequest();
        HttpServerMeta httpServerMeta = this.parseHttpRequestMethod(annotation);
        request.setUri(httpServerMeta.getTemplateUrl());
        request.setMethod(String.valueOf(httpServerMeta.getHttpMethod()));
    }

    @Override
    public List<AnnotationScope> getScope() {
        return Collections.singletonList(AnnotationScope.METHOD);
    }

    @Override
    public void doProcess(RequestContext context, HttpServer annotation) {
        Request request = context.getRequest();
        request.setMethod(String.valueOf(annotation.method()));
        request.setUri(annotation.value());
    }

    private HttpServerMeta parseHttpRequestMethod(Annotation annotation) {
        HttpServer httpServer = annotation.annotationType().getAnnotation(HttpServer.class);
        if (httpServer == null) {
            throw new RuntimeException("not find parent annotation @HttpServer");
        }

        return new HttpServerMeta(httpServer.method(), getAnnoValue(annotation));
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
}
