package com.weatherfit.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class LikeDTO {
    private int like_id;
    private int board_id;
}
