package com.example.demo.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReportResponse {

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
}
