package com.example.demo.repository.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceSanctionHistoryDto;
import java.util.List;

public interface ServiceSanctionHistoryRepository {

    int count();

    int countByUserSeq(Integer user_seq);

    int countBySearchCondition(SearchCondition sc);

    boolean existsBySeq(Integer seq);

    boolean existsBySeqForUpdate(Integer seq);

    boolean existsByPoliStat(String poli_stat);

    ServiceSanctionHistoryDto selectBySeq(Integer seq);

    List<ServiceSanctionHistoryDto> selectByPoliStat(String poli_stat);

    List<ServiceSanctionHistoryDto> selectByUserSeq(Integer user_seq);

    List<ServiceSanctionHistoryDto> selectByUserSeqForNow(Integer user_seq);

    List<ServiceSanctionHistoryDto> selectBySearchCondition(SearchCondition sc);

    ServiceSanctionHistoryDto selectBySeqForUpdate(Integer seq);

    List<ServiceSanctionHistoryDto> selectAll();

    int insert(ServiceSanctionHistoryDto dto);

    int update(ServiceSanctionHistoryDto dto);

    int updateForApplEnd(ServiceSanctionHistoryDto dto);

    int deleteBySeq(Integer seq);

    int deleteByUserSeq(Integer user_seq);

    int deleteAll();
}
