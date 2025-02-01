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
        var dto = request.toDto(formatter.getCurrentDateFormat(),
                formatter.getManagerSeq(), formatter.getCurrentDateFormat(),
                formatter.getLastDateFormat());

        boolean exists = reportRepository.existsByRno(request.getRno());
        if (!exists) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 리포트가 존재하지 않습니다. rno: {}", request.getRno());
            throw new ReportNotFoundException();
        }

        int rowCnt = reportChangeHistoryRepository.insert(dto);
        if (rowCnt != 1) {
            log.error("[REPORT_CHANGE_HISTORY] DBMS에 정상 반영되지 않았습니다. rno: {}", request.getRno());
            throw new NotApplyOnDbmsException();
        }

        return dto.toResponse();
    }

    @Transactional(rollbackFor = Exception.class)
    public ReportChangeHistoryResponse renew(final ReportChangeHistoryRequest request) {
        var dto = request.toDto(formatter.getCurrentDateFormat(),
                formatter.getManagerSeq(), formatter.getCurrentDateFormat(),
                formatter.getLastDateFormat());

        boolean existsReport = reportRepository.existsByRnoForUpdate(request.getRno());
        if (!existsReport) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 리포트가 존재하지 않습니다. rno: {}", request.getRno());
            throw new ReportNotFoundException();
        }

        boolean existsChangeHistory = reportChangeHistoryRepository.existsByRnoForUpdate(request.getRno());
        if (!existsChangeHistory) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 리포트의 변경 이력이 존재하지 않습니다. rno: {}", request.getRno());
            throw new ReportChangeHistoryNotFoundException();
        }

        var foundOriginal = reportChangeHistoryRepository.selectLatestByRno(request.getRno());
        foundOriginal.updateApplEnd(formatter.minusDateFormat(1), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = reportChangeHistoryRepository.update(foundOriginal);

        if (rowCnt != 1) {
            log.error("[REPORT_CHANGE_HISTORY] DBMS에 정상 반영되지 않았습니다. rno: {}", request.getRno());
            throw new NotApplyOnDbmsException();
        }

        rowCnt = reportChangeHistoryRepository.insert(dto);
        if (rowCnt != 1) {
            log.error("[REPORT_CHANGE_HISTORY] DBMS에 정상 반영되지 않았습니다. rno: {}", request.getRno());
            throw new NotApplyOnDbmsException();
        }

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
        var dto = request.toDto(formatter.getCurrentDateFormat(),
                formatter.getManagerSeq(), formatter.getCurrentDateFormat(),
                formatter.getLastDateFormat());

        boolean exists = reportChangeHistoryRepository.existsBySeqForUpdate(request.getSeq());
        if (!exists) {
            log.error("[REPORT_CHANGE_HISTORY] 해당 변경 이력이 존재하지 않습니다. seq: {}", request.getSeq());
            throw new ReportChangeHistoryNotFoundException();
        }

        int rowCnt = reportChangeHistoryRepository.update(dto);
        if (rowCnt != 1) {
            log.error("[REPORT_CHANGE_HISTORY] DBMS에 정상 반영되지 않았습니다. seq: {}", request.getSeq());
            throw new NotApplyOnDbmsException();
        }
    }

    public void removeBySeq(final Integer seq) {
        int rowCnt = reportChangeHistoryRepository.delete(seq);
        if (rowCnt != 1) {
            log.error("[REPORT_CHANGE_HISTORY] DBMS에 정상 반영되지 않았습니다. seq: {}", seq);
            throw new NotApplyOnDbmsException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByRno(final Integer rno) {
        int totalCnt = reportChangeHistoryRepository.countByRno(rno);
        int rowCnt = reportChangeHistoryRepository.deleteByRno(rno);

        if (totalCnt != rowCnt) {
            log.error("[REPORT_CHANGE_HISTORY] DBMS에 정상 반영되지 않았습니다. rno: {}", rno);
            throw new NotApplyOnDbmsException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void removeAll() {
        int totalCnt = reportChangeHistoryRepository.count();
        int rowCnt = reportChangeHistoryRepository.deleteAll();

        if (totalCnt != rowCnt) {
            log.error("[REPORT_CHANGE_HISTORY] DBMS에 정상 반영되지 않았습니다.");
            throw new NotApplyOnDbmsException();
        }
    }
}
