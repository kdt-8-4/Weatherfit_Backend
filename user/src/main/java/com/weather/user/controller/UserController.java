package com.weather.user.controller;

import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import com.weather.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login/social/google")
    public String googleLogin(){
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        return "redirect:/oauth2/authorization/google";
    }

    @PostMapping("/signup/email")
    public ResponseEntity<JSONObject> verifyEmail(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        boolean verifyEmail = userService.verifyEmail(userDTO.getEmail());

        JSONObject result = new JSONObject();
        result.put("result", verifyEmail);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/signup/nickname")
    public ResponseEntity<JSONObject> verifyNickname(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        boolean verifyNickname = userService.verifyNickname(userDTO.getNickname());

        JSONObject result = new JSONObject();
        result.put("result", verifyNickname);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        userService.signup(userDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/profile")
    public ResponseEntity<UserDTO> profile(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        UserDTO result = userService.profile(userDTO.getEmail());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/profile/modify")
    public ResponseEntity<UserDTO> modify(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        UserDTO result = userService.modify(userDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/profile/remove/{email}")
    public ResponseEntity remove(@PathVariable String email) {
        log.info("email: " + email);

        userService.remove(email);

        return new ResponseEntity(HttpStatus.OK);
    }
}
