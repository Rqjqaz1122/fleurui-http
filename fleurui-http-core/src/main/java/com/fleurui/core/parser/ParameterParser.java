package com.fleurui.core.parser;

import com.fleurui.model.Request;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 参数解析器
 */
public class ParameterParser {

    public static void parser(Request request, Method method) {
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {

        }
    }

}
