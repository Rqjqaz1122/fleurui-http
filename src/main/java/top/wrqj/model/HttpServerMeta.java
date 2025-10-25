package top.wrqj.model;

import lombok.Data;
import top.wrqj.common.enums.HttpMethod;

@Data
public class HttpServerMeta {

    private HttpMethod httpMethod;
    private String templateUrl;

    public HttpServerMeta() {}

    public HttpServerMeta(HttpMethod httpMethod, String templateUrl) {
        this.httpMethod = httpMethod;
        this.templateUrl = templateUrl;
    }
}
