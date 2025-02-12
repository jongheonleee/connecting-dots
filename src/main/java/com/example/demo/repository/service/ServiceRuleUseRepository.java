package com.example.demo.repository.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceRuleUseDto;
import java.util.List;

public interface ServiceRuleUseRepository {

    int count();

    int countByCode(String code);

    boolean existsByRuleStatForUpdate(String rule_stat);

    boolean existsByRuleStat(String rule_stat);

    int countBySearchCondition(SearchCondition sc);

    int insert(ServiceRuleUseDto dto);

    ServiceRuleUseDto selectByRuleStat(String rule_stat);

    List<ServiceRuleUseDto> selectByCode(String code);

    List<ServiceRuleUseDto> selectBySearchCondition(SearchCondition sc);

    List<ServiceRuleUseDto> selectAll();

    int update(ServiceRuleUseDto dto);

    int updateUse(ServiceRuleUseDto dto);

    int deleteByRuleStat(String rule_stat);

    int deleteByCode(String code);

    int deleteAll();
}
