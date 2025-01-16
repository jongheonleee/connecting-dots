package com.example.demo.dto.board;


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
public class BoardDto {

    private Integer bno;
    private String cate_code;
    private Integer user_seq;
    private String writer;
    private String title;
    private Integer view_cnt;
    private Integer reco_cnt;
    private Integer not_reco_cnt;
    private String cont;
    private String comt;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;

}
