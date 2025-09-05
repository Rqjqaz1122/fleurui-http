package top.wrqj.core.builder;

import top.wrqj.client.HttpClient;
import top.wrqj.client.NativeHttpClientAdapter;
import top.wrqj.core.builder.register.ConverterRegister;
import top.wrqj.converters.HttpConverter;
import top.wrqj.converters.JsonConverter;
import top.wrqj.converters.TextHtmlConverter;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.core.type.ArrayParserAdapter;
import top.wrqj.core.type.NumberParserAdapter;
import top.wrqj.core.type.ObjectParserAdapter;
import top.wrqj.core.type.ParserParams;
import top.wrqj.model.ArrayType;
import top.wrqj.model.HttpConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServiceBuilder {

    private ConverterRegister converterRegister;

    private ParserParamsRegister parserParamsRegister;

    private HttpClient httpClient;

    private InterceptorRegister interceptorRegister;

    private HttpConfig config;

    public HttpServiceBuilder() {
        converterRegister = new ConverterRegister();
        parserParamsRegister = new ParserParamsRegister();
        httpClient = new NativeHttpClientAdapter();
        this.config = new HttpConfig();
    }

    public static HttpServiceBuilder builder() {
        return new HttpServiceBuilder();
    }

    public HttpServiceBuilder setConverterRegister(ConverterRegister converterRegister) {
        this.converterRegister = converterRegister;
        return this;
    }

    public HttpServiceBuilder setParserParamsRegister(ParserParamsRegister parserParamsRegister) {
        this.parserParamsRegister = parserParamsRegister;
        return this;
    }

    public HttpServiceBuilder setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public HttpServiceBuilder setInterceptorRegister(InterceptorRegister interceptorRegister) {
        this.interceptorRegister = interceptorRegister;
        return this;
    }

    public HttpServiceBuilder setHttpConfig(HttpConfig httpConfig) {
        this.config = httpConfig;
        return this;
    }

    public <T> T build(Class<T> clazz) {
        HttpServiceFactory httpServiceFactory = new HttpServiceFactory();
        this.initConfig();
        httpServiceFactory.setParserParamsRegister(this.parserParamsRegister);
        httpServiceFactory.setConverterRegister(this.converterRegister);
        httpServiceFactory.setHttpClient(this.httpClient);
        httpServiceFactory.setInterceptorRegister(this.interceptorRegister);
        httpServiceFactory.setHttpConfig(this.config);
        return httpServiceFactory.createHttpService(clazz);
    }

    private void initConfig() {
        Map<Class<?>, ParserParams> parserParamsMap = new HashMap<>();
        parserParamsMap.put(Number.class,new NumberParserAdapter());
        parserParamsMap.put(ArrayType.class,new ArrayParserAdapter());
        parserParamsMap.put(Object.class,new ObjectParserAdapter());
        parserParamsRegister.addParserParams(parserParamsMap);
        List<HttpConverter> httpConverters = new ArrayList<>();
        httpConverters.add(new TextHtmlConverter());
        httpConverters.add(new JsonConverter());
        converterRegister.addConverter(httpConverters);
    }
}
