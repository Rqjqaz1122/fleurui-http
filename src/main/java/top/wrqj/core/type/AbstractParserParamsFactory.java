package top.wrqj.core.type;

import top.wrqj.model.ArrayType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractParserParamsFactory {

    private final Map<Class<?>,ParserParams> parserParamsMap = new HashMap<>(6);

    public abstract void init();

    public AbstractParserParamsFactory() {
        this.init();
    }

    public ParserParams getParserParams(Class<?> clazz) {
        boolean isArray = clazz.isArray();
        if(isArray) {
            return this.parserParamsMap.get(ArrayType.class);
        }
        if (List.class.isAssignableFrom(clazz)) {
            return this.parserParamsMap.get(List.class);
        }
        if (isPrimitiveOrWrapper(clazz)) {
            return this.parserParamsMap.get(Number.class);
        }
        return this.parserParamsMap.get(Object.class);
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Double.class
                || clazz == Float.class
                || clazz == Boolean.class
                || clazz == Byte.class
                || clazz == Short.class
                || clazz == Character.class;
    }

    public void addParserParam(Class<?> clazz,ParserParams parserParams) {
        this.parserParamsMap.put(clazz,parserParams);
    }

    public void addParserParam(Map<Class<?>,ParserParams> map) {
        this.parserParamsMap.putAll(map);
    }

}
