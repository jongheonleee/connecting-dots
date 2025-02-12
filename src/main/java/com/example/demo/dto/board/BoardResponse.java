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
public class BoardResponse {
    private Integer user_seq;
    private Integer bno;
    private String cate_code;
    private String title;
    private String writer;
    private Integer view_cnt;
    private Integer reco_cnt;
    private Integer not_reco_cnt;
    private String cont;
    private String comt;
}
