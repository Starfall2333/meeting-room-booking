package com.campus.meeting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有接口
                        .allowedOrigins("http://localhost:5173") // 允许你的Vue项目地址
                        .allowedMethods("*") // 允许所有请求方法
                        .allowedHeaders("*");
            }
        };
    }
}
