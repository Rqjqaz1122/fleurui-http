package com.fleurui.core.parser;

import com.fleurui.model.Request;

import java.lang.reflect.Method;

public interface Parser {

    void parser(Request request, Method method,Object[] args);
}
