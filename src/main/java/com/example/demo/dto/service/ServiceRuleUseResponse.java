package com.example.demo.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRuleUseResponse {

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
    private Integer curr_use_op;
    private Integer curr_use_val;
    private String chk_use;

    public ServiceRuleUseResponse(ServiceRuleUseDto dto) {
        this.rule_stat = dto.getRule_stat();
        this.name = dto.getName();
        this.tar_name = dto.getTar_name();
        this.op1 = dto.getOp1();
        this.op2 = dto.getOp2();
        this.op3 = dto.getOp3();
        this.val1 = dto.getVal1();
        this.val2 = dto.getVal2();
        this.val3 = dto.getVal3();
        this.val4 = dto.getVal4();
        this.val5 = dto.getVal5();
        this.chk_use = dto.getChk_use();
    }
}
