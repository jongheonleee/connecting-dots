package com.example.demo.dto.board;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDetailDto {
    private Integer bno;
    private String cate_code;
    private String id;
    private String title;
    private String writer;
    private Integer view_cnt;
    private Integer reco_cnt;
    private Integer not_reco_cnt;
    private String content;
    private String comt;
    private String reg_date;
    private String reg_id;
    private String up_date;
    private String up_id;
    private List<BoardImgDto> boardImgs;
    private BoardCategoryDto category;
}
