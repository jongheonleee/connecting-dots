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
public class CommentChangeHistoryResponse {

    private Integer seq;
    private String bef_cont;
    private String aft_cont;
    private String appl_time;
    private String comt;
    private Integer cno;
    private Integer bno;
}
