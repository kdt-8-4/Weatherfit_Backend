package com.weather.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true)
    String email;

    String name, nickname, phone, image, password;

    boolean fromSocial, status;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeImage(String image) {
        this.image = image;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeStatus() {
        if(this.status) {
            this.status = false;
        } else {
            this.status = true;
        }
    }
}
