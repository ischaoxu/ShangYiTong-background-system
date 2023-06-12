package com.atguigu.syt.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author liuzhaoxu
 * @date 2023年06月12日 17:54
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); //所有源服务器
        config.addAllowedHeader("*"); //所有请求头
        config.addAllowedMethod("*"); //所有方法：GET、POST、PUT、DElETE
        config.setAllowCredentials(true);//cookie可跨域
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config); //所有路径
        // cors过滤器
        return new CorsWebFilter(source);
    }

}
