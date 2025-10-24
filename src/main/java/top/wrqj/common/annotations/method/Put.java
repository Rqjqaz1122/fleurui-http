package top.wrqj.common.annotations.method;

import top.wrqj.common.enums.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@HttpServer(method = HttpMethod.PUT)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Put {
    String value() default "";
}
