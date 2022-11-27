package com.jeongho.portfolio.interceptor;

import com.jeongho.portfolio.constant.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 여부 파악 {}", requestURI);

        HttpSession session = request.getSession(false);

        log.info("세션정보 확인 -> {}",  session.getAttribute(SessionConst.LOGIN_MEMBER));

        if (StringUtils.contains(requestURI, "/api")) {
            if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                return false;
            }
        }

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
}
