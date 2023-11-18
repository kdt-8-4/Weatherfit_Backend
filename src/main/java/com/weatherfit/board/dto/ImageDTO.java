package com.weatherfit.board.dto;

import com.weatherfit.board.domain.BoardEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@ToString
public class ImageDTO {
    private int image_id;
    private int board_id;
    private String image_url;
}
