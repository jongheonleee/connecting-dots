package com.example.demo.repository.mybatis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.Code;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.dto.service.ServiceRuleUseRequest;
import com.example.demo.repository.mybatis.code.CommonCodeDaoImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ServiceRuleUseDaoImplTest {

    @Autowired
    private ServiceRuleUseDaoImpl serviceRuleUseDao;

    @Autowired
    private CommonCodeDaoImpl commonCodeDao;

    @BeforeEach
    void setUp() {
        assertNotNull(serviceRuleUseDao);
        assertNotNull(commonCodeDao);

        for (int level = Code.MAX_LEVEL; level > 0; level--) {
            commonCodeDao.deleteByLevel(level);
        }
        serviceRuleUseDao.deleteAll();

        Code code1 = Code.of("0000");
        CodeDto dto1 = new CodeDto(code1, "Y", "2021-01-01", 1, "2021-01-01", 1);
        assertEquals(1, commonCodeDao.insert(dto1));

        Code code2 = Code.of("2000");
        CodeDto dto2 = new CodeDto(code2, "Y", "2021-01-01", 1, "2021-01-01", 1);
        assertEquals(1, commonCodeDao.insert(dto2));

        Code code3 = Code.of("2002");
        CodeDto dto3 = new CodeDto(code3, "Y", "2021-01-01", 1, "2021-01-01", 1);
        assertEquals(1, commonCodeDao.insert(dto3));
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            assertEquals(1, serviceRuleUseDao.insert(dto));
        }

        int totalCnt = serviceRuleUseDao.count();
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_조회_테스트(int cnt) {
        String code = "2002";
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            dto.setCode(code);
            assertEquals(1, serviceRuleUseDao.insert(dto));
        }

        int totalCnt = serviceRuleUseDao.countByCode(code);
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            assertEquals(1, serviceRuleUseDao.insert(dto));

            ServiceRuleUseDto actualDto = serviceRuleUseDao.selectByRuleStat(request.getRule_stat());
            assertEquals(dto.getRule_stat(), actualDto.getRule_stat());
            assertEquals(dto.getName(), actualDto.getName());
            assertEquals(dto.getTar_name(), actualDto.getTar_name());
            assertEquals(dto.getOp1(), actualDto.getOp1());
            assertEquals(dto.getVal1(), actualDto.getVal1());
            assertEquals(dto.getChk_use(), actualDto.getChk_use());
        }
    }

    @DisplayName("룰 규칙 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 룰_규칙_코드로_조회_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            assertEquals(1, serviceRuleUseDao.insert(dto));

            ServiceRuleUseDto actualDto = serviceRuleUseDao.selectByRuleStat(request.getRule_stat());
            assertEquals(dto.getRule_stat(), actualDto.getRule_stat());
            assertEquals(dto.getName(), actualDto.getName());
            assertEquals(dto.getTar_name(), actualDto.getTar_name());
            assertEquals(dto.getOp1(), actualDto.getOp1());
            assertEquals(dto.getVal1(), actualDto.getVal1());
            assertEquals(dto.getChk_use(), actualDto.getChk_use());
        }
    }

    @DisplayName("전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_조회_테스트(int cnt) {
        List<ServiceRuleUseDto> dummy = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            dummy.add(dto);

            assertEquals(1, serviceRuleUseDao.insert(dto));
        }

        int totalCnt = serviceRuleUseDao.count();
        assertEquals(cnt, totalCnt);

        List<ServiceRuleUseDto> actualDtos = serviceRuleUseDao.selectAll();
        dummy.sort((a, b) -> a.getRule_stat().compareTo(b.getRule_stat()));
        actualDtos.sort((a, b) -> a.getRule_stat().compareTo(b.getRule_stat()));

        for (int i=0; i<cnt; i++) {
            assertEquals(dummy.get(i).getRule_stat(), actualDtos.get(i).getRule_stat());
            assertEquals(dummy.get(i).getName(), actualDtos.get(i).getName());
            assertEquals(dummy.get(i).getTar_name(), actualDtos.get(i).getTar_name());
            assertEquals(dummy.get(i).getOp1(), actualDtos.get(i).getOp1());
            assertEquals(dummy.get(i).getVal1(), actualDtos.get(i).getVal1());
            assertEquals(dummy.get(i).getChk_use(), actualDtos.get(i).getChk_use());
        }
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            assertEquals(1, serviceRuleUseDao.insert(dto));

            ServiceRuleUseDto actualDto = serviceRuleUseDao.selectByRuleStat(request.getRule_stat());
            assertEquals(dto.getRule_stat(), actualDto.getRule_stat());
            assertEquals(dto.getName(), actualDto.getName());
            assertEquals(dto.getTar_name(), actualDto.getTar_name());
            assertEquals(dto.getOp1(), actualDto.getOp1());
            assertEquals(dto.getVal1(), actualDto.getVal1());
            assertEquals(dto.getChk_use(), actualDto.getChk_use());

            ServiceRuleUseDto updateDto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            updateDto.setRule_stat(request.getRule_stat());
            updateDto.setName("수정된 테스트용");
            updateDto.setTar_name("mod_cnt");
            updateDto.setOp1("<=");
            updateDto.setVal1("10");
            updateDto.setChk_use("N");

            assertEquals(1, serviceRuleUseDao.update(updateDto));

            ServiceRuleUseDto updatedDto = serviceRuleUseDao.selectByRuleStat(request.getRule_stat());
            assertEquals(updateDto.getRule_stat(), updatedDto.getRule_stat());
            assertEquals(updateDto.getName(), updatedDto.getName());
            assertEquals(updateDto.getTar_name(), updatedDto.getTar_name());
            assertEquals(updateDto.getOp1(), updatedDto.getOp1());
            assertEquals(updateDto.getVal1(), updatedDto.getVal1());
            assertEquals(updateDto.getChk_use(), updatedDto.getChk_use());
        }
    }

    @DisplayName("사용여부 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 사용여부_수정_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            assertEquals(1, serviceRuleUseDao.insert(dto));

            ServiceRuleUseDto actualDto = serviceRuleUseDao.selectByRuleStat(request.getRule_stat());
            assertEquals(dto.getRule_stat(), actualDto.getRule_stat());
            assertEquals(dto.getName(), actualDto.getName());
            assertEquals(dto.getTar_name(), actualDto.getTar_name());
            assertEquals(dto.getOp1(), actualDto.getOp1());
            assertEquals(dto.getVal1(), actualDto.getVal1());
            assertEquals(dto.getChk_use(), actualDto.getChk_use());

            ServiceRuleUseDto updateDto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            updateDto.setRule_stat(request.getRule_stat());
            updateDto.setChk_use("N");

            assertEquals(1, serviceRuleUseDao.updateUse(updateDto));

            ServiceRuleUseDto updatedDto = serviceRuleUseDao.selectByRuleStat(request.getRule_stat());
            assertEquals(updateDto.getRule_stat(), updatedDto.getRule_stat());
            assertEquals(updateDto.getChk_use(), updatedDto.getChk_use());
        }
    }

    @DisplayName("룰 규칙 코드로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 삭제_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            assertEquals(1, serviceRuleUseDao.insert(dto));
        }

        int totalCnt = serviceRuleUseDao.count();
        assertEquals(cnt, totalCnt);

        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            assertEquals(1, serviceRuleUseDao.deleteByRuleStat(request.getRule_stat()));
        }

        totalCnt = serviceRuleUseDao.count();
        assertEquals(0, totalCnt);
    }

    @DisplayName("코드로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_삭제_테스트(int cnt) {
        String code = "2002";
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            dto.setCode(code);
            assertEquals(1, serviceRuleUseDao.insert(dto));
        }

        int totalCnt = serviceRuleUseDao.count();
        assertEquals(cnt, totalCnt);

        assertEquals(cnt, serviceRuleUseDao.deleteByCode(code));

        totalCnt = serviceRuleUseDao.count();
        assertEquals(0, totalCnt);
    }

    @DisplayName("전체 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_삭제_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            assertEquals(1, serviceRuleUseDao.insert(dto));
        }

        int totalCnt = serviceRuleUseDao.count();
        assertEquals(cnt, totalCnt);

        assertEquals(cnt, serviceRuleUseDao.deleteAll());

        totalCnt = serviceRuleUseDao.count();
        assertEquals(0, totalCnt);
    }

    @DisplayName("페이징 처리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {10, 50, 100, 150, 200})
    void 페이징_처리_테스트(int cnt) {
        SearchCondition sc = createSearchCondition(1, 10, "NM", "테스트용", "1");

        for (int i=0; i<cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, "2025-01-09", 1, "2025-01-09", 1);
            assertEquals(1, serviceRuleUseDao.insert(dto));
        }

        int totalCnt = serviceRuleUseDao.countBySearchCondition(sc);
        assertEquals(cnt, totalCnt);
        List<ServiceRuleUseDto> codeDtos = serviceRuleUseDao.selectBySearchCondition(sc);
        assertEquals(10, codeDtos.size());
    }



    private ServiceRuleUseRequest createRequest(int i) {
        ServiceRuleUseRequest request = new ServiceRuleUseRequest();
        request.setRule_stat("SA000" + i);
        request.setName("테스트용");
        request.setTar_name("orig_cnt");
        request.setOp1(">=");
        request.setVal1("1");
        request.setChk_use("Y");
        request.setCode("2002");
        return request;
    }

    private SearchCondition createSearchCondition(int page, int pageSize, String searchOption, String searchKeyword, String sortOption) {
        return new SearchCondition(page, pageSize, searchOption, searchKeyword, sortOption);
    }
}