package top.wrqj.model;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class MethodContext {

    private Method method;

    private Class<?> clazz;

    private Object parameter;

}
