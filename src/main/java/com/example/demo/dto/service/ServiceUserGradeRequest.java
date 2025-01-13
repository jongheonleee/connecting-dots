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
public class ServiceUserGradeRequest {

    @NotBlank(message = "stat_code는 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "stat_code는 대문자 3글자와 숫자 4자리로 구성되어야 합니다.")
    private String stat_code;

    @NotBlank(message = "name은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "ord는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{1,6}$", message = "ord는 1~6자리 숫자로 구성되어야 합니다.")
    private Integer ord;

    @NotBlank(message = "short_exp는 필수 입력 값입니다.")
    private String short_exp;

    @NotBlank(message = "long_exp는 필수 입력 값입니다.")
    private String long_exp;

    @NotBlank(message = "img는 필수 입력 값입니다.")
    private String img;

    @NotBlank(message = "chk_use는 필수 입력 값입니다.")
    @Pattern(regexp = "^[YN]$", message = "chk_use는 Y 또는 N으로 구성되어야 합니다.")
    private String chk_use;

    private String comt;
}
