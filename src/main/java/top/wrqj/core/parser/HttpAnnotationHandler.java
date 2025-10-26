package top.wrqj.core.parser;

import top.wrqj.common.annotations.request.Http;
import top.wrqj.exception.HeaderException;
import top.wrqj.model.Request;
import top.wrqj.model.RequestContext;
import top.wrqj.plugins.annotation.AbstractAnnotationHandler;

import java.util.Map;

public class HttpAnnotationHandler extends AbstractAnnotationHandler<Http> {
    @Override
    protected Class<Http> getSupportedAnnotation() {
        return Http.class;
    }

    @Override
    public void doProcess(RequestContext context, Http http) {
        Request request = context.getRequest();
        request.setUrl(http.value());
        Map<String, String> headers = request.getHeaders();
        String[] defaultHeader = http.defaultHeader();
        this.parserHeaders(headers, defaultHeader);
    }

    private void parserHeaders(Map<String, String> headers, String[] value) {
        for (String item : value) {
            String[] split = item.split(":");
            if (split.length != 2) {
                throw new HeaderException("header解析失败，不支持格式：" + item, item);
            }
            headers.put(split[0].trim(), split[1].trim());
        }
    }
}
