package com.weatherfit.board.dto;

import com.weatherfit.board.domain.ImageEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class BoardListResponseDTO {
    private int boardId;
    private String nickName;
    private int likeCount;
    private int temperature;
    private List<ImageEntity> images;
}
