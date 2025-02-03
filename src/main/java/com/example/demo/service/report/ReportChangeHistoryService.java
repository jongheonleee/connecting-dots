package com.example.demo.service.report;

import com.example.demo.dto.report.ReportChangeHistoryRequest;
import com.example.demo.dto.report.ReportChangeHistoryResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ReportChangeHistoryService {

    ReportChangeHistoryResponse create(ReportChangeHistoryRequest request);

    @Transactional(rollbackFor = Exception.class)
    ReportChangeHistoryResponse renew(ReportChangeHistoryRequest request);

    ReportChangeHistoryResponse readBySeq(Integer seq);

    List<ReportChangeHistoryResponse> readByRno(Integer rno);

    List<ReportChangeHistoryResponse> readAll();

    void modify(ReportChangeHistoryRequest request);

    void removeBySeq(Integer seq);

    @Transactional(rollbackFor = Exception.class)
    void removeByRno(Integer rno);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
