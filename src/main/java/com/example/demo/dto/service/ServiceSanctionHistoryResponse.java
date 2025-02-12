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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ServiceSanctionHistoryResponse {
    private Integer seq;
    private String poli_stat;
    private Integer user_seq;
    private String appl_begin;
    private String appl_end;
    private String short_exp;
    private String long_exp;
    private String comt;

    public ServiceSanctionHistoryResponse(ServiceSanctionHistoryDto dto) {
        this.seq = dto.getSeq();
        this.poli_stat = dto.getPoli_stat();
        this.user_seq = dto.getUser_seq();
        this.appl_begin = dto.getAppl_begin();
        this.appl_end = dto.getAppl_end();
        this.short_exp = dto.getShort_exp();
        this.long_exp = dto.getLong_exp();
        this.comt = dto.getComt();
    }
}
