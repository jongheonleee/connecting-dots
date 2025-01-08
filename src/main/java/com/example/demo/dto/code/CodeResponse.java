package com.example.demo.dto.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CodeResponse {
    private Integer seq;
    private Integer level;
    private String code;
    private String name;
    private String chk_use;
    private String top_code;

    public CodeResponse(CodeDto dto) {
        this.seq = dto.getSeq();
        this.level = dto.getLevel();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.chk_use = dto.getChk_use();
        this.top_code = dto.getTop_code();
    }
}
