package top.wrqj.core.parser;

import top.wrqj.common.annotations.request.Body;
import top.wrqj.common.utils.HttpServiceContextHolder;
import top.wrqj.converters.AbstractConverterFactory;
import top.wrqj.converters.HttpConverter;
import top.wrqj.model.HttpServiceContext;
import top.wrqj.model.Request;
import top.wrqj.model.RequestContext;
import top.wrqj.plugins.annotation.AbstractAnnotationHandler;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class BodyAnnotationHandler extends AbstractAnnotationHandler<Body> {
    @Override
    protected Class<Body> getSupportedAnnotation() {
        return Body.class;
    }

    @Override
    public void doProcess(RequestContext context, Body body) {
        Request request = context.getRequest();
        Map<String, String> headers = request.getHeaders();
        String contentType = headers.get("Content-Type");
        HttpServiceContext serviceContext = HttpServiceContextHolder.getContext();
        AbstractConverterFactory converterFactory = serviceContext.getAbstractConverterFactory();
        if (contentType == null || contentType.isEmpty()) {
           contentType = "application/json";
        }
        HttpConverter converter = converterFactory.getConverter(contentType);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        converter.write(context.getParameter(), output);
        request.setBody(output.toByteArray());
    }
}
