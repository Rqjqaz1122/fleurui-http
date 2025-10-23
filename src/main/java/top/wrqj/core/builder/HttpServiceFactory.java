package top.wrqj.core.builder;

import top.wrqj.client.HttpClient;
import top.wrqj.converters.ConverterFactory;
import top.wrqj.converters.HttpConverter;
import top.wrqj.core.HttpServiceProxy;
import top.wrqj.core.InterceptorRegister;
import top.wrqj.core.builder.register.ConverterRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.model.HttpConfig;
import top.wrqj.model.HttpServiceContext;

import java.util.List;

public class HttpServiceFactory {

    private final HttpServiceContext httpServiceContext;

    public HttpServiceFactory() {
        httpServiceContext = new HttpServiceContext();
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpServiceContext.setHttpClient(httpClient);
    }

    public void setConverterRegister(ConverterRegister converterRegister) {
        List<HttpConverter> converterList = converterRegister.getConverterList();
        ConverterFactory converterFactory = new ConverterFactory();
        converterFactory.addConverter(converterList);
    }

    public void setParserParamsRegister(ParserParamsRegister parserParamsRegister) {
        this.httpServiceContext.setParserParamsRegister(parserParamsRegister);
    }

    public void setInterceptorRegister(InterceptorRegister interceptorRegister) {
        this.httpServiceContext.setInterceptorRegister(interceptorRegister);
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpServiceContext.setHttpConfig(httpConfig);
    }

    public <T> T createHttpService(Class<T> serviceInterface) {
        HttpServiceProxy httpServiceProxy = new HttpServiceProxy(this.httpServiceContext);
        return httpServiceProxy.createProxy(serviceInterface);
    }
}
