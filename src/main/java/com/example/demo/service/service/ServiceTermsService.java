package com.example.demo.service.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceTermsRequest;
import com.example.demo.dto.service.ServiceTermsResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceTermsService {

    int count();

    int countBySearchCondition(SearchCondition sc);

    ServiceTermsResponse create(ServiceTermsRequest request);

    ServiceTermsResponse readByPoliStat(String poli_stat);

    PageResponse<ServiceTermsResponse> readBySearchCondition(SearchCondition sc);

    List<ServiceTermsResponse> readAll();

    // 리스폰스 형태로 반환하게 만들기
    String getServiceTermsCondition(String poli_stat);

    @Transactional(rollbackFor = Exception.class)
    void modify(ServiceTermsRequest request);

    @Transactional(rollbackFor = Exception.class)
    void modifyChkUse(ServiceTermsRequest request);

    void remove(String poli_stat);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
