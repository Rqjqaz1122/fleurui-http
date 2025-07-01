package com.fleurui.core.type;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParserParamsFactory {

    private final Map<Class<?>,ParserParams> parserParamsMap = new HashMap<>(6);

    public ParserParamsFactory() {}

    public ParserParams getParserParams(Class<?> clazz) {
        boolean isArray = clazz.isArray();
        if(isArray) {
            return this.parserParamsMap.get(Array.class);
        }
        ParserParams parserParams = this.parserParamsMap.get(clazz);
        if (List.class.isAssignableFrom(clazz)) {
            parserParams = this.parserParamsMap.get(List.class);
        }
        if(Number.class.isAssignableFrom(clazz)){
            parserParams = this.parserParamsMap.get(Number.class);
        }
        if(parserParams == null) {
            //没有适配的使用Object适配器
            parserParams = this.parserParamsMap.get(Object.class);
        }
        return parserParams;
    }

    public void addParserParam(Class<?> clazz,ParserParams parserParams) {
        this.parserParamsMap.put(clazz,parserParams);
    }

    public void addParserParam(Map<Class<?>,ParserParams> map) {
        this.parserParamsMap.putAll(map);
    }
}
