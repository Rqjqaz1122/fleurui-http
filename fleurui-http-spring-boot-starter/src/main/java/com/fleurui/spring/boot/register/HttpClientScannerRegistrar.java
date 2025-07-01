package com.fleurui.spring.boot.register;

import com.fleurui.spring.boot.enable.EnableHttpClients;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import top.wrqj.annotations.request.Http;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class HttpClientScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);

        ClassPathScanningCandidateComponentProvider scanner = new HttpClientClassPathScanner(registry);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Http.class));

        for (String basePackage : packagesToScan) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                if (candidate instanceof AnnotatedBeanDefinition) {
                    registerHttpClient(registry, (AnnotatedBeanDefinition) candidate);
                }
            }
        }
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
            metadata.getAnnotationAttributes(EnableHttpClients.class.getName()));
        Set<String> packagesToScan = new LinkedHashSet<>();
        assert attributes != null;
        packagesToScan.addAll(Arrays.asList(attributes.getStringArray("value")));
        packagesToScan.addAll(Arrays.asList(attributes.getStringArray("basePackages")));
        for (Class<?> clazz : attributes.getClassArray("basePackageClasses")) {
            packagesToScan.add(ClassUtils.getPackageName(clazz));
        }
        if (packagesToScan.isEmpty()) {
            packagesToScan.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packagesToScan;
    }

    private void registerHttpClient(BeanDefinitionRegistry registry,
                                 AnnotatedBeanDefinition beanDefinition) {
        String className = beanDefinition.getMetadata().getClassName();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .rootBeanDefinition(HttpClientFactoryBean.class);
        builder.addConstructorArgValue(className);
        builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition beanDefinitionToRegister = builder.getBeanDefinition();
        beanDefinitionToRegister.setPrimary(false);
        beanDefinitionToRegister.setLazyInit(true);
        registry.registerBeanDefinition(className, beanDefinitionToRegister);
    }
}