package top.wrqj.plugins.annotation;

import top.wrqj.common.enums.AnnotationScope;
import top.wrqj.model.RequestContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface AnnotationHandler {

    boolean supports(Annotation annotation);

    void process(RequestContext context, Annotation annotation);

    default void beforeParse(Method method, Object[] args) {}

    default AnnotationScope getScope() {
        return AnnotationScope.PARAMETER;
    }

    default int getOrder() {
        return 0;
    }

}
