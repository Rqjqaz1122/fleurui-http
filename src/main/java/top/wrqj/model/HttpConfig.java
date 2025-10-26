package top.wrqj.model;

import lombok.Data;

@Data
public class HttpConfig {

    private Integer connectionTimeout = 10000;

    private Integer readTimeout = 50000;

    private Integer maxRetry = 0;

    private Integer retryInterval;

    private String proxyHost;

    private Integer proxyPort;

    private String nonProxyHosts;

}
