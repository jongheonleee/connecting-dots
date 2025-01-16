package com.example.demo.dto.ord_user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdatedFormDto {

    private String originId;

    // 변경된 이름, 아아디, 비번, 이메일, 생일, sns
    private String id;
    private String name;
    private String email;
    private String pwd;
    private String birth;
    private String sns;
}
