package com.weather.user.security;

import com.weather.user.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JWTTest {
    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore() {
        System.out.println("testBefore.................");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() throws Exception {
        String email = "user95@test.com";

        String str = jwtUtil.generateToken(email);

        System.out.println(str);

        Thread.sleep(5000);

        String resultEmail = jwtUtil.validateAndExtract(str);

        System.out.println(resultEmail);
    }
}
