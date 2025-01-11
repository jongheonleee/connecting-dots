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
public class ServiceUserConditionsDto {
    private Integer seq;
    private String conds_code;
    private String name;
    private String cond_code1;
    private String chk_cond_code1;
    private String cond_code2;
    private String chk_cond_code2;
    private String cond_code3;
    private String chk_cond_code3;
    private String cond_code4;
    private String chk_cond_code4;
    private String chk_use;
    private String comt;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;
}
