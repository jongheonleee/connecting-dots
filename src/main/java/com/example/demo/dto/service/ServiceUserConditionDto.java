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

    public ServiceUserConditionDto(ServiceUserConditionRequest request, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.cond_code = request.getCond_code();
        this.name = request.getName();
        this.short_exp = request.getShort_exp();
        this.long_exp = request.getLong_exp();
        this.chk_use = request.getChk_use();
        this.law1 = request.getLaw1();
        this.law2 = request.getLaw2();
        this.law3 = request.getLaw3();
        this.comt = request.getComt();
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

    public ServiceUserConditionResponse toResponse() {
        return new ServiceUserConditionResponse(this);
    }
}
