package com.example.demo.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // 세션에 사용자 아이디 저장(id)
        String userId = authentication.getName();
        request.getSession().setAttribute("id", userId); // 세션에 아이디 저장
        String rememberMe = request.getParameter("rememberMe");

        if (rememberMe != null) {
            Cookie cookie = new Cookie("id", userId);
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7일간 유지
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("id", "");
            cookie.setMaxAge(0); // 쿠키 삭제
            response.addCookie(cookie);
        }

        // 💥 기본 페이지로 리다이렉트
        response.sendRedirect("/");
    }
}
