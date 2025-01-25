package com.example.demo.dto.reply;

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
public class ReplyResponse {

    private Integer rcno;
    private Integer cno;
    private Integer bno;
    private String cont;
    private Integer like_cnt;
    private Integer dislike_cnt;
    private Integer user_seq;
    private String writer;
}
