package com.weather.user.controller;

import com.weather.user.dto.MailCodeDTO;
import com.weather.user.dto.UserDTO;
import com.weather.user.service.MailService;
import com.weather.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login/google/token")
    public ResponseEntity<JSONObject> googleToken(@RequestBody String token) throws Exception {
        log.info("token: " + token.toString());

        JSONObject result = new JSONObject();
        result.put("result", true);

        return new ResponseEntity<>(result, HttpStatus.OK);
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
    public ResponseEntity<JSONObject> signup(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        userService.signup(userDTO);

        JSONObject result = new JSONObject();
        result.put("result", true);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/api/profile")
    public ResponseEntity<UserDTO> profile(@RequestBody UserDTO userDTO) {
        log.info("userDTO: " + userDTO);

        UserDTO result = userService.profile(userDTO.getEmail());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/api/profile/modify")
    public ResponseEntity<UserDTO> modify(
            @RequestPart("file") MultipartFile file,
            @RequestPart("userDTO") UserDTO userDTO) {
        log.info("userDTO: " + userDTO);
        log.info("file: " + file);

        UserDTO result = userService.modify(userDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/api/profile/remove/{email}")
    public ResponseEntity<JSONObject> remove(@PathVariable String email) {
        log.info("email: " + email);

        userService.remove(email);

        JSONObject result = new JSONObject();
        result.put("result", true);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
