package com.example.demo.service.report.impl;


import com.example.demo.domain.Code;
import com.example.demo.dto.report.ReportCategoryResponse;
import com.example.demo.dto.report.ReportChangeHistoryRequest;
import com.example.demo.dto.report.ReportDetailResponse;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.dto.report.ReportProcessDetailsRequest;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.dto.report.ReportRequest;
import com.example.demo.dto.report.ReportResponse;
import com.example.demo.global.error.exception.business.BusinessException;
import com.example.demo.global.error.exception.business.InvalidValueException;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportAlreadyProcessedException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.global.error.exception.technology.network.RetryFailedException;
import com.example.demo.repository.report.ReportCategoryRepository;
import com.example.demo.repository.report.ReportProcessDetailsRepository;
import com.example.demo.repository.report.ReportRepository;
import com.example.demo.service.report.ReportCategoryService;
import com.example.demo.service.report.ReportChangeHistoryService;
import com.example.demo.service.report.ReportProcessDetailsService;
import com.example.demo.service.report.ReportService;
import com.example.demo.utils.CustomFormatter;
import java.sql.SQLSyntaxErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    /**
     * 추가 구현 목록
     *
     * - 1. removeAllForRemoveUser : 회원 탈퇴 시 해당 회원의 모든 리포트 삭제
     * - 2. readMainReportOnAdmin : 관리자가 메인 페이지에서 보는 리포트 리스트
     * - 3. readMainReportForUser : 회원이 메인 페이지에서 보는 리포트 리스트
     *
     */

    private static final int MAX_RETRY = 10;
    private static final int RETRY_DELAY = 5_000;


    //    private final UserService userService; - 추후에 추가해야 할 서비스 오브젝트
    private final ReportRepository reportRepository;
    private final ReportCategoryRepository reportCategoryRepository;
    private final ReportCategoryService reportCategoryService;
    private final ReportChangeHistoryService reportChangeHistoryService;
    private final ReportProcessDetailsService reportProcessDetailsService;
    private final ReportProcessDetailsRepository reportProcessDetailsRepository;
    private final CustomFormatter formatter;


    @Override
    @Retryable(
            value = { // exclude 외의 런타임 예외는 재시도 복구 처리
                    RuntimeException.class
            },
            exclude = { // 작성된 예외는 재시도 처리를 해도 의미가 없는 예외이므로 예외 처리에서 제외
                    BusinessException.class, InvalidValueException.class, DataIntegrityViolationException.class,
                    SQLSyntaxErrorException.class, InvalidDataAccessApiUsageException.class, InvalidDataAccessResourceUsageException.class,
                    EmptyResultDataAccessException.class, TypeMismatchDataAccessException.class
            },
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY),
            recover = "recover"
    )
    @Transactional(rollbackFor = Exception.class)
    public ReportResponse create(final ReportRequest request) {
        checkReportCategoryExists(request.getCate_code());
        var dto = request.toDto(formatter.getCurrentDateFormat(),formatter.getLastDateFormat(), formatter.getManagerSeq());
        checkApplied(1, reportRepository.insert(dto));
        createReportChangeHistory(dto);
        createInitReportProcessDetails(dto);
        return dto.toResponse();
    }

    @Override
    @Recover
    public ReportResponse recover(final RuntimeException e) {
        log.error("[REPORT] 리포트 예외 복구를 위해 재시도를 했지만 실패했습니다. 최대 재시도 횟수 : {}, 재시도 간격 : {}ms", MAX_RETRY, RETRY_DELAY);
        log.error("[REPORT] 예외 내용 : {}", e.getMessage());
        throw new RetryFailedException();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDetailResponse readReportDetailsByRno(final Integer rno) {
        var foundReport = reportRepository.selectByRno(rno);
        if (foundReport == null) {
            log.error("[REPORT] 해당 리포트를 찾을 수 없습니다. rno: {}", rno);
            throw new ReportNotFoundException();
        }

        ReportProcessDetailsResponse foundCurrentReportProcessDetails = reportProcessDetailsService.readByRnoAtPresent(rno);
        ReportCategoryResponse foundReportCategory = reportCategoryService.readByCateCode(foundReport.getCate_code());
        return ReportDetailResponse.of(foundReport, foundReportCategory, foundCurrentReportProcessDetails);
    }

    @Override
    @Transactional(
            rollbackFor = Exception.class,
            propagation = Propagation.REQUIRED
    )
    public void modify(final ReportRequest request) {
        checkReportExistsForUpdate(request.getRno());
        if (!reportProcessDetailsService.canChangeReport(request.getRno())) {
            log.error("[REPORT] 리포트 처리가 진행되었기 때문에 수정을 할 수 없습니다. rno: {}", request.getRno());
            throw new ReportAlreadyProcessedException();
        }

        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getLastDateFormat(), formatter.getManagerSeq());
        checkApplied(1, reportRepository.update(dto));
        updateOldChangeHistoryAndCreateNewChangeHistory(request);
    }



    @Override
    @Transactional(
            rollbackFor = Exception.class,
            propagation = Propagation.REQUIRED
    )
    public void removeByRno(final Integer rno) {
        var found = reportRepository.selectByRno(rno);
        // 신고자의 시퀀스와 현재 회원의 시퀀스 비교 -> true/false; 추후에 개발할 예정

        if (!reportProcessDetailsService.canChangeReport(rno)) {
            log.error("[REPORT] 리포트 삭제 권한이 없습니다. rno: {}", rno);
            throw new ReportAlreadyProcessedException();
        }


        reportChangeHistoryService.removeByRno(rno);
        reportProcessDetailsService.removeByRno(rno);
        checkApplied(1, reportRepository.delete(rno));
    }

    // 추후에 개발할 내용 [ ]
    @Override
    public void removeAllForUserLeave(final Integer userSeq) {

    }

    @Override
    @Transactional(
            rollbackFor = Exception.class,
            propagation = Propagation.REQUIRED
    )
    public void removeAll() {
        // 이를 호출한 사용자가 관리자 인지 확인 -> true/false; 추후에 개발할 예정
        reportChangeHistoryService.removeAll();
        reportProcessDetailsService.removeAll();
        checkApplied(reportRepository.count(), reportRepository.deleteAll());
    }


    private void createInitReportProcessDetails(final ReportDto dto) {
        ReportProcessDetailsRequest processDetailsRequest = ReportProcessDetailsRequest.builder()
                                                                                        .rno(dto.getRno())
                                                                                        .pros_code(Code.REPORT_CREATE.getCode())
                                                                                        .build();
        reportProcessDetailsService.create(processDetailsRequest);
    }

    private void createReportChangeHistory(final ReportDto dto) {
        var historyRequest = ReportChangeHistoryRequest.builder()
                                                    .rno(dto.getRno())
                                                    .title(dto.getTitle())
                                                    .cont(dto.getCont())
                                                    .build();
        reportChangeHistoryService.create(historyRequest);
    }

    private void checkReportCategoryExists(final String cateCode) {
        boolean existsReportCategory = reportCategoryRepository.existsByCateCode(cateCode);
        if (!existsReportCategory) {
            log.error("[REPORT] 해당 카테고리 코드가 존재하지 않습니다. cate_code: {}", cateCode);
            throw new CodeNotFoundException();
        }
    }

    private void checkApplied(final Integer expected, final Integer actual) {
        if (expected != actual) {
            log.error("[REPORT] 리포트 처리 과정에서 RDBMS가 정상적으로 반영되지 못했습니다. rno: {}", expected);
            throw new NotApplyOnDbmsException();
        }
    }

    private void updateOldChangeHistoryAndCreateNewChangeHistory(ReportRequest request) {
        ReportChangeHistoryRequest reportChangeHistoryRequest = ReportChangeHistoryRequest.builder()
                                                                                        .rno(request.getRno())
                                                                                        .title(request.getTitle())
                                                                                        .cont(request.getCont())
                                                                                        .build();
        reportChangeHistoryService.renew(reportChangeHistoryRequest);
    }

    private void checkReportExistsForUpdate(final Integer rno) {
        boolean exists = reportRepository.existsByRnoForUpdate(rno);
        if (!exists) {
            log.error("[REPORT] 해당 리포트를 찾을 수 없습니다. rno: {}", rno);
            throw new ReportNotFoundException();
        }
    }

}
