package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardFormDto {
    private String cate_code;
    private String id;
    private String title;
    private String writer;
    private Long view_cnt;
    private Long reco_cnt;
    private Long not_reco_cnt;
    private String content;
    private String comt;
    private String reg_date;
    private String reg_id;
    private String up_date;
    private String up_id;
}
