package com.weather.user.controller;

import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import com.weather.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log4j2
@RequiredArgsConstructor
public class WeatherController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<Long> signup(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        Long id = userService.register(userDTO);
        log.info("id: " + id);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
