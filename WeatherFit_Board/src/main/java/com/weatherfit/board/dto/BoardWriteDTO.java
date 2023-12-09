package com.weatherfit.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardWriteDTO {
    private int boardId;
    private String nickName;
    private String content;
    private double temperature;
    private List<String> category;
    private List<String> hashTag;
    private String weatherIcon;
}
