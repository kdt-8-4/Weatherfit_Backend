package com.weatherfit.board.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.domain.ImageEntity;
import com.weatherfit.board.dto.BoardListResponseDTO;
import com.weatherfit.board.dto.BoardSearchDTO;
import com.weatherfit.board.dto.BoardUpdateDTO;
import com.weatherfit.board.repository.BoardRepository;
import com.weatherfit.board.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final ImageRepository imageRepository;
    private final AmazonS3Client amazonS3Client;
    private final ImageService imageService;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
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
                    .temperature(board.getTemperature());

            if(!board.getImages().isEmpty()) {
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
                    .temperature(board.getTemperature());

            if(!board.getImages().isEmpty()) {
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
                    .temperature(board.getTemperature());

            if(!board.getImages().isEmpty()) {
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
        ObjectMapper mapper = new ObjectMapper();

        String joiendString = board.getTemperature() + "/" + String.join("/", board.getCategory());
        String joiendString2 = String.join("/", board.getHashTag());

        System.out.println(joiendString);
        System.out.println(joiendString2);

        kafkaTemplate.send("category", 0, "category", joiendString);
        kafkaTemplate.send("hashtag", 0, "hashtag", joiendString2);
        return board;
    }

    // 게시글 수정
    public void patchBoard(int boardId, BoardUpdateDTO boardUpdateDTO, MultipartFile[] images) {
        Optional<BoardEntity> optionalBoard = Optional.ofNullable(boardRepository.findById(boardId));

        BoardEntity originalBoard = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardId));


        List<Integer> imageIdsToDelete = boardUpdateDTO.getImageIdsToDelete();
        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
            for (Integer imageId : imageIdsToDelete) {
                Optional<ImageEntity> optionalImage = imageRepository.findById(imageId);
                ImageEntity imageToDelete = optionalImage.orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다. id=" + imageId));


                String imageUrl = imageToDelete.getImage_url();
                String keyName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                amazonS3Client.deleteObject(bucketName, keyName);

                imageRepository.delete(imageToDelete);
            }
        }

        if (images != null && images.length > 0) {
            for (MultipartFile imageToAdd : images) {
                String imageUrl = imageService.saveImage(imageToAdd);
                ImageEntity newImageEntity = ImageEntity.builder()
                        .image_url(imageUrl)
                        .boardId(originalBoard)
                        .build();
                imageRepository.save(newImageEntity);
            }
        }

        String afterJoiendString = originalBoard.getTemperature() + "/" + String.join("/", originalBoard.getCategory()) + ":" + String.join("/", boardUpdateDTO.getCategory());
        String afterJoiendString2 = String.join("/", originalBoard.getHashTag()) + ":" + String.join("/", boardUpdateDTO.getHashTag());
        System.out.println(afterJoiendString);
        System.out.println(afterJoiendString2);

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
                    .likeCount(likeService.countLikes(board.getBoardId()))
                    .images(board.entityToDTO(board.getImages().get(0)))
                    .build();

            result.add(dto);
        }

        return result;
    }

}
