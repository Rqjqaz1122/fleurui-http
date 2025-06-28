package com.fleurui.core.type;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ArrayParserAdapter implements ParserParams{
    @Override
    public Map<String, String> parseParamType(String key,Object value) {
        int length = Array.getLength(value);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length;i++) {
            Object item = Array.get(value, i);
            sb.append(item);
            if(i + 1 != length) {
                sb.append(",");
            }
        }
        Map<String,String> map = new HashMap<>(1);
        map.put(key,sb.toString());
        return map;
    }
}
