package com.example.demo.service.report;

import com.example.demo.dto.report.ReportProcessDetailsRequest;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ReportProcessDetailsService {

    ReportProcessDetailsResponse create(ReportProcessDetailsRequest request);

    @Transactional(rollbackFor = Exception.class)
    ReportProcessDetailsResponse renew(ReportProcessDetailsRequest request);

    ReportProcessDetailsResponse readBySeq(Integer seq);

    List<ReportProcessDetailsResponse> readByRno(Integer rno);

    List<ReportProcessDetailsResponse> readAll();

    void modify(ReportProcessDetailsRequest request);

    void removeBySeq(Integer seq);

    @Transactional(rollbackFor = Exception.class)
    void removeByRno(Integer rno);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
