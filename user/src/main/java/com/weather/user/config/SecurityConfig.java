package com.weather.user.config;

import com.weather.user.security.filter.ApiCheckFilter;
import com.weather.user.security.filter.ApiLoginFilter;
import com.weather.user.security.handler.LoginSuccessHandler;
import com.weather.user.security.service.AuthUserDetailsService;
import com.weather.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
                .requestMatchers("/**").hasRole("USER"));
        http.csrf((csrf) -> csrf.disable());
        http.formLogin(login -> login.loginPage("https://weatherfit-frontend.vercel.app/login"));
        http.oauth2Login(Customizer.withDefaults());
        http.logout(Customizer.withDefaults());
        http.rememberMe(rememberMe -> rememberMe.tokenValiditySeconds(60*60*24*60).userDetailsService(authUserDetailsService));

        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);

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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ApiLoginFilter apiLoginFilter(AuthenticationManager authenticationManager) throws Exception{
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        return apiLoginFilter;
    }
}
