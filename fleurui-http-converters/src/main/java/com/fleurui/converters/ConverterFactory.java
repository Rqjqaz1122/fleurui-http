package com.fleurui.converters;

import com.fleurui.annotations.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterFactory {

    private static final List<HttpConverter> converterList = new ArrayList<>();

    public ConverterFactory() {}

    /**
     * 获取适配器
     * @param contentType
     * @return
     */
    public static HttpConverter getConverter(String contentType) {
        ConverterFactory converterFactory = new ConverterFactory();
        List<HttpConverter> list = converterFactory.getConverterList();
        List<HttpConverter> httpConverterList = list.stream()
                .filter(item -> item.getContentType().equals(contentType))
                .sorted((o1,o2) -> {
                    Class<? extends HttpConverter> o1Class = o1.getClass();
                    int o1value = o1Class.getAnnotation(Order.class).value();
                    Class<? extends HttpConverter> o2Class = o2.getClass();
                    int o2Value = o2Class.getAnnotation(Order.class).value();
                    return o1value - o2Value;
                })
                .collect(Collectors.toList());
        if(httpConverterList.isEmpty()) {
            throw new RuntimeException("找不到适配：" + contentType + "类型转换器");
        }
        return httpConverterList.get(0);
    }

    public static void addConverter(HttpConverter converter) {
        converterList.add(converter);
    }

    public static void addConverter(List<HttpConverter> converters) {
        converterList.addAll(converters);
    }

    public List<HttpConverter> getConverterList() {
        return converterList;
    }
}
