package com.weatherfit.board.dto;

import com.weatherfit.board.domain.LikeEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailResponseDTO {
    private int boardId;
    private String nickName;
    private String content;
    private int likeCount;
    private double temperature;
    private List<String> category;
    private List<String> hashTag;
    private boolean status;
    private List<ImageDTO> images;
    private List<CommentResponseDTO> comments;
    private List<LikeEntity> likelist;


}
