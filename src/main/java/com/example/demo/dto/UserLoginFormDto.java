package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLoginFormDto {

    private String id;
    private String pwd;
    private String toUrl;
    private boolean rememberMe;
}
