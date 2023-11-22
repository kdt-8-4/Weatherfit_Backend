package com.weatherfit.board.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String image_url;
}
