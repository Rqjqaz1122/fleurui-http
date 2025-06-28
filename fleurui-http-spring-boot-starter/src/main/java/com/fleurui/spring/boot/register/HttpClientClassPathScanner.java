package com.fleurui.spring.boot.register;

import com.fleurui.annotations.request.Http;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class HttpClientClassPathScanner extends ClassPathBeanDefinitionScanner {

    public HttpClientClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    protected void registerDefaultFilters() {
        addIncludeFilter(new AnnotationTypeFilter(Http.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }
}