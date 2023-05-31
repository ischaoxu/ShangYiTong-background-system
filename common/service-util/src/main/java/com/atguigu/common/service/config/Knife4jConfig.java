package com.atguigu.common.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean
    public Docket adminApiConfig() {

        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("尚医通 APIs")
                        .description("本文档描述了尚医通系统接口")
                        .contact("admin@atguigu.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("管理平台")
                .select()
                .paths(PathSelectors.regex("/admin/.*"))
                .build();

        return docket;
    }
}