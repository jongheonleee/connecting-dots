package com.example.demo.dto.code.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CodeResponseDto {
    private Integer seq;
    private Integer level;
    private String code;
    private String name;
    private String chk_use;
    private String top_code;
}
