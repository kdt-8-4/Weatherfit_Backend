package com.weatherfit.board.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weatherfit.board.dto.ImageDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ImageEntity")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId", nullable = false)
    @JsonIgnore
    private BoardEntity boardId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    public ImageDTO entityToDTO(ImageEntity imageEntity) {
        return ImageDTO.builder()
                .boardId(imageEntity.getBoardId().getBoardId())
                .imageId(imageEntity.getImageId())
                .imageUrl(imageEntity.getImageUrl())
                .build();

    }
}
