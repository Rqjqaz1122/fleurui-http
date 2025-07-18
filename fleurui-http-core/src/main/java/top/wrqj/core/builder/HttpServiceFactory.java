package top.wrqj.core.builder;

import top.wrqj.clients.HttpClient;
import top.wrqj.converters.ConverterFactory;
import top.wrqj.converters.HttpConverter;
import top.wrqj.core.builder.register.ConverterRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.model.HttpConfig;

import java.util.List;

public class HttpServiceFactory {

    private HttpClient httpClient;

    private InterceptorRegister interceptorRegister;

    private ParserParamsRegister parserParamsRegister;

    private HttpConfig httpConfig;

    public HttpServiceFactory() {}

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setConverterRegister(ConverterRegister converterRegister) {
        List<HttpConverter> httpConverters = converterRegister.getHttpConverters();
        ConverterFactory.addConverter(httpConverters);
    }

    public void setParserParamsRegister(ParserParamsRegister parserParamsRegister) {
        this.parserParamsRegister = parserParamsRegister;
    }

    public void setInterceptorRegister(InterceptorRegister interceptorRegister) {
        this.interceptorRegister = interceptorRegister;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    public <T> T createHttpService(Class<T> serviceInterface) {
        HttpServiceProxy httpServiceProxy = new HttpServiceProxy(this.httpClient,this.interceptorRegister,this.parserParamsRegister,this.httpConfig);
        return httpServiceProxy.createProxy(serviceInterface);
    }
}
