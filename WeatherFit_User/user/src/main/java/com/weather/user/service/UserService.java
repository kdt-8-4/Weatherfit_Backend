package com.weather.user.service;


import com.weather.user.dto.GoogleUserDTO;
import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface UserService {

    boolean verifyEmail(String email);

    boolean verifyNickname(String email);

    void signup(UserDTO userDTO);

    UserDTO googleUserCheck(GoogleUserDTO googleUserDTO) throws Exception;

    UserDTO googleUserAdditional(UserDTO userDTO) throws Exception;

    UserDTO profile(String email);

    UserDTO modify(UserDTO userDTO);

    void saveImage(String email, MultipartFile image);

    void resetImage(String email);

    void remove(String email);

    public UserDTO userInfoByNickname(String nickname);

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
