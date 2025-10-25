package top.wrqj.model;

import lombok.Data;

import java.util.Map;

@Data
public class Response {

    private final int statusCode;

    private final Map<String,String> headers;

    private final byte[] body;

    public Response(int statusCode, Map<String, String> headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

}
