package top.wrqj.core;

import lombok.Setter;
import top.wrqj.client.HttpClient;
import top.wrqj.client.NativeHttpClientAdapter;
import top.wrqj.converters.AbstractConverterFactory;
import top.wrqj.converters.ConverterFactory;
import top.wrqj.converters.HttpConverter;
import top.wrqj.core.builder.register.ConverterRegister;
import top.wrqj.core.builder.register.ParserParamsRegister;
import top.wrqj.core.type.ParserParams;
import top.wrqj.core.type.ParserParamsFactory;
import top.wrqj.model.HttpConfig;
import top.wrqj.model.HttpServiceContext;

import java.util.List;
import java.util.Map;

@Setter
public class HttpServiceFactory {

    private HttpClient httpClient = new NativeHttpClientAdapter();

    private HttpConfig httpConfig = new HttpConfig();

    private InterceptorRegister interceptorRegister = new InterceptorRegister();

    private ParserParamsRegister parserParamsRegister = new ParserParamsRegister();

    private ConverterRegister converterRegister = new ConverterRegister();

    public HttpServiceFactory() {
    }
    
    public <T> T createHttpService(Class<T> serviceInterface) {
        HttpServiceProxy httpServiceProxy = new HttpServiceProxy(this.initHttpServiceContext());
        return httpServiceProxy.createProxy(serviceInterface);
    }

    private HttpServiceContext initHttpServiceContext() {
        HttpServiceContext context = new HttpServiceContext();
        context.setHttpClient(this.httpClient);
        context.setHttpConfig(this.httpConfig);
        context.setInterceptorExecutionChain(interceptorRegister.getInterceptorExecutionChain());
        context.setParserParamsFactory(this.getParserParamsFactory());
        context.setAbstractConverterFactory(this.getConverterFactory());
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
