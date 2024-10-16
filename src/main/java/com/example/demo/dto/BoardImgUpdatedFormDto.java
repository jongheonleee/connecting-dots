package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardImgUpdatedFormDto {

    private Integer ino;
    private Integer bno;
    private String img;
    private String comt;
    private String up_id;
}
