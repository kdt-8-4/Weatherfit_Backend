package com.weather.user.controller;

import com.weather.user.dto.GoogleUserDTO;
import com.weather.user.dto.MailCodeDTO;
import com.weather.user.dto.UserDTO;
import com.weather.user.service.MailService;
import com.weather.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login/google")
    public ResponseEntity<JSONObject> google(@RequestBody GoogleUserDTO googleUserDTO) throws Exception {
        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ googleToken controller ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("googleUserDTO: " + googleUserDTO);

        JSONObject result = new JSONObject();
        result.put("result", true);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/signup/email")
    public ResponseEntity<JSONObject> verifyEmail(@RequestBody UserDTO userDTO) {
        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ verifyEmail controller ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("userDTO: " + userDTO);

        boolean verifyEmail = userService.verifyEmail(userDTO.getEmail());

        JSONObject result = new JSONObject();
        result.put("result", verifyEmail);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/signup/nickname")
    public ResponseEntity<JSONObject> verifyNickname(@RequestBody UserDTO userDTO) {
        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ verifyNickname controller ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("userDTO: " + userDTO);

        boolean verifyNickname = userService.verifyNickname(userDTO.getNickname());

        JSONObject result = new JSONObject();
        result.put("result", verifyNickname);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<JSONObject> signup(@RequestBody UserDTO userDTO) {
        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ signup controller ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("userDTO: " + userDTO);

        userService.signup(userDTO);

        JSONObject result = new JSONObject();
        result.put("result", true);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/api/profile")
    public ResponseEntity<UserDTO> profile(@RequestBody UserDTO userDTO) {
        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ profile controller ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("userDTO: " + userDTO);

        UserDTO result = userService.profile(userDTO.getEmail());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/api/profile/modify")
    public ResponseEntity<UserDTO> modify(@RequestBody UserDTO userDTO) {
        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ modify controller ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("userDTO: " + userDTO);

        UserDTO result = userService.modify(userDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping(value = "/api/profile/modify/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> modifyImage(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart("email") String email) {
        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ modifyImage controller ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("email: " + email);
        log.info("image: " + image);

        //UserDTO result = userService.modify(userDTO);

        UserDTO temp = new UserDTO();

        return new ResponseEntity<>(temp, HttpStatus.OK);
    }

    @DeleteMapping("/api/profile/remove/{email}")
    public ResponseEntity<JSONObject> remove(@PathVariable String email) {
        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ remove controller ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("email: " + email);

        userService.remove(email);

        JSONObject result = new JSONObject();
        result.put("result", true);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
