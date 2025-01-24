package com.example.demo.application.code.impl;

import com.example.demo.application.code.CommonCodeService;
import com.example.demo.domain.Code;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.code.CodeRequest;
import com.example.demo.dto.code.CodeResponse;
import com.example.demo.repository.mybatis.code.CommonCodeDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@Transactional
@AllArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {

    private final CommonCodeDaoImpl commonCodeDao;
    private final CustomFormatter formatter;

    @Override
    public int count() {
        return commonCodeDao.count();
    }

    @Override
    public List<CodeResponse> readByTopCode(String top_code) {
        List<CodeDto> codeDtos = commonCodeDao.selectByTopCode(top_code);
        return codeDtos.stream()
                       .map(CodeResponse::new)
                       .toList();
    }

    @Override
    public CodeResponse readBySeq(Integer seq) {
        CodeDto dto = commonCodeDao.selectBySeq(seq);
        return new CodeResponse(dto);
    }

    @Override
    public CodeResponse readByCode(String code) {
        CodeDto dto = commonCodeDao.selectByCode(code);
        return new CodeResponse(dto);
    }

    @Override
    public PageResponse readBySearchCondition(SearchCondition sc) {
        int totalCnt = commonCodeDao.countBySearchCondition(sc);
        List<CodeDto> codeDtos = commonCodeDao.selectBySearchCondition(sc);
        List<CodeResponse> responses = codeDtos.stream()
                                                .map(CodeResponse::new)
                                                .toList();
        return new PageResponse<CodeResponse>(totalCnt, sc, responses);
    }

    @Override
    public List<CodeResponse> readAll() {
        List<CodeDto> codeDtos = commonCodeDao.selectAll();
        return codeDtos.stream()
                        .map(CodeResponse::new)
                        .toList();
    }

    @Override
    public CodeResponse create(CodeRequest request) {
        CodeDto dto = new CodeDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = commonCodeDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[CODE] - create() 실패 : {}", dto);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }

        return commonCodeDao.selectBySeq(dto.getSeq())
                            .toResponse();
    }

    @Override
    public void modify(CodeRequest request) {
        CodeDto dto = new CodeDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = commonCodeDao.update(dto);

        if (rowCnt != 1) {
            log.error("[CODE] - update() 실패 : {}", dto);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    @Override
    public void modifyUse(CodeRequest request) {
        CodeDto dto = new CodeDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = commonCodeDao.updateUse(dto);

        if (rowCnt != 1) {
            log.error("[CODE] - updateUse() 실패 : {}", dto);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    @Override
    public void removeByLevel(Integer level) {
        int rowCnt = commonCodeDao.deleteByLevel(level);

        if (rowCnt != 1) {
            log.error("[CODE] - deleteByLevel() 실패 : {}", level);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    @Override
    public void removeByCode(String code) {
        int rowCnt = commonCodeDao.deleteByCode(code);

        if (rowCnt != 1) {
            log.error("[CODE] - deleteByCode() 실패 : {}", code);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    @Override
    public void removeBySeq(Integer seq) {
        int rowCnt = commonCodeDao.delete(seq);

        if (rowCnt != 1) {
            log.error("[CODE] - delete() 실패 : {}", seq);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        for (int level = Code.MAX_LEVEL; level > 0; level--) {
            removeByLevel(level);
        }
        int totalCnt = count();

        if (totalCnt != 0) {
            log.error("[CODE] - removeAll() 실패 : {}", totalCnt);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

}
