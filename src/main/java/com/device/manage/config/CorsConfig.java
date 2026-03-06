package com.device.manage.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 解决 Spring5.3.31 版本 WebMvcConfigurer 跨域配置失效+报错问题
 * Servlet级别过滤器，优先级最高，100%生效
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建跨域配置对象
        CorsConfiguration config = new CorsConfiguration();

        // ✅ 允许所有源跨域 + 兼容 携带凭证(cookie/token)，彻底避开 * 号的坑
        config.addAllowedOriginPattern("*");

        // ✅ 允许所有请求方式
        config.addAllowedMethod("*");

        // ✅ 允许所有请求头（自定义头、token、cookie都放行）
        config.addAllowedHeader("*");

        // ✅ 核心：允许前端携带凭证（cookie、token、session），保留你的核心需求
        config.setAllowCredentials(true);

        // ✅ 预检请求缓存时间，减少OPTIONS请求，提升性能
        config.setMaxAge(3600L);

        // 2. 配置拦截所有请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // 3. 返回过滤器实例
        return new CorsFilter(source);
    }
}
