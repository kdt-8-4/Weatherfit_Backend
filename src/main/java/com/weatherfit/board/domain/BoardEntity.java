package com.weatherfit.board.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.weatherfit.board.repository.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board")
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardId;

    @Builder.Default
    @Column(name = "nickName", nullable = false)
    private String nickName = "";

    @Builder.Default
    @Column(name = "content", nullable = false)
    private String content = "";

    @Column(name = "likeCount", columnDefinition = "int default 0")
    private int likeCount = 0;

    @Builder.Default
    @Column(name = "temperature", nullable = false)
    private int temperature = 0;

    @ElementCollection
    @Column(name = "category", nullable = false)
    private List<String> category = new ArrayList<>();


    @ElementCollection
    @Builder.Default
    @Column(name = "hashTag")
    private List<String> hashTag = new ArrayList<>();

    @Column(name = "status")
    @Builder.Default
    private boolean status = true;

    @OneToMany(mappedBy = "boardId", cascade = CascadeType.ALL)
    private List<ImageEntity> images;

    @JsonManagedReference
    @OneToMany(mappedBy = "boardId", cascade = CascadeType.REMOVE)
    private List<LikeEntity> likelist;

}
