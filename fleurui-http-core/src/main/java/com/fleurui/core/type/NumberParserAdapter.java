package com.fleurui.core.type;

import java.util.HashMap;
import java.util.Map;

public class NumberParserAdapter implements ParserParams{
    @Override
    public Map<String, String> parseParamType(String key, Object value) {
        Map<String,String> map = new HashMap<>();
        map.put(key,value.toString());
        return map;
    }
}
