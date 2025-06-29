package com.fleurui.spring.boot.autoconfigure;

import com.fleurui.clients.HttpClient;
import com.fleurui.clients.NativeHttpClientAdapter;
import com.fleurui.converters.HttpConverter;
import com.fleurui.converters.json.JacksonConverter;
import com.fleurui.converters.text.TextHtmlConverter;
import com.fleurui.core.InterceptorRegister;
import com.fleurui.core.builder.HttpServiceFactory;
import com.fleurui.core.builder.register.ConverterRegister;
import com.fleurui.core.builder.register.ParserParamsRegister;
import com.fleurui.core.type.ArrayParserAdapter;
import com.fleurui.core.type.NumberParserAdapter;
import com.fleurui.core.type.ObjectParserAdapter;
import com.fleurui.core.type.ParserParams;
import com.fleurui.model.ArrayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(HttpServiceProperties.class)
public class HttpServiceAutoConfiguration {

    private final HttpServiceProperties properties;

    public HttpServiceAutoConfiguration(HttpServiceProperties properties) {
        this.properties = properties;
    }

    @Bean(name ="defaultHttpConverters")
    public BeanHodler<List<HttpConverter>> defaultHttpConverters(){
        List<HttpConverter> converters = new ArrayList<>();
        converters.add(new TextHtmlConverter());
        converters.add(new JacksonConverter());
        return new BeanHodler<>(converters);
    }

    @Bean(name = "defaultParserParamsMap")
    public BeanHodler<Map<Class<?>, ParserParams>>defaultParserParamsMap() {
        Map<Class<?>, ParserParams> map = new HashMap<>();
        map.put(Number.class, new NumberParserAdapter());
        map.put(ArrayType.class, new ArrayParserAdapter());
        map.put(Object.class, new ObjectParserAdapter());
        return  new BeanHodler<>(map);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConverterRegister converterRegister(
            @Autowired(required = false) List<HttpConverter> customHttpConverters,
            @Qualifier("defaultHttpConverters") BeanHodler<List<HttpConverter>> listBeanHodler) {
        ConverterRegister register = new ConverterRegister();
        register.addConverter(listBeanHodler.getValues());
        if (customHttpConverters != null && !customHttpConverters.isEmpty()) {
            register.addConverter(customHttpConverters);
        }
        return register;
    }

    @Bean
    @ConditionalOnMissingBean
    public ParserParamsRegister parserParamsRegister(
            @Autowired(required = false) Map<Class<?>, ParserParams> customParserParams,
            @Qualifier("defaultParserParamsMap") BeanHodler<Map<Class<?>, ParserParams>> defaultParams) {
        ParserParamsRegister register = new ParserParamsRegister();
        register.addParserParams(defaultParams.getValues());
        if (customParserParams != null && !customParserParams.isEmpty()) {
            register.addParserParams(customParserParams);
        }
        return register;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClient httpClient() {
        return new NativeHttpClientAdapter();
    }

    @Bean
    @ConditionalOnMissingBean
    public InterceptorRegister interceptorRegister() {
        return new InterceptorRegister();
    }

    @Bean
    public HttpServiceFactory httpServiceFactory(ConverterRegister converterRegister,
                                                 ParserParamsRegister parserParamsRegister,
                                                 InterceptorRegister interceptorRegister,
                                                 HttpClient httpClient) {
        HttpServiceFactory factory = new HttpServiceFactory();
        factory.setConverterRegister(converterRegister);
        factory.setParserParamsRegister(parserParamsRegister);
        factory.setInterceptorRegister(interceptorRegister);
        factory.setHttpClient(httpClient);
        return factory;
    }
}
