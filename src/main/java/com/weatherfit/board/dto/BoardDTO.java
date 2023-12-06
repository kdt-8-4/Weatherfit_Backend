package com.weatherfit.board.dto;

import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.domain.LikeEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private int boardId;
    private double temperature;
    private ImageDTO images;


    public BoardDTO(BoardEntity boardEntity) {
        this.boardId = boardEntity.getBoardId();
        this.temperature = boardEntity.getTemperature();
    }


}
