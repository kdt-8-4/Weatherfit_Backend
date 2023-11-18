package com.weatherfit.board.controller;

import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")  // 공통된 URL 패턴은 클래스 레벨에서 한 번만 선언
public class BoardController {

    @Autowired
    BoardService boardService;

    // 게시글 목록 조회
    @GetMapping
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
    @GetMapping("/{boardId}")
    public BoardEntity detailBoard(@PathVariable int boardId) {
        return boardService.getBoardById(boardId);
    }

    // 게시글 생성
    @PostMapping("/write")
    public void insertBoard(@RequestBody BoardEntity board) {
        boardService.insertBoard(board);
    }

    // 게시글 수정
    @PatchMapping("/{boardId}")
    public void patchBoard(@PathVariable int boardId, @RequestBody BoardEntity boardEntity) {
        boardService.patchBoard(boardId, boardEntity);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    @ResponseBody
    public void deleteBoard(@PathVariable int boardId) {
        boardService.deleteBoard(boardId);
    }

}
