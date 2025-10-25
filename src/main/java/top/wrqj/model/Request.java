package top.wrqj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    private String url;

    private String method;

    private Map<String,String> headers;

    private Map<String,String> params;

    private byte[] body;

    public Request(String url, String method, Map<String, String> headers, byte[] body) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

}
