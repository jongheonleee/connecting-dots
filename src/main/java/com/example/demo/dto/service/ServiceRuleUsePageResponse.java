package com.example.demo.dto.service;

import com.example.demo.dto.SearchCondition;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

// 현재 이 코드는 중복되고 있음
// code, serviceRuleUse, ....
// 서로 다른 부분은 List<CodeResponse> responses; 또는 List<ServiceRuleUseResponse> responses; 부분임
// 이 부분 지네릭스로 처리할 예정
@Getter
@ToString
public class ServiceRuleUsePageResponse {

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final Integer MAX_PAGE_SIZE = 100;
    private static final Integer MIN_PAGE_SIZE = 10;

    private Integer totalCnt;
    private Integer page;
    private Integer pageSize;
    private Integer totalPage;
    private List<ServiceRuleUseResponse> responses;

    private ServiceRuleUsePageResponse(Integer totalCnt, SearchCondition sc, List<ServiceRuleUseResponse> responses) {
        this.totalCnt = totalCnt;
        this.page = sc.getPage() == null ? DEFAULT_PAGE : sc.getPage();
        this.pageSize = sc.getPageSize() == null ? DEFAULT_PAGE_SIZE : sc.getPageSize();
        this.pageSize = this.pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : this.pageSize;
        this.pageSize = this.pageSize < MIN_PAGE_SIZE ? MIN_PAGE_SIZE : this.pageSize;
        totalPage = (totalCnt / sc.getPageSize()) + (totalCnt % sc.getPageSize() == 0 ? 0 : 1);
        this.responses = responses;
    }

    public static ServiceRuleUsePageResponse of(Integer totalCnt, SearchCondition sc, List<ServiceRuleUseResponse> responses) {
        return new ServiceRuleUsePageResponse(totalCnt, sc, responses);
    }

}
