package com.example.demo.dto.ord_category;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDto {
    private String cate_code;
    private String top_cate;
    private String name;
    private String comt;
    private String reg_date;
    private String reg_id;
    private String up_date;
    private String up_id;
    private List<CategoryDto> subCategories;
}
