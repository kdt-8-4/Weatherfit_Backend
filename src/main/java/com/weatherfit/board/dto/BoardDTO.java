package com.weatherfit.board.dto;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class BoardDTO {
    private int board_id;
    private String nickName;
    private String content;
    private int likeCount;
    private int temperature;
    private String category;
    private String hashTag;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private boolean status;
}
