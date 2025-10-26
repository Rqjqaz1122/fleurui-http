package top.wrqj.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class RequestContext {

    private final Request request;

    private final Method method;

    private final Class<?> clazz;

    private final Object parameter;

}
