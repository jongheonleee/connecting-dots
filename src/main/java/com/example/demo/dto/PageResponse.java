package com.example.demo.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class PageResponse<T> {

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final Integer MAX_PAGE_SIZE = 100;
    private static final Integer MIN_PAGE_SIZE = 10;

    private Integer totalCnt;
    private Integer page;
    private Integer pageSize;
    private Integer totalPage;
    private List<T> responses;

    public PageResponse(Integer totalCnt, SearchCondition sc, List<T> responses) {
        this.totalCnt = totalCnt;
        this.page = sc.getPage() == null ? DEFAULT_PAGE : sc.getPage();
        this.pageSize = sc.getPageSize() == null ? DEFAULT_PAGE_SIZE : sc.getPageSize();
        this.pageSize = this.pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : this.pageSize;
        this.pageSize = this.pageSize < MIN_PAGE_SIZE ? MIN_PAGE_SIZE : this.pageSize;
        totalPage = (totalCnt / sc.getPageSize()) + (totalCnt % sc.getPageSize() == 0 ? 0 : 1);
        this.responses = responses;
    }

}
