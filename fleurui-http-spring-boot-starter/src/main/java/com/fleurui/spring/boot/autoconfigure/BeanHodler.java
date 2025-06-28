package com.fleurui.spring.boot.autoconfigure;

public class BeanHodler<T> {

    private final T values;

    public BeanHodler(T values) {
        this.values = values;
    }

    public T getValues(){
        return this.values;
    }
}
