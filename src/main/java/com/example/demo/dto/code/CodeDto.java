package com.example.demo.dto.code;

import com.example.demo.domain.Code;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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

    public CodeDto(Code code, String chk_use, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.level = code.getLevel();
        this.code = code.getCode();
        this.name = code.getName();
        this.top_code = code.getTopCode();
        this.chk_use = chk_use;
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

    public CodeDto(CodeRequest request, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        this.level = request.getLevel();
        this.code = request.getCode();
        this.name = request.getName();
        this.top_code = request.getTop_code();
        this.chk_use = request.getChk_use();
        this.reg_date = reg_date;
        this.reg_user_seq = reg_user_seq;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

    public CodeResponse toResponse() {
        return new CodeResponse(this);
    }



}
