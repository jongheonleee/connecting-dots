package com.example.demo.dto.ord_comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentResponseDto {
    private Integer cno;
    private Integer bno;
    private String content;
    private String writer;
    private Integer like_cnt;
    private Integer dislike_cnt;
    private String reg_date;
    private String reg_id;
    private String up_date;
    private String up_id;
}
