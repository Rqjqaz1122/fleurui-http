package top.wrqj.model;

import top.wrqj.client.HttpClient;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;

public class HttpServiceContext {

    private HttpClient httpClient;

    private InterceptorRegister interceptorRegister;

    private ParserParamsRegister parserParamsRegister;

    private HttpConfig httpConfig;

    public HttpServiceContext() {}

    public HttpServiceContext(HttpClient httpClient, InterceptorRegister interceptorRegister, ParserParamsRegister parserParamsRegister, HttpConfig httpConfig) {
        this.httpClient = httpClient;
        this.interceptorRegister = interceptorRegister;
        this.parserParamsRegister = parserParamsRegister;
        this.httpConfig = httpConfig;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public InterceptorRegister getInterceptorRegister() {
        return interceptorRegister;
    }

    public void setInterceptorRegister(InterceptorRegister interceptorRegister) {
        this.interceptorRegister = interceptorRegister;
    }

    public ParserParamsRegister getParserParamsRegister() {
        return parserParamsRegister;
    }

    public void setParserParamsRegister(ParserParamsRegister parserParamsRegister) {
        this.parserParamsRegister = parserParamsRegister;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }
}
