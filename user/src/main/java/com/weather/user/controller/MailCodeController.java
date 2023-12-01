package com.weather.user.controller;

import com.weather.user.dto.MailCodeDTO;
import com.weather.user.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MailCodeController {
    private final MailService mailService;

    @PostMapping("/api/signup/email/send")
    public ResponseEntity sendEmail(@RequestBody MailCodeDTO mailCodeDTO) throws Exception {
        String email = mailCodeDTO.getEmail();
        log.info("email: " + email);
        mailService.sendCodeToEmail(email);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/api/signup/email/verify")
    public ResponseEntity<JSONObject> verifyEmail(@RequestBody MailCodeDTO mailCodeDTO) throws Exception {
        String email = mailCodeDTO.getEmail();
        String code = mailCodeDTO.getCode();
        log.info("email: " + email);
        log.info("code: " + code);

        boolean verified = mailService.verifiedCode(email, code);
        System.out.println(verified);

        JSONObject result = new JSONObject();
        if(verified) {
            result.put("result", true);
        } else {
            result.put("result", false);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
