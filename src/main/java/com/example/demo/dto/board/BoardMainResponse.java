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
public class BoardMainResponse {
    private Integer bno;
    private String title;
    private String writer;
    private String cate_name;
    private String reg_date;
    private Integer view_cnt;
    private Integer reco_cnt;
    private String thumb;
    private Integer comment_cnt;
}
