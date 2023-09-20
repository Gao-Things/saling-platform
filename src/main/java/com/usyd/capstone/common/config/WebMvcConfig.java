package com.usyd.capstone.common.config;

import com.usyd.capstone.common.compents.RequestRateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestRateLimitInterceptor requestRateLimitInterceptor;

    @Autowired
    public WebMvcConfig(RequestRateLimitInterceptor requestRateLimitInterceptor) {
        this.requestRateLimitInterceptor = requestRateLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册你的自定义拦截器
        registry.addInterceptor(requestRateLimitInterceptor)
                .addPathPatterns("/public/product/productList"); // 拦截指定的API路径
    }

}
