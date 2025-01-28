package com.example.demo.repository.service;

import com.example.demo.dto.service.ServiceUserConditionDto;
import java.util.List;

public interface ServiceUserConditionRepository {

    int count();

    boolean existsByCondCode(String cond_code);

    boolean existsByCondCodeForUpdate(String cond_code);

    ServiceUserConditionDto select(String cond_code);

    List<ServiceUserConditionDto> selectAll();

    int insert(ServiceUserConditionDto dto);

    int update(ServiceUserConditionDto dto);

    int updateChkUse(ServiceUserConditionDto dto);

    int delete(String cond_code);

    int deleteAll();
}
