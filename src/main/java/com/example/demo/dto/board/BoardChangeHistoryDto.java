package com.example.demo.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BoardChangeHistoryDto {

    private Integer seq;
    private Integer bno;
    private String title;
    private String cont;
    private String comt;
    private String appl_begin;
    private String appl_end;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;

    public void updateApplEnd(String appl_end, String up_date, Integer up_user_seq) {
        this.appl_end = appl_end;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }
}
