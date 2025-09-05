package top.wrqj.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class XmlConverter implements HttpConverter {
    @Override
    public String getType() {
        return "application/xml";
    }

    @Override
    public <T> T read(InputStream inputStream, Class<T> clazz) throws IOException {
        return null;
    }

    @Override
    public <T> T read(byte[] bytes, Class<T> clazz) throws IOException {
        return null;
    }

    @Override
    public void write(Object value, OutputStream output) {

    }
}
