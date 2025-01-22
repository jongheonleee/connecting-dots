package com.example.demo.dto.comment;

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
public class CommentChangeHistoryDto {
    private Integer seq;
    private String bef_cont;
    private String aft_cont;
    private String appl_time;
    private String comt;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;
    private Integer cno;
    private Integer bno;
}
