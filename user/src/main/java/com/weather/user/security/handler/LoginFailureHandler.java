package com.weather.user.security.handler;

import com.weather.user.entity.User;
import com.weather.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.Optional;

@Log4j2
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");

        JSONObject json = new JSONObject();

        String email = request.getParameter("email");
        log.info(email);

        Optional<User> user = userRepository.findByEmail(email, false);

        if(user.isEmpty()) {
            json.put("code", "401");
            json.put("message", "존재하지 않는 이메일입니다.");

        } else  {
            json.put("code", "401");
            json.put("message", "비밀번호가 일치하지 않습니다.");
        }

        response.getWriter().write(String.valueOf(json));
    }
}
