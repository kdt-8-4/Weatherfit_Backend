package com.weatherfit.board.controller;
import com.weatherfit.board.dto.MyLikeDTO;
import com.weatherfit.board.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;


@RestController
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like/{boardId}")
    public boolean like (@RequestHeader("decodedToken") String nickName, @PathVariable("boardId") int boardId) throws UnsupportedEncodingException {
        String decodedNickname = new String(Base64.getDecoder().decode(nickName), "UTF-8");
        likeService.like(boardId, decodedNickname);
        return true;
    }

    @GetMapping("/like/mylike")
    public List<MyLikeDTO> myLike(@RequestHeader("decodedToken") String nickName) throws UnsupportedEncodingException {
        String decodedNickname = new String(Base64.getDecoder().decode(nickName), "UTF-8");
        List<MyLikeDTO> myLikeList = likeService.myLike(decodedNickname);

        return myLikeList;
    }
}