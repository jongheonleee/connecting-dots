package com.example.demo.dto.code.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CodeRequestDto {
    private Integer seq;
    private Integer level;
    private String code;
    private String name;
    private String chk_use;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;
    private String top_code;
}
