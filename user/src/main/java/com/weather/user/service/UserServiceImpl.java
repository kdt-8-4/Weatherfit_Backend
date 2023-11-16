package com.weather.user.service;

import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import com.weather.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Long register(UserDTO userDTO) {
        log.info(userDTO);

        User user = dtoToEntity(userDTO);
        userRepository.save(user);

        return user.getId();
    }
}
