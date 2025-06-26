package com.fleurui.core.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: RenQingJun
 * @Date: 2025/6/26 17:43
 * @description:
 */
public class NumberParserAdapter implements ParserParams{
    @Override
    public Map<String, String> parseParamType(String key, Object value) {
        Map<String,String> map = new HashMap<>();
        map.put(key,value.toString());
        return map;
    }
}
