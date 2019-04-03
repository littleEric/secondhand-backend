package com.lyy.secondhand.config;

import com.lyy.secondhand.aop.AuthTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/29
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authTokenInterceptor()).addPathPatterns("/**");//拦截所有请求，通过判断是否有 @AuthToken 注解 决定是否需要验证token
    }

    @Bean
    public AuthTokenInterceptor authTokenInterceptor(){
        return new AuthTokenInterceptor();
    }
}
