package com.example.demo.dto.ord_board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BestCommentBoardDto {

    private Integer seq;
    private Integer bno;
    private String appl_begin;
    private String appl_end;
    private String comt;
    private String reg_date;
    private String reg_id;
    private String up_date;
    private String up_id;
    private String used;
}
