package com.weather.user.security.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
    GsonJsonParser gsonJsonParser = new GsonJsonParser();

    public ApiLoginFilter(String defaultFilterProcessUrl) {
        super(defaultFilterProcessUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        String body = request.getReader().lines().collect(Collectors.joining());
        log.info("body: " + body);

        Map<String, Object> bodyMap = gsonJsonParser.parseMap(body);
        String email = (String) bodyMap.get("email");
        String password = (String) bodyMap.get("password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        log.info("token: " + token);

        return getAuthenticationManager().authenticate(token);
    }
}
