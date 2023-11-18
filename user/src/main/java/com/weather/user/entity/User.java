package com.weather.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true)
    String email;

    String name, nickname, phone, image, password;

    boolean fromSocial, status;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<>();

    public void addRole(UserRole role) {
        roleSet.add(role);
    }

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
