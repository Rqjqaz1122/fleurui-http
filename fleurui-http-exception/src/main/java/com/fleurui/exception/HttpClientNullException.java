package com.fleurui.exception;

/**
 * 当HttpClient适配器未初始化或为null时抛出的异常
 * <p>
 * 此异常表示框架尝试执行HTTP请求时，发现配置的HttpClient适配器实例为空。
 * 通常是由于未正确初始化HttpServiceBuilder或手动设置了null值导致的。
 * </p>
 */
public class HttpClientNullException extends FleuruiHttpException {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_MESSAGE = "HttpClient适配器不能为null，请正确配置HttpClient实例";

    /**
     * 使用默认错误消息构造异常
     */
    public HttpClientNullException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * 使用自定义错误消息构造异常
     * @param message 自定义错误描述
     */
    public HttpClientNullException(String message) {
        super(message);
    }

    /**
     * 使用默认消息和导致异常的根源构造异常
     * @param cause 导致此异常的根源异常
     */
    public HttpClientNullException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * 使用自定义消息和导致异常的根源构造异常
     * @param message 自定义错误描述
     * @param cause 导致此异常的根源异常
     */
    public HttpClientNullException(String message, Throwable cause) {
        super(message, cause);
    }
}