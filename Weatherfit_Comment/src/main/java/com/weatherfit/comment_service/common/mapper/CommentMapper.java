package com.weatherfit.comment_service.common.mapper;

import com.weatherfit.comment_service.dto.CommentRepsonseDTO;
import com.weatherfit.comment_service.dto.CommentRequestDTO;
import com.weatherfit.comment_service.dto.ReplyRequestDTO;
import com.weatherfit.comment_service.dto.ReplyResponseDTO;
import com.weatherfit.comment_service.entity.Comment;
import com.weatherfit.comment_service.entity.Reply;
import org.mapstruct.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {ReplyMapper.class})
public interface CommentMapper {

    @Mapping(target = "createdTime", ignore = true)
    CommentRepsonseDTO commentToDTO(Comment comment);

    @Mapping(target = "createdTime", ignore = true)
    ReplyResponseDTO replyToDTO(Reply reply);
    @AfterMapping
    default void splitDateTime(Comment comment, @MappingTarget CommentRepsonseDTO commentRepsonseDTO) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String createdDate = comment.getCreatedDate().format(dateFormatter);
        String createdTime = comment.getCreatedDate().format(timeFormatter);

        commentRepsonseDTO.setCreatedDate(createdDate);
        commentRepsonseDTO.setCreatedTime(createdTime);
    }
    List<CommentRepsonseDTO> commentsToDTOList(List<Comment> comments);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "replyList", ignore = true)
    Comment DTOToComment(CommentRequestDTO commentRequestDTO);
}

