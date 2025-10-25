package top.wrqj.model;

import lombok.Data;

@Data
public class HttpConfig {

    private Integer connectionTimeout = 1000;

    private Integer readTimeout = 5000;

    private Integer maxRetry = 0;

    private Integer retryInterval;

    private String proxyHost;

    private Integer proxyPort;

    private String nonProxyHosts;

}
