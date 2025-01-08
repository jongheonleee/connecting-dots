package com.example.demo.application.code;

import com.example.demo.domain.Code;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.code.CodeResponse;
import com.example.demo.repository.mybatis.code.CommonCodeDaoImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CommonCodeServiceImpl {

    private static final Integer MAX_LEVEL = 3;

    private final CommonCodeDaoImpl commonCodeDao;

    public int count() {
        return commonCodeDao.count();
    }

    public List<CodeResponse> readByTopCode(String top_code) {
        List<CodeDto> codeDtos = commonCodeDao.selectByTopCode(top_code);
        return codeDtos.stream()
                       .map(CodeResponse::new)
                       .toList();
    }

    public CodeResponse readBySeq(Integer seq) {
        CodeDto dto = commonCodeDao.selectBySeq(seq);
        return new CodeResponse(dto);
    }

    public CodeResponse readByCode(String code) {
        CodeDto dto = commonCodeDao.selectByCode(code);
        return new CodeResponse(dto);
    }

    public List<CodeResponse> readAll() {
        List<CodeDto> codeDtos = commonCodeDao.selectAll();
        return codeDtos.stream()
                        .map(CodeResponse::new)
                        .toList();
    }

    public void create(Code code) {
        CodeDto dto = code.toCodeDto();
        int rowCnt = commonCodeDao.insert(dto);
        if (rowCnt != 1) {
            log.error("[CODE] - create() 실패 : {}", dto);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    public void modify(Code code) {
        CodeDto dto = code.toCodeDto();
        int rowCnt = commonCodeDao.update(dto);
        if (rowCnt != 1) {
            log.error("[CODE] - update() 실패 : {}", dto);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    public void modifyUse(Code code) {
        CodeDto dto = code.toCodeDto();

        int rowCnt = commonCodeDao.updateUse(dto);

        if (rowCnt != 1) {
            log.error("[CODE] - updateUse() 실패 : {}", dto);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    public void removeByLevel(Integer level) {
        int rowCnt = commonCodeDao.deleteByLevel(level);

        if (rowCnt != 1) {
            log.error("[CODE] - deleteByLevel() 실패 : {}", level);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    public void removeByCode(String code) {
        int rowCnt = commonCodeDao.deleteByCode(code);

        if (rowCnt != 1) {
            log.error("[CODE] - deleteByCode() 실패 : {}", code);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    public void removeBySeq(Integer seq) {
        int rowCnt = commonCodeDao.delete(seq);

        if (rowCnt != 1) {
            log.error("[CODE] - delete() 실패 : {}", seq);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        for (int level = MAX_LEVEL; level > 0; level--) {
            removeByLevel(level);
        }

        int totalCnt = count();

        if (totalCnt != 0) {
            log.error("[CODE] - removeAll() 실패 : {}", totalCnt);
            throw new RuntimeException(); // 다른 에러 정의해서 던지기
        }
    }

}
