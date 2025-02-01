package com.example.demo.service.report.impl;

import com.example.demo.dto.report.ReportChangeHistoryRequest;
import com.example.demo.dto.report.ReportChangeHistoryResponse;
import com.example.demo.global.error.exception.business.report.ReportChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.report.ReportChangeHistoryRepository;
import com.example.demo.repository.report.ReportRepository;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportChangeHistoryServiceImpl {

    private final ReportChangeHistoryRepository reportChangeHistoryRepository;
    private final ReportRepository reportRepository;
    private final CustomFormatter formatter;


    public ReportChangeHistoryResponse create(final ReportChangeHistoryRequest request) {
        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getLastDateFormat());
        checkReportExists(request.getRno());
        checkApplied(1, reportChangeHistoryRepository.insert(dto));
        return dto.toResponse();
    }

    @Transactional(rollbackFor = Exception.class)
    public ReportChangeHistoryResponse renew(final ReportChangeHistoryRequest request) {
        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getLastDateFormat());
        checkReportExistsForUpdate(request.getRno());
        checkReportChangeHistoryExistsForUpdate(request.getRno());
        var foundOriginal = reportChangeHistoryRepository.selectLatestByRno(request.getRno());
        foundOriginal.updateApplEnd(formatter.minusDateFormat(1), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        checkApplied(2, reportChangeHistoryRepository.update(foundOriginal) + reportChangeHistoryRepository.insert(dto));
        return dto.toResponse();
    }



    public ReportChangeHistoryResponse readBySeq(final Integer seq) {
        var found = reportChangeHistoryRepository.selectBySeq(seq);
        if (found == null) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 변경 이력이 존재하지 않습니다. seq: {}", seq);
            throw new ReportChangeHistoryNotFoundException();
        }

        return found.toResponse();
    }

    public List<ReportChangeHistoryResponse> readByRno(final Integer rno) {
        return reportChangeHistoryRepository.selectByRno(rno)
                                            .stream()
                                            .map(o -> o.toResponse())
                                            .toList();
    }


    public List<ReportChangeHistoryResponse> readAll() {
        return reportChangeHistoryRepository.selectAll()
                                            .stream()
                                            .map(o -> o.toResponse())
                                            .toList();
    }

    public void modify(final ReportChangeHistoryRequest request) {
        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getLastDateFormat());
        checkReportChangeHistoryForUpdateBySeq(request.getSeq());
        checkApplied(1, reportChangeHistoryRepository.update(dto));
    }

    public void removeBySeq(final Integer seq) {
        checkApplied(1, reportChangeHistoryRepository.delete(seq));
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByRno(final Integer rno) {
        checkApplied(reportChangeHistoryRepository.countByRno(rno), reportChangeHistoryRepository.deleteByRno(rno));
    }

    @Transactional(rollbackFor = Exception.class)
    void removeAll() {
        checkApplied(reportChangeHistoryRepository.count(), reportChangeHistoryRepository.deleteAll());
    }

    private void checkReportExists(final Integer rno) {
        boolean exists = reportRepository.existsByRno(rno);
        if (!exists) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 리포트가 존재하지 않습니다. rno: {}", rno);
            throw new ReportNotFoundException();
        }
    }

    private void checkApplied(Integer expected, Integer actual) {
        if (expected != actual) {
            log.error("[REPORT_CHANGE_HISTORY] DBMS에 정상 반영되지 않았습니다. expected: {}, actual: {}", expected, actual);
            throw new NotApplyOnDbmsException();
        }
    }

    private void checkReportExistsForUpdate(final Integer rno) {
        boolean existsReport = reportRepository.existsByRnoForUpdate(rno);
        if (!existsReport) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 리포트가 존재하지 않습니다. rno: {}", rno);
            throw new ReportNotFoundException();
        }
    }

    private void checkReportChangeHistoryExistsForUpdate(final Integer rno) {
        boolean existsChangeHistory = reportChangeHistoryRepository.existsByRnoForUpdate(rno);
        if (!existsChangeHistory) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 리포트의 변경 이력이 존재하지 않습니다. rno: {}", rno);
            throw new ReportChangeHistoryNotFoundException();
        }
    }

    private void checkReportChangeHistoryForUpdateBySeq(final Integer seq) {
        boolean exists = reportChangeHistoryRepository.existsBySeqForUpdate(seq);
        if (!exists) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 변경 이력이 존재하지 않습니다. seq: {}", seq);
            throw new ReportChangeHistoryNotFoundException();
        }
    }

}
