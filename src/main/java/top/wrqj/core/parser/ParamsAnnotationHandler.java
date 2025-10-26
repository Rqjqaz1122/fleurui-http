package top.wrqj.core.parser;

import top.wrqj.common.annotations.request.Params;
import top.wrqj.common.utils.HttpServiceContextHolder;
import top.wrqj.core.type.ParserParams;
import top.wrqj.core.type.ParserParamsFactory;
import top.wrqj.model.HttpServiceContext;
import top.wrqj.model.RequestContext;
import top.wrqj.plugins.annotation.AbstractAnnotationHandler;

import java.util.Map;

public class ParamsAnnotationHandler extends AbstractAnnotationHandler<Params> {
    @Override
    protected Class<Params> getSupportedAnnotation() {
        return Params.class;
    }

    @Override
    public void doProcess(RequestContext context, Params params) {
        HttpServiceContext serviceContext = HttpServiceContextHolder.getContext();
        ParserParamsFactory paramsFactory = serviceContext.getParserParamsFactory();
        ParserParams parserParams = paramsFactory.getParserParams(context.getParameter().getClass());
        if (parserParams == null) {
            return;
        }
        String key = params.value();
        if (key == null || key.isEmpty()) {
            key = context.getParameter().getClass().getName();
        }
        Map<String, String> paramsMap = parserParams.parseParamType(key, context.getParameter());
        context.getRequest().setParams(paramsMap);
    }
}
