package com.example.demo.dto.board;

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
public class BoardChangeHistoryResponse {

    private Integer seq;
    private Integer bno;
    private String title;
    private String cont;
    private String comt;
    private String appl_begin;
    private String appl_end;
}
