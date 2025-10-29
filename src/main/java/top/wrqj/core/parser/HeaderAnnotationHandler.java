package top.wrqj.core.parser;

import top.wrqj.common.annotations.request.Header;
import top.wrqj.common.enums.AnnotationScope;
import top.wrqj.common.utils.HttpServiceContextHolder;
import top.wrqj.core.type.ParserParams;
import top.wrqj.core.type.ParserParamsFactory;
import top.wrqj.model.HttpServiceContext;
import top.wrqj.model.Request;
import top.wrqj.model.RequestContext;
import top.wrqj.plugins.annotation.AbstractAnnotationHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HeaderAnnotationHandler extends AbstractAnnotationHandler<Header> {
    @Override
    protected Class<Header> getSupportedAnnotation() {
        return Header.class;
    }

    @Override
    public void doProcess(RequestContext context, Header header) {
        String[] value = header.value();
        Request request = context.getRequest();
        Map<String, String> headers = request.getHeaders();
        if (context.getParameter() != null) {
            HttpServiceContext serviceContext = HttpServiceContextHolder.getContext();
            ParserParamsFactory parserParamsFactory = serviceContext.getParserParamsFactory();
            ParserParams parserParams = parserParamsFactory.getParserParams(context.getParameter().getClass());
            String key = context.getParameter().getClass().getSimpleName();
            if (value != null && value.length > 0) {
                key = value[0];
            }
            Map<String, String> parsedHeaders = parserParams.parseParamType(key, context.getParameter());
            headers.putAll(parsedHeaders);
            return;
        }
        if (value == null || value.length < 1) {
            return;
        }
        Parser.parserHeaders(context.getRequest().getHeaders(), value);
    }

    @Override
    public List<AnnotationScope> getScope() {
        return Arrays.asList(AnnotationScope.METHOD, AnnotationScope.CLASS, AnnotationScope.PARAMETER);
    }
}
