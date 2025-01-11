package com.example.demo.dto.service;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceUserConditionsDetailDto {

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
}
