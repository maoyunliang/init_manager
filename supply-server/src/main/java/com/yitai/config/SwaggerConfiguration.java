package com.yitai.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: SwaggerConfiguration
 * Package: com.yitai.config
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/22 2:47
 * @Version: 1.0
 */
@Configuration
@OpenAPIDefinition
@Slf4j
public class SwaggerConfiguration {
    //通过knife4j生成接口文档
    @Bean
    public OpenAPI springShopOpenAPI() {
        log.info("开始生成静态文档");
        return new OpenAPI()
                .info(new Info().title("供应链系统项目接口文档")
                        //描叙
                        .description("供应链系统项目接口文档")
                        //版本
                        .version("v1")
                        //作者信息，自行设置
                        .contact(new Contact().name("mao").email("18879182511@163.com").url("https://www.baidu.com"))
                        //设置接口文档的许可证信息
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
