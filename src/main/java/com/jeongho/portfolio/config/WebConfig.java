package com.jeongho.portfolio.config;

import com.jeongho.portfolio.interceptor.LoginCheckInterceptor;
import com.jeongho.portfolio.interceptor.MemberInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/api/new-member","/api/login","/api/logout","/api/board/list-all", "/api/board/list-page",
                        "/practice", "/member/new", "/member/login", "/board/list", "/board/dtl/**", "/css/**", "/static/css/layout1.css",
                        "/*.css", "/templates/layouts/layout1.html", "/*.ico", "/resources/**","/comment/update");

        registry.addInterceptor(new MemberInfoInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/*.css","/api/**", "/*.ico", "/css/layout1.css","/comment/update");
    }
}


