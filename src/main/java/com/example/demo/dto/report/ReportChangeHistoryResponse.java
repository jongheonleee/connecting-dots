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
public class ReportChangeHistoryResponse {

    private Integer seq;
    private Integer rno;
    private String title;
    private String cont;
    private String comt;
    private String appl_begin;
    private String appl_end;
}
