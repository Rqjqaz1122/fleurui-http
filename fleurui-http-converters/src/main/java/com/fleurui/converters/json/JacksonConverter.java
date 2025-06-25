package com.fleurui.converters.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleurui.converters.HttpConverter;

import java.io.IOException;
import java.io.InputStream;

public class JacksonConverter implements HttpConverter {
    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public <T> T read(InputStream inputStream,Class<T> clazz) throws IOException {

        return null;
    }

    @Override
    public <T> T read(byte[] bytes,Class<T> clazz) throws IOException {
        if (clazz == String.class) {
            return clazz.cast(new String(bytes));
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(bytes, clazz);
    }
}
