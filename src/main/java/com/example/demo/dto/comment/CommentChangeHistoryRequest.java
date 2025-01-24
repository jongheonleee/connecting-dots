package com.example.demo.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentChangeHistoryRequest {

    private Integer seq;
    private String bef_cont;
    private String aft_cont;
    private String appl_time;
    private String comt;
    private Integer cno;
    private Integer bno;
}
