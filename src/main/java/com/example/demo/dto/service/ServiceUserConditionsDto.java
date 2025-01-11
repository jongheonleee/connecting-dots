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

    public ServiceUserConditionsDto(ServiceUserConditionsRequest request, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.conds_code = request.getConds_code();
        this.name = request.getName();
        this.cond_code1 = request.getCond_code1();
        this.chk_cond_code1 = request.getChk_cond_code1();
        this.cond_code2 = request.getCond_code2();
        this.chk_cond_code2 = request.getChk_cond_code2();
        this.cond_code3 = request.getCond_code3();
        this.chk_cond_code3 = request.getChk_cond_code3();
        this.cond_code4 = request.getCond_code4();
        this.chk_cond_code4 = request.getChk_cond_code4();
        this.chk_use = request.getChk_use();
        this.comt = request.getComt();
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

    public ServiceUserConditionsResponse toResponse() {
        return new ServiceUserConditionsResponse(this);
    }
}
