package com.weather.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.user.security.dto.AuthUserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
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
    private PasswordEncoder passwordEncoder;

    public LoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("인증에 성공했습니다.");

        log.info(authentication);

        AuthUserDTO authUserDTO = (AuthUserDTO) authentication.getPrincipal();
        boolean fromSocial = authUserDTO.isFromSocial();
        String name = authUserDTO.getName();

        log.info(authUserDTO);

        if(fromSocial && name == null) {
            redirectStrategy.sendRedirect(request, response, "https://weatherfit-frontend.vercel.app");
        } else  {
            String result = objectMapper.writeValueAsString(authUserDTO);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(result);
        }
    }
}
