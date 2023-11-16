package com.weather.user.service;


import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;

public interface UserService {
    Long register(UserDTO userDTO);

    default User dtoToEntity(UserDTO userDTO) {
        User user = User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .password(userDTO.getPassword())
                .phone(userDTO.getPhone())
                .image(userDTO.getImage())
                .fromSocial(userDTO.isFromSocial())
                .status(userDTO.isStatus()).build();

        return user;
    }
}
