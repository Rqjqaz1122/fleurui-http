package top.wrqj.converters;

import java.util.ArrayList;
import java.util.List;

public class ConverterFactory extends AbstractConverterFactory{

    public ConverterFactory() {}

    @Override
    public void init() {
        List<HttpConverter> httpConverters = new ArrayList<>();
        httpConverters.add(new TextHtmlConverter());
        httpConverters.add(new JsonConverter());
        this.registerConverter(httpConverters);
    }

}
