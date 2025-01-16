package com.example.demo.dto.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BoardImgDto {
    private Integer bno;
    private Integer ino;
    private String name;
    private String chk_thumb;
    private String img;
    private String comt;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;

    public void updateBoardImg(String imgName, String imgUrl) {
        this.name = imgName;
        this.img = imgUrl;
    }
}
