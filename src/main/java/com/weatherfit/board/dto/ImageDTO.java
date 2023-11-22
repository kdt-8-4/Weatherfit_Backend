package com.weatherfit.board.dto;

import com.weatherfit.board.domain.BoardEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@ToString
public class ImageDTO {
    private int imageId;
    private int boardId;
    private String image_url;
}
