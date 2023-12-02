package com.weatherfit.board.controller;


import com.amazonaws.services.kms.model.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.domain.ImageEntity;
import com.weatherfit.board.dto.*;
import com.weatherfit.board.feignclient.CommentClient;
import com.weatherfit.board.repository.ImageRepository;
import com.weatherfit.board.service.BoardService;
import com.weatherfit.board.service.ImageService;
import com.weatherfit.board.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


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

    @Autowired
    private LikeService likeService;

    // 게시글 목록 조회
    @GetMapping("/list")
    public List<BoardListResponseDTO> listBoards(@RequestParam(required = false) String sort) {
        List<BoardListResponseDTO> list;
        if ("date".equals(sort)) {
            list = boardService.findDate();
        } else if ("like".equals(sort)) {
            list = boardService.findLike();
        } else {
            list = boardService.findAll();
        }


        return list;
    }

    // 내가 쓴 게시글 조회
    @GetMapping("/myList")
    public List<BoardListResponseDTO> myListBoards(@RequestHeader("decodedToken") String nickName) throws UnsupportedEncodingException {
        String decodedNickname = new String(Base64.getDecoder().decode(nickName), "UTF-8");
        return boardService.findNickname(decodedNickname);
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{boardId}")
    public BoardDetailResponseDTO detailBoard(@PathVariable int boardId) {
        BoardEntity boardEntity = boardService.getBoardById(boardId);
        if (boardEntity == null) {
            throw new NotFoundException("Board not found with id " + boardId);
        }
        BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO();
        boardDetailResponseDTO.setBoardId(boardEntity.getBoardId());
        boardDetailResponseDTO.setNickName(boardEntity.getNickName());
        boardDetailResponseDTO.setContent(boardEntity.getContent());
        boardDetailResponseDTO.setLikeCount(likeService.countLikes(boardId));
        boardDetailResponseDTO.setTemperature(boardEntity.getTemperature());
        boardDetailResponseDTO.setCategory(boardEntity.getCategory());
        boardDetailResponseDTO.setHashTag(boardEntity.getHashTag());
        boardDetailResponseDTO.setStatus(boardEntity.isStatus());
        List<ImageDTO> imageDTOList = new ArrayList<>();
        for (ImageEntity images : boardEntity.getImages()) {
            imageDTOList.add(images.entityToDTO(images));
        }
        boardDetailResponseDTO.setImages(imageDTOList);

        Optional<List<CommentResponseDTO>> comments = commentClient.getCommentAndReply(boardId);
        boardDetailResponseDTO.setComments(comments.orElse(new ArrayList<>()));
        return boardDetailResponseDTO;
    }

    // 게시글 작성
    @PostMapping("/write")
    public String insertBoard(@RequestHeader("decodedToken") String nickName, @RequestParam("board") String boardJson, @RequestPart("images") MultipartFile[] images) throws UnsupportedEncodingException {
        String decodedNickname = new String(Base64.getDecoder().decode(nickName), "UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            BoardWriteDTO boardWriteDTO = objectMapper.readValue(boardJson, BoardWriteDTO.class);

            BoardEntity boardEntity = BoardEntity.builder()
                    .boardId(boardWriteDTO.getBoardId())
                    .nickName(decodedNickname)
                    .content(boardWriteDTO.getContent())
                    .temperature(boardWriteDTO.getTemperature())
                    .category(boardWriteDTO.getCategory())
                    .hashTag(boardWriteDTO.getHashTag())
                    .weatherIcon(boardWriteDTO.getWeatherIcon())
                    .build();

            BoardEntity savedBoard = boardService.insertBoard(boardEntity);

            for (MultipartFile image : images) {
                String imageUrl = imageService.saveImage(image);

                ImageEntity imageEntity = ImageEntity.builder()
                        .image_url(imageUrl)
                        .boardId(savedBoard)
                        .build();
                imageRepository.save(imageEntity);
            }

            return "savedBoard";

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 게시글 수정
    @PatchMapping(value = "/edit/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public boolean patchBoard(
            @RequestHeader("decodedToken") String nickName,
            @PathVariable int boardId,
            @RequestPart("board") String boardJson,
            @RequestPart(value = "images", required = false) MultipartFile[] newImages,
            @RequestPart(value = "deleteImageIds", required = false) List<String> deleteImageIds) throws UnsupportedEncodingException {
        String decodedNickname = new String(Base64.getDecoder().decode(nickName), "UTF-8");

        boardService.patchBoard(boardId, boardJson, newImages, deleteImageIds, decodedNickname);
        return true;
    }


    // 게시글 삭제
    @DeleteMapping("/delete/{boardId}")
    @ResponseBody
    public void deleteBoard(
            @PathVariable int boardId) throws UnsupportedEncodingException {
        boardService.deleteBoard(boardId);
    }

    // 게시글 검색
    @GetMapping("/search")
    public List<BoardSearchDTO> search(@RequestParam(required = false) List<String> categories,
                                       @RequestParam(required = false) List<String> hashtags) {
        return boardService.search(categories, hashtags);
    }
}