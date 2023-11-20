package com.weather.user.config;

import com.weather.user.security.filter.ApiCheckFilter;
import com.weather.user.security.handler.LoginSuccessHandler;
import com.weather.user.security.service.AuthUserDetailsService;
import com.weather.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthUserDetailsService authUserDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/**").permitAll());
        http.csrf((csrf) -> csrf.disable());
        http.formLogin(login -> login.successHandler(loginSuccessHandler()));
        http.oauth2Login(login -> login.successHandler(loginSuccessHandler()));
        http.logout(Customizer.withDefaults());
        http.rememberMe(rememberMe -> rememberMe.tokenValiditySeconds(60*60*24*60).userDetailsService(authUserDetailsService));

        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter();
    }
}
