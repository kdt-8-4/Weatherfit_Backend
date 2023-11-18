package com.weather.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;

    private String email, name, nickname, phone, image, password;

    private boolean fromSocial, status;
}
