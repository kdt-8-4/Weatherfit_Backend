package com.weather.user.repository;

import com.weather.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class UserTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void insertUser() {
        System.out.println(userRepository.getClass().getName());
        IntStream.rangeClosed(1, 100).forEach(i -> {
            User user = User.builder()
                    .email("user" + i + "@testmail.com")
                    .password("1234")
                    .name("USER" + i)
                    .build();

            userRepository.save(user);
        });
    }
}
