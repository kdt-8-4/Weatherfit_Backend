package com.weather.user.security.service;

import com.weather.user.entity.User;
import com.weather.user.repository.UserRepository;
import com.weather.user.security.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("email: " + email);

        Optional<User> optionalUser = userRepository.findByEmail(email, false);

        if(optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
        }

        User user = optionalUser.get();
        log.info("user: " + user.getPassword());

        AuthUserDTO result = new AuthUserDTO(
                user.getEmail(), user.getPassword(), user.getImage(), user.getName(), user.getNickname(),
                user.isFromSocial(), user.isStatus(),
                user.getRoleSet().stream().map((role) ->
                        new SimpleGrantedAuthority("Role_" + role.name())).collect(Collectors.toSet())
        );
        log.info("authUserDTO: " + result);

        return result;
    }
}
