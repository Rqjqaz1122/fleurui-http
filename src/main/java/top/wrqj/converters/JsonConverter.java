package top.wrqj.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonConverter implements HttpConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getType() {
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
        return mapper.readValue(bytes, clazz);
    }

    @Override
    public void write(Object value, OutputStream output) {
        try {
            mapper.writeValue(output, value);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write object to output stream", e);
        }
    }
}
