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
public class BoardImgResponse {
    private Integer bno;
    private Integer ino;
    private String name;
    private String chk_thumb;
    private String img;
    private String comt;

    public BoardImgResponse(BoardImgDto boardImgDto) {
    }
}
