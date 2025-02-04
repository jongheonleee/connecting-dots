package com.example.demo.service.report.impl;

import com.example.demo.dto.report.ReportProcessDetailsRequest;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportProcessAlreadyExistsException;
import com.example.demo.global.error.exception.business.report.ReportProcessNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.code.CommonCodeRepository;
import com.example.demo.repository.report.ReportProcessDetailsRepository;
import com.example.demo.repository.report.ReportRepository;
import com.example.demo.service.report.ReportProcessDetailsService;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportProcessDetailsServiceImpl implements ReportProcessDetailsService {

    private final ReportProcessDetailsRepository reportProcessDetailsRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final ReportRepository reportRepository;
    private final CustomFormatter formatter;

    @Override
    public ReportProcessDetailsResponse create(final ReportProcessDetailsRequest request) {
        checkReportExists(request.getRno());
        checkCodeExists(request.getPros_code());
        checkDuplicatedReportProcessDetails(request.getRno());
        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getLastDateFormat());
        checkApplied(1, reportProcessDetailsRepository.insert(dto));
        return dto.toResponse();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReportProcessDetailsResponse renew(final ReportProcessDetailsRequest request) {
        checkReportExists(request.getRno());
        checkCodeExists(request.getPros_code());
        checkReportProcessDetailsExists(request.getRno());
        var foundOriginal = reportProcessDetailsRepository.selectLatestByRno(request.getRno());
        foundOriginal.updateApplEnd(formatter.minusDateFormat(1), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        var newOne = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getLastDateFormat());
        checkApplied(2, reportProcessDetailsRepository.update(foundOriginal) + reportProcessDetailsRepository.insert(newOne));
        return newOne.toResponse();
    }

    @Override
    public ReportProcessDetailsResponse readBySeq(final Integer seq) {
        var found = reportProcessDetailsRepository.selectBySeq(seq);
        if (found == null) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트 처리 내역이 존재하지 않습니다. seq: {}", seq);
            throw new ReportProcessNotFoundException();
        }

        return found.toResponse();
    }

    @Override
    public List<ReportProcessDetailsResponse> readByRno(final Integer rno) {
        checkReportExists(rno);
        return reportProcessDetailsRepository.selectByRno(rno)
                                             .stream()
                                             .map(o -> o.toResponse())
                                             .toList();
    }

    @Override
    public List<ReportProcessDetailsResponse> readAll() {
        return reportProcessDetailsRepository.selectAll()
                                             .stream()
                                             .map(o -> o.toResponse())
                                             .toList();
    }

    @Override
    public void modify(final ReportProcessDetailsRequest request) {
        checkReportProcessDetailsExistsForUpdate(request.getSeq());
        var found = reportProcessDetailsRepository.selectBySeq(request.getSeq());
        found.update(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        checkApplied(1, reportProcessDetailsRepository.update(found));
    }

    @Override
    public void removeBySeq(final Integer seq) {
        checkApplied(1, reportProcessDetailsRepository.deleteBySeq(seq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRno(final Integer rno) {
        checkApplied(reportProcessDetailsRepository.countByRno(rno), reportProcessDetailsRepository.deleteByRno(rno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        checkApplied(reportProcessDetailsRepository.count(), reportProcessDetailsRepository.deleteAll());
    }

    private void checkReportExists(final Integer rno) {
        boolean existsReport = reportRepository.existsByRno(rno);

        if (!existsReport) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트가 존재하지 않습니다. rno: {}", rno);
            throw new ReportNotFoundException();
        }
    }

    private void checkCodeExists(final String pros_code) {
        boolean existsCode = commonCodeRepository.existsByCode(pros_code);
        if (!existsCode) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 코드가 존재하지 않습니다. pros_code: {}", pros_code);
            throw new CodeNotFoundException();
        }
    }

    private void checkDuplicatedReportProcessDetails(final Integer rno) {
        boolean exists = reportProcessDetailsRepository.existsByRno(rno);
        if (exists) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트 처리 내역이 이미 존재합니다. rno: {}", rno);
            throw new ReportProcessAlreadyExistsException();
        }
    }

    private void checkApplied(final Integer expected, final Integer actual) {
        if (expected != actual) {
            log.error("[REPORT_PROCESS_DETAILS] 리포트 처리 내역 처리에 실패하였습니다. expected: {}, actual: {}", expected, actual);
            throw new NotApplyOnDbmsException();
        }
    }

    private void checkReportProcessDetailsExists(final Integer rno) {
        boolean exists = reportProcessDetailsRepository.existsByRno(rno);
        if (!exists) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트 처리 내역이 존재하지 않습니다. rno: {}", rno);
            throw new ReportProcessNotFoundException();
        }
    }

    private void checkReportProcessDetailsExistsForUpdate(final Integer seq) {
        boolean exists = reportProcessDetailsRepository.existsBySeqForUpdate(seq);

        if (!exists) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트 처리 내역이 존재하지 않습니다. seq: {}", seq);
            throw new ReportProcessNotFoundException();
        }
    }
}
