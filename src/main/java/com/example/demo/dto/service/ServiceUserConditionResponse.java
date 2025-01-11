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

    public ServiceUserConditionResponse(ServiceUserConditionDto dto) {
        this.cond_code = dto.getCond_code();
        this.name = dto.getName();
        this.short_exp = dto.getShort_exp();
        this.long_exp = dto.getLong_exp();
        this.chk_use = dto.getChk_use();
        this.law1 = dto.getLaw1();
        this.law2 = dto.getLaw2();
        this.law3 = dto.getLaw3();
        this.comt = dto.getComt();
    }
}
