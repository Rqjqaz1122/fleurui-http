package top.wrqj.exception;

public class ConverterNotFoundException extends FleuruiHttpException{

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MESSAGE = "找不到请求响应适配的类型转换器";

    /**
     * 使用默认错误消息构造异常
     */
    public ConverterNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * 使用自定义错误消息构造异常
     * @param message 自定义错误描述
     */
    public ConverterNotFoundException(String message) {
        super(message);
    }

    /**
     * 使用自定义错误消息构造异常
     * @param message 自定义错误描述
     */
    public ConverterNotFoundException(String message,String contentType) {
        super(message);
    }

    /**
     * 使用默认消息和导致异常的根源构造异常
     * @param cause 导致此异常的根源异常
     */
    public ConverterNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    /**
     * 使用自定义消息和导致异常的根源构造异常
     * @param message 自定义错误描述
     * @param cause 导致此异常的根源异常
     */
    public ConverterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
