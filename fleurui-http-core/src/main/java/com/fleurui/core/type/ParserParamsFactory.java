package com.fleurui.core.type;

import com.fleurui.model.ArrayType;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: RenQingJun
 * @Date: 2025/6/26 16:06
 * @description:
 */
public class ParserParamsFactory {

    private final Map<Class<?>,ParserParams> parserParamsMap = new ConcurrentHashMap<>(6);

    public ParserParamsFactory() {
        parserParamsMap.put(Number.class,new NumberParserAdapter());
        parserParamsMap.put(ArrayType.class,new ArrayParserAdapter());
        parserParamsMap.put(Object.class,new ObjectParserAdapter());
    }

    public void addParserParam(Class<?> clazz,ParserParams parserParams) {
        this.parserParamsMap.put(clazz,parserParams);
    }

    public ParserParams getParserParams(Class<?> clazz) {
        boolean isArray = clazz.isArray();
        if(isArray) {
            return parserParamsMap.get(Array.class);
        }
        ParserParams parserParams = parserParamsMap.get(clazz);
        if (List.class.isAssignableFrom(clazz)) {
            parserParams = parserParamsMap.get(List.class);
        }
        if(Number.class.isAssignableFrom(clazz)){
            parserParams = parserParamsMap.get(Number.class);
        }
        if(parserParams == null) {
            //没有适配的使用Object适配器
            parserParams = parserParamsMap.get(Object.class);
        }
        return parserParams;
    }
}
