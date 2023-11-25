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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
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
        boardDetailResponseDTO.setLikeCount(boardEntity.getLikeCount());
        boardDetailResponseDTO.setTemperature(boardEntity.getTemperature());
        boardDetailResponseDTO.setCategory(boardEntity.getCategory());
        boardDetailResponseDTO.setHashTag(boardEntity.getHashTag());
        boardDetailResponseDTO.setStatus(boardEntity.isStatus());
        List<ImageDTO> imageDTOList = new ArrayList<>();
        for(ImageEntity images : boardEntity.getImages()) {
            imageDTOList.add(images.entityToDTO(images));
        }
        boardDetailResponseDTO.setImages(imageDTOList);

        Optional<List<CommentResponseDTO>> comments = commentClient.getCommentAndReply(boardId);
        boardDetailResponseDTO.setComments(comments.orElse(new ArrayList<>()));
        return boardDetailResponseDTO;
//        return boardService.getBoardById(boardId);
    }

    // 게시글 작성
    @PostMapping("/write")
    public String insertBoard(@RequestParam("board") String boardJson, @RequestPart("images") MultipartFile[] images) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            BoardWriteDTO boardWriteDTO = objectMapper.readValue(boardJson, BoardWriteDTO.class);

            BoardEntity boardEntity = BoardEntity.builder()
                    .boardId(boardWriteDTO.getBoardId())
                    .nickName(boardWriteDTO.getNickName())
                    .content(boardWriteDTO.getContent())
                    .temperature(boardWriteDTO.getTemperature())
                    .category(boardWriteDTO.getCategory())
                    .hashTag(boardWriteDTO.getHashTag())
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
    public boolean patchBoard(@PathVariable int boardId,
                              @RequestPart("board") String boardJson,
                              @RequestPart(value = "images", required = false) MultipartFile[] images) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BoardUpdateDTO boardUpdateDTO = objectMapper.readValue(boardJson, BoardUpdateDTO.class);
            boardService.patchBoard(boardId, boardUpdateDTO, images);
            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private final KafkaTemplate<String, String> kafkaTemplate;
    @GetMapping("/test")
    public String partion() {
        kafkaTemplate.send("categories", 1, "update", "Test");

        return "done";
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{boardId}")
    @ResponseBody
    public void deleteBoard(@PathVariable int boardId) {
        boardService.deleteBoard(boardId);
    }

    // 게시글 검색
    @GetMapping("/search")
    public List<BoardSearchDTO> search(@RequestParam(required = false) List<String> categories,
                    @RequestParam(required = false) List<String> hashtags) {
        return boardService.search(categories, hashtags);
    }
}