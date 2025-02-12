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

    public ServiceTermsDto(ServiceTermsRequest request, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.poli_stat = request.getPoli_stat();
        this.name = request.getName();
        this.rule_stat1 = request.getRule_stat1();
        this.op1 = request.getOp1();
        this.rule_stat2 = request.getRule_stat2();
        this.op2 = request.getOp2();
        this.rule_stat3 = request.getRule_stat3();
        this.comt = request.getComt();
        this.chk_use = request.getChk_use();
        this.code = request.getCode();
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

    public ServiceTermsResponse toResponse() {
        return new ServiceTermsResponse(this);
    }
}
