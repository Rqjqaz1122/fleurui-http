package top.wrqj.model;

import top.wrqj.common.enums.HttpMethod;

public class HttpServerMeta {

    private HttpMethod httpMethod;
    private String templateUrl;

    public HttpServerMeta() {}

    public HttpServerMeta(HttpMethod httpMethod, String templateUrl) {
        this.httpMethod = httpMethod;
        this.templateUrl = templateUrl;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }
}
