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
    private String code;
    private String chk_use;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;

    public ServiceRuleUseDto(ServiceRuleUseRequest request, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.rule_stat = request.getRule_stat();
        this.name = request.getName();
        this.tar_name = request.getTar_name();
        this.op1 = request.getOp1();
        this.op2 = request.getOp2();
        this.op3 = request.getOp3();
        this.val1 = request.getVal1();
        this.val2 = request.getVal2();
        this.val3 = request.getVal3();
        this.val4 = request.getVal4();
        this.val5 = request.getVal5();
        this.code = request.getCode();
        this.chk_use = request.getChk_use();
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

    public ServiceRuleUseResponse toResponse() {
        return new ServiceRuleUseResponse(this);
    }
}
