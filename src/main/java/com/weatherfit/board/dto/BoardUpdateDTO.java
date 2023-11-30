package com.weatherfit.board.dto;

import com.weatherfit.board.domain.ImageEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
public class BoardUpdateDTO {
    private int boardId;
    private String content;
    private List<String> category;
    private List<String> hashTag;
    private List<Integer> imageIdsToDelete;
    private List<ImageEntity> images;
}
