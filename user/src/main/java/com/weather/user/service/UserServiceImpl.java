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
    public void signup(UserDTO userDTO) {
        log.info(userDTO);

        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isPresent()) {
            throw new Error("이미 존재하는 이메일입니다.");
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = dtoToEntity(userDTO);
        userRepository.save(user);
    }

    @Override
    public Optional<UserDTO> signin(String email, String password, boolean isFromSocial) {
        log.info(email + ", " + password + ", " + isFromSocial);
        Optional<User> optionalUser = userRepository.findByEmail(email, isFromSocial);
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isEmpty()) {
            throw new Error("존재하지 않는 이메일입니다.");
        }

        User user = optionalUser.get();

        if(passwordEncoder.matches(password, user.getPassword())) {
            Optional<UserDTO> result = Optional.ofNullable(entityToDTO(user));
            return result;
        } else {
            throw new Error("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public UserDTO profile(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isEmpty()) {
            throw new Error("존재하지 않는 유저입니다.");
        }
        User user = optionalUser.get();

        if(user.isStatus()) {
            throw new Error("탈퇴 대기중인 유저입니다.");
        }

        UserDTO result = entityToDTO(user);
        return result;
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
        } else {
            throw new Error("잘못 된 접근입니다.");
        }

    }
}
