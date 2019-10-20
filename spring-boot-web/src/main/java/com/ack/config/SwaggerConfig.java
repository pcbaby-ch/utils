package com.ack.config;

import com.ack.utils.PropertiesUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @ClassName: SwaggerConfig
 * @Description:Swagger配置
 * @author: zhaop
 * @date: 2018/5/25 10:31
 * @Version: 1.0
 */
@EnableSwagger2
@Configuration
@EnableWebMvc
public class SwaggerConfig extends WebMvcConfigurerAdapter {


    private static final Contact contact = new Contact("赵鹏程", "", "zhao.pengcheng@cenntchain.com");

    private String swaggerSwitch = PropertiesUtils.getProperty("swagger.switch","0");

    private Predicate<String> select = "1".equalsIgnoreCase(swaggerSwitch) ? PathSelectors.any() : PathSelectors.none();


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public HttpMessageConverter responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Bean
    public HttpMessageConverter jacksonConverter() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
        converters.add(jacksonConverter());
    }

    /**
     * swagger文档分组信息
     * @return
     */
    @Bean
    public Docket pointsPresentDocket() {

        return new Docket(DocumentationType.SWAGGER_2).groupName("积分转赠")
                .apiInfo(pointsPresentApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jfmore.controller"))
                .paths(select)
                .build();
    }

    private ApiInfo pointsPresentApiInfo() {
        return new ApiInfoBuilder()
                .title("积分转赠相关接口")
                .description("本文档描述了积分转赠相关部分的接口文档")
                .contact(contact)
                .version("1.0.1")
                .build();
    }



}


