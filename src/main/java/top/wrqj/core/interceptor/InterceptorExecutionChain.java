package top.wrqj.core.interceptor;

import java.util.LinkedList;
import java.util.List;

public class InterceptorExecutionChain {

    private final List<InterceptorHandler> interceptorList = new LinkedList<>();

    public void addInterceptor(InterceptorHandler interceptor) {
        interceptorList.add(interceptor);
    }

    public List<InterceptorHandler> getInterceptorList() {
        return interceptorList;
    }
}
