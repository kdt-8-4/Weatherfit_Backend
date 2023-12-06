package com.weatherfit.board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.domain.ImageEntity;
import com.weatherfit.board.dto.BoardListResponseDTO;
import com.weatherfit.board.dto.BoardSearchDTO;
import com.weatherfit.board.dto.BoardUpdateDTO;
import com.weatherfit.board.dto.ImageDTO;
import com.weatherfit.board.repository.BoardRepository;
import com.weatherfit.board.repository.ImageRepository;
import com.weatherfit.board.repository.LikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    @Autowired
    private LikeService likeService;

    // 게시글 전체 조회
    public List<BoardListResponseDTO> findAll() {
        List<BoardEntity> entities = boardRepository.findAll();
        List<BoardListResponseDTO> dtoList = new ArrayList<>();
        for (BoardEntity board : entities) {
            BoardListResponseDTO.BoardListResponseDTOBuilder builder = BoardListResponseDTO.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .likeCount(likeService.countLikes(board.getBoardId()))
                    .hashTag(board.getHashTag())
                    .category(board.getCategory())
                    .temperature(board.getTemperature())
                    .likelist(board.getLikelist())
                    .weatherIcon(board.getWeatherIcon());

            if (!board.getImages().isEmpty()) {
                builder.images(board.entityToDTO(board.getImages().get(0)));
            }

            dtoList.add(builder.build());
        }

        return dtoList;
    }

    // 게시글 날짜순 조회
    public List<BoardListResponseDTO> findDate() {
        List<BoardEntity> entities = boardRepository.findAllByOrderByCreateDateDesc();
        List<BoardListResponseDTO> dtoList = new ArrayList<>();
        for (BoardEntity board : entities) {
            BoardListResponseDTO.BoardListResponseDTOBuilder builder = BoardListResponseDTO.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .likeCount(likeService.countLikes(board.getBoardId()))
                    .hashTag(board.getHashTag())
                    .category(board.getCategory())
                    .temperature(board.getTemperature())
                    .likelist(board.getLikelist())
                    .weatherIcon(board.getWeatherIcon());

            if (!board.getImages().isEmpty()) {
                builder.images(board.entityToDTO(board.getImages().get(0)));
            }

            dtoList.add(builder.build());
        }
        return dtoList;

    }

    // 게시글 좋아요순 조회
    public List<BoardListResponseDTO> findLike() {
        List<BoardEntity> entities = boardRepository.findAllByOrderByLikeCountDesc();
        List<BoardListResponseDTO> dtoList = new ArrayList<>();
        for (BoardEntity board : entities) {
            BoardListResponseDTO.BoardListResponseDTOBuilder builder = BoardListResponseDTO.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .likeCount(likeService.countLikes(board.getBoardId()))
                    .hashTag(board.getHashTag())
                    .category(board.getCategory())
                    .temperature(board.getTemperature())
                    .likelist(board.getLikelist())
                    .weatherIcon(board.getWeatherIcon());

            if (!board.getImages().isEmpty()) {
                builder.images(board.entityToDTO(board.getImages().get(0)));
            }

            dtoList.add(builder.build());
        }

        return dtoList;
    }

    // 내가 쓴 게시글 조회
    public List<BoardListResponseDTO> findNickname(String nickName) {
        List<BoardEntity> entities = boardRepository.findByNickName(nickName);
        List<BoardListResponseDTO> dtoList = new ArrayList<>();

        for (BoardEntity board : entities) {
            BoardListResponseDTO.BoardListResponseDTOBuilder builder = BoardListResponseDTO.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .likeCount(likeService.countLikes(board.getBoardId()))
                    .hashTag(board.getHashTag())
                    .category(board.getCategory())
                    .temperature(board.getTemperature())
                    .likelist(board.getLikelist())

                    .weatherIcon(board.getWeatherIcon());

            if (!board.getImages().isEmpty()) {
                builder.images(board.entityToDTO(board.getImages().get(0)));
            }
            dtoList.add(builder.build());
        }
        return dtoList;
    }


    // 게시글 상세 조회
    public BoardEntity getBoardById(int boardId) {
        return boardRepository.findById(boardId);
    }

    // 게시글 작성
    public BoardEntity insertBoard(BoardEntity board) {
        boardRepository.save(board);

        String joiendString = board.getTemperature() + "/" + String.join("/", board.getCategory());
        String joiendString2 = String.join("/", board.getHashTag());

        kafkaTemplate.send("category", 0, "category", joiendString);
        kafkaTemplate.send("hashtag", 0, "hashtag", joiendString2);
        return board;
    }

    // 게시글 수정
    @Transactional
    public void patchBoard(int boardId, String boardJson, MultipartFile[] images, String nickName) {
        Optional<BoardEntity> optionalBoard = Optional.ofNullable(boardRepository.findById(boardId));

        BoardEntity originalBoard = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardId));

        ObjectMapper objectMapper = new ObjectMapper();

        BoardUpdateDTO boardUpdateDTO;
        try {
            boardUpdateDTO = objectMapper.readValue(boardJson, BoardUpdateDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        for (Integer imageId : boardUpdateDTO.getDeletedImages()) {
            Optional<ImageEntity> optionalImage = imageRepository.findById(imageId);
            ImageEntity imageEntity = optionalImage.orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다. id=" + imageId));
            imageRepository.delete(imageEntity);
        }

        if (images != null) {
            for (MultipartFile image : images) {
                String imageUrl = imageService.saveImage(image);
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

                ImageEntity imageEntity = ImageEntity.builder()
                        .fileName(fileName)
                        .imageUrl(imageUrl)
                        .boardId(originalBoard)
                        .build();
                imageRepository.save(imageEntity);

                originalBoard.getImages().add(imageEntity);
            }
        }

        BoardEntity boardEntity = BoardEntity.builder()
                .boardId(boardId)
                .content(boardUpdateDTO.getContent())
                .category(boardUpdateDTO.getCategory())
                .hashTag(boardUpdateDTO.getHashTag())
                .build();

        String afterJoiendString = originalBoard.getTemperature() + "/" + String.join("/", originalBoard.getCategory()) + ":" + String.join("/", boardUpdateDTO.getCategory());
        String afterJoiendString2 = String.join("/", originalBoard.getHashTag()) + ":" + String.join("/", boardUpdateDTO.getHashTag());

        originalBoard.setNickName(nickName);
        originalBoard.setContent(boardUpdateDTO.getContent());
        originalBoard.setCategory(boardUpdateDTO.getCategory());
        originalBoard.setHashTag(boardUpdateDTO.getHashTag());
        boardRepository.save(originalBoard);

        // 카프카 전송
        kafkaTemplate.send("category", 1, "category", afterJoiendString);
        kafkaTemplate.send("hashtag", 1, "hashtag", afterJoiendString2);
    }


    // 게시글 삭제
    public void deleteBoard(int boardId) {
        Optional<BoardEntity> optionalBoard = Optional.ofNullable(boardRepository.findById(boardId));

        BoardEntity originalBoard = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardId));

        String afterJoiendString = originalBoard.getTemperature() + "/" + String.join("/", originalBoard.getCategory());
        String afterJoiendString2 = String.join("/", originalBoard.getHashTag());

        kafkaTemplate.send("category", 2, "category", afterJoiendString);
        kafkaTemplate.send("hashtag", 2, "hashtag", afterJoiendString2);


        boardRepository.deleteById(boardId);

    }

    // 게시글 검색
    public List<BoardSearchDTO> search(List<String> categories, List<String> hashTags) {
        String[] categoriesArray = categories != null ? categories.toArray(new String[0]) : new String[0];
        String[] hashTagsArray = hashTags != null ? hashTags.toArray(new String[0]) : new String[0];
        List<BoardEntity> boardEntities = boardRepository.findBoardEntitiesWithCategoriesAndHashtags(List.of(categoriesArray), List.of(hashTagsArray));

        List<BoardSearchDTO> result = new ArrayList<>();
        for (BoardEntity board : boardEntities) {
            BoardSearchDTO dto = BoardSearchDTO.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .category(board.getCategory())
                    .hashTag(board.getHashTag())
                    .likeCount(likeService.countLikes(board.getBoardId()))
                    .images(board.entityToDTO(board.getImages().get(0)))
                    .weatherIcon(board.getWeatherIcon())
                    .build();

            result.add(dto);
        }

        return result;
    }


    // 온도 별 좋아요 Top 5 게시글
    public Page<BoardListResponseDTO> getTop5Board(double minTemp, double maxTemp, Pageable pageable) {
        double averageTemp = (minTemp + maxTemp) / 2;
        List<BoardEntity> boards = boardRepository.findBoardsByTemperatureRange(averageTemp - 1, averageTemp + 1);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), boards.size());

        List<BoardListResponseDTO> dtos = new ArrayList<>();

        for (BoardEntity board : boards.subList(start, end)) {
            BoardListResponseDTO dto = BoardListResponseDTO.builder()
                    .boardId(board.getBoardId())
                    .temperature(board.getTemperature())
                    .likeCount(likeService.countLikes(board.getBoardId()))
                    .images(board.entityToDTO(board.getImages().get(0)))
                    .build();

            dtos.add(dto);
        }

        return new PageImpl<BoardListResponseDTO>(dtos, pageable, boards.size());
    }

}
