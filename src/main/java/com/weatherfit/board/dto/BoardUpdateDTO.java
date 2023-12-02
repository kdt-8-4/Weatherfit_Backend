package com.weatherfit.board.dto;

import com.weatherfit.board.domain.ImageEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
public class BoardUpdateDTO {
    private int boardId;
    private String nickName;
    private String content;
    private List<String> category;
    private List<String> hashTag;
    private List<ImageEntity> images;
<<<<<<< HEAD
    private String[] deletedImages;
=======
       private String[] deletedImages;
>>>>>>> 2ca267785a1aa67187f05e2b056b70f571d417bf
}
