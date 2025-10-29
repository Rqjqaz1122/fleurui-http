package top.wrqj.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Data
@AllArgsConstructor
public class RequestContext {

    private final Request request;

    private final Method method;

    private final Class<?> clazz;

    private final Parameter parameterType;

    private final Object parameter;

}
