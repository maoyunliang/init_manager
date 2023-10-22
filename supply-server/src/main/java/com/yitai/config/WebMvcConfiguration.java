package com.yitai.config;

import com.yitai.interceptor.JwtTokenAdminInterceptor;
import com.yitai.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


import java.util.List;

/**
 * ClassName: WebMvcConfiguration
 * Package: com.yitai.config
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 13:54
 * @Version: 1.0
 */

@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     *
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/sys/user/login");
    }
    //    @Bean
//    public Docket docket(){
//        log.info("开始生成接口文档");
//        ApiInfo apiInfo = new ApiInfoBuilder()
//                .title("供应链系统项目接口文档")
//                .version("1.0")
//                .description("供应链系统系统项目接口文档")
//                .build();
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.yitai.controller"))
//                .paths(PathSelectors.any())
//                .build();
//        return docket;
//    }
//
//


//    设置静态资源映射
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.18.1/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展mvc框架消息的转换器
     *
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters){
        log.info("开始扩展消息转换器");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器,将java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自定义的消息转换器添加进去
        converters.add(1,converter);
//        converters.add(converter);
    }
}

