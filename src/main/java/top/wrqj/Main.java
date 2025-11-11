package top.wrqj;

import lombok.Data;
import top.wrqj.common.annotations.method.Get;
import top.wrqj.common.annotations.method.Post;
import top.wrqj.common.annotations.request.Header;
import top.wrqj.common.annotations.request.Http;
import top.wrqj.common.annotations.request.Params;
import top.wrqj.common.annotations.request.PathParam;
import top.wrqj.common.enums.AnnotationScope;
import top.wrqj.core.builder.HttpServiceBuilder;
import top.wrqj.model.RequestContext;
import top.wrqj.plugins.annotation.AbstractAnnotationHandler;
import top.wrqj.plugins.annotation.AnnotationHandlerRegister;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        AnnotationHandlerRegister annotationHandlerRegister = new AnnotationHandlerRegister();
        annotationHandlerRegister.registerAnnotationHandler(new DemoHandler());
        Demo build = HttpServiceBuilder.create()
                .setAnnotationHandlerRegister(annotationHandlerRegister)
                .build(Demo.class);
        DemoEntity address = build.getAddress("223.167.235.221", true);
        System.out.println(address);
//        long startTime = System.currentTimeMillis();
//        String demo = build.getDemo(1901674407231488L, 1);
//        long endTime = System.currentTimeMillis();
//        System.out.println(demo);
//        System.out.println(endTime - startTime);
//        long startTime1 = System.currentTimeMillis();
//        String demo1 = build.getDemo(1901674407231488L, 2);
//        long endTime1 = System.currentTimeMillis();
//        System.out.println(demo1);
//        System.out.println(endTime1 - startTime1);

    }

    @Data
    public static class DemoEntity {
        private String city;
        private String cityCode;
        private String addr;
        private String ip;
    }

    public static class DemoHandler extends AbstractAnnotationHandler<DemoIn> {

        @Override
        protected Class<DemoIn> getSupportedAnnotation() {
            return DemoIn.class;
        }

        @Override
        public void doProcess(RequestContext context, DemoIn annotation) {
            System.out.println(context);
        }

        @Override
        public List<AnnotationScope> getScope() {
            return Collections.singletonList(AnnotationScope.PARAMETER);
        }
    }

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface DemoIn {

    }

    @Http("http://whois.pconline.com.cn/ipJson.jsp")
    interface Demo {
        @Get("{id}")
        String getDemo(@PathParam("id") Long id, @Params @DemoIn int is);

        @Get
        DemoEntity getAddress(@Params("ip") String ip, @Params("json") Boolean isJson);
    }

}
