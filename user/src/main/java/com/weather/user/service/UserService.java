package com.weather.user.service;


import com.weather.user.dto.GoogleUserDTO;
import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;


public interface UserService {

    boolean verifyEmail(String email);

    boolean verifyNickname(String email);

    void signup(UserDTO userDTO);

    UserDTO profile(String email);

    UserDTO modify(UserDTO userDTO);

    void remove(String email);

    UserDTO googleUserCheck(GoogleUserDTO googleUserDTO);

    UserDTO googleUserAdditional(UserDTO userDTO) throws Exception;

    default User dtoToEntity(UserDTO userDTO) {
        User user = User.builder()
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .password(userDTO.getPassword())
                .image(userDTO.getImage())
                .fromSocial(userDTO.isFromSocial())
                .status(userDTO.isStatus()).build();

        return user;
    }

    default UserDTO entityToDTO(User user) {
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .image(user.getImage())
                .fromSocial(user.isFromSocial())
                .status(user.isStatus()).build();

        return  userDTO;
    }
}
