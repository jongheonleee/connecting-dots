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
public class ServiceUserConditionRequest {
    @NotBlank(message = "cond_code은 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "cond_code은 대문자 3글자와 숫자 4자리로 구성되어야 합니다.")
    private String cond_code;

    @NotBlank(message = "name은 필수값입니다.")
    private String name;

    @NotBlank(message = "short_exp은 필수값입니다.")
    private String short_exp;

    @NotBlank(message = "long_exp은 필수값입니다.")
    private String long_exp;

    @NotBlank(message = "chk_use은 필수값입니다.")
    @Pattern(regexp = "^[YN]$", message = "chk_use은 Y, N만 허용됩니다.")
    private String chk_use;

    private String law1;
    private String law2;
    private String law3;
    private String comt;
}
