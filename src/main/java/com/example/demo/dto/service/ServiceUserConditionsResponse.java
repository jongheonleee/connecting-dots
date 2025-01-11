package com.example.demo.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUserConditionsResponse {

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

    public ServiceUserConditionsResponse(ServiceUserConditionsDto dto) {
        this.seq = dto.getSeq();
        this.conds_code = dto.getConds_code();
        this.name = dto.getName();
        this.cond_code1 = dto.getCond_code1();
        this.chk_cond_code1 = dto.getChk_cond_code1();
        this.cond_code2 = dto.getCond_code2();
        this.chk_cond_code2 = dto.getChk_cond_code2();
        this.cond_code3 = dto.getCond_code3();
        this.chk_cond_code3 = dto.getChk_cond_code3();
        this.cond_code4 = dto.getCond_code4();
        this.chk_cond_code4 = dto.getChk_cond_code4();
        this.chk_use = dto.getChk_use();
        this.comt = dto.getComt();
        this.reg_date = dto.getReg_date();
        this.reg_user_seq = dto.getReg_user_seq();
        this.up_date = dto.getUp_date();
        this.up_user_seq = dto.getUp_user_seq();
    }
}
