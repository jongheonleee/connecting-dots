package com.example.demo.dto.ord_user;

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

    public void setToUrl(String toUrl) {
        if (toUrl == null || toUrl.equals("")) {
            this.toUrl = "/";
            return;
        }

        this.toUrl = toUrl;
    }
}
