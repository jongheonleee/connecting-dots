package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

    private String id;
    private String pwd;
    private String email;
    private String birth;
    private String sns;
    private String reg_date;
    private String reg_id;
    private String up_date;
    private String up_id;
}
