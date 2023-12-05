package com.weatherfit.board.feignclient;

import com.weatherfit.board.dto.CommentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(value = "${feignClient.url}", path = "/comment")
public interface CommentClient {
    @GetMapping("/comments")
    public Optional<List<CommentResponseDTO>> getCommentAndReply(@RequestParam int boardId);
}
