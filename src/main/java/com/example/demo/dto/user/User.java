package com.example.demo.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    // 이름, 아아디, 비번, 이메일, 생일, sns
    private String id;
    private String name;
    private String email;
    private String pwd;
    private String birth;
    private String sns;

    public boolean checkPwd(String otherPwd) {
        return pwd.equals(otherPwd);
    }
}
