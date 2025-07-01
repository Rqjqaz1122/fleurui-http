package top.wrqj.core.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.stream.Collectors;

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
