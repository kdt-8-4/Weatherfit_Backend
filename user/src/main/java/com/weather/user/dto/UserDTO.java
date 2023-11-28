package com.weather.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;

    private String email, name, nickname, phone, image, password, token;

    private boolean fromSocial, status;
}
