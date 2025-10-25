package top.wrqj.model;

import lombok.Data;
import top.wrqj.client.HttpClient;
import top.wrqj.converters.AbstractConverterFactory;
import top.wrqj.core.interceptor.InterceptorExecutionChain;
import top.wrqj.core.type.ParserParamsFactory;

@Data
public class HttpServiceContext {

    private HttpClient httpClient;

    private InterceptorExecutionChain interceptorExecutionChain;

    private ParserParamsFactory parserParamsFactory;

    private AbstractConverterFactory abstractConverterFactory;

    private HttpConfig httpConfig;

}
