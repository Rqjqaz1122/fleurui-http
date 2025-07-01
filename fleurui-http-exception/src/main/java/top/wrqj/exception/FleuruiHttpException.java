package top.wrqj.exception;

public class FleuruiHttpException extends RuntimeException {

    public FleuruiHttpException(){}

    public FleuruiHttpException(String message) {
        super(message);
    }

    public FleuruiHttpException(String message,Throwable cause) {
        super(message,cause);
    }
}
