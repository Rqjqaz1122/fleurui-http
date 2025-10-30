package top.wrqj.core.type;

import java.util.HashMap;
import java.util.Map;

public class UniversalParserAdapter implements ParserParams{
    @Override
    public Map<String, String> parseParamType(String key, Object value) {
        Map<String,String> map = new HashMap<>();
        map.put(key, String.valueOf(value));
        return map;
    }
}
