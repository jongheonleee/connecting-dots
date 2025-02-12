package com.example.demo.repository.report;

import com.example.demo.dto.report.ReportProcessDetailsDto;
import java.util.List;

public interface ReportProcessDetailsRepository {

    int count();

    int countByRno(Integer rno);

    int insert(ReportProcessDetailsDto dto);

    boolean existsBySeq(Integer seq);

    boolean existsByRno(Integer rno);

    boolean existsBySeqForUpdate(Integer seq);

    boolean existsByRnoForUpdate(Integer rno);

    ReportProcessDetailsDto selectLatestByRno(Integer rno);

    ReportProcessDetailsDto selectBySeq(Integer seq);

    List<ReportProcessDetailsDto> selectByRno(Integer rno);

    List<ReportProcessDetailsDto> selectAll();

    ReportProcessDetailsDto selectByRnoAtPresent(Integer rno);

    int update(ReportProcessDetailsDto dto);

    int deleteBySeq(Integer seq);

    int deleteByRno(Integer rno);

    int deleteAll();
}
