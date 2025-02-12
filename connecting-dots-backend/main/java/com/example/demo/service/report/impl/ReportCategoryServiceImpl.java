package com.example.demo.service.report.impl;

import com.example.demo.dto.report.ReportCategoryDto;
import com.example.demo.dto.report.ReportCategoryRequest;
import com.example.demo.dto.report.ReportCategoryResponse;
import com.example.demo.global.error.exception.business.report.ReportCategoryAlreadyExistsException;
import com.example.demo.global.error.exception.business.report.ReportCategoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.report.ReportCategoryRepository;
import com.example.demo.service.report.ReportCategoryService;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportCategoryServiceImpl implements ReportCategoryService {

    private final ReportCategoryRepository reportCategoryRepository;
    private final CustomFormatter formatter;

    @Override
    public ReportCategoryResponse create(final ReportCategoryRequest request) {
        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        checkDuplicated(dto.getCate_code());
        checkApplied(1, reportCategoryRepository.insert(dto));
        return dto.toResponse();
    }


    @Override
    public ReportCategoryResponse readByCateCode(final String cate_code) {
        var found = reportCategoryRepository.selectByCateCode(cate_code);
        if (found == null) {
            log.error("[REPORT_CATEGORY] 조회된 카테고리가 존재하지 않습니다. cate_code: {}", cate_code);
            throw new ReportCategoryNotFoundException();
        }

        return found.toResponse();
    }

    @Override
    public List<ReportCategoryResponse> readByTopCate(final String top_cate) {
        return reportCategoryRepository.selectByTopCate(top_cate)
                                       .stream()
                                       .map(ReportCategoryDto::toResponse)
                                       .toList();
    }


    @Override
    public List<ReportCategoryResponse> readAll() {
        return reportCategoryRepository.selectAll()
                                       .stream()
                                       .map(ReportCategoryDto::toResponse)
                                       .toList();
    }

    @Override
    public void modify(final ReportCategoryRequest request) {
        var dto = request.toDto(formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        checkExistsForUpdate(dto.getCate_code());
        checkApplied(1, reportCategoryRepository.update(dto));
    }

    @Override
    public void modifyChkUseY(final String cate_code) {
        checkExistsForUpdate(cate_code);
        checkApplied(1, reportCategoryRepository.updateChkUseY(cate_code));
    }

    @Override
    public void modifyChkUseN(final String cate_code) {
        checkExistsForUpdate(cate_code);
        checkApplied(1, reportCategoryRepository.updateChkUseN(cate_code));
    }

    @Override
    public void remove(final String cate_code) {
        checkApplied(1, reportCategoryRepository.deleteByCateCode(cate_code));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        checkApplied(reportCategoryRepository.count(), removeLevelByLevel());
    }

    private int removeLevelByLevel() {
        int rowCnt = 0;

        for (int i=3; i>0; i--) {
            rowCnt += reportCategoryRepository.deleteByLevel(i);
        }
        return rowCnt;
    }

    private void checkApplied(Integer expected, Integer actual) {
        if (expected != actual) {
            log.error("[REPORT_CATEGORY] 카테고리 작업 과정에서 DBMS에 정상 반영이 안되었습니다.");
            throw new NotApplyOnDbmsException();
        }
    }

    private void checkDuplicated(String cate_code) {
        boolean exists = reportCategoryRepository.existsByCateCode(cate_code);
        if (exists) {
            log.error("[REPORT_CATEGORY] 중복된 코드번호가 존재합니다. cate_code: {}", cate_code);
            throw new ReportCategoryAlreadyExistsException();
        }
    }

    private void checkExistsForUpdate(String cate_code) {
        boolean exists = reportCategoryRepository.existsByCateCodeForUpdate(cate_code);
        if (!exists) {
            log.error("[REPORT_CATEGORY] 수정하려는 카테고리가 존재하지 않습니다. cate_code: {}", cate_code);
            throw new ReportCategoryNotFoundException();
        }
    }



}
