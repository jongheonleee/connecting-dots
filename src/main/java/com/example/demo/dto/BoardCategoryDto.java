package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardCategoryDto {

    private String cate_code;
    private String top_cate;
    private String name;
    private String comt;
}
