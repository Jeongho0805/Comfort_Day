package com.jeongho.portfolio.config;

import com.jeongho.portfolio.interceptor.LoginCheckInterceptor;
import com.jeongho.portfolio.interceptor.MemberInfoInterceptor;
import org.springframework.context.annotation.Bean;
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
                .excludePathPatterns("/", "/member/new", "/member/login", "/board/list", "/board/dtl/**","/css/**");

        registry.addInterceptor(memberInfoInterceptor())
                .order(2)
                .addPathPatterns("/**");
    }
    // 인터셉터에서 서비스 레이어를 사용하기 위한 Bean 생성, ex) Bean없이 new를 통한 객체생성 시, SrpingContainer 관리 범위에 들지 못한다.
    @Bean
    public MemberInfoInterceptor memberInfoInterceptor() {
        return new MemberInfoInterceptor();
    }
}
