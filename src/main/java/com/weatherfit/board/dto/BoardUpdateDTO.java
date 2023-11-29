package com.weatherfit.board.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
public class BoardUpdateDTO {
    private String content;
    private List<String> category;
    private List<String> hashTag;
    private int temperature;
    private List<Integer> imageIdsToDelete;
    private MultipartFile[] imagesToAdd;
}
