package com.example.demo.dto.report;

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
public class ReportCategoryResponse {

    private String cate_code;
    private String top_cate;
    private String name;
    private String comt;
    private Integer ord;
    private String chk_use;
    private Integer level;
}
