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
    private String nickName;
    private int likeCount;
    private double temperature;
    private ImageDTO images;
    private List<String> category;
    private List<String> hashTag;
    private String weatherIcon;
    private List<LikeEntity> likelist;

    public BoardDTO(BoardEntity boardEntity) {
        this.boardId = boardEntity.getBoardId();
        this.nickName = boardEntity.getNickName();
        this.temperature = boardEntity.getTemperature();
        this.category = boardEntity.getCategory();
        this.hashTag = boardEntity.getHashTag();
        this.weatherIcon = boardEntity.getWeatherIcon();
    }
}
