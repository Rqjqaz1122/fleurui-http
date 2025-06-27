package com.fleurui.core.builder;

import com.fleurui.core.builder.register.ConverterRegister;
import com.fleurui.core.builder.register.ParserParamsRegister;
import com.fleurui.clients.HttpClient;
import com.fleurui.clients.NativeHttpClientAdapter;
import com.fleurui.converters.HttpConverter;
import com.fleurui.converters.json.JacksonConverter;
import com.fleurui.converters.text.TextHtmlConverter;
import com.fleurui.core.type.ArrayParserAdapter;
import com.fleurui.core.type.NumberParserAdapter;
import com.fleurui.core.type.ObjectParserAdapter;
import com.fleurui.core.type.ParserParams;
import com.fleurui.model.ArrayType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServiceBuilder {

    private ConverterRegister converterRegister;

    private ParserParamsRegister parserParamsRegister;

    private HttpClient httpClient;

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

    public <T> T build(Class<T> clazz) {
        HttpServiceFactory httpServiceFactory = new HttpServiceFactory();
        this.initConfig();
        httpServiceFactory.setParserParamsRegister(this.parserParamsRegister);
        httpServiceFactory.setConverterRegister(this.converterRegister);
        httpServiceFactory.setHttpClient(this.httpClient);
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
