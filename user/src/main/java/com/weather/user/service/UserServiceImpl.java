package com.weather.user.service;

import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import com.weather.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean verifyEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) {
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public boolean verifyNickname(String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);

        if(optionalUser.isEmpty()) {
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public void signup(UserDTO userDTO) {
        log.info(userDTO);

        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isPresent()) {
            throw new Error("이미 존재하는 이메일입니다.");
        }

        optionalUser = userRepository.findByNickname(userDTO.getNickname());

        if(optionalUser.isPresent()) {
            throw new Error("이미 존재하는 닉네임입니다.");
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setStatus(true);
        User user = dtoToEntity(userDTO);
        userRepository.save(user);
    }

    @Override
    public UserDTO profile(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isEmpty()) {
            throw new Error("존재하지 않는 유저입니다.");
        }
        User user = optionalUser.get();

        if(!user.isStatus()) {
            throw new Error("탈퇴 대기중인 유저입니다.");
        }

        UserDTO result = entityToDTO(user);
        return result;
    }

    @Override
    public UserDTO modify(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isEmpty()) {
            throw new Error("존재하지 않는 유저입니다.");
        }
        User user = optionalUser.get();

        if(user.isStatus()) {
            user.changeNickname(userDTO.getNickname());
            user.changeImage(userDTO.getImage());
            user.changePassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(user);

            UserDTO result = entityToDTO(user);
            return result;
        } else {
            throw new Error("이미 탈퇴 처리중인 유저입니다.");
        }
    }

    @Override
    public void remove(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isEmpty()) {
            throw new Error("존재하지 않는 유저입니다.");
        }
        User user = optionalUser.get();

        if(user.isStatus()) {
            user.changeStatus();
            userRepository.save(user);
        } else {
            throw new Error("잘못 된 접근입니다.");
        }

    }
}
