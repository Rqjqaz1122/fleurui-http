package top.wrqj.core.utils;

import java.util.Map;

public class UrlBuilder {

    private String url;

    public UrlBuilder(String url) {
        this.url = url;
    }

    public UrlBuilder(){}

    public static UrlBuilder create(String url) {
        return new UrlBuilder(url);
    }

    public UrlBuilder addPath(String path){
        if(path == null || path.isEmpty()) {
            return this;
        }
        String urlPath = checkPath(path);
        this.url = this.url + urlPath;
        return this;
    }

    public UrlBuilder addQuery(String key, String value) {
        if (this.url.contains("?")) {
            this.url = this.url + "&" + key + "=" + value;
        } else {
            this.url = this.url + "?" + key + "=" + value;
        }
        return this;
    }

    public String addQuery(Map<String, String> query) {
        if (query == null || query.isEmpty()) {
           throw new IllegalArgumentException("query is null or empty");
        }
        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (queryStringBuilder.length() == 0) {
                queryStringBuilder.append("?");
            } else {
                queryStringBuilder.append("&");
            }
            queryStringBuilder.append(key).append("=").append(value);
        }
        this.url += queryStringBuilder.toString();
        return toString();
    }

    private String checkPath(String path){
        String trim = path.trim();
        if(this.url.endsWith("/")){
             trim = trim.replaceFirst("/","");
        }else{
            boolean flag = trim.startsWith("/");
            if(!flag){
                trim = "/" + trim;
            }
            boolean endFlag = trim.endsWith("/");
            if(endFlag){
                trim = trim.substring(trim.lastIndexOf("/"));
            }
        }
        return trim;
    }

    @Override
    public String toString() {
        return this.url;
    }

}