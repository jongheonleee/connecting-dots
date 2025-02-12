package com.example.demo.global.security;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class ConnectingDotsAuthUser extends User {

    private final Integer userId;
    private final String username;
    private final String password;
    private final String email;
    private final String phone;

    public ConnectingDotsAuthUser(Integer userId, String username, String password, String email, String phone, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
}
