package top.wrqj.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface HttpConverter {

    /**
     * 获取contentType，实现接口必须要指定返回转换器适配的类型
     * @return string
     */
    String getContentType();

    /**
     * 从输入流中读取数据转为Java对象
     * @param inputStream
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T read(InputStream inputStream,Class<T> clazz) throws IOException;

    /**
     * 从byte数组中读取数据转为Java对象
     * @param bytes
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T read(byte[] bytes,Class<T> clazz) throws IOException;

    /**
     * 将数据写入输出流
     * @param value
     * @param output
     */
    void write(Object value, OutputStream output);
}
