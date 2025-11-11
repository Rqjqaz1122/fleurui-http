package com.fleurui.spring.boot.register;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.wrqj.core.HttpServiceFactory;

public class HttpClientFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private final String className;
    private Object proxy;
    private ApplicationContext applicationContext;

    public HttpClientFactoryBean(String className) {
        this.className = className;
    }

    @Override
    public Object getObject() {
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return Class.forName(this.className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Class<?> clazz = Class.forName(this.className);
        HttpServiceFactory factory = applicationContext.getBean(HttpServiceFactory.class);
        this.proxy = factory.createHttpService(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}