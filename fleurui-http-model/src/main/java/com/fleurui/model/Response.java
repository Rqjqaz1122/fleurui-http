package com.fleurui.model;

import java.io.InputStream;
import java.util.Map;

public class Response {

    private final int statusCode;

    private final Map<String,String> headers;

    private final byte[] body;

    public Response(int statusCode, Map<String, String> headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
