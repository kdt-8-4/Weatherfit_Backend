package com.weatherfit.board.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.domain.ImageEntity;
import com.weatherfit.board.dto.BoardDetailResponseDTO;
import com.weatherfit.board.dto.BoardListResponseDTO;
import com.weatherfit.board.dto.BoardUpdateDTO;
import com.weatherfit.board.dto.CommentResponseDTO;
//import com.weatherfit.board.feignclient.CommentClient;
import com.weatherfit.board.feignclient.CommentClient;
import com.weatherfit.board.repository.BoardRepository;
import com.weatherfit.board.repository.ImageRepository;
import com.weatherfit.board.repository.LikeRepository;
import com.weatherfit.board.service.BoardService;
import com.weatherfit.board.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class BoardController {

    @Autowired
    BoardService boardService;
    @Autowired
    ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CommentClient commentClient;

    @GetMapping("/test")
    public String testTopic1() throws JsonProcessingException {


        return "done";
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public List<BoardListResponseDTO> listBoards(@RequestParam(required = false) String sort) {
        List<BoardEntity> list;
        if ("date".equals(sort)) {
            list = boardService.findDate();
        } else if ("like".equals(sort)) {
            list = boardService.findLike();
        } else {
            list = boardService.findAll();
        }
        List<BoardListResponseDTO> dtoList = list.stream()
                .map(board -> BoardListResponseDTO.builder()
                        .boardId(board.getBoardId())
                        .nickName(board.getNickName())
                        .likeCount(board.getLikeCount())
                        .temperature(board.getTemperature())
                        .images(board.getImages())
                        .build())
                .collect(Collectors.toList());

        return dtoList;
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{boardId}")
    public BoardDetailResponseDTO detailBoard(@PathVariable int boardId) {
        BoardEntity boardEntity = boardService.getBoardById(boardId);

        BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO();
        boardDetailResponseDTO.setBoardId(boardEntity.getBoardId());
        boardDetailResponseDTO.setNickName(boardEntity.getNickName());
        boardDetailResponseDTO.setContent(boardEntity.getContent());
        boardDetailResponseDTO.setLikeCount(boardEntity.getLikeCount());
        boardDetailResponseDTO.setTemperature(boardEntity.getTemperature());
        boardDetailResponseDTO.setCategory(boardEntity.getCategory());
        boardDetailResponseDTO.setHashTag(boardEntity.getHashTag());
        boardDetailResponseDTO.setStatus(boardEntity.isStatus());
        boardDetailResponseDTO.setImages(boardEntity.getImages());

        Optional<List<CommentResponseDTO>> comments = commentClient.getCommentAndReply(boardId);
        boardDetailResponseDTO.setComments(comments.orElse(new ArrayList<>()));
        return boardDetailResponseDTO;
//        return boardService.getBoardById(boardId);
    }

    // 게시글 생성
    @PostMapping("/board/write")
    public BoardEntity insertBoard(@RequestParam("board") String boardJson, @RequestPart("images") MultipartFile[] images) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 문자열을 BoardEntity 객체로 변환
            BoardEntity boardEntity = objectMapper.readValue(boardJson, BoardEntity.class);
            BoardEntity savedBoard = boardService.insertBoard(boardEntity);

            for (MultipartFile image : images) {
                String imageUrl = imageService.saveImage(image);  // 이미지를 업로드하고 URL을 반환받음

                ImageEntity imageEntity = ImageEntity.builder()
                        .image_url(imageUrl)
                        .boardId(savedBoard)
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
    @PatchMapping("/edit/{boardId}")
    @ResponseBody
    public boolean patchBoard(@PathVariable int boardId, @RequestBody BoardUpdateDTO boardUpdateDTO) {
        boardService.patchBoard(boardId, boardUpdateDTO);
        return true;
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{boardId}")
    @ResponseBody
    public void deleteBoard(@PathVariable int boardId) {
        boardService.deleteBoard(boardId);
    }

    // 게시글 검색
    @GetMapping("/search")
    public List<BoardEntity> search(@RequestParam(required = false) List<String> categories,
                    @RequestParam(required = false) List<String> hashtags) {
        return boardService.search(categories, hashtags);
    }
}