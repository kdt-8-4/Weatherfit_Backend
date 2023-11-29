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

            Cookie cookieToken = new Cookie("token", token);
            cookieToken = cookieSetting(cookieToken);
//            String cookieTokenHeader = String.format("%s; %s", cookieToken.toString(), "SameSite=None; Secure");
            response.addCookie(cookieToken);

            String userinfo = authUserDTO.getEmail() + "|" + authUserDTO.getName() + "|" + authUserDTO.getImage();
//            Cookie cookieUserinfo = new Cookie("userinfo", userinfo);
//            cookieUserinfo = cookieSetting(cookieUserinfo);
//            String test = cookieUserinfo.toString();
//            log.info(test);
            String cookieUserinfoHeader = String.format("accessToken=%s; SameSite=None; Secure", token);
            response.addHeader("Set-Cookie", cookieUserinfoHeader);

            if(authUserDTO.isFromSocial()) {
                redirectStrategy.sendRedirect(request, response, "https://weatherfit-frontend.vercel.app/");
            } else {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cookie cookieSetting(Cookie cookie) {
        cookie.setHttpOnly(false);
        cookie.setMaxAge(3 * 60 * 60);
        cookie.setSecure(true);
        cookie.setDomain("weatherfit-frontend.vercel.app");

        return cookie;
    }
}
