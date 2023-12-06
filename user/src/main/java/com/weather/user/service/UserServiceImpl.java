package com.weather.user.service;

import com.weather.user.dto.GoogleUserDTO;
import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import com.weather.user.entity.UserRole;
import com.weather.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
            if(userDTO.getNickname() != null) {
                user.changeNickname(userDTO.getNickname());
            }
            if(userDTO.getImage() != null) {
                user.changeImage(userDTO.getImage());
            }
            if(userDTO.getPassword() != null) {
                user.changePassword(passwordEncoder.encode(userDTO.getPassword()));
            }
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
            throw new Error("이미 탈퇴 처리중인 유저입니다.");
        }

    }

    @Override
    public UserDTO googleUserCheck(GoogleUserDTO googleUserDTO) {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        String email = googleUserDTO.getEmail();
        String image = googleUserDTO.getPicture();
        Optional<User> optionalUser = userRepository.findByEmail(email, true);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO result = entityToDTO(user);
            return result;
        } else {
            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("1234"))
                    .image(image)
                    .fromSocial(true)
                    .status(true)
                    .build();
            user.addRole(UserRole.USER);

            userRepository.save(user);

            UserDTO result = entityToDTO(user);
            return result;
        }
    }

    @Override
    public UserDTO googleUserAdditional(UserDTO userDTO) throws Exception{
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail(), true);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.changeNickname(userDTO.getNickname());
            user.changeName(userDTO.getName());
            userRepository.save(user);
            UserDTO result = entityToDTO(user);
            return result;
        } else {
            throw new Exception("잘못 된 접근입니다.");
        }
    }
}
