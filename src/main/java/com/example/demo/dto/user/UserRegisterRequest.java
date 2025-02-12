package com.example.demo.dto.user;

import com.example.demo.annotation.PasswordEncryption;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserRegisterRequest {

    private final String username;

    @PasswordEncryption
    private String password;

    private final String email;
    private final String phone;

    public UserRegisterRequest(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
}
