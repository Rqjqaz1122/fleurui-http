package top.wrqj.converters;

import top.wrqj.exception.ConverterNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterFactory {

    private static final Map<String,HttpConverter> converterMap = new HashMap<>();

    public ConverterFactory() {}

    /**
     * obtain converter
     * @param contentType
     * @return
     */
    public static HttpConverter getConverter(String contentType) {
        HttpConverter httpConverter = get(contentType);
        if(httpConverter == null) {
            throw new ConverterNotFoundException("找不到适配：" + contentType + "类型转换器");
        }
        return httpConverter;
    }

    public static HttpConverter get(String key) {
        return converterMap.get(key);
    }

    public void addConverter(HttpConverter converter) {
        String type = converter.getType();
        converterMap.put(type,converter);
    }

    public void addConverter(List<HttpConverter> converters) {
        converters.forEach(this::addConverter);
    }
}
