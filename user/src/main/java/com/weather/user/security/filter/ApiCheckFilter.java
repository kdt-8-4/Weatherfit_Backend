package com.weather.user.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

        boolean checkHeader = checkAuthHeader(request);

        if(checkHeader) {
            filterChain.doFilter(request, response);
            return;
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkAuthHeader(HttpServletRequest request) {
        boolean result = false;

        String header = request.getHeader("Authorization");

        if(StringUtils.hasText(header)) {
            if(header.equals("weatherfit")) {
                result = true;
            }
        }

        return result;
    }
}