package com.weatherfit.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.weatherfit.board.domain.BoardEntity;

import com.weatherfit.board.dto.BoardUpdateDTO;
import com.weatherfit.board.repository.BoardCustomRepository;
import com.weatherfit.board.repository.BoardCustomRepositoryImpl;
import com.weatherfit.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BoardRepository boardRepository;

    // 게시글 전체 조회
    public List<BoardEntity> findAll() {
        List<BoardEntity> result = boardRepository.findAll();
        return result;
    }

    // 게시글 날짜순 조회
    public List<BoardEntity> findDate() {
        List<BoardEntity> result = boardRepository.findAllByOrderByCreateDateDesc();
        return result;
    }

    // 게시글 좋아요순 조회
    public List<BoardEntity> findLike() {
        List<BoardEntity> result = boardRepository.findAllByOrderByLikeCountDesc();
        return result;
    }

    // 게시글 상세 조회
    public BoardEntity getBoardById(int boardId) {
        return boardRepository.findById(boardId);
    }

    // 게시글 작성
    public BoardEntity insertBoard(BoardEntity board) {
        boardRepository.save(board);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(board.getCategory().toString());
        return board;
    }

    // 게시글 수정
    public void patchBoard(int boardId, BoardUpdateDTO boardUpdateDTO) {
        Optional<BoardEntity> optionalBoard = Optional.ofNullable(boardRepository.findById(boardId));
        BoardEntity originalBoard = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardId));
        originalBoard.setContent(boardUpdateDTO.getContent());
        originalBoard.setCategory(boardUpdateDTO.getCategory());
        originalBoard.setHashTag(boardUpdateDTO.getHashTag());
        boardRepository.save(originalBoard);
    }


    // 게시글 삭제
    public void deleteBoard(int boardId) {
        boardRepository.deleteById(boardId);
    }

    // 게시글 검색
//    public List<BoardEntity> search(List<String> categories, List<String> hashTags) {
////        if(categories != null && hashTags != null) {
////            return boardRepository.findByCategoryInAndHashTagIn(categories, hashTags);
////        }
////        else if(categories != null) {
////            return boardRepository.findByCategoryIn(categories);
////        }
////        else if(hashTags != null) {
////            return boardRepository.findByHashTagIn(hashTags);
////        }
////        return new ArrayList<>();
//        return boardCustomRepository.findBoardEntitiesWithCategoriesAndHashtags(categories, hashTags);
//    }
    public List<BoardEntity> search(List<String> categories, List<String> hashTags) {
        String[] categoriesArray = categories != null ? categories.toArray(new String[0]) : new String[0];
        String[] hashTagsArray = hashTags != null ? hashTags.toArray(new String[0]) : new String[0];
        return boardRepository.findBoardEntitiesWithCategoriesAndHashtags(List.of(categoriesArray), List.of(hashTagsArray));
    }

}
