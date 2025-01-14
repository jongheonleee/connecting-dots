package com.example.demo.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTermsRequest {

    @NotBlank(message = "poli_stat은 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "rule_stat은 대문자 3글자와 숫자 4자리로 구성되어야 합니다.")
    private String poli_stat;

    @NotBlank(message = "name은 필수값입니다.")
    private String name;

    @NotBlank(message = "rule_stat1은 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "rule_stat1은 대문자 2글자와 숫자 4자리로 구성되어야 합니다.")
    private String rule_stat1;

    @NotBlank(message = "op1은 필수값입니다.")
    @Pattern(regexp = "^(AND|OR|NOT|IS NULL|IS NOT NULL|BETWEEN|IN|LIKE)$", message = "op1는 대문자 형태의 SQL 조건절 연산자만 허용됩니다.")
    private String op1;

    @NotBlank(message = "rule_stat2은 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "rule_stat2은 대문자 2글자와 숫자 4자리로 구성되어야 합니다.")
    private String rule_stat2;

    @NotBlank(message = "op2은 필수값입니다.")
    @Pattern(regexp = "^(AND|OR|NOT|IS NULL|IS NOT NULL|BETWEEN|IN|LIKE)$", message = "op2는 대문자 형태의 SQL 조건절 연산자만 허용됩니다.")
    private String op2;

    @NotBlank(message = "rule_stat3은 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "rule_stat3은 대문자 2글자와 숫자 4자리로 구성되어야 합니다.")
    private String rule_stat3;

    private String comt;

    @NotBlank(message = "필수 여부는 필수값입니다.")
    @Pattern(regexp = "^[YN]$", message = "chk_use는 Y, N만 허용됩니다.")
    private String chk_use;

    @NotBlank(message = "code는 필수값입니다.")
    @Pattern(regexp = "^[0-9]{4}$", message = "code는 4자리 숫자 형태로 구성되어야 합니다.")
    private String code;
}
