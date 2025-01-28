package com.example.demo.dto.report;

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
public class ReportDto {

    private Integer rno;
    private String cate_code;
    private String title;
    private String cont;
    private String chk_change;
    private String comt;
    private Integer repo_seq; // 신고자
    private Integer resp_seq; // 신고 대상자
    private Integer boar; // bno
    private Integer cmnt; // cno
    private Integer repl; // rcno
    private Integer type; // 0 : bno, 1 : cno, 2 : rcno
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;
}
