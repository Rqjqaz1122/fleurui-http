package com.fleurui.core;

import com.fleurui.converters.ConverterFactory;
import com.fleurui.converters.HttpConverter;
import com.fleurui.clients.HttpClient;
import com.fleurui.core.parser.*;
import com.fleurui.core.utils.UrlBuilder;
import com.fleurui.model.Request;
import com.fleurui.model.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvokeHttpClient implements InvocationHandler {

    private final HttpClient httpClient;

    public InvokeHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setHeaders(new HashMap<>());
        List<Parser> parserList = new ParserFactory().getParserList();
        for (Parser parser : parserList) {
            parser.parser(request,method,args);
        }
        Map<String, String> params = request.getParams();
        if(params != null && !params.isEmpty()) {
            String finalUrl = UrlBuilder.create(request.getUrl()).addQuery(params);
            request.setUrl(finalUrl);
        }
        Response response = httpClient.execute(request);
        byte[] body = response.getBody();
        Map<String, String> headers = response.getHeaders();
        String contentType = headers.get("Content-Type");
        HttpConverter converter = ConverterFactory.getConverter(contentType);
        if(converter == null) {
            throw new RuntimeException("找不到适配：" + contentType + "类型转换器");
        }
        return converter.read(body, method.getReturnType());
    }
}
