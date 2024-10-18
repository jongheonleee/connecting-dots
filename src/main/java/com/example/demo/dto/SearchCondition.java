package com.example.demo.dto;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Objects.requireNonNullElse;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

// 검색 쿼리 처리
// ?page=10&pageSize=10&option=A&keyword=title
@Getter
@Setter
@ToString
public class SearchCondition {

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final Integer MAX_PAGE_SIZE = 100;
    private static final Integer MIN_PAGE_SIZE = 10;

    private Integer page;
    private Integer pageSize;
    private String option;
    private String keyword;

    public SearchCondition() {
        this(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, "", "");
    }

    public SearchCondition(Integer page, Integer pageSize, String option, String keyword) {
        this.page = page;
        this.pageSize = pageSize;
        this.option = option;
        this.keyword = keyword;
    }

    public String getQueryString() {
        return UriComponentsBuilder.newInstance()
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .queryParam("option", option)
                .queryParam("keyword", keyword)
                .build()
                .toString();
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE);
        this.pageSize = max(MIN_PAGE_SIZE, min(MAX_PAGE_SIZE, this.pageSize));
    }

    public Integer getOffset() {
        return (page - 1) * pageSize;
    }
}
