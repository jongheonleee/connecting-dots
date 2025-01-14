package com.example.demo.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTermsResponse {

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

    public ServiceTermsResponse(ServiceTermsDto dto) {
        this.poli_stat = dto.getPoli_stat();
        this.name = dto.getName();
        this.rule_stat1 = dto.getRule_stat1();
        this.op1 = dto.getOp1();
        this.rule_stat2 = dto.getRule_stat2();
        this.op2 = dto.getOp2();
        this.rule_stat3 = dto.getRule_stat3();
        this.comt = dto.getComt();
        this.chk_use = dto.getChk_use();
        this.code = dto.getCode();
    }
}
