package com.example.demo.service.report;

import com.example.demo.dto.report.ReportProcessDetailsRequest;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import java.util.List;

public interface ReportProcessDetailsService {

    ReportProcessDetailsResponse create(ReportProcessDetailsRequest request);

    ReportProcessDetailsResponse renew(ReportProcessDetailsRequest request);

    ReportProcessDetailsResponse readBySeq(Integer seq);

    List<ReportProcessDetailsResponse> readByRno(Integer rno);

    List<ReportProcessDetailsResponse> readAll();

    boolean canChangeReport(Integer rno);

    void modify(ReportProcessDetailsRequest request);

    void removeBySeq(Integer seq);


    void removeByRno(Integer rno);

    void removeAll();
}
