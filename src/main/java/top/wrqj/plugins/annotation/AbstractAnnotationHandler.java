package top.wrqj.plugins.annotation;
import top.wrqj.model.MethodContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class AbstractAnnotationHandler implements AnnotationHandler {

    @Override
    public boolean supports(Annotation annotation) {
        return annotation.annotationType().equals(this.getSupportedAnnotation());
    }

    protected abstract Class<? extends Annotation> getSupportedAnnotation();

    @Override
    public final void process(MethodContext context, Annotation annotation) {
        try {
            this.doProcess(context, annotation);
        } catch (Exception e) {
            throw new RuntimeException("Error handing @" + annotation.annotationType().getSimpleName(), e);
        }
    }

    public abstract void doProcess(MethodContext context, Annotation annotation);

    @Override
    public void beforeParse(Method method, Object[] args) {
        AnnotationHandler.super.beforeParse(method, args);
    }

}
