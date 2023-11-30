package com.weather.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MailCode {
    @Id
    String email;

    String code;

    public void changeCode(String code) {
        this.code = code;
    }
}
