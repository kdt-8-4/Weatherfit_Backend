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
    public boolean like (@RequestHeader("decodedToken") String nickName, @PathVariable("boardId") int boardId) {
        likeService.like(boardId, nickName);
        return true;
    }
}