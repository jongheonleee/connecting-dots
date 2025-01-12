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
public class ServiceSanctionHistoryRequest {
    private Integer seq;

    @NotBlank(message = "poli_stat은 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "rule_stat은 대문자 3글자와 숫자 4자리로 구성되어야 합니다.")
    private String poli_stat;

    @NotBlank(message = "user_seq은 필수값입니다.")
    private Integer user_seq;

    @NotBlank(message = "appl_begin은 필수값입니다.")
    @Pattern(regexp = "^(\\d{4})/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])$",
            message = "appl_begin은 yyyy/mm/dd 형식이어야 합니다.")
    private String appl_begin;

    @NotBlank(message = "appl_end은 필수값입니다.")
    @Pattern(regexp = "^(\\d{4})/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])$",
            message = "appl_end은 yyyy/mm/dd 형식이어야 합니다.")
    private String appl_end;

    @NotBlank(message = "short_exp은 필수값입니다.")
    private String short_exp;

    @NotBlank(message = "long_exp은 필수값입니다.")
    private String long_exp;

    private String comt;
}
