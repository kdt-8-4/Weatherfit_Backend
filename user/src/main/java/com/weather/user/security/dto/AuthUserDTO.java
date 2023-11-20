package com.weather.user.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class AuthUserDTO extends User {
    private String email, password, name, nickname, image;

    private boolean fromSocial, status;

    Map<String, Object> attributes;

    public AuthUserDTO(String username, String password, String name, String nickname,String image, boolean fromSocial, boolean status,
                       Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.image = image;
        this.fromSocial = fromSocial;
        this.status = status;
    }
}
