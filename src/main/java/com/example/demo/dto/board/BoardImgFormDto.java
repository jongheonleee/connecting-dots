package com.example.demo.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardImgFormDto {

    private Integer ino;
    private Integer bno;
    private String name;
    private String img;
    private String comt;
    private String reg_date;
    private String reg_id;
    private String up_date;
    private String up_id;

    public void updateBoardImg(String imgName, String imgUrl) {
        this.name = imgName;
        this.img = imgUrl;
    }
}
