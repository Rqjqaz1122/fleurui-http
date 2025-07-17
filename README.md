# Fleurui Http
## 简介
Fleurui Http 是一个基于Java注解实现的轻量级HTTP请求框架，灵感来源于Feign，旨在简化HTTP客户端的开发工作。通过简单的注解配置，开发者可以快速定义和发起HTTP请求，无需编写繁琐的HTTP连接代码。
## 特性
基于注解配置HTTP请求

支持常见的HTTP方法（GET, POST, PUT, DELETE等）

支持请求参数自动处理

支持请求头和请求体的灵活配置

支持同步和异步请求

轻量级，无复杂依赖
## 快速开始
### 引入依赖

```xml
<dependency>
    <groupId>com.fleurui</groupId>
    <artifactId>fleurui-http</artifactId>
    <version>0.0.1-beta</version>
</dependency>
```

### 基本用法

```java
@Http("https://www.api.wrqj.top/blog/web")
public interface ExampleApi {

    @Get("/article/{id}")
    @HttpServer()
    String getArticleInfo(@PathParam("id") Long id);

    @Post
    @Header({"Content-Type:application/json"})
    void postDemo(@Body User user,@Header("Authorization") String token);
}
```

### 接口调用方式

```java
// 创建接口实例
ExampleApi exmapleApi = HttpServiceBuilder.builder().build(ExampleApi.class);
//调用接口方法
String articleInfo = build.getArticleInfo(1889267805298688L);
```

## 注解说明

|    注解     | 说明                                                         | 参数描述                                                     |
| :---------: | :----------------------------------------------------------- | :----------------------------------------------------------- |
| @HttpServer | HttpServer用于标识接口方法请求方式，默认为GET请求，当前注解目前只可标记接口请求方式，无法配置请求URI。使用请求推荐使用@Get、@Post、@Delete、@Put。 | value：表示方法的请求方式，默认GET请求。                     |
|    @Get     | Get标识接口方法请求方式为GET请求                             | value：设置请求方法的URI                                     |
|    @Post    | Post标识接口方法请求方式为Post请求                           | value：使用方法同上                                          |
|   @Delete   | Delete标识接口方法请求方式为Delete请求                       | value：使用方法同上                                          |
|    @Put     | Delete标识接口方法请求方式为Delete请求                       | value：使用方法同上                                          |
|   @Header   | Header可设置当前请求的请求头，可在方法上或形参中使用         | value：数组类型，可方法中只可配置固定参数，在形参中会根据参数值动态设置 |
|   @Params   | Params用于方法形参中，标识参数为URL中的请求参数，既 ？后的参数，如果Params标识Object类型，key值默认为属性名称 | value：设置请求参数key，为Object类型可不设置                 |
| @PathParam  | PathParam适用于RESTful风格的API，与SpringMVC中的@PathVariable使用相同，但必须指定URI中对应的名称 | value：指定URI中的名称，匹配参数值                           |
|    @Http    | Http标识接口为可被Fleurui Http识别的接口                     | value：设置请求URL<br />defaultHeadler：同@Header，同样也是配置请求头，但作用域为整个接口所有的请求方法 |

## 更多功能

### 自定义请求拦截器

```java
// 注册拦截器
InterceptorRegister interceptorRegister = new InterceptorRegister();
interceptorRegister.addInterceptor(new InterceptorHandler() {
            @Override
            public void beforeExecution(Request request) {
                System.out.println(request.getUrl());
            }

            @Override
            public void afterExecution(Response response) {

            }
        });
//创建实例
ExampleApi build = HttpServiceBuilder.builder()
                .setInterceptorRegister(interceptorRegister)
                .build(ExampleApi.class);
```

> 注：在Fleurui Http项目中本身只是用于发起Http请求功能，所以拦截器并不会有放行请求的概念。开发者可在请求前置中设置Request请求参数等信息。

### 自定义HTTP请求引擎

在Fleurui Http中，默认使用的原生**HttpURLConnection**实现请求，当然开发者也可自定义HTTP引擎，实现方法如下

```java
// 实现top.wrqj.clients.HttpClientt接口
public class ApacheHttpClientAdapter implements HttpClient{
    @Override
    public Response execute(Request request) throws IOException {
       //具体实现省略...
       return null;
    }
}
//配置HttpClient实例
ExampleApi build = HttpServiceBuilder.builder()
                .setHttpClient(new ApacheHttpClientAdapter())
                .build(ExampleApi.class);
```

### 自定义请求/响应参数转换器

在Fleurui Http中，为了解决各种类型请求入参与响应参数，例如JSON、XML等，开发者可自行添加转换器。

在请求在执行过程中框架会自动根据请求头Content-Type获取所适配的转换器解析响应数据，具体实现方法如下

```java
// 实现top.wrqj.converters.HttpConverter接口
public class JacksonConverter implements HttpConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public <T> T read(InputStream inputStream,Class<T> clazz) throws IOException {

        return null;
    }

    @Override
    public <T> T read(byte[] bytes,Class<T> clazz) throws IOException {
        if (clazz == String.class) {
            return clazz.cast(new String(bytes));
        }
        return mapper.readValue(bytes, clazz);
    }

    @Override
    public void write(Object value, OutputStream output) {
        try {
            mapper.writeValue(output, value);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write object to output stream", e);
        }
    }
}

// 注册适配器
ConverterRegister converterRegister = new ConverterRegister();
converterRegister.addConverter(new JacksonConverter());
ExampleApi build = HttpServiceBuilder.builder()
                .setConverterRegister(converterRegister)
                .build(ExampleApi.class);
```

> 当前示例为Fleurui Http中默认采用的JSON格式转换器，开发者通过实现HttpConverter接口中的方法，可定制请求入参以及响应参数，Fleurui Http按照getContentType()方法return的参数来获取转换器，当getContentType()存在相同的返回参数时，新添加的转换器会覆盖之前的转换器。

