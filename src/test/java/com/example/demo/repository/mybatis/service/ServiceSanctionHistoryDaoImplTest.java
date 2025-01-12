package com.example.demo.repository.mybatis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.Code;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.dto.service.ServiceSanctionHistoryDto;
import com.example.demo.dto.service.ServiceTermsDto;
import com.example.demo.repository.mybatis.code.CommonCodeDaoImpl;
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
    private ServiceSanctionHistoryDaoImpl serviceSanctionHistoryDao;

    @Autowired
    private ServiceTermsDaoImpl serviceTermsDao;

    @Autowired
    private ServiceRuleUseDaoImpl serviceRuleUseDao;

    @Autowired
    private CommonCodeDaoImpl commonCodeDao;

    private final String poli_stat = "PS100";
    private final String rule_stat1 = "1001";
    private final String rule_stat2 = "1002";
    private final String rule_stat3 = "1003";
    private final String code = "100";

    private final String currentDateFormat = "2025-01-08";
    private final Integer managerSeq = 1;
    private final String currentUpdateDateFormat = "2025-01-12";
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

    private ServiceSanctionHistoryDto createServiceSanctionHistoryDto(String poli_stat) {
        ServiceSanctionHistoryDto dto = new ServiceSanctionHistoryDto();
        dto.setPoli_stat(poli_stat);
        dto.setUser_seq(1);
        dto.setAppl_begin("2025-01-01");
        dto.setAppl_end("2025-01-01");
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