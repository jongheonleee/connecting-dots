package com.example.demo.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUserConditionsDetailResponse {

    private String conds_code;
    private String name;

    private String cond_code1;
    private String name1;
    private String short_exp1;
    private String long_exp1;
    private String chk_cond_code1;

    private String cond_code2;
    private String name2;
    private String short_exp2;
    private String long_exp2;
    private String chk_cond_code2;

    private String cond_code3;
    private String name3;
    private String short_exp3;
    private String long_exp3;
    private String chk_cond_code3;

    private String cond_code4;
    private String name4;
    private String short_exp4;
    private String long_exp4;
    private String chk_cond_code4;

    public ServiceUserConditionsDetailResponse(ServiceUserConditionsDetailDto dto) {
        this.conds_code = dto.getConds_code();
        this.name = dto.getName();

        this.cond_code1 = dto.getCond_code1();
        this.name1 = dto.getName1();
        this.short_exp1 = dto.getShort_exp1();
        this.long_exp1 = dto.getLong_exp1();
        this.chk_cond_code1 = dto.getChk_cond_code1();

        this.cond_code2 = dto.getCond_code2();
        this.name2 = dto.getName2();
        this.short_exp2 = dto.getShort_exp2();
        this.long_exp2 = dto.getLong_exp2();
        this.chk_cond_code2 = dto.getChk_cond_code2();

        this.cond_code3 = dto.getCond_code3();
        this.name3 = dto.getName3();
        this.short_exp3 = dto.getShort_exp3();
        this.long_exp3 = dto.getLong_exp3();
        this.chk_cond_code3 = dto.getChk_cond_code3();

        this.cond_code4 = dto.getCond_code4();
        this.name4 = dto.getName4();
        this.short_exp4 = dto.getShort_exp4();
        this.long_exp4 = dto.getLong_exp4();
        this.chk_cond_code4 = dto.getChk_cond_code4();
    }
}
