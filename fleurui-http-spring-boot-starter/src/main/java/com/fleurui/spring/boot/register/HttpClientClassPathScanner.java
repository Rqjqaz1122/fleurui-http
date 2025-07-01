package com.fleurui.spring.boot.register;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import top.wrqj.annotations.request.Http;

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