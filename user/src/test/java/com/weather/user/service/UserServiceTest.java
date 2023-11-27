package com.weather.user.service;

import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import com.weather.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void signupTest() {
        UserDTO userDTO = UserDTO.builder()
                .email("servicetester1" + "@test.com")
                .password("1235")
                .name("tester")
                .nickname("테스터")
                .build();

        userService.signup(userDTO);
    }
}
