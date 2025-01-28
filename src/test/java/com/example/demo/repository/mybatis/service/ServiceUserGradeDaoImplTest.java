package com.example.demo.repository.mybatis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.Code;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.dto.service.ServiceTermsDto;
import com.example.demo.dto.service.ServiceUserGradeDto;
import com.example.demo.repository.code.CommonCodeRepository;
import com.example.demo.repository.service.ServiceRuleUseRepository;
import com.example.demo.repository.service.ServiceTermsRepository;
import com.example.demo.repository.service.ServiceUserGradeRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ServiceUserGradeDaoImplTest {

    @Autowired
    private ServiceUserGradeRepository serviceUserGradeDao;

    // 테스트 환경 구축에 필요한 데이터들
    // 1. 정책 데이터 - 회원 등급 정책
    // 2. 코드 데이터 - 정책 데이터의 카데고리 분류를 위함
    // 3. 위의 정책을 구성하는 이용 규칙 데이터 - 회원 등급 이용 규칙

    @Autowired
    private ServiceRuleUseRepository serviceRuleUseDao;

    @Autowired
    private ServiceTermsRepository serviceTermsDao;

    @Autowired
    private CommonCodeRepository codeDao;

    private final String poli_stat = "PS100";
    private final String rule_stat1 = "1001";
    private final String rule_stat2 = "1002";
    private final String rule_stat3 = "1003";
    private final String code = "100";

    private final String currentDateFormat = "2025-01-08 00:00:00";
    private final Integer managerSeq = 1;
    private final String currentUpdateDateFormat = "2025-01-12 00:00:00";
    private final Integer updateManagerSeq = 1;

    private final Integer page = 1;
    private final Integer pageSize = 10;

    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(serviceUserGradeDao);
        assertNotNull(serviceRuleUseDao);
        assertNotNull(serviceTermsDao);
        assertNotNull(codeDao);

        // 테스트용 데이터 삭제 및 DB 초기화
        serviceUserGradeDao.deleteAll();
        serviceTermsDao.deleteAll();
        serviceRuleUseDao.deleteAll();

        for (int i = Code.MAX_LEVEL; i>=0; i--) {
            codeDao.deleteByLevel(i);
        }

        // 1. 위의 정책을 구성하는 이용 규칙 데이터 - 회원 등급 이용 규칙
        ServiceRuleUseDto ruleUseDto1 = createServiceRuleUseDto(1, rule_stat1);
        ServiceRuleUseDto ruleUseDto2 = createServiceRuleUseDto(2, rule_stat2);
        ServiceRuleUseDto ruleUseDto3 = createServiceRuleUseDto(3, rule_stat3);

        assertEquals(1, serviceRuleUseDao.insert(ruleUseDto1));
        assertEquals(1, serviceRuleUseDao.insert(ruleUseDto2));
        assertEquals(1, serviceRuleUseDao.insert(ruleUseDto3));


        // 2. 코드 데이터 - 정책 데이터의 카데고리 분류를 위함
        CodeDto codeDto = createCodeDto();
        assertEquals(1, codeDao.insert(codeDto));

        // 3. 정책 데이터 - 회원 등급 정책
        ServiceTermsDto serviceTermsDto = createServiceTermsDto(code);
        assertEquals(1, serviceTermsDao.insert(serviceTermsDto));
    }

    @AfterEach
    void clean() {
        serviceUserGradeDao.deleteAll();
        serviceTermsDao.deleteAll();
        serviceRuleUseDao.deleteAll();
        for (int i = Code.MAX_LEVEL; i>=0; i--) {
            codeDao.deleteByLevel(i);
        }

        assertEquals(0, serviceUserGradeDao.count());
        assertEquals(0, serviceTermsDao.count());
        assertEquals(0, serviceRuleUseDao.count());
        assertEquals(0, codeDao.count());
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));
        }

        assertEquals(cnt, serviceUserGradeDao.count());
    }

    @DisplayName("검색 조건에 따른 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 검색_조건에_따른_카운팅_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));
        }

        SearchCondition sc = createSearchCondition("NM", "테스트용", "1");
        assertEquals(cnt, serviceUserGradeDao.countBySearchCondition(sc));
    }

    @DisplayName("코드로 존재여부 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_존재여부_확인_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));
            assertTrue(serviceUserGradeDao.existsByStatCode(dto.getStat_code()));
        }
    }

    @DisplayName("코드로 존재여부 확인 테스트2")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_존재여부_확인_테스트2(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));
            assertTrue(serviceUserGradeDao.existsByStatCodeForUpdate(dto.getStat_code()));
        }
    }

    @DisplayName("코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_조회_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));
            ServiceUserGradeDto actualDto = serviceUserGradeDao.selectByStatCode(dto.getStat_code());


            assertNotNull(actualDto);
            assertEquals(dto.getStat_code(), actualDto.getStat_code());
            assertEquals(dto.getName(), actualDto.getName());
            assertEquals(dto.getOrd(), actualDto.getOrd());
            assertEquals(dto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(dto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(dto.getImg(), actualDto.getImg());
            assertEquals(dto.getChk_use(), actualDto.getChk_use());
            assertEquals(dto.getComt(), actualDto.getComt());
        }
    }

    @DisplayName("검색 조건에 따른 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 검색_조건에_따른_조회_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));
        }

        SearchCondition sc = createSearchCondition("NM", "테스트용", "1");

        int expectedSize = cnt > pageSize ? pageSize : cnt;
        int actualSize = serviceUserGradeDao.selectBySearchCondition(sc).size();
        assertEquals(expectedSize, actualSize);
    }

    @DisplayName("전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_조회_테스트(int cnt) {
        List<ServiceUserGradeDto> dummy = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));
            dummy.add(dto);
        }

        List<ServiceUserGradeDto> actualDtos = serviceUserGradeDao.selectAll();

        dummy.sort((a, b) -> a.getStat_code().compareTo(b.getStat_code()));
        actualDtos.sort((a, b) -> a.getStat_code().compareTo(b.getStat_code()));

        for (int i=0; i<cnt; i++) {
            ServiceUserGradeDto expectedDto = dummy.get(i);
            ServiceUserGradeDto actualDto = actualDtos.get(i);

            assertEquals(expectedDto.getStat_code(), actualDto.getStat_code());
            assertEquals(expectedDto.getName(), actualDto.getName());
            assertEquals(expectedDto.getOrd(), actualDto.getOrd());
            assertEquals(expectedDto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(expectedDto.getImg(), actualDto.getImg());
            assertEquals(expectedDto.getChk_use(), actualDto.getChk_use());
            assertEquals(expectedDto.getComt(), actualDto.getComt());

        }
    }

    @DisplayName("등록 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 등록_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));

            ServiceUserGradeDto actualDto = serviceUserGradeDao.selectByStatCode(dto.getStat_code());

            assertNotNull(actualDto);
            assertEquals(dto.getStat_code(), actualDto.getStat_code());
            assertEquals(dto.getName(), actualDto.getName());
            assertEquals(dto.getOrd(), actualDto.getOrd());
            assertEquals(dto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(dto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(dto.getImg(), actualDto.getImg());
            assertEquals(dto.getChk_use(), actualDto.getChk_use());
            assertEquals(dto.getComt(), actualDto.getComt());

        }
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));

            ServiceUserGradeDto actualDto = serviceUserGradeDao.selectByStatCode(dto.getStat_code());

            assertNotNull(actualDto);
            assertEquals(dto.getStat_code(), actualDto.getStat_code());
            assertEquals(dto.getName(), actualDto.getName());
            assertEquals(dto.getOrd(), actualDto.getOrd());
            assertEquals(dto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(dto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(dto.getImg(), actualDto.getImg());
            assertEquals(dto.getChk_use(), actualDto.getChk_use());
            assertEquals(dto.getComt(), actualDto.getComt());

            actualDto.setName("수정된 테스트용");
            actualDto.setOrd(100);
            actualDto.setShort_exp("수정된 테스트용");
            actualDto.setLong_exp("수정된 테스트용");
            actualDto.setImg("수정된 테스트용");
            actualDto.setChk_use("N");
            actualDto.setComt("수정된 테스트용");

            assertEquals(1, serviceUserGradeDao.update(actualDto));

            ServiceUserGradeDto updatedDto = serviceUserGradeDao.selectByStatCode(dto.getStat_code());

            assertNotNull(updatedDto);
            assertEquals(actualDto.getStat_code(), updatedDto.getStat_code());
            assertEquals(actualDto.getName(), updatedDto.getName());
            assertEquals(actualDto.getOrd(), updatedDto.getOrd());
            assertEquals(actualDto.getShort_exp(), updatedDto.getShort_exp());
            assertEquals(actualDto.getLong_exp(), updatedDto.getLong_exp());
            assertEquals(actualDto.getImg(), updatedDto.getImg());
            assertEquals(actualDto.getChk_use(), updatedDto.getChk_use());
            assertEquals(actualDto.getComt(), updatedDto.getComt());
        }
    }

    @DisplayName("삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 삭제_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));

            ServiceUserGradeDto actualDto = serviceUserGradeDao.selectByStatCode(dto.getStat_code());

            assertNotNull(actualDto);
            assertEquals(dto.getStat_code(), actualDto.getStat_code());
            assertEquals(dto.getName(), actualDto.getName());
            assertEquals(dto.getOrd(), actualDto.getOrd());
            assertEquals(dto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(dto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(dto.getImg(), actualDto.getImg());
            assertEquals(dto.getChk_use(), actualDto.getChk_use());
            assertEquals(dto.getComt(), actualDto.getComt());

            assertEquals(1, serviceUserGradeDao.deleteByStatCode(dto.getStat_code()));
            assertNull(serviceUserGradeDao.selectByStatCode(dto.getStat_code()));
        }
    }

    @DisplayName("전체 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_삭제_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeDto dto = createServiceUserGradeDto(i);
            assertEquals(1, serviceUserGradeDao.insert(dto));
        }

        assertEquals(cnt, serviceUserGradeDao.count());
        assertEquals(cnt, serviceUserGradeDao.deleteAll());
        assertEquals(0, serviceUserGradeDao.count());
    }

    private ServiceUserGradeDto createServiceUserGradeDto(int i) {
        ServiceUserGradeDto dto = new ServiceUserGradeDto();
        dto.setStat_code(poli_stat + i);
        dto.setName("테스트용" + i);
        dto.setOrd(1 + i);
        dto.setShort_exp("테스트용" + i);
        dto.setLong_exp("테스트용" + i);
        dto.setImg("테스트용");
        dto.setChk_use("Y");
        dto.setComt("테스트용");
        return dto;
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

        dto.setReg_user_seq(managerSeq);
        dto.setReg_date(currentDateFormat);
        dto.setUp_user_seq(updateManagerSeq);
        dto.setUp_date(currentUpdateDateFormat);
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

        dto.setReg_user_seq(managerSeq);
        dto.setReg_date(currentDateFormat);
        dto.setUp_user_seq(updateManagerSeq);
        dto.setUp_date(currentUpdateDateFormat);
        return dto;
    }

    private CodeDto createCodeDto() {
        CodeDto dto = new CodeDto();
        dto.setLevel(1);
        dto.setCode(code);
        dto.setName("테스트용");
        dto.setTop_code("100");
        dto.setChk_use("Y");

        dto.setReg_user_seq(managerSeq);
        dto.setReg_date(currentDateFormat);
        dto.setUp_user_seq(updateManagerSeq);
        dto.setUp_date(currentUpdateDateFormat);
        return dto;
    }

    private SearchCondition createSearchCondition(String searchOption, String searchKeyword, String sortOption) {
        return new SearchCondition(page, pageSize, searchOption, searchKeyword, sortOption);
    }


}