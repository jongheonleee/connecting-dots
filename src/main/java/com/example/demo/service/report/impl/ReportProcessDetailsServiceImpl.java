package com.example.demo.service.report.impl;

import com.example.demo.dto.report.ReportProcessDetailsDto;
import com.example.demo.dto.report.ReportProcessDetailsRequest;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportProcessAlreadyExistsException;
import com.example.demo.global.error.exception.business.report.ReportProcessNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.code.CommonCodeRepository;
import com.example.demo.repository.code.impl.CommonCodeDaoImpl;
import com.example.demo.repository.report.ReportProcessDetailsRepository;
import com.example.demo.repository.report.ReportRepository;
import com.example.demo.service.code.CommonCodeService;
import com.example.demo.service.code.impl.CommonCodeServiceImpl;
import com.example.demo.utils.CustomFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportProcessDetailsServiceImpl {

    private final ReportProcessDetailsRepository reportProcessDetailsRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final ReportRepository reportRepository;
    private final CustomFormatter formatter;

    public ReportProcessDetailsResponse create(final ReportProcessDetailsRequest request) {
        boolean existsReport = reportRepository.existsByRno(request.getRno());

        if (!existsReport) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트가 존재하지 않습니다. rno: {}", request.getRno());
            throw new ReportNotFoundException();
        }

        boolean existsCode = commonCodeRepository.existsByCode(request.getPros_code());
        if (!existsCode) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 코드가 존재하지 않습니다. pros_code: {}", request.getPros_code());
            throw new CodeNotFoundException();
        }

        boolean exists = reportProcessDetailsRepository.existsByRno(request.getRno());
        if (exists) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트 처리 내역이 이미 존재합니다. rno: {}", request.getRno());
            throw new ReportProcessAlreadyExistsException(); //
        }

        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getLastDateFormat());
        int rowCnt = reportProcessDetailsRepository.insert(dto);

        if (rowCnt != 1) {
            log.error("[REPORT_PROCESS_DETAILS] 리포트 처리 내역 등록에 실패하였습니다. rno: {}, pros_code: {}", request.getRno(), request.getPros_code());
            throw new NotApplyOnDbmsException();
        }


        return dto.toResponse();
    }



    public ReportProcessDetailsResponse renew(final ReportProcessDetailsRequest request) {
        boolean existsReport = reportRepository.existsByRno(request.getRno());

        if (!existsReport) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트가 존재하지 않습니다. rno: {}", request.getRno());
            throw new ReportNotFoundException();
        }

        boolean existsCode = commonCodeRepository.existsByCode(request.getPros_code());
        if (!existsCode) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 코드가 존재하지 않습니다. pros_code: {}", request.getPros_code());
            throw new CodeNotFoundException();
        }

        boolean exists = reportProcessDetailsRepository.existsByRno(request.getRno());
        if (!exists) {
            log.error("[REPORT_PROCESS_DETAILS] 해당 리포트 처리 내역이 존재하지 않습니다. rno: {}", request.getRno());
            throw new ReportProcessNotFoundException();
        }

        var foundOriginal = reportProcessDetailsRepository.selectLatestByRno(request.getRno());
        foundOriginal.updateApplEnd(formatter.minusDateFormat(1), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = reportProcessDetailsRepository.update(foundOriginal);

        if (rowCnt != 1) {
            log.error("[REPORT_PROCESS_DETAILS] 리포트 처리 내역 갱신에 실패하였습니다. rno: {}, pros_code: {}", request.getRno(), request.getPros_code());
            throw new NotApplyOnDbmsException();
        }

        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getLastDateFormat());
        rowCnt = reportProcessDetailsRepository.insert(dto);

        if (rowCnt != 1) {
            log.error("[REPORT_PROCESS_DETAILS] 리포트 처리 내역 등록에 실패하였습니다. rno: {}, pros_code: {}", request.getRno(), request.getPros_code());
            throw new NotApplyOnDbmsException();
        }

        return dto.toResponse();
    }
}
