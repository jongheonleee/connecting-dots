package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Setter
@ToString
public class BoardImgFormDto {

    private Integer ino;
    private Integer bno;
    private String img;
    private String comt;
    private String reg_date;
    private String reg_id;
    private String up_date;
    private String up_id;
}
