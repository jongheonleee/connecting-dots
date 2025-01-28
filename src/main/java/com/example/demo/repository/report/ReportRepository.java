package com.example.demo.repository.report;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.report.ReportDto;
import java.util.List;

public interface ReportRepository {

    int count();

    int countByCateCode(String cate_code);

    int countByRepoSeq(Integer repo_seq);

    int countByRespSeq(Integer resp_seq);

    int countBySearchCondition(SearchCondition sc);

    ReportDto selectByRno(Integer rno);

    List<ReportDto> selectBySearchCondition(SearchCondition sc);

    List<ReportDto> selectAll();

    boolean existsByRno(Integer rno);

    boolean existsByRnoForUpdate(Integer rno);

    boolean existsByRepoSeq(Integer repo_seq);

    boolean existsByRespSeq(Integer resp_seq);

    int insert(ReportDto dto);

    int update(ReportDto dto);

    int delete(Integer rno);

    int deleteAll();
}
