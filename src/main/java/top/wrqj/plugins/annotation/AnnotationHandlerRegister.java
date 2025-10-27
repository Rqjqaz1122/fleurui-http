package top.wrqj.plugins.annotation;

import top.wrqj.common.enums.AnnotationScope;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public AnnotationHandler getAnnotationHandler(Annotation annotation, AnnotationScope scope) {
        List<AnnotationHandler> scopeHandler = this.handlers.stream()
                .filter(handle -> handle.getScope().contains(scope))
                .collect(Collectors.toList());
        for (AnnotationHandler handler : scopeHandler) {
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
