package com.weatherfit.board.dto;

import com.weatherfit.board.domain.BoardEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyLikeDTO {
    private int boardId;
    private String images;

    public MyLikeDTO(BoardEntity boardEntity) {
        this.boardId = boardEntity.getBoardId();
        if(!boardEntity.getImages().isEmpty()) {
            this.images = String.valueOf(boardEntity.getImages().get(0).getImage_url());
        }
    }
}
