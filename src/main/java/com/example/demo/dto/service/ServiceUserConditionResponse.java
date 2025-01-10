package com.example.demo.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUserConditionResponse {

    private String cond_code;
    private String name;
    private String short_exp;
    private String long_exp;
    private String chk_use;
    private String law1;
    private String law2;
    private String law3;
    private String comt;
}
