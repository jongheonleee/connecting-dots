package com.example.demo.repository.mybatis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.Code;
import com.example.demo.domain.ServiceTerms;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.dto.service.ServiceTermsConditionDto;
import com.example.demo.dto.service.ServiceTermsDto;
import com.example.demo.repository.mybatis.code.CommonCodeDaoImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ServiceTermsDaoImplTest {

    @Autowired
    private ServiceTermsDaoImpl serviceTermsDao;

    @Autowired
    private ServiceRuleUseDaoImpl serviceRuleUseDao;

    @Autowired
    private CommonCodeDaoImpl commonCodeDao;

    private final String rule_stat1 = "1001";
    private final String rule_stat2 = "1002";
    private final String rule_stat3 = "1003";
    private final String code = "100";
    private final Integer page = 1;
    private final Integer pageSize = 10;

    @BeforeEach
    void setUp() {
        assertNotNull(serviceTermsDao);
        assertNotNull(serviceRuleUseDao);
        assertNotNull(commonCodeDao);

        serviceTermsDao.deleteAll();
        serviceRuleUseDao.deleteAll();
        for (int i= Code.MAX_LEVEL; i>=0; i--) {
            commonCodeDao.deleteByLevel(i);
        }

        // 공통 코드 1개 추가
        CodeDto codeDto = createCodeDto();
        assertEquals(1, commonCodeDao.insert(codeDto));

        // 서비스 이용 규칙 테스트용 데이터 3개 추가
        for (int i=1; i<=3; i++) {
            ServiceRuleUseDto serviceRuleUseDto = createServiceRuleUseDto(i, codeDto.getCode());
            assertEquals(1, serviceRuleUseDao.insert(serviceRuleUseDto));
        }

    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createServiceTermsDto("100" + i);
            assertEquals(1, serviceTermsDao.insert(dto));
        }

        int totalCnt = serviceTermsDao.count();
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("서비스 정책 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_이용_규칙_조회_테스트(int cnt) {
        List<ServiceTermsDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createServiceTermsDto("100" + i);
            assertEquals(1, serviceTermsDao.insert(dto));
            dummy.add(dto);
        }

        int randomIdx = (int) (Math.random() * cnt);
        ServiceTermsDto selectedDto = dummy.get(randomIdx);
        ServiceTermsDto response = serviceTermsDao.select(selectedDto.getPoli_stat());

        assertNotNull(response);
        assertEquals(selectedDto.getPoli_stat(), response.getPoli_stat());
        assertEquals(selectedDto.getName(), response.getName());
        assertEquals(selectedDto.getChk_use(), response.getChk_use());
        assertEquals(selectedDto.getCode(), response.getCode());
    }

    @DisplayName("서비스 정책 검색 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_이용_규칙_검색_조회_테스트(int cnt) {
        List<ServiceTermsDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createServiceTermsDto("100" + i);
            assertEquals(1, serviceTermsDao.insert(dto));
            dummy.add(dto);
        }

        SearchCondition sc = createSearchCondition(page, pageSize, "NM", "테스트용", "1");
        List<ServiceTermsDto> responses = serviceTermsDao.selectBySearchCondition(sc);

        int expectedSize = cnt > pageSize ? pageSize : cnt;
        assertEquals(expectedSize, responses.size());
        responses.stream().forEach(response -> {
            assertEquals("테스트용", response.getName());
        });
    }

    @DisplayName("서비스 정책 추가 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_이용_규칙_추가_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createServiceTermsDto("100" + i);
            assertEquals(1, serviceTermsDao.insert(dto));
        }

        int totalCnt = serviceTermsDao.count();
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("서비스 정책 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_이용_규칙_수정_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createServiceTermsDto("100" + i);
            assertEquals(1, serviceTermsDao.insert(dto));
        }

        int randomIdx = (int) (Math.random() * cnt);
        ServiceTermsDto selectedDto = serviceTermsDao.select("100" + randomIdx);

        selectedDto.setName("수정된 테스트용");
        selectedDto.setChk_use("N");
        selectedDto.setRule_stat1("1000");
        selectedDto.setRule_stat2("1001");
        selectedDto.setRule_stat3("1002");
        selectedDto.setOp1("OR");
        selectedDto.setOp2("OR");
        assertEquals(1, serviceTermsDao.update(selectedDto));

        ServiceTermsDto updatedDto = serviceTermsDao.select("100" + randomIdx);
        assertNotNull(updatedDto);
        assertEquals(selectedDto.getName(), updatedDto.getName());
        assertEquals(selectedDto.getChk_use(), updatedDto.getChk_use());
        assertEquals(selectedDto.getRule_stat1(), updatedDto.getRule_stat1());
        assertEquals(selectedDto.getRule_stat2(), updatedDto.getRule_stat2());
        assertEquals(selectedDto.getRule_stat3(), updatedDto.getRule_stat3());
        assertEquals(selectedDto.getOp1(), updatedDto.getOp1());
        assertEquals(selectedDto.getOp2(), updatedDto.getOp2());
    }


    @DisplayName("서비스 정책 코드로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_이용_규칙_코드로_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createServiceTermsDto("100" + i);
            assertEquals(1, serviceTermsDao.insert(dto));
        }

        int randomIdx = (int) (Math.random() * cnt);
        ServiceTermsDto selectedDto = serviceTermsDao.select("100" + randomIdx);
        assertEquals(1, serviceTermsDao.delete(selectedDto.getPoli_stat()));

        ServiceTermsDto deletedDto = serviceTermsDao.select("100" + randomIdx);
        assertNull(deletedDto);
    }

    @DisplayName("서비스 정책 전체 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_이용_규칙_전체_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createServiceTermsDto("100" + i);
            assertEquals(1, serviceTermsDao.insert(dto));
        }

        assertEquals(cnt, serviceTermsDao.count());
        assertEquals(cnt, serviceTermsDao.deleteAll());
        assertEquals(0, serviceTermsDao.count());
    }

    @DisplayName("서비스 정책 조건 생성 테스트")
    @Test
    void 서비스_이용_규칙_조건_생성_테스트() {
        ServiceTermsDto dto = createServiceTermsDto("1001");
        assertEquals(1, serviceTermsDao.insert(dto));

        String expectedCond = "col1 >= 5 AND col2 >= 5 AND col3 >= 5";
        ServiceTermsConditionDto conditionDto = serviceTermsDao.selectForCondition("1001");
        assertNotNull(conditionDto);
        assertEquals(expectedCond, conditionDto.getCond());
    }

    private ServiceTermsDto createServiceTermsDto(String poli_stat) {
        ServiceTermsDto dto = new ServiceTermsDto();
        dto.setPoli_stat(poli_stat);
        dto.setName("테스트용");
        dto.setChk_use("Y");
        dto.setCode(code);
        dto.setRule_stat1(rule_stat1);
        dto.setOp1("AND");
        dto.setRule_stat2(rule_stat2);
        dto.setOp2("AND");
        dto.setRule_stat3(rule_stat3);
        dto.setReg_date("2025-01-01");
        dto.setReg_user_seq(1);
        dto.setUp_user_seq(1);
        dto.setUp_date("2025-01-01");
        return dto;
    }



    private ServiceRuleUseDto createServiceRuleUseDto(int i, String code) {
        ServiceRuleUseDto dto = new ServiceRuleUseDto();
        dto.setRule_stat("100" + i);
        dto.setName("테스트용" + i);
        dto.setTar_name("col" + i);
        dto.setOp1(">=");
        dto.setVal1("5");
        dto.setCode(code);
        dto.setChk_use("Y");
        dto.setReg_date("2025-01-01");
        dto.setReg_user_seq(1);
        dto.setUp_user_seq(1);
        dto.setUp_date("2025-01-01");
        return dto;
    }

    private CodeDto createCodeDto() {
        CodeDto dto = new CodeDto();
        dto.setLevel(1);
        dto.setCode(code);
        dto.setName("테스트용");
        dto.setTop_code("100");
        dto.setChk_use("Y");
        dto.setReg_date("2025-01-01");
        dto.setReg_user_seq(1);
        dto.setUp_user_seq(1);
        dto.setUp_date("2025-01-01");
        return dto;
    }

    private SearchCondition createSearchCondition(int page, int pageSize, String searchOption, String searchKeyword, String sortOption) {
        return new SearchCondition(page, pageSize, searchOption, searchKeyword, sortOption);
    }


}