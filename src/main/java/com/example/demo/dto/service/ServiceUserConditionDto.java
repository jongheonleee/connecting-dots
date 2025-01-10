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
public class ServiceUserConditionDto {

    private String cond_code;
    private String name;
    private String short_exp;
    private String long_exp;
    private String chk_use;
    private String law1;
    private String law2;
    private String law3;
    private String comt;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;
}
