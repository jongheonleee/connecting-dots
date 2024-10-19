package com.example.demo.dto;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Objects.requireNonNullElse;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;


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
    private String searchOption;
    private String searchKeyword;
    private String sortOption;


    public SearchCondition() {
        this(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, "", "", "");
    }

    public SearchCondition(Integer page, Integer pageSize) {
        this(page, pageSize, "", "", "");
    }

    public SearchCondition(Integer page, Integer pageSize, String searchOption, String searchKeyword, String sortOption) {
        this.page = page;
        this.pageSize = pageSize;
        this.searchOption = searchOption;
        this.searchKeyword = searchKeyword;
        this.sortOption = sortOption;
    }

    public String getQueryString(Integer page) {
        return UriComponentsBuilder.newInstance()
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .queryParam("searchOption", searchOption)
                .queryParam("searchKeyword", searchKeyword)
                .queryParam("sortOption", sortOption)
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
