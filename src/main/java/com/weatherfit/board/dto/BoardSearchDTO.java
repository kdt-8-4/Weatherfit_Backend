package com.weatherfit.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class BoardSearchDTO {
    private int boardId;
    private String nickName;
    private int temperature;
    private int likeCount;
    private ImageDTO images;
}
