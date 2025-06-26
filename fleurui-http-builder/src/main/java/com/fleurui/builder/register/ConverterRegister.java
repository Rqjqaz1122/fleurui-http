package com.fleurui.builder.register;

import com.fleurui.converters.HttpConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConverterRegister {

    private final List<HttpConverter> httpConverters = new ArrayList<>();

    public ConverterRegister(){}

    public ConverterRegister(HttpConverter ...httpConverters) {
        this.addConverter(httpConverters);
    }

    public void addConverter(HttpConverter ...httpConverters) {
        this.httpConverters.addAll(Arrays.asList(httpConverters));
    }

    public void addConverter(List<HttpConverter> httpConverters) {
        this.httpConverters.addAll(httpConverters);
    }

    public List<HttpConverter> getHttpConverters() {
        return this.httpConverters;
    }
}
