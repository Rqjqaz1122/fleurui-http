package top.wrqj.converters;

import top.wrqj.exception.ConverterNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractConverterFactory {

    private final Map<String,HttpConverter> converterMap = new HashMap<>();

    public AbstractConverterFactory() {
        this.init();
    }

    public abstract void init();

    /**
     * obtain converter
     * @param contentType
     * @return
     */
    public HttpConverter getConverter(String contentType) {
        String[] split = contentType.split(";");
        if (split.length == 2) {
            contentType = split[0].trim();
        }
        HttpConverter httpConverter = this.get(contentType);
        if(httpConverter == null) {
            throw new ConverterNotFoundException("ContentType adapter not found: " + contentType);
        }
        return httpConverter;
    }

    public HttpConverter get(String key) {
        return converterMap.get(key);
    }

    public void registerConverter(HttpConverter converter) {
        String type = converter.getType();
        converterMap.put(type,converter);
    }

    public void registerConverter(List<HttpConverter> converters) {
        converters.forEach(this::registerConverter);
    }

}
