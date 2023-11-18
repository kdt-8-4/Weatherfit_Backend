package com.weather.user.repository;

import com.weather.user.entity.User;
import com.weather.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class UserTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void insertUser() {
        System.out.println(userRepository.getClass().getName());
        IntStream.rangeClosed(1, 100).forEach(i -> {
            User user = User.builder()
                    .email("user" + i + "@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .name("USER" + i)
                    .nickname("테스터")
                    .fromSocial(false)
                    .status(true)
                    .build();

            if(i < 95) {
                user.addRole(UserRole.USER);
            } else {
                user.addRole(UserRole.ADMIN);
            }

            userRepository.save(user);
        });
    }
}
