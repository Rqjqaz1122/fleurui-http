package com.fleurui.core.parser;

import com.fleurui.annotations.request.Body;
import com.fleurui.annotations.request.Params;
import com.fleurui.converters.ConverterRegister;
import com.fleurui.converters.HttpConverter;
import com.fleurui.core.type.ParserParams;
import com.fleurui.core.type.ParserParamsFactory;
import com.fleurui.model.Request;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 参数解析器
 */
public class ParameterParser implements Parser{

    @Override
    public void parser(Request request, Method method,Object[] args) {
        Map<String, String> headers = request.getHeaders();
        String contentType = headers.get("Content-Type");
        Parameter[] parameters = method.getParameters();
        Map<String,String> paramsMap = new HashMap<>(args.length);
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Parameter parameter = parameters[i];
            Body body = parameter.getAnnotation(Body.class);
            if(body != null && contentType != null && !contentType.isEmpty()) {
                HttpConverter converter = ConverterRegister.getConverter(contentType);
                ByteArrayOutputStream  output = new ByteArrayOutputStream();
                converter.write(arg,output);
                byte[] byteArray = output.toByteArray();
                request.setBody(byteArray);
            }
            Params params = parameter.getAnnotation(Params.class);
            if(params != null) {
                ParserParams parserParams = new ParserParamsFactory().getParserParams(arg.getClass());
                if(parserParams == null) {
                    throw new RuntimeException("不存在当前适配类型解析器");
                }
                String value = params.value();
                if(value == null || value.isEmpty()) {
                    value = arg.getClass().getName();
                }
                Map<String, String> map = parserParams.parseParamType(value,arg);
                paramsMap.putAll(map);
            }
        }
        request.setParams(paramsMap);
    }

}
