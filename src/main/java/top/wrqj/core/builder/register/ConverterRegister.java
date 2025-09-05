package top.wrqj.core.builder.register;

import top.wrqj.converters.HttpConverter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ConverterRegister {

    private final List<HttpConverter> httpConverterList = new LinkedList<>();

    public ConverterRegister(){
    }

    public ConverterRegister(HttpConverter ...httpConverters) {
        this.addConverter(httpConverters);
    }

    public void addConverter(HttpConverter ...httpConverters) {
        this.httpConverterList.addAll(Arrays.asList(httpConverters));
    }

    public void addConverter(List<HttpConverter> httpConverters) {
        this.httpConverterList.addAll(httpConverters);
    }

    public List<HttpConverter> getConverterList() {
        return this.httpConverterList;
    }
}
