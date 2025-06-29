package com.fleurui.core;

import com.fleurui.core.interceptor.InterceptorExecutionChain;
import com.fleurui.core.interceptor.InterceptorHandler;

public class InterceptorRegister {

    private final InterceptorExecutionChain interceptorExecutionChain;

    public InterceptorRegister(){
        this.interceptorExecutionChain = new InterceptorExecutionChain();
    }

    public InterceptorRegister addInterceptor(InterceptorHandler interceptorHandler) {
        interceptorExecutionChain.addInterceptor(interceptorHandler);
        return this;
    }

    protected InterceptorExecutionChain getInterceptorExecutionChain() {
        return interceptorExecutionChain;
    }
}