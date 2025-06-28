package com.fleurui.exception;

public class HeaderException extends RuntimeException{

    private String header;

    public HeaderException() {
    }

    public HeaderException(String message) {
        super(message);
    }

    public HeaderException(String message,String header) {
        super(message);
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
