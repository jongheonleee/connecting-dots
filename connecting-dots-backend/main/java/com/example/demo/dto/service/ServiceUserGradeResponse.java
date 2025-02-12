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
public class ServiceUserGradeResponse {
    private String stat_code;
    private String name;
    private Integer ord;
    private String short_exp;
    private String long_exp;
    private String img;
    private String chk_use;
    private String comt;

    public ServiceUserGradeResponse(ServiceUserGradeDto dto) {
        this.stat_code = dto.getStat_code();
        this.name = dto.getName();
        this.ord = dto.getOrd();
        this.short_exp = dto.getShort_exp();
        this.long_exp = dto.getLong_exp();
        this.img = dto.getImg();
        this.chk_use = dto.getChk_use();
        this.comt = dto.getComt();
    }
}
