package com.example.demo.dto.comment;

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
public class CommentResponse {
    private Integer cno;
    private Integer bno;
    private Integer like_cnt;
    private Integer dislike_cnt;
    private String cont;
    private Integer user_seq;
    private String writer;
}
