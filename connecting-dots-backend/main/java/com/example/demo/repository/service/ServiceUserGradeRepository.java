package com.example.demo.repository.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserGradeDto;
import java.util.List;

public interface ServiceUserGradeRepository {

    int count();

    int countBySearchCondition(SearchCondition sc);

    boolean existsByStatCode(String stat_code);

    boolean existsByStatCodeForUpdate(String stat_code);

    ServiceUserGradeDto selectByStatCode(String stat_code);

    List<ServiceUserGradeDto> selectBySearchCondition(SearchCondition sc);

    List<ServiceUserGradeDto> selectAll();

    int insert(ServiceUserGradeDto dto);

    int update(ServiceUserGradeDto dto);

    int deleteByStatCode(String stat_code);

    int deleteAll();
}
