package top.wrqj.core.parser;

import top.wrqj.common.annotations.request.PathParam;
import top.wrqj.common.utils.UrlTemplateUtils;
import top.wrqj.model.Request;
import top.wrqj.model.RequestContext;
import top.wrqj.plugins.annotation.AbstractAnnotationHandler;

public class PathParamAnnotationHandler extends AbstractAnnotationHandler<PathParam> {

    @Override
    protected Class<PathParam> getSupportedAnnotation() {
        return PathParam.class;
    }

    @Override
    public void doProcess(RequestContext context, PathParam pathParam) {
        Request request = context.getRequest();
        String matchUri = UrlTemplateUtils.ofTemplate(request.getUri())
                .matching(pathParam.value(), context.getParameter().toString());
        request.setUri(matchUri);
    }

}
