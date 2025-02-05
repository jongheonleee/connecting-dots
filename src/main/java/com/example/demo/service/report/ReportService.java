package com.example.demo.service.report;

import com.example.demo.dto.report.ReportDetailResponse;
import com.example.demo.dto.report.ReportRequest;
import com.example.demo.dto.report.ReportResponse;


public interface ReportService {

    ReportResponse create(ReportRequest request);

    ReportResponse recover(RuntimeException e);

    ReportDetailResponse readReportDetailsBySeq(Integer rno);

    void modify(ReportRequest request);

    void removeByRno(Integer rno);

    void removeAllForUserLeave(Integer userSeq);

    void removeAll();
}
