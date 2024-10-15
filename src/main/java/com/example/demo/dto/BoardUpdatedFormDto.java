package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardUpdatedFormDto {

    private Long bno;
    private String title;
    private String content;
    private String comt;
    private String up_date;
    private String up_id;

}
