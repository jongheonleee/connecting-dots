package com.example.demo.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSanctionHistoryResponse {
    private Integer seq;
    private String poli_stat;
    private Integer user_seq;
    private String appl_begin;
    private String appl_end;
    private String short_exp;
    private String long_exp;
    private String comt;
}
