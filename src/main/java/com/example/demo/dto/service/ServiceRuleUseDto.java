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
public class ServiceRuleUseDto {

    private String rule_stat;
    private String name;
    private String tar_name;
    private String op1;
    private String op2;
    private String op3;
    private String val1;
    private String val2;
    private String val3;
    private String val4;
    private String val5;
    private String chk_use;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;

    public ServiceRuleUseResponse toResponse() {
        return new ServiceRuleUseResponse(this);
    }
}
