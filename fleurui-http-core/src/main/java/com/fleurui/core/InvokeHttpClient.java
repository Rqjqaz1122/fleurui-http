package com.fleurui.core;

import com.fleurui.converters.ConverterRegister;
import com.fleurui.converters.HttpConverter;
import com.fleurui.annotations.HttpDemo;
import com.fleurui.clients.HttpClient;
import com.fleurui.clients.NativeHttpClientAdapter;
import com.fleurui.core.parser.ClassParser;
import com.fleurui.core.parser.MethodParser;
import com.fleurui.core.parser.ParameterParser;
import com.fleurui.model.Request;
import com.fleurui.model.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
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
        ClassParser.parser(request,method);
        MethodParser.parser(request,method);
        ParameterParser.parser(request,method);
        Response response = httpClient.execute(request);
        System.out.println(response);
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
        Class<HttpDemo> httpDemoClass = HttpDemo.class;
        HttpDemo o = (HttpDemo)Proxy.newProxyInstance(httpDemoClass.getClassLoader(), new Class<?>[]{httpDemoClass}, new InvokeHttpClient(new NativeHttpClientAdapter()));
        String s = o.sendRequest(1L);
        System.out.println(s);
    }
}
