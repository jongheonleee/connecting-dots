package com.example.demo.dto.service;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceRuleUseRequest {


    @NotBlank(message = "rule_stat은 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "rule_stat은 대문자 2글자와 숫자 4자리로 구성되어야 합니다.")
    private String rule_stat;

    @NotBlank(message = "name은 필수값입니다.")
    private String name;

    @NotBlank(message = "tar_name은 필수값입니다.")
    private String tar_name;

    @NotBlank(message = "op1은 필수값입니다.")
    @Pattern(regexp = "^(=|!=|<=|>=|<|>)$", message = "op1는 연산자만 허용됩니다.")
    private String op1;

    @Pattern(regexp = "^(=|!=|<=|>=|<|>)$", message = "op2는 연산자만 허용됩니다.")
    private String op2;

    @Pattern(regexp = "^(=|!=|<=|>=|<|>)$", message = "op3는 연산자만 허용됩니다.")
    private String op3;

    @NotBlank(message = "val1은 필수값입니다.")
    @Pattern(regexp = "^\\d+$", message = "val1는 숫자만 허용됩니다.")
    private String val1;

    @Pattern(regexp = "^\\d+$", message = "val2는 숫자만 허용됩니다.")
    private String val2;

    @Pattern(regexp = "^\\d+$", message = "val3는 숫자만 허용됩니다.")
    private String val3;

    @Pattern(regexp = "^\\d+$", message = "val4는 숫자만 허용됩니다.")
    private String val4;

    @Pattern(regexp = "^\\d+$", message = "val5는 숫자만 허용됩니다.")
    private String val5;

    @NotBlank(message = "필수 여부는 필수값입니다.")
    @Pattern(regexp = "^[YN]$", message = "chk_use는 Y, N만 허용됩니다.")
    private String chk_use;

    @NotBlank(message = "code는 필수값입니다.")
    @Pattern(regexp = "^[0-9]{4}$", message = "code는 4자리 숫자 형태로 구성되어야 합니다.")
    private String code;

    @NotNull(message = "curr_use_val은 필수값입니다.")
    @Min(value = 1, message = "curr_use_val은 1 이상이어야 합니다.")
    @Max(value = 5, message = "curr_use_val은 5 이하여야 합니다.")
    private Integer curr_use_val;

    @NotNull(message = "curr_use_op은 필수값입니다.")
    @Min(value = 1, message = "curr_use_op은 1 이상이어야 합니다.")
    @Max(value = 3, message = "curr_use_op은 3 이하여야 합니다.")
    private Integer curr_use_op;

}
