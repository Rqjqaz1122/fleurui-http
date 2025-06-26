package com.fleurui.builder;

import com.fleurui.builder.register.ConverterRegister;
import com.fleurui.builder.register.ParserParamsRegister;
import com.fleurui.clients.HttpClient;
import com.fleurui.clients.NativeHttpClientAdapter;
import com.fleurui.converters.ConverterFactory;
import com.fleurui.converters.HttpConverter;
import com.fleurui.core.type.ParserParams;
import com.fleurui.core.type.ParserParamsFactory;

import java.util.List;
import java.util.Map;

public class HttpServiceFactory {

    private HttpClient httpClient;

    public HttpServiceFactory() {}

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setConverterRegister(ConverterRegister converterRegister) {
        List<HttpConverter> httpConverters = converterRegister.getHttpConverters();
        ConverterFactory.addConverter(httpConverters);
    }

    public void setParserParamsRegister(ParserParamsRegister parserParamsRegister) {
        Map<Class<?>, ParserParams> parserParamsMap = parserParamsRegister.getParserParamsMap();
        if(!parserParamsMap.isEmpty()) {
            ParserParamsFactory.addParserParam(parserParamsMap);
        }
    }

    public <T> T createHttpService(Class<T> serviceInterface) {
        HttpServiceProxy httpServiceProxy = new HttpServiceProxy(this.httpClient);
        return httpServiceProxy.createProxy(serviceInterface);
    }
}
