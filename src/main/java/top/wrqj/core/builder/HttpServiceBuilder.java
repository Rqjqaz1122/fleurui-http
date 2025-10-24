package top.wrqj.core.builder;

import top.wrqj.client.HttpClient;
import top.wrqj.core.HttpServiceFactory;
import top.wrqj.core.builder.register.ConverterRegister;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.model.HttpConfig;

public class HttpServiceBuilder {

    private final HttpServiceFactory httpServiceFactory;

    public HttpServiceBuilder() {
        this.httpServiceFactory = new HttpServiceFactory();
    }

    public static HttpServiceBuilder create() {
        return new HttpServiceBuilder();
    }

    public HttpServiceBuilder setConverterRegister(ConverterRegister converterRegister) {
        this.httpServiceFactory.setConverterRegister(converterRegister);
        return this;
    }

    public HttpServiceBuilder setParserParamsRegister(ParserParamsRegister parserParamsRegister) {
        this.httpServiceFactory.setParserParamsRegister(parserParamsRegister);
        return this;
    }

    public HttpServiceBuilder setHttpClient(HttpClient httpClient) {
        this.httpServiceFactory.setHttpClient(httpClient);
        return this;
    }

    public HttpServiceBuilder setInterceptorRegister(InterceptorRegister interceptorRegister) {
        this.httpServiceFactory.setInterceptorRegister(interceptorRegister);
        return this;
    }

    public HttpServiceBuilder setHttpConfig(HttpConfig httpConfig) {
        this.httpServiceFactory.setHttpConfig(httpConfig);
        return this;
    }

    public <T> T build(Class<T> clazz) {
        return this.httpServiceFactory.createHttpService(clazz);
    }

}
