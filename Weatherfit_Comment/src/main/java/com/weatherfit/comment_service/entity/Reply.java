package com.weatherfit.comment_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String nickname;

    @Column(columnDefinition = "VARCHAR(500) NOT NULL")
    private String content;

    @Column(nullable = true, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private int status;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @PrePersist
    public void prePersist() {
        this.status = this.status == 0 ? 1 : this.status;
    }
}
