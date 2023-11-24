package com.weatherfit.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.dto.BoardListResponseDTO;
import com.weatherfit.board.dto.BoardSearchDTO;
import com.weatherfit.board.dto.BoardUpdateDTO;
import com.weatherfit.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BoardRepository boardRepository;

    // 게시글 전체 조회
    public List<BoardListResponseDTO> findAll() {
        List<BoardEntity> entities = boardRepository.findAll();
        List<BoardListResponseDTO> dtoList = new ArrayList<>();
        for (BoardEntity board : entities) {
            dtoList.add(BoardListResponseDTO.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .likeCount(board.getLikeCount())
                    .temperature(board.getTemperature())
                    .images(board.entityToDTO(board.getImages().get(0)))
                    .build()
            );
        }
        return dtoList;
    }

    // 게시글 날짜순 조회
    public List<BoardListResponseDTO> findDate() {
        List<BoardEntity> entities = boardRepository.findAllByOrderByCreateDateDesc();
        List<BoardListResponseDTO> dtoList = new ArrayList<>();
        for (BoardEntity board : entities) {
            dtoList.add(BoardListResponseDTO.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .likeCount(board.getLikeCount())
                    .temperature(board.getTemperature())
                    .images(board.entityToDTO(board.getImages().get(0)))
                    .build()
            );
        }
        return dtoList;

    }

    // 게시글 좋아요순 조회
    public List<BoardListResponseDTO> findLike() {
        List<BoardEntity> entities = boardRepository.findAllByOrderByLikeCountDesc();
        List<BoardListResponseDTO> dtoList = new ArrayList<>();
        for (BoardEntity board : entities) {
            dtoList.add(BoardListResponseDTO.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .likeCount(board.getLikeCount())
                    .temperature(board.getTemperature())
                    .images(board.entityToDTO(board.getImages().get(0)))
                    .build()
            );
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

//        kafkaTemplate.send("category", joiendString);
//        kafkaTemplate.send("hashtag", joiendString2);
        return board;
    }

    // 게시글 수정
    public void patchBoard(int boardId, BoardUpdateDTO boardUpdateDTO) {
        Optional<BoardEntity> optionalBoard = Optional.ofNullable(boardRepository.findById(boardId));
//        kafkaTemplate.send("category before", boardUpdateDTO.getCategory().toString());
//        kafkaTemplate.send("hashtag before", boardUpdateDTO.getHashTag().toString());
        BoardEntity originalBoard = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardId));
        originalBoard.setContent(boardUpdateDTO.getContent());
        originalBoard.setCategory(boardUpdateDTO.getCategory());
        originalBoard.setHashTag(boardUpdateDTO.getHashTag());
        boardRepository.save(originalBoard);
//        kafkaTemplate.send("category after", originalBoard.getCategory().toString());
//        kafkaTemplate.send("hashtag after", originalBoard.getHashTag().toString());

    }

    // 게시글 삭제
    public void deleteBoard(int boardId) {
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
                    .likeCount(board.getLikeCount())
                    .images(board.entityToDTO(board.getImages().get(0)))
                    .build();

            result.add(dto);
        }

        return result;
    }

}
