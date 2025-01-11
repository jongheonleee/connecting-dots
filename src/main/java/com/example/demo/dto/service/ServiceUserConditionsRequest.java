package com.example.demo.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUserConditionsRequest {

    @NotBlank(message = "약관 정책 코드는 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "약관 정책 코드는 대문자 3글자와 숫자 4자리로 구성되어야 합니다.")
    private String conds_code;

    @NotBlank(message = "약관 정책명은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "약관 항목1 코드는 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "약관 정책 코드는 대문자 2글자와 숫자 4자리로 구성되어야 합니다.")
    private String cond_code1;

    @NotBlank(message = "약관 항목1의 필수 여부는 필수 입력 값입니다.")
    @Pattern(regexp = "^[YN]$", message = "약관 항목1의 필수 여부는 Y 또는 N으로 구성되어야 합니다.")
    private String chk_cond_code1;

    @NotBlank(message = "약관 항목2 코드는 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "약관 정책 코드2는 대문자 2글자와 숫자 4자리로 구성되어야 합니다.")
    private String cond_code2;

    @NotBlank(message = "약관 항목2의 필수 여부는 필수 입력 값입니다.")
    @Pattern(regexp = "^[YN]$", message = "약관 항목2의 필수 여부는 Y 또는 N으로 구성되어야 합니다.")
    private String chk_cond_code2;

    @NotBlank(message = "약관 항목3 코드는 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "약관 정책 코드3는 대문자 2글자와 숫자 4자리로 구성되어야 합니다.")
    private String cond_code3;

    @NotBlank(message = "약관 항목3의 필수 여부는 필수 입력 값입니다.")
    @Pattern(regexp = "^[YN]$", message = "약관 항목3의 필수 여부는 Y 또는 N으로 구성되어야 합니다.")
    private String chk_cond_code3;

    @NotBlank(message = "약관 항목4 코드는 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "약관 정책 코4드는 대문자 2글자와 숫자 4자리로 구성되어야 합니다.")
    private String cond_code4;

    @NotBlank(message = "약관 항목4의 필수 여부는 필수 입력 값입니다.")
    @Pattern(regexp = "^[YN]$", message = "약관 항목4의 필수 여부는 Y 또는 N으로 구성되어야 합니다.")
    private String chk_cond_code4;

    @NotBlank(message = "사용 여부는 필수 입력 값입니다.")
    @Pattern(regexp = "^[YN]$", message = "사용 여부는 Y 또는 N으로 구성되어야 합니다.")
    private String chk_use;

    private String comt;
}
