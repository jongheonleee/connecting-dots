package com.example.demo.application.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceRuleUseRequest;
import com.example.demo.dto.service.ServiceRuleUseResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceRuleUseService {

    int count();

    int countByCode(String code);

    ServiceRuleUseResponse create(ServiceRuleUseRequest request);

    ServiceRuleUseResponse readByRuleStat(String rule_stat);

    List<ServiceRuleUseResponse> readByCode(String code);

    PageResponse<ServiceRuleUseResponse> readBySearchCondition(SearchCondition sc);

    List<ServiceRuleUseResponse> readAll();

    @Transactional(rollbackFor = Exception.class)
    void modify(ServiceRuleUseRequest request);

    @Transactional(rollbackFor = Exception.class)
    void modifyChkUse(ServiceRuleUseRequest request);

    void removeByRuleStat(String rule_stat);

    @Transactional(rollbackFor = Exception.class)
    void removeByCode(String code);

    @Transactional
    void removeAll();
}
