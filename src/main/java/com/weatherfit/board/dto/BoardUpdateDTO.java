package com.weatherfit.board.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardUpdateDTO {
    private String content;
    private List<String> category;
    private List<String> hashTag;
}
