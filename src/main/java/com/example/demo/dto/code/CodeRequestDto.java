package com.example.demo.dto.code;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CodeRequestDto {

    private Integer level;
    private String code;
    private String name;
    private String chk_use;
    private String reg_date;
    private Integer reg_user_seq;
    private Integer up_user_seq;
    private String up_date;
}
