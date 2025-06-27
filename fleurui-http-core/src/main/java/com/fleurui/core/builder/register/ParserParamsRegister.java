package com.fleurui.core.builder.register;

import com.fleurui.core.type.ParserParams;

import java.util.HashMap;
import java.util.Map;

public class ParserParamsRegister {

    private final Map<Class<?>,ParserParams> parserParamsMap = new HashMap<>();

    public ParserParamsRegister(){}

    public ParserParamsRegister(Map<Class<?>, ParserParams> map) {
        parserParamsMap.putAll(map);
    }

    public ParserParamsRegister(Class<?> clazz,ParserParams parserParams) {
        parserParamsMap.put(clazz,parserParams);
    }

    public void addParserParams(Class<?> clazz,ParserParams parserParams) {
        parserParamsMap.put(clazz,parserParams);
    }

    public void addParserParams(Map<Class<?>, ParserParams> map) {
        parserParamsMap.putAll(map);
    }

    public Map<Class<?>,ParserParams> getParserParamsMap() {
        return parserParamsMap;
    }
}
