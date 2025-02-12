package com.example.demo.repository.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceTermsConditionDto;
import com.example.demo.dto.service.ServiceTermsDto;
import java.util.List;

public interface ServiceTermsRepository {

    int count();

    int countBySearchCondition(SearchCondition sc);

    boolean existsByPoliStat(String poli_stat);

    boolean existsByPoliStatForUpdate(String poli_stat);

    ServiceTermsDto select(String poli_stat);

    List<ServiceTermsDto> selectBySearchCondition(SearchCondition sc);

    List<ServiceTermsDto> selectAll();

    ServiceTermsConditionDto selectForCondition(String poli_stat);

    int insert(ServiceTermsDto dto);

    int update(ServiceTermsDto dto);

    int updateChkUse(ServiceTermsDto dto);

    int delete(String poli_stat);

    int deleteAll();
}
