package com.weather.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class WeatherController {
    @GetMapping("/hello")
    public String test() {
        log.info("test!!");
        return "hello";
    }
}
