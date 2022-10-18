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
                .excludePathPatterns("/", "/practice", "/member/new", "/member/login", "/board/list", "/board/dtl/**", "/css/**", "/static/css/layout1.css", "/*.css", "/templates/layouts/layout1.html", "/*.ico", "/resources/**");

        registry.addInterceptor(new MemberInfoInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/*.css", "/*.ico", "/css/layout1.css");

    }
}
    // 인터셉터에서 서비스 레이어를 사용하기 위한 Bean 생성, ex) Bean없이 new를 통한 객체생성 시, SrpingContainer 관리 범위에 들지 못한다.
    // -> 빈 활용한 등록시 해당 인터셉터가 2번 호출되는 문제 발생
//    @Bean
//    public MemberInfoInterceptor memberInfoInterceptor() {
//        return new MemberInfoInterceptor();
//    }

