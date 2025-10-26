package top.wrqj.plugins.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnnotationHandlerRegister {

    private final Set<AnnotationHandler> handlers = new HashSet<>();

    public AnnotationHandler getAnnotationHandler(Annotation annotation) {
        for (AnnotationHandler handler : handlers) {
            if (handler.supports(annotation)) {
                return handler;
            }
        }
        return null;
    }

    public void registerAnnotationHandler(AnnotationHandler annotationHandler) {
        handlers.add(annotationHandler);
    }

    public void registerAnnotationHandler(AnnotationHandler ...annotationHandler) {
        handlers.addAll(Arrays.asList(annotationHandler));
    }

}
