package com.weather.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherController {
    @PreAuthorize("permitAll()")
    @GetMapping("/test")
    public String test() {
        return "test!!";
    }
}
