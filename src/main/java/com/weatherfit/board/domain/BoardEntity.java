package com.weatherfit.board.domain;

import com.weatherfit.board.repository.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board")
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int board_id;

    @Column(name = "nickName", nullable = false)
    private String nickName;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "likeCount", columnDefinition = "int default 0")
    private int likeCount;

    @Column(name = "temperature", nullable = false)
    private int temperature;

    @ElementCollection
    @Column(name = "category", nullable = false)
    private List<String> category;


    @ElementCollection
    @Column(name = "hashTag")
    private List<String> hashTag;

    @Column(name = "status")
    private boolean status = true;

    @OneToMany(mappedBy = "board_id", cascade = CascadeType.ALL)
    private List<ImageEntity> images;


}
