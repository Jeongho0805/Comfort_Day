package com.jeongho.portfolio.interceptor;

import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MemberInfoInterceptor implements HandlerInterceptor {
    @Autowired
    MemberService memberService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Member findMember = memberService.findMemberBySession(request);
        if(findMember != null && modelAndView != null) {
            String name = findMember.getName();
            // 회원 이름 디버깅 및 호출 정상 여부파악
            log.info("postHandle 호출, memberName = {} ", name);
            modelAndView.addObject("memberName", name);
        }
    }
}
