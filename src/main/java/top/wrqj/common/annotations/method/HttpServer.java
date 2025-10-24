package top.wrqj.common.annotations.method;

import top.wrqj.common.enums.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpServer {
    String value() default "";
    HttpMethod method() default HttpMethod.GET;
}
