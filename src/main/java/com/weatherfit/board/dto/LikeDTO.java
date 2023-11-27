package com.weatherfit.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LikeDTO {
    private int likeId;
    private int boardId;
    private String nickName;
}
