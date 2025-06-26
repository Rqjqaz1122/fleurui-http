package com.fleurui.core;

import com.fleurui.annotations.Demo;
import com.fleurui.converters.ConverterRegister;
import com.fleurui.converters.HttpConverter;
import com.fleurui.annotations.HttpDemo;
import com.fleurui.clients.HttpClient;
import com.fleurui.clients.NativeHttpClientAdapter;
import com.fleurui.core.parser.*;
import com.fleurui.core.type.ParserParamsFactory;
import com.fleurui.model.Request;
import com.fleurui.model.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InvokeHttpClient implements InvocationHandler {

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
        long l = System.currentTimeMillis();
        Response response = httpClient.execute(request);
        System.out.println(System.currentTimeMillis() - l);
        byte[] body = response.getBody();
        Map<String, String> headers = response.getHeaders();
        String contentType = headers.get("Content-Type");
        HttpConverter converter = ConverterRegister.getConverter(contentType);
        if(converter == null) {
            throw new RuntimeException("找不到适配：" + contentType + "类型转换器");
        }
        return converter.read(body, method.getReturnType());
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        System.out.println(l);
        Class<HttpDemo> httpDemoClass = HttpDemo.class;
        HttpDemo o = (HttpDemo)Proxy.newProxyInstance(httpDemoClass.getClassLoader(), new Class<?>[]{httpDemoClass}, new InvokeHttpClient(new NativeHttpClientAdapter()));
        String s = o.sendRequest(1L,new Demo());
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(s);
    }
}
