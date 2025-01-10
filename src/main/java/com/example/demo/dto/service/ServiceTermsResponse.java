package com.example.demo.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
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
}
