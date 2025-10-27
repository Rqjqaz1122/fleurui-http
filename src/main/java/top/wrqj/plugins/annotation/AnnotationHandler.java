package top.wrqj.plugins.annotation;

import top.wrqj.common.enums.AnnotationScope;
import top.wrqj.model.RequestContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public interface AnnotationHandler {

    boolean supports(Annotation annotation);

    void process(RequestContext context, Annotation annotation);

    default void beforeParse(Method method, Object[] args) {}

    List<AnnotationScope> getScope();

    default int getOrder() {
        return 0;
    }

}
