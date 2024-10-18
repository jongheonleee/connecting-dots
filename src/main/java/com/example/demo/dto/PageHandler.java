package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

// 페이징 처리
@Getter
@Setter
@ToString
public class PageHandler {

    private SearchCondition sc;
    private int totalCnt;
    private int totalPage;
    private int beginPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    public PageHandler(int page, int totalCnt) {
        this(totalCnt, new SearchCondition(page, 10));
    }


    public PageHandler(int totalCnt, SearchCondition sc) {
        this.totalCnt = totalCnt;
        this.sc = sc;
        calculate();
    }

    private void calculate() {
        totalPage = (totalCnt / sc.getPageSize()) + (totalCnt % sc.getPageSize() == 0 ? 0 : 1);
        beginPage = ((sc.getPage()-1) / sc.getPageSize()) * sc.getPageSize() + 1;
        endPage = (beginPage - 1) + sc.getPageSize();
        endPage = endPage > totalPage ? totalPage : endPage;
        prev = beginPage == sc.getPage() ? false : true;
        next = endPage == totalPage ? false : true;
    }

    public String getQuery() {
        return UriComponentsBuilder.newInstance()
                .queryParam("page", sc.getPage())
                .queryParam("pageSize", sc.getPageSize())
                .queryParam("searchOption", sc.getSearchOption())
                .queryParam("searchKeyword", sc.getSearchKeyword())
                .queryParam("sortOption", sc.getSortOption())
                .build()
                .toString();
    }

    public String getResult() {
        StringBuilder sb = new StringBuilder();
        // <
        if (prev) {
            sb.append("<");
        }

        // 번호
        for (int i=beginPage; i<=endPage; i++) {
            sb.append(" ").append(i).append(" ");
        }

        // >
        if (next) {
            sb.append(">");
        }

        return sb.toString();
    }


}
