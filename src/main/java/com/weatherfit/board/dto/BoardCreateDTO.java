package com.weatherfit.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class BoardCreateDTO {
    private String nickName;
    private String content;
    private int temperature;
    private List<String> category;
    private List<String> hashTag;
}
