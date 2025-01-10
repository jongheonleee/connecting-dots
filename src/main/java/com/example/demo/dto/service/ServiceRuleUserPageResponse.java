package com.example.demo.dto.service;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ServiceRuleUserPageResponse {

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final Integer MAX_PAGE_SIZE = 100;
    private static final Integer MIN_PAGE_SIZE = 10;

    private Integer totalCnt;
    private Integer page;
    private Integer pageSize;
    private Integer totalPage;
    private List<ServiceRuleUseResponse> responses;

    private ServiceRuleUserPageResponse(Integer totalCnt, Integer page, Integer pageSize, List<ServiceRuleUseResponse> responses) {
        this.totalCnt = totalCnt;
        this.page = page == null ? DEFAULT_PAGE : page;
        this.pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        this.pageSize = this.pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : this.pageSize;
        this.pageSize = this.pageSize < MIN_PAGE_SIZE ? MIN_PAGE_SIZE : this.pageSize;
        totalPage = (totalCnt / pageSize) + (totalCnt % pageSize == 0 ? 0 : 1);
        this.responses = responses;
    }

    public static ServiceRuleUserPageResponse of(Integer totalCnt, Integer page, Integer pageSize, List<ServiceRuleUseResponse> responses) {
        return new ServiceRuleUserPageResponse(totalCnt, page, pageSize, responses);
    }

}
