package com.weather.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.user.security.dto.AuthUserDTO;
import com.weather.user.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private ObjectMapper objectMapper = new ObjectMapper();
    private JWTUtil jwtUtil;

    public LoginSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("인증에 성공했습니다.");

        log.info(authentication);

        AuthUserDTO authUserDTO = (AuthUserDTO) authentication.getPrincipal();
        log.info(authUserDTO);

        try {
            String token = jwtUtil.generateToken(authUserDTO.getNickname());
            String result = objectMapper.writeValueAsString(authUserDTO);
            result = result.replace("}", ", \"token\": \"" + token + "\"}");

            Cookie cookie = new Cookie("result", result);
            cookie.setHttpOnly(true); // JavaScript를 통한 쿠키 접근을 막기 위해 사용
            cookie.setMaxAge(3 * 60 * 60);
            response.addCookie(cookie);

            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(result);

            redirectStrategy.sendRedirect(request, response, "https://weatherfit-frontend.vercel.app/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
