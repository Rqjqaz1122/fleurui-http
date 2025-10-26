package top.wrqj.core;

import lombok.Setter;
import top.wrqj.client.HttpClient;
import top.wrqj.client.NativeHttpClientAdapter;
import top.wrqj.converters.AbstractConverterFactory;
import top.wrqj.converters.ConverterFactory;
import top.wrqj.core.builder.register.ConverterRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.core.parser.*;
import top.wrqj.core.type.ParserParams;
import top.wrqj.core.type.ParserParamsFactory;
import top.wrqj.model.HttpConfig;
import top.wrqj.model.HttpServiceContext;
import top.wrqj.plugins.annotation.AnnotationHandlerRegister;

import java.util.Map;

@Setter
public class HttpServiceFactory {

    private HttpClient httpClient;

    private HttpConfig httpConfig;

    private InterceptorRegister interceptorRegister;

    private ParserParamsRegister parserParamsRegister;

    private ConverterRegister converterRegister;

    private AnnotationHandlerRegister annotationHandlerRegister;

    public HttpServiceFactory() {

    }
    
    public <T> T createHttpService(Class<T> serviceInterface) {
        this.defaultCreateInstance();
        HttpServiceProxy httpServiceProxy = new HttpServiceProxy(this.initHttpServiceContext());
        return httpServiceProxy.createProxy(serviceInterface);
    }

    private void defaultCreateInstance() {
        if (this.httpClient == null) {
            this.httpClient = new NativeHttpClientAdapter();
        }
        if (this.httpConfig == null) {
            this.httpConfig = new HttpConfig();
        }
        if (this.converterRegister == null) {
            this.converterRegister = new ConverterRegister();
        }
        if (this.parserParamsRegister == null) {
            this.parserParamsRegister = new ParserParamsRegister();
        }
        if (this.annotationHandlerRegister == null) {
            this.annotationHandlerRegister = new AnnotationHandlerRegister();
        }
        if (this.interceptorRegister == null) {
            this.interceptorRegister = new InterceptorRegister();
        }
        annotationHandlerRegister.registerAnnotationHandler(new HttpServerAnnotationHandler());
        annotationHandlerRegister.registerAnnotationHandler(new HttpAnnotationHandler());
        annotationHandlerRegister.registerAnnotationHandler(new PathParamAnnotationHandler());
        annotationHandlerRegister.registerAnnotationHandler(new BodyAnnotationHandler());
        annotationHandlerRegister.registerAnnotationHandler(new ParamsAnnotationHandler());
        annotationHandlerRegister.registerAnnotationHandler(new HeaderAnnotationHandler());
    }

    private HttpServiceContext initHttpServiceContext() {
        HttpServiceContext context = new HttpServiceContext();
        context.setHttpClient(this.httpClient);
        context.setHttpConfig(this.httpConfig);
        context.setInterceptorExecutionChain(interceptorRegister.getInterceptorExecutionChain());
        context.setParserParamsFactory(this.getParserParamsFactory());
        context.setAbstractConverterFactory(this.getConverterFactory());
        context.setAnnotationHandlerRegister(annotationHandlerRegister);
        return context;
    }

    private AbstractConverterFactory getConverterFactory() {
        ConverterRegister converterRegister = this.converterRegister;
        AbstractConverterFactory converterFactory = new ConverterFactory();
        converterFactory.registerConverter(converterRegister.getConverterList());
        return converterFactory;
    }

    private ParserParamsFactory getParserParamsFactory() {
        Map<Class<?>, ParserParams> parserParamsMap = this.parserParamsRegister.getParserParamsMap();
        ParserParamsFactory parserParamsFactory = new ParserParamsFactory();
        parserParamsFactory.addParserParam(parserParamsMap);
        return parserParamsFactory;
    }

}
