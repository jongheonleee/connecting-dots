package com.example.demo.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserRegisterRequest {

    private final String username;
    private final String password;
    private final String email;
    private final String phone;
}
