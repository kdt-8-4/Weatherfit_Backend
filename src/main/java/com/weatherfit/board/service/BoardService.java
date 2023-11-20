package com.weatherfit.board.service;

import com.weatherfit.board.domain.BoardEntity;

import com.weatherfit.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    BoardRepository boardRepository;

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
        return board;
    }

    // 게시글 수정
    public void patchBoard(int boardId, BoardEntity updatedBoard) {
        Optional<BoardEntity> optionalBoard = Optional.ofNullable(boardRepository.findById(boardId));
        BoardEntity originalBoard = optionalBoard.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + boardId));

        BoardEntity modifiedBoard = BoardEntity.builder()
                .board_id(originalBoard.getBoard_id())
                .nickName(updatedBoard.getNickName())
                .content(updatedBoard.getContent())
                .likeCount(updatedBoard.getLikeCount())
                .temperature(updatedBoard.getTemperature())
                .category(updatedBoard.getCategory())
                .hashTag(updatedBoard.getHashTag())
                .status(updatedBoard.isStatus())
                .build();

        boardRepository.save(modifiedBoard);
    }


    // 게시글 삭제
    public void deleteBoard(int boardId) {
        boardRepository.deleteById(boardId);
    }

}
