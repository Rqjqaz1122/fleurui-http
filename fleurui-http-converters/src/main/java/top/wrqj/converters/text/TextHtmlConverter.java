package top.wrqj.converters.text;

import top.wrqj.annotations.Order;
import top.wrqj.converters.HttpConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Order(1)
public class TextHtmlConverter implements HttpConverter {
    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public <T> T read(InputStream inputStream, Class<T> clazz) throws IOException {
        return null;
    }

    @Override
    public <T> T read(byte[] bytes, Class<T> clazz) throws IOException {
        if (clazz == String.class) {
            return clazz.cast(new String(bytes));
        }
        return null;
    }

    @Override
    public void write(Object value, OutputStream output) {

    }
}
