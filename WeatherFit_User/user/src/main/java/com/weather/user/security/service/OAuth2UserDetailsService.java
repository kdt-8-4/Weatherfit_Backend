package com.weather.user.security.service;

import com.weather.user.entity.User;
import com.weather.user.entity.UserRole;
import com.weather.user.repository.UserRepository;
import com.weather.user.security.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthUserDTO loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        log.info("request: " + request);

        log.info("clientName: " + request.getClientRegistration().getClientName());
        log.info("params: " + request.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(request);
        oAuth2User.getAttributes().forEach((key, value) -> {
            log.info(key + ": " + value);
        });

        User user = saveSocialUser(oAuth2User);

        AuthUserDTO result = new AuthUserDTO(user.getEmail(), user.getPassword(), user.getImage(),
                user.isFromSocial(), user.isStatus(),
                user.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()),
                oAuth2User.getAttributes());

        log.info("AuthUserDTO: " + request);

        return result;
    }

    private User saveSocialUser(OAuth2User oAuth2User) {
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        String email = oAuth2User.getAttribute("email");
        String image = oAuth2User.getAttribute("picture");
        Optional<User> optionalUser = userRepository.findByEmail(email, true);

        if(optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            User socialUser = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("1234"))
                    .image(image)
                    .fromSocial(true)
                    .status(true)
                    .build();

            socialUser.addRole(UserRole.USER);

            userRepository.save(socialUser);

            return socialUser;
        }
    }
}
