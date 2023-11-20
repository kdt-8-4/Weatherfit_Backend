package com.weatherfit.board.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.domain.ImageEntity;
import com.weatherfit.board.repository.ImageRepository;
import com.weatherfit.board.service.BoardService;
import com.weatherfit.board.service.ImageService;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController

public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;



    @GetMapping("/test")
    public String testTopic1() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> list = Arrays.asList("test1", "test2", "test2");
        List<String> list2 = Arrays.asList("test3", "test4", "test5");
        String joiendString = String.join("/", list);
        String joiendString2 = String.join("/", list2);

        kafkaTemplate.send("category", joiendString);
        kafkaTemplate.send("hashtag", joiendString2);

        return "done";
    }

    // 게시글 목록 조회
    @GetMapping("/board")
    public List<BoardEntity> listBoards(@RequestParam(required = false) String sort) {
        List<BoardEntity> list;
        if ("date".equals(sort)) {
            list = boardService.findDate();
        } else if ("like".equals(sort)) {
            list = boardService.findLike();
        } else {
            list = boardService.findAll();
        }
        return list;
    }

    // 게시글 상세 조회
    @GetMapping("/board/{boardId}")
    public BoardEntity detailBoard(@PathVariable int boardId) {
        return boardService.getBoardById(boardId);
    }

    // 게시글 생성
    @PostMapping("/board/write")
    public BoardEntity insertBoard(@RequestParam("board") String boardJson, @RequestPart("images") MultipartFile[] images) {
        System.out.println("요청확인 : " + boardJson);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 문자열을 BoardEntity 객체로 변환
            BoardEntity boardEntity = objectMapper.readValue(boardJson, BoardEntity.class);
            BoardEntity savedBoard = boardService.insertBoard(boardEntity);

            for (MultipartFile image : images) {
                String imageUrl = imageService.saveImage(image);  // 이미지를 업로드하고 URL을 반환받음

                ImageEntity imageEntity = ImageEntity.builder()
                        .image_url(imageUrl)
                        .board_id(savedBoard)
                        .build();
                imageRepository.save(imageEntity);
            }

            return savedBoard;

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 게시글 수정
    @PatchMapping("/board/{boardId}")
    public void patchBoard(@PathVariable int boardId, @RequestBody BoardEntity boardEntity) {
        boardService.patchBoard(boardId, boardEntity);
    }

    // 게시글 삭제
    @DeleteMapping("/board/{boardId}")
    @ResponseBody
    public void deleteBoard(@PathVariable int boardId) {
        boardService.deleteBoard(boardId);
    }

}
