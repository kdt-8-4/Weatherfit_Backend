package com.weatherfit.board.dto;

import com.weatherfit.board.domain.LikeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Builder
@Setter
public class BoardListResponseDTO {
    private int boardId;
    private String nickName;
    private int likeCount;
    private double temperature;
    private ImageDTO images;
    private List<String> category;
    private List<String> hashTag;
    private String weatherIcon;
    private List<LikeEntity> likelist;


}
