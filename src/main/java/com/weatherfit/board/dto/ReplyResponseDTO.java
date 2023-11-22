package com.weatherfit.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReplyResponseDTO {

    private int id;

    private String nickname;

    private String content;

    private int status;

    private String createdDate;

    private String createdTime;
}