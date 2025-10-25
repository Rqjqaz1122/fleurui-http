package top.wrqj.plugins.annotation;

import top.wrqj.common.enums.AnnotationScope;
import top.wrqj.model.MethodContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

interface AnnotationHandler {

    boolean supports(Annotation annotation);

    void process(MethodContext context, Annotation annotation);

    default void beforeParse(Method method, Object[] args) {}

    default AnnotationScope getScope() {
        return AnnotationScope.PARAMETER;
    }

    default int getOrder() {
        return 0;
    }

}
