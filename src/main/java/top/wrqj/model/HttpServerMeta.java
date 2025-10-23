package top.wrqj.model;

public class HttpServerMeta {

    private String httpMethod;
    private String templateUrl;

    public HttpServerMeta() {}

    public HttpServerMeta(String httpMethod, String templateUrl) {
        this.httpMethod = httpMethod;
        this.templateUrl = templateUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }
}
