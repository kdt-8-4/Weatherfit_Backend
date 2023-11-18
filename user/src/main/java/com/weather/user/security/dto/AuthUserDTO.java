package com.weather.user.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class AuthUserDTO extends User {
    private String email, password;

    private boolean fromSocial, status;

    public AuthUserDTO(String username, String password, boolean fromSocial, boolean status,
                       Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.fromSocial = fromSocial;
        this.password = password;
        this.status = status;
    }
}
