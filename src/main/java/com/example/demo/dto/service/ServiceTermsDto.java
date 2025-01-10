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
public class ServiceTermsDto {
    private String poli_stat;
    private String name;
    private String rule_stat1;
    private String op1;
    private String rule_stat2;
    private String op2;
    private String rule_stat3;
    private String comt;
    private String chk_use;
    private String code;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;
}
