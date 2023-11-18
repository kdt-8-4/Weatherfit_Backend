package com.weatherfit.board.controller;

import com.netflix.discovery.converters.Auto;
import com.weatherfit.board.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/boards")
public class LikeController {
    @Autowired
    ImageService imageService;



}
