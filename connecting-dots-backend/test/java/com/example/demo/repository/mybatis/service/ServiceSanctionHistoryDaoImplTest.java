package com.example.demo.repository.mybatis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.Code;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.dto.service.ServiceSanctionHistoryDto;
import com.example.demo.dto.service.ServiceTermsDto;
import com.example.demo.repository.code.CommonCodeRepository;
import com.example.demo.repository.service.ServiceRuleUseRepository;
import com.example.demo.repository.service.ServiceSanctionHistoryRepository;
import com.example.demo.repository.service.ServiceTermsRepository;
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
class ServiceSanctionHistoryDaoImplTest {

    @Autowired
    private ServiceSanctionHistoryRepository serviceSanctionHistoryDao;

    @Autowired
    private ServiceTermsRepository serviceTermsDao;

    @Autowired
    private ServiceRuleUseRepository serviceRuleUseDao;

    @Autowired
    private CommonCodeRepository commonCodeDao;

    private final String poli_stat = "PS100";
    private final String rule_stat1 = "1001";
    private final String rule_stat2 = "1002";
    private final String rule_stat3 = "1003";
    private final String code = "100";

    private final String appl_begin = "2025-01-01 00:00:00";
    private final String appl_end = "9999-12-31 23:59:59";

    private final String currentDateFormat = "2025-01-08 00:00:00";
    private final Integer managerSeq = 1;
    private final String currentUpdateDateFormat = "2025-01-12 00:00:00";
    private final Integer updateManagerSeq = 1;

    private final Integer page = 1;
    private final Integer pageSize = 10;

    @BeforeEach
    void setUp() {
        // 자동 주입 확인
        assertNotNull(serviceSanctionHistoryDao);
        assertNotNull(serviceTermsDao);
        assertNotNull(serviceRuleUseDao);
        assertNotNull(commonCodeDao);

        // 테스트용 데이터 삭제, DB 초기화
        serviceSanctionHistoryDao.deleteAll();
        serviceTermsDao.deleteAll();
        serviceRuleUseDao.deleteAll();

        for (int i= Code.MAX_LEVEL; i>=0; i--) {
            commonCodeDao.deleteByLevel(i);
        }

        // 공통 코드 1개 추가
        CodeDto codeDto = createCodeDto();
        assertEquals(1, commonCodeDao.insert(codeDto));

        // 서비스 이용 규칙 테스트용 데이터 3개 추가
        ServiceRuleUseDto ruleUseDto1 = createServiceRuleUseDto(1, rule_stat1);
        ServiceRuleUseDto ruleUseDto2 = createServiceRuleUseDto(2, rule_stat2);
        ServiceRuleUseDto ruleUseDto3 = createServiceRuleUseDto(3, rule_stat3);

        assertEquals(1, serviceRuleUseDao.insert(ruleUseDto1));
        assertEquals(1, serviceRuleUseDao.insert(ruleUseDto2));
        assertEquals(1, serviceRuleUseDao.insert(ruleUseDto3));

        // 서비스 정책 테스트용 데이터 1개 추가
        ServiceTermsDto serviceTermsDto = createServiceTermsDto(code);
        assertEquals(1, serviceTermsDao.insert(serviceTermsDto));
    }

    @AfterEach
    void clean() {
        // 테스트용 데이터 삭제, DB 초기화
        serviceSanctionHistoryDao.deleteAll();
        serviceTermsDao.deleteAll();
        serviceRuleUseDao.deleteAll();

        for (int i= Code.MAX_LEVEL; i>=0; i--) {
            commonCodeDao.deleteByLevel(i);
        }

        assertEquals(0, serviceSanctionHistoryDao.count());
        assertEquals(0, serviceTermsDao.count());
        assertEquals(0, serviceRuleUseDao.count());

    }

    // 카운팅 테스트
    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
        }

        int totalCnt = serviceSanctionHistoryDao.count();
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("검색 기반 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 검색_기반_카운팅_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
        }

        SearchCondition sc = createSearchCondition(page, pageSize, "PS", poli_stat, "1");
        int totalCnt = serviceSanctionHistoryDao.countBySearchCondition(sc);
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("회원 시퀀스 기반 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 회원_시퀀스_기반_카운팅_테스트(int cnt) {
        // 테스트 서버에는 회원 시퀀스 1이 항상 존재함
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
        }

        int totalCnt = serviceSanctionHistoryDao.countByUserSeq(1);
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("회원 시퀀스 기반 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 회원_시퀀스_기반_조회_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();

        // 테스트 서버에는 회원 시퀀스 1이 항상 존재함
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        int totalCnt = serviceSanctionHistoryDao.countByUserSeq(1);
        assertEquals(cnt, totalCnt);

        List<ServiceSanctionHistoryDto> selected = serviceSanctionHistoryDao.selectByUserSeq(1);

        // 순서 보장을 위한 정렬 처리
        dummy.sort((a, b) -> a.getSeq());
        selected.sort((a, b) -> a.getSeq() - b.getSeq());

        for(int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto expectedDto = dummy.get(i);
            ServiceSanctionHistoryDto actualDto = selected.get(i);

            assertEquals(expectedDto.getSeq(), actualDto.getSeq());
            assertEquals(expectedDto.getPoli_stat(), actualDto.getPoli_stat());
            assertEquals(expectedDto.getUser_seq(), actualDto.getUser_seq());
            assertEquals(expectedDto.getAppl_begin(), actualDto.getAppl_begin());
            assertEquals(expectedDto.getAppl_end(), actualDto.getAppl_end());
            assertEquals(expectedDto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(expectedDto.getComt(), actualDto.getComt());
        }
    }

    @DisplayName("시퀀스로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 시퀀스로_조회_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        // 랜덤 조회 처리
        int randomIdx = (int) (Math.random() * cnt);
        ServiceSanctionHistoryDto expectedDto = dummy.get(randomIdx);
        ServiceSanctionHistoryDto actualDto = serviceSanctionHistoryDao.selectBySeq(expectedDto.getSeq());

        assertNotNull(actualDto);

        assertEquals(expectedDto.getSeq(), actualDto.getSeq());
        assertEquals(expectedDto.getPoli_stat(), actualDto.getPoli_stat());
        assertEquals(expectedDto.getUser_seq(), actualDto.getUser_seq());
        assertEquals(expectedDto.getAppl_begin(), actualDto.getAppl_begin());
        assertEquals(expectedDto.getAppl_end(), actualDto.getAppl_end());
        assertEquals(expectedDto.getShort_exp(), actualDto.getShort_exp());
        assertEquals(expectedDto.getLong_exp(), actualDto.getLong_exp());
        assertEquals(expectedDto.getComt(), actualDto.getComt());
    }

    @DisplayName("정책 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 정책_코드로_조회_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        // 랜덤 조회 처리
        List<ServiceSanctionHistoryDto> actualDtos = serviceSanctionHistoryDao.selectByPoliStat(poli_stat);

        dummy.sort((a, b) -> a.getSeq() - b.getSeq());
        actualDtos.sort((a, b) -> a.getSeq() - b.getSeq());

        for(int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto expectedDto = dummy.get(i);
            ServiceSanctionHistoryDto actualDto = actualDtos.get(i);

            assertEquals(expectedDto.getSeq(), actualDto.getSeq());
            assertEquals(expectedDto.getPoli_stat(), actualDto.getPoli_stat());
            assertEquals(expectedDto.getUser_seq(), actualDto.getUser_seq());
            assertEquals(expectedDto.getAppl_begin(), actualDto.getAppl_begin());
            assertEquals(expectedDto.getAppl_end(), actualDto.getAppl_end());
            assertEquals(expectedDto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(expectedDto.getComt(), actualDto.getComt());
        }

    }

    @DisplayName("회원 시퀀스로 현재 적용 중인 정책 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 회원_시퀀스로_현재_적용_중인_정책_조회_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        // 랜덤 조회 처리
        List<ServiceSanctionHistoryDto> actualDtos = serviceSanctionHistoryDao.selectByUserSeqForNow(1);
        assertEquals(cnt, actualDtos.size());

        dummy.sort((a, b) -> a.getSeq() - b.getSeq());
        actualDtos.sort((a, b) -> a.getSeq() - b.getSeq());

        for(int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto expectedDto = dummy.get(i);
            ServiceSanctionHistoryDto actualDto = actualDtos.get(i);

            assertEquals(expectedDto.getSeq(), actualDto.getSeq());
            assertEquals(expectedDto.getPoli_stat(), actualDto.getPoli_stat());
            assertEquals(expectedDto.getUser_seq(), actualDto.getUser_seq());
            assertEquals(expectedDto.getAppl_begin(), actualDto.getAppl_begin());
            assertEquals(expectedDto.getAppl_end(), actualDto.getAppl_end());
            assertEquals(expectedDto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(expectedDto.getComt(), actualDto.getComt());
        }
    }

    @DisplayName("변경 처리를 위한 시퀀스 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 변경_처리를_위한_시퀀스_조회_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        // 랜덤 조회 처리
        int randomIdx = (int) (Math.random() * cnt);
        ServiceSanctionHistoryDto expectedDto = dummy.get(randomIdx);
        ServiceSanctionHistoryDto actualDto = serviceSanctionHistoryDao.selectBySeqForUpdate(expectedDto.getSeq());

        assertNotNull(actualDto);

        assertEquals(expectedDto.getSeq(), actualDto.getSeq());
        assertEquals(expectedDto.getPoli_stat(), actualDto.getPoli_stat());
        assertEquals(expectedDto.getUser_seq(), actualDto.getUser_seq());
        assertEquals(expectedDto.getAppl_begin(), actualDto.getAppl_begin());
        assertEquals(expectedDto.getAppl_end(), actualDto.getAppl_end());
        assertEquals(expectedDto.getShort_exp(), actualDto.getShort_exp());
        assertEquals(expectedDto.getLong_exp(), actualDto.getLong_exp());
        assertEquals(expectedDto.getComt(), actualDto.getComt());
    }

    @DisplayName("전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_조회_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        List<ServiceSanctionHistoryDto> actualDtos = serviceSanctionHistoryDao.selectAll();
        assertEquals(cnt, actualDtos.size());

        dummy.sort((a, b) -> a.getSeq() - b.getSeq());
        actualDtos.sort((a, b) -> a.getSeq() - b.getSeq());

        for(int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto expectedDto = dummy.get(i);
            ServiceSanctionHistoryDto actualDto = actualDtos.get(i);

            assertEquals(expectedDto.getSeq(), actualDto.getSeq());
            assertEquals(expectedDto.getPoli_stat(), actualDto.getPoli_stat());
            assertEquals(expectedDto.getUser_seq(), actualDto.getUser_seq());
            assertEquals(expectedDto.getAppl_begin(), actualDto.getAppl_begin());
            assertEquals(expectedDto.getAppl_end(), actualDto.getAppl_end());
            assertEquals(expectedDto.getShort_exp(), actualDto.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualDto.getLong_exp());
            assertEquals(expectedDto.getComt(), actualDto.getComt());
        }
    }

    @DisplayName("생성 처리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_처리_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
        }
    }

    @DisplayName("변경 처리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 변경_처리_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=0; i<cnt; i++) {
            String updateComt = "변경 처리 테스트";
            ServiceSanctionHistoryDto dto = dummy.get(i);
            dto.setComt(updateComt);

            assertEquals(1, serviceSanctionHistoryDao.update(dto));

            ServiceSanctionHistoryDto actualDto = serviceSanctionHistoryDao.selectBySeq(dto.getSeq());
            assertEquals(updateComt, actualDto.getComt());
        }
    }

    @DisplayName("삭제 처리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 삭제_처리_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = dummy.get(i);
            assertEquals(1, serviceSanctionHistoryDao.deleteBySeq(dto.getSeq()));
        }
    }

    @DisplayName("회원 시퀀스로 삭제 처리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 정책_코드로_삭제_처리_테스트(int cnt) {
        Integer user_seq = 1;
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        assertEquals(cnt, serviceSanctionHistoryDao.selectByPoliStat(poli_stat).size());
        assertEquals(cnt, serviceSanctionHistoryDao.deleteByUserSeq(user_seq));
        assertEquals(0, serviceSanctionHistoryDao.countByUserSeq(user_seq));
    }

    @DisplayName("전체 삭제 처리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_삭제_처리_테스트(int cnt) {
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto dto = createServiceSanctionHistoryDto(poli_stat);
            assertEquals(1, serviceSanctionHistoryDao.insert(dto));
            dummy.add(dto);
        }

        assertEquals(cnt, serviceSanctionHistoryDao.selectAll().size());
        assertEquals(cnt, serviceSanctionHistoryDao.deleteAll());
        assertEquals(0, serviceSanctionHistoryDao.count());
    }

    private ServiceSanctionHistoryDto createServiceSanctionHistoryDto(String poli_stat) {
        ServiceSanctionHistoryDto dto = new ServiceSanctionHistoryDto();
        dto.setPoli_stat(poli_stat);
        dto.setUser_seq(1);
        dto.setAppl_begin(appl_begin);
        dto.setAppl_end(appl_end);
        dto.setShort_exp("테스트용");
        dto.setLong_exp("테스트용");
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

    private SearchCondition createSearchCondition(int page, int pageSize, String searchOption, String searchKeyword, String sortOption) {
        return new SearchCondition(page, pageSize, searchOption, searchKeyword, sortOption);
    }
}