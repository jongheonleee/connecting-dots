package com.example.demo.dto.code;

import com.example.demo.domain.Code;
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
public class CodeDto {
    private Integer seq;
    private Integer level;
    private String code;
    private String name;
    private String chk_use;
    private String top_code;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;

    public CodeDto(Code code, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.level = code.getLevel();
        this.code = code.getCode();
        this.name = code.getName();
        this.chk_use = code.getChkUse();
        this.top_code = code.getTopCode();
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }
}
