package com.example.aneukbeserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // 정확한 클라이언트 오리진 지정
                .allowedOrigins("http://43.203.232.54:2518")
                .allowedOrigins("https://aneuk-api.dev-lr.com")
                .allowedOrigins("https://aneuk.dev-lr.com")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
