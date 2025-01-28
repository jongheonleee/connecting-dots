package com.example.demo.repository.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserConditionsDetailDto;
import com.example.demo.dto.service.ServiceUserConditionsDto;
import java.util.List;

public interface ServiceUserConditionsRepository {

    int count();

    int countBySearchCondition(SearchCondition sc);

    boolean existsBySeq(Integer seq);

    boolean existsBySeqForUpdate(Integer seq);

    boolean existsByCondsCode(String conds_code);

    boolean existsByCondsCodeForUpdate(String conds_code);

    ServiceUserConditionsDto select(Integer seq);

    ServiceUserConditionsDto selectByCondsCode(String conds_code);

    List<ServiceUserConditionsDto> selectBySearchCondition(SearchCondition sc);

    List<ServiceUserConditionsDto> selectAll();

    ServiceUserConditionsDetailDto selectForUserConditions(String conds_code);

    int insert(ServiceUserConditionsDto dto);

    int update(ServiceUserConditionsDto dto);

    int updateChkUse(ServiceUserConditionsDto dto);

    int delete(Integer seq);

    int deleteByCondsCode(String conds_code);

    int deleteAll();
}
