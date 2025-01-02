package com.example.demo.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardResponseDto {

    private Integer bno;
    private String title;
    private String writer;
    private String cate_code;
    private String reg_date;
    private String thumb;
    private Integer comment_cnt;
    private Integer view_cnt;
    private Integer reco_cnt;
}
