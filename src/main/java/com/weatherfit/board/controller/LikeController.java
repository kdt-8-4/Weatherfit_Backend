package com.weatherfit.board.controller;

import com.weatherfit.board.dto.LikeRequestDTO;
import com.weatherfit.board.service.LikeService;
import org.springframework.web.bind.annotation.*;


@RestController
public class LikeController {

    private final LikeService likeService;


    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like/{boardId}")
    public boolean like (@PathVariable("boardId") int boardId, @RequestBody LikeRequestDTO likeRequestDto) {
        likeService.like(boardId, likeRequestDto.getUserId());
        return true;
    }
}