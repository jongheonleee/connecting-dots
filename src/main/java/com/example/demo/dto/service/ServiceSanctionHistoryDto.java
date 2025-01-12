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
public class ServiceSanctionHistoryDto {
    private Integer seq;
    private String poli_stat;
    private Integer user_seq;
    private String appl_begin;
    private String appl_end;
    private String short_exp;
    private String long_exp;
    private String comt;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;

    public ServiceSanctionHistoryDto(ServiceSanctionHistoryRequest request, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.poli_stat = request.getPoli_stat();
        this.user_seq = request.getUser_seq();
        this.appl_begin = request.getAppl_begin();
        this.appl_end = request.getAppl_end();
        this.short_exp = request.getShort_exp();
        this.long_exp = request.getLong_exp();
        this.comt = request.getComt();
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

    public ServiceSanctionHistoryResponse toResponse() {
        return new ServiceSanctionHistoryResponse(this);
    }
}
