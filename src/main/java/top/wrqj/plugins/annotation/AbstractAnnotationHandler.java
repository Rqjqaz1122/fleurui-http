package top.wrqj.plugins.annotation;
import top.wrqj.model.RequestContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class AbstractAnnotationHandler<T extends Annotation> implements AnnotationHandler {

    @Override
    public boolean supports(Annotation annotation) {
        return annotation.annotationType().equals(this.getSupportedAnnotation());
    }

    protected abstract Class<T> getSupportedAnnotation();

    @Override
    @SuppressWarnings("unchecked")
    public void process(RequestContext context, Annotation annotation) {
        try {
            this.doProcess(context, (T) annotation);
        } catch (Exception e) {
            throw new RuntimeException("Error handing @" + annotation.annotationType().getSimpleName(), e);
        }
    }

    public abstract void doProcess(RequestContext context, T annotation);

    @Override
    public void beforeParse(Method method, Object[] args) {
        AnnotationHandler.super.beforeParse(method, args);
    }

}
