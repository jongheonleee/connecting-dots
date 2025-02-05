package com.example.demo.service.report.impl;


import com.example.demo.dto.report.ReportChangeHistoryRequest;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.dto.report.ReportProcessDetailsRequest;
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
import com.example.demo.repository.report.ReportRepository;
import com.example.demo.service.report.ReportCategoryService;
import com.example.demo.service.report.ReportChangeHistoryService;
import com.example.demo.service.report.ReportProcessDetailsService;
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
public class ReportServiceImpl {

    private static final int MAX_RETRY = 10;
    private static final int RETRY_DELAY = 5_000;


    //    private final UserService userService; - 추후에 추가해야 할 서비스 오브젝트
    private final ReportRepository reportRepository;
    private final ReportCategoryRepository reportCategoryRepository;
    private final ReportCategoryService reportCategoryService;
    private final ReportChangeHistoryService reportChangeHistoryService;
    private final ReportProcessDetailsService reportProcessDetailsService;
    private final CustomFormatter formatter;


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
        boolean existsCode = reportCategoryRepository.existsByCateCode(request.getCate_code());
        if (!existsCode) {
            log.error("[REPORT] 해당 카테고리 코드가 존재하지 않습니다. cate_code: {}", request.getCate_code());
            throw new CodeNotFoundException();
        }

        var dto = request.toDto(formatter.getCurrentDateFormat(),formatter.getLastDateFormat(), formatter.getManagerSeq());
        int rowCnt = reportRepository.insert(dto);

        if (rowCnt != 1) {
            log.error("[REPORT] 리포트 생성에 실패하였습니다. rowCnt: {}", rowCnt);
            throw new NotApplyOnDbmsException();
        }

        var historyRequest = ReportChangeHistoryRequest.builder()
                                                       .rno(dto.getRno())
                                                       .title(dto.getTitle())
                                                       .cont(dto.getCont())
                                                       .build();
        reportChangeHistoryService.create(historyRequest);

        ReportProcessDetailsRequest processDetailsRequest = ReportProcessDetailsRequest.builder()
                                                                                      .rno(dto.getRno())
                                                                                      .pros_code(dto.getCate_code())
                                                                                      .build();
        reportProcessDetailsService.create(processDetailsRequest);


        return dto.toResponse();
    }

    @Recover
    public ReportResponse recover(final RuntimeException e) {
        log.error("[REPORT] 리포트 예외 복구를 위해 재시도를 했지만 실패했습니다. 최대 재시도 횟수 : {}, 재시도 간격 : {}ms", MAX_RETRY, RETRY_DELAY);
        log.error("[REPORT] 예외 내용 : {}", e.getMessage());
        throw new RetryFailedException();
    }

    @Transactional(
            rollbackFor = Exception.class,
            propagation = Propagation.REQUIRED
    )
    public void modify(final ReportRequest request) {
        boolean exists = reportRepository.existsByRnoForUpdate(request.getRno());
        if (!exists) {
            log.error("[REPORT] 해당 리포트를 찾을 수 없습니다. rno: {}", request.getRno());
            throw new ReportNotFoundException();
        }


        if (!reportProcessDetailsService.canChangeReport(request.getRno())) {
            log.error("[REPORT] 리포트 수정 권한이 없습니다. rno: {}", request.getRno());
            throw new ReportAlreadyProcessedException();
        }

        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getLastDateFormat(), formatter.getManagerSeq());
        int rowCnt = reportRepository.update(dto);

        if (rowCnt != 1) {
            log.error("[REPORT] 리포트 수정에 실패하였습니다. rowCnt: {}", rowCnt);
            throw new NotApplyOnDbmsException();
        }

        ReportChangeHistoryRequest reportChangeHistoryRequest = ReportChangeHistoryRequest.builder()
                                                                                          .rno(request.getRno())
                                                                                          .title(request.getTitle())
                                                                                          .cont(request.getCont())
                                                                                          .build();
        reportChangeHistoryService.renew(reportChangeHistoryRequest);

    }


    @Transactional(
            rollbackFor = Exception.class,
            propagation = Propagation.REQUIRED
    )
    public void removeBySeq(final Integer rno) {
        var found = reportRepository.selectByRno(rno);
        // 신고자의 시퀀스와 현재 회원의 시퀀스 비교 -> true/false; 추후에 개발할 예정

        if (!reportProcessDetailsService.canChangeReport(rno)) {
            log.error("[REPORT] 리포트 삭제 권한이 없습니다. rno: {}", rno);
            throw new ReportAlreadyProcessedException();
        }


        reportChangeHistoryService.removeBySeq(rno);
        reportProcessDetailsService.removeByRno(rno);

        int rowCnt = reportRepository.delete(rno);

        if (rowCnt != 1) {
            log.error("[REPORT] 리포트 삭제에 실패하였습니다. rowCnt: {}", rowCnt);
            throw new NotApplyOnDbmsException();
        }
    }

    // 추후에 개발할 내용 [ ]
    public void removeAllForUserLeave(final Integer userSeq) {

    }

    @Transactional(
            rollbackFor = Exception.class,
            propagation = Propagation.REQUIRED
    )
    public void removeAll() {
        // 이를 호출한 사용자가 관리자 인지 확인 -> true/false; 추후에 개발할 예정
        reportChangeHistoryService.removeAll();
        reportProcessDetailsService.removeAll();
        int totalCnt = reportRepository.count();
        int rowCnt = reportRepository.deleteAll();

        if (totalCnt != rowCnt) {
            log.error("[REPORT] 리포트 전체 삭제에 실패하였습니다. totalCnt: {}, rowCnt: {}", totalCnt, rowCnt);
            throw new NotApplyOnDbmsException();
        }
    }

}
