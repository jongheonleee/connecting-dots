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
public class ServiceUserGradeDto {
    private String stat_code;
    private String name;
    private Integer ord;
    private String short_exp;
    private String long_exp;
    private String img;
    private String chk_use;
    private String comt;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;
    private String poli_stat;

    public ServiceUserGradeDto(ServiceUserGradeRequest request, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.stat_code = request.getStat_code();
        this.name = request.getName();
        this.ord = request.getOrd();
        this.short_exp = request.getShort_exp();
        this.long_exp = request.getLong_exp();
        this.img = request.getImg();
        this.chk_use = request.getChk_use();
        this.comt = request.getComt();
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

    public ServiceUserGradeResponse toResponse() {
        return new ServiceUserGradeResponse(this);
    }
}
