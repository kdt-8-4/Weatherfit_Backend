package com.weatherfit.comment_service.controller;

import com.weatherfit.comment_service.config.LocalConfig;
import com.weatherfit.comment_service.dto.CommentRepsonseDTO;
import com.weatherfit.comment_service.dto.CommentRequestDTO;
import com.weatherfit.comment_service.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    private final LocalConfig localConfig;
    @GetMapping("/jwtTest2")
    public String test() {
        return localConfig.toString();
    }
    @PostMapping("/write")
    public ResponseEntity<CommentRepsonseDTO> writeComment(@RequestHeader("decodedToken") String nickname, @RequestBody CommentRequestDTO commentRequestDTO) throws UnsupportedEncodingException {
        String decodedNickname = new String(Base64.getDecoder().decode(nickname), "UTF-8");
        commentRequestDTO.setNickname(decodedNickname);
        CommentRepsonseDTO result = commentService.writeComment(commentRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/reply")
    public ResponseEntity<ReplyResponseDTO> writeReply(@RequestHeader("decodedToken") String nickname, @RequestBody ReplyRequestDTO replyRequestDTO) throws UnsupportedEncodingException {
        String decodedNickname = new String(Base64.getDecoder().decode(nickname), "UTF-8");
        replyRequestDTO.setNickname(decodedNickname);
        ReplyResponseDTO result = commentService.writeReply(replyRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentRepsonseDTO>> getCommentsByBoardId(@RequestParam int boardId) {
        List<CommentRepsonseDTO> result = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Boolean> removeComment(@RequestParam int commentId) {
        Boolean result = commentService.removeComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/remove/reply")
    public ResponseEntity<Boolean> removeReply(@RequestParam int replyId) {
        Boolean result = commentService.removeReply(replyId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/modify")
    public ResponseEntity<Boolean> modifyComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        Boolean result = commentService.modifyComment(commentRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/modify/reply")
    public ResponseEntity<Boolean> modifyReply(@RequestBody ReplyRequestDTO replyRequestDTO) {
        Boolean result = commentService.modifyReply(replyRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
