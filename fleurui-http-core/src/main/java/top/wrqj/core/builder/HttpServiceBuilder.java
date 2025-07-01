package top.wrqj.core.builder;

import top.wrqj.clients.HttpClient;
import top.wrqj.clients.NativeHttpClientAdapter;
import top.wrqj.converters.HttpConverter;
import top.wrqj.converters.json.JacksonConverter;
import top.wrqj.converters.text.TextHtmlConverter;
import top.wrqj.core.builder.register.ConverterRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.core.type.ArrayParserAdapter;
import top.wrqj.core.type.NumberParserAdapter;
import top.wrqj.core.type.ObjectParserAdapter;
import top.wrqj.core.type.ParserParams;
import top.wrqj.model.ArrayType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServiceBuilder {

    private ConverterRegister converterRegister;

    private ParserParamsRegister parserParamsRegister;

    private HttpClient httpClient;

    private InterceptorRegister interceptorRegister;

    public HttpServiceBuilder() {
        converterRegister = new ConverterRegister();
        parserParamsRegister = new ParserParamsRegister();
        httpClient = new NativeHttpClientAdapter();
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

    public <T> T build(Class<T> clazz) {
        HttpServiceFactory httpServiceFactory = new HttpServiceFactory();
        this.initConfig();
        httpServiceFactory.setParserParamsRegister(this.parserParamsRegister);
        httpServiceFactory.setConverterRegister(this.converterRegister);
        httpServiceFactory.setHttpClient(this.httpClient);
        httpServiceFactory.setInterceptorRegister(this.interceptorRegister);
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
        httpConverters.add(new JacksonConverter());
        converterRegister.addConverter(httpConverters);
    }
}
