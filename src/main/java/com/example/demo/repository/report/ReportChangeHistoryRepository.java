package com.example.demo.repository.report;

import com.example.demo.dto.report.ReportChangeHistoryDto;
import java.util.List;

public interface ReportChangeHistoryRepository {

    int count();

    int countByRno(Integer rno);

    boolean existsBySeq(Integer seq);

    boolean existsByRno(Integer rno);

    boolean existsBySeqForUpdate(Integer seq);

    boolean existsByRnoForUpdate(Integer rno);

    ReportChangeHistoryDto selectBySeq(Integer seq);

    List<ReportChangeHistoryDto> selectByRno(Integer rno);

    List<ReportChangeHistoryDto> selectAll();

    ReportChangeHistoryDto selectLatestByRno(Integer rno);

    int insert(ReportChangeHistoryDto dto);

    int update(ReportChangeHistoryDto dto);

    int delete(Integer seq);

    int deleteByRno(Integer rno);

    int deleteAll();
}
