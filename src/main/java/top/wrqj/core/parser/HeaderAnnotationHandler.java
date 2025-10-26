package top.wrqj.core.parser;

import top.wrqj.common.annotations.request.Header;
import top.wrqj.model.RequestContext;
import top.wrqj.plugins.annotation.AbstractAnnotationHandler;

public class HeaderAnnotationHandler extends AbstractAnnotationHandler<Header> {
    @Override
    protected Class<Header> getSupportedAnnotation() {
        return Header.class;
    }

    @Override
    public void doProcess(RequestContext context, Header header) {
        String[] headers = header.value();
        Parser.parserHeaders(context.getRequest().getHeaders(), headers);
    }
}
