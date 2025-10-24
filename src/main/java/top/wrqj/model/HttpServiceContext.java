package top.wrqj.model;

import top.wrqj.client.HttpClient;
import top.wrqj.converters.AbstractConverterFactory;
import top.wrqj.converters.ConverterFactory;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.core.interceptor.InterceptorExecutionChain;
import top.wrqj.core.type.ParserParamsFactory;

public class HttpServiceContext {

    private HttpClient httpClient;

    private InterceptorExecutionChain interceptorExecutionChain;

    private ParserParamsFactory parserParamsFactory;

    private AbstractConverterFactory abstractConverterFactory;

    private HttpConfig httpConfig;

    public HttpServiceContext() {}

    public HttpServiceContext(HttpClient httpClient, InterceptorExecutionChain interceptorExecutionChain, ParserParamsFactory parserParamsFactory, HttpConfig httpConfig) {
        this.httpClient = httpClient;
        this.interceptorExecutionChain = interceptorExecutionChain;
        this.parserParamsFactory = parserParamsFactory;
        this.httpConfig = httpConfig;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public InterceptorExecutionChain getInterceptorExecutionChain() {
        return interceptorExecutionChain;
    }

    public void setInterceptorExecutionChain(InterceptorExecutionChain interceptorExecutionChain) {
        this.interceptorExecutionChain = interceptorExecutionChain;
    }

    public ParserParamsFactory getParserParamsFactory() {
        return parserParamsFactory;
    }

    public void setParserParamsFactory(ParserParamsFactory parserParamsFactory) {
        this.parserParamsFactory = parserParamsFactory;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    public AbstractConverterFactory getAbstractConverterFactory() {
        return abstractConverterFactory;
    }

    public void setAbstractConverterFactory(AbstractConverterFactory abstractConverterFactory) {
        this.abstractConverterFactory = abstractConverterFactory;
    }
}
