package com.weatherfit.comment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentRepsonseDTO {
    private int id;
    private int boardId;
    private String nickname;
    private String content;

    private String createdDate;
    private String createdTime;

    private int status;

    private List<ReplyResponseDTO> replyList;
}
