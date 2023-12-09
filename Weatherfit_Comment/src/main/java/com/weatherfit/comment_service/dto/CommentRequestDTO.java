package com.weatherfit.comment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentRequestDTO {

    private int id;
    private int boardId;
    private String nickname;
    private String content;
}
