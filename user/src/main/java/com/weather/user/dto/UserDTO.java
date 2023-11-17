package com.weather.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;

    String email, name, nickname, phone, image, password;

    boolean fromSocial, status;
}
