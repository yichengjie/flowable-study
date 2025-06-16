package com.yicj.web.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // setAllowedOrigins
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径应用跨域配置
            .allowedOrigins("http://localhost:63342") // 允许来自http://example.com的请求
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的方法
            .allowedHeaders("*") // 允许的头部信息，可以使用"*"表示所有头部信息
            .allowCredentials(true) // 是否允许发送Cookie信息，这对于前端携带Cookie非常重要
            .maxAge(3600); // 预检请求的缓存时间（秒）
    }
}
