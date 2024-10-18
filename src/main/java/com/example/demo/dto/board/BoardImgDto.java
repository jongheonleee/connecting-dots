package com.example.demo.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardImgDto {
    private Integer ino;
    private String img;
    private String comt;
    private String name;
}
