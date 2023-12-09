package com.weather.user.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void passwordTest() {
        boolean result = passwordEncoder.matches("1234", "$2a$10$NIaB9zr7gVuSEkHT41FaaeyT8bo7k4qnGPfAaxcj6SPWH6URIYzZW");

        System.out.println(result);
    }
}
