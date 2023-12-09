package com.weatherfit.comment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReplyRequestDTO {

    private int id;
    private int commentId;

    private String nickname;

    private String content;
}
