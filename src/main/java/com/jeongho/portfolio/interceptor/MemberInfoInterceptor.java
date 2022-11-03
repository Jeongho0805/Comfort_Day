package com.jeongho.portfolio.interceptor;

import com.jeongho.portfolio.constant.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class MemberInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("requestURI = {}", requestURI);
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute(SessionConst.LOGIN_MEMBER_NAME) != null) {
            String memberName = (String) session.getAttribute(SessionConst.LOGIN_MEMBER_NAME);
            log.info("postHandle 호출, memberName = {} ", memberName);
            log.info("modelAndView null포인트 오류 확인 -> {}", modelAndView);
            // comment/new는 api호출 및 응답이므로 modelandView가 없어서 null포인트 오류가 터진다.
            if(!requestURI.equals("/comment/new")) {
                modelAndView.addObject("memberName", memberName);
            }
        }
    }
}


//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//        // 2번 호출 이유 디버깅
//        String requestURI = request.getRequestURI();
//        log.info("requestUri -> {} ", requestURI);

//        HttpSession session = request.getSession();
//        if(session != null) {
//            if(session.getAttribute(SessionConst.LOGIN_MEMBER_NAME) != null) {
//                String memberName = (String) session.getAttribute(SessionConst.LOGIN_MEMBER_NAME);
//                log.info("postHandle 호출, memberName = {} ", memberName);
//                log.info("modelAndView null포인트 오류 확인 -> {}", modelAndView);
//                modelAndView.addObject("memberName", memberName);

//        if(session != null && session.getAttribute(SessionConst.LOGIN_MEMBER_NAME) != null) {
//            String memberName = (String) session.getAttribute(SessionConst.LOGIN_MEMBER_NAME);
//            log.info("postHandle 호출, memberName = {} ", memberName);
//            log.info("modelAndView null포인트 오류 확인 -> {}", modelAndView);
//            modelAndView.addObject("memberName", memberName);
//        }
//    }

