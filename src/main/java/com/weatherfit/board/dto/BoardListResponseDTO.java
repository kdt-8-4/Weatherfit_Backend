package com.weatherfit.board.dto;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class BoardListResponseDTO {
    private int boardId;
    private String nickName;
    private int likeCount;
    private int temperature;
    private ImageDTO images;
}
