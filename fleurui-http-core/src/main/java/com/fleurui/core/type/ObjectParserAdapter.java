package com.fleurui.core.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: RenQingJun
 * @Date: 2025/6/26 16:33
 * @description:
 */
public class ObjectParserAdapter implements ParserParams{
    @Override
    public Map<String, String> parseParamType(String key,Object value) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.convertValue(value, new TypeReference<Map<String, String>>() {
        });
        map = map.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return map;
    }
}
