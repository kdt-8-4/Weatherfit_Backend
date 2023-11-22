package com.weatherfit.board.controller;

import com.weatherfit.board.dto.LikeRequestDto;
import com.weatherfit.board.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class LikeController {

    private final LikeService likeService;


    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like/{boardId}")
    public boolean like (@PathVariable("boardId") int boardId, @RequestBody LikeRequestDto likeRequestDto) {
        likeService.like(boardId, likeRequestDto.getUserId());
        return true;
    }
}