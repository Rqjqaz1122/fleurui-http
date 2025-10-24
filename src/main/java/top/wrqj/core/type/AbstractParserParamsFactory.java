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
        ParserParams parserParams = this.parserParamsMap.get(clazz);
        if (List.class.isAssignableFrom(clazz)) {
            parserParams = this.parserParamsMap.get(List.class);
        }
        if(Number.class.isAssignableFrom(clazz)){
            parserParams = this.parserParamsMap.get(Number.class);
        }
        if(parserParams == null) {
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
