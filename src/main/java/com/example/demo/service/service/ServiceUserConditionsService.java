package com.example.demo.service.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserConditionsDetailResponse;
import com.example.demo.dto.service.ServiceUserConditionsRequest;
import com.example.demo.dto.service.ServiceUserConditionsResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceUserConditionsService {

    int count();

    int countBySearchCondition(SearchCondition sc);

    ServiceUserConditionsResponse create(ServiceUserConditionsRequest request);

    ServiceUserConditionsResponse readByCondsCode(String conds_code);

    PageResponse<ServiceUserConditionsResponse> readBySearchCondition(SearchCondition sc);

    ServiceUserConditionsDetailResponse readByCondsCodeForUserConditions(String conds_code);

    @Transactional(rollbackFor = Exception.class)
    void modify(ServiceUserConditionsRequest request);

    @Transactional(rollbackFor = Exception.class)
    void modifyChkUse(ServiceUserConditionsRequest request);

    void removeByCondsCode(String conds_code);

    void remove(Integer seq);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
