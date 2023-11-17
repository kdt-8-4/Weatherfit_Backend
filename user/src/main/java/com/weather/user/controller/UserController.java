package com.weather.user.controller;

import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import com.weather.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signin")
    public ResponseEntity<Optional<UserDTO>> signin(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        Optional<UserDTO> result = userService.signin(userDTO.getEmail(), userDTO.getPassword(), userDTO.isFromSocial());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/user/signup")
    public ResponseEntity signup(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        userService.signup(userDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/profile")
    public ResponseEntity<UserDTO> profile(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        UserDTO result = userService.profile(userDTO.getEmail());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/user/profile/modify")
    public ResponseEntity modify(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/user/profile/remove")
    public ResponseEntity remove(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        userService.remove(userDTO.getEmail());

        return new ResponseEntity(HttpStatus.OK);
    }
}
