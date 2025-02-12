package com.example.demo.repository.mybatis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserConditionDto;
import com.example.demo.dto.service.ServiceUserConditionsDetailDto;
import com.example.demo.dto.service.ServiceUserConditionsDto;
import com.example.demo.repository.service.ServiceUserConditionRepository;
import com.example.demo.repository.service.ServiceUserConditionsRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ServiceUserConditionsDaoImplTest {

    @Autowired
    private ServiceUserConditionRepository serviceUserConditionDao;

    @Autowired
    private ServiceUserConditionsRepository serviceUserConditionsDao;

    private final String currentDateFormat = "2025/01/09";
    private final Integer managerSeq = 1;
    private final Integer size = 4;
    private final Integer page = 1;
    private final Integer pageSize = 10;

    // 코드 형식
    // - SUC0001, SUC0002, SUC0003, SUC0004
    private List<ServiceUserConditionDto> dummy = new ArrayList<>();

    @BeforeEach
    void setUp() {
        assertNotNull(serviceUserConditionDao);
        assertNotNull(serviceUserConditionsDao);

        serviceUserConditionDao.deleteAll();
        serviceUserConditionsDao.deleteAll();

        for (int i=1; i<=size; i++) {
            ServiceUserConditionDto dto = createUserConditionDto(i);
            assertEquals(1, serviceUserConditionDao.insert(dto));
            dummy.add(dto);
        }
    }

    @AfterEach
    void clean() {
        serviceUserConditionDao.deleteAll();
        serviceUserConditionsDao.deleteAll();

        dummy.clear();

        assertEquals(0, serviceUserConditionDao.count());
        assertEquals(0, serviceUserConditionsDao.count());
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
        }

        assertEquals(cnt, serviceUserConditionsDao.count());
    }

    @DisplayName("검색 기반 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 검색_기반_카운팅_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
        }

        SearchCondition sc = createSearchCondition();
        assertEquals(cnt, serviceUserConditionsDao.countBySearchCondition(sc));
    }

    @DisplayName("시퀀스로 데이터 존재 여부 판단 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 시퀀스_데이터_존재_여부_판단_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            assertTrue(serviceUserConditionsDao.existsBySeq(dto.getSeq()));
        }
    }

    @DisplayName("코드로 데이터 존재 여부 판단 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드_데이터_존재_여부_판단_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            assertTrue(serviceUserConditionsDao.existsByCondsCode(dto.getConds_code()));
        }
    }

    @DisplayName("시퀀스로 데이터 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 시퀀스_데이터_조회_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            ServiceUserConditionsDto selected = serviceUserConditionsDao.select(dto.getSeq());

            assertNotNull(selected);
            assertEquals(dto.getConds_code(), selected.getConds_code());
            assertEquals(dto.getName(), selected.getName());
            assertEquals(dto.getCond_code1(), selected.getCond_code1());
            assertEquals(dto.getChk_cond_code1(), selected.getChk_cond_code1());
            assertEquals(dto.getCond_code2(), selected.getCond_code2());
            assertEquals(dto.getChk_cond_code2(), selected.getChk_cond_code2());
            assertEquals(dto.getCond_code3(), selected.getCond_code3());
            assertEquals(dto.getChk_cond_code3(), selected.getChk_cond_code3());
            assertEquals(dto.getCond_code4(), selected.getCond_code4());
            assertEquals(dto.getChk_cond_code4(), selected.getChk_cond_code4());
            assertEquals(dto.getChk_use(), selected.getChk_use());
            assertEquals(dto.getComt(), selected.getComt());
        }
    }

    @DisplayName("코드로 데이터 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드_데이터_조회_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            ServiceUserConditionsDto selected = serviceUserConditionsDao.selectByCondsCode(dto.getConds_code());

            assertNotNull(selected);
            assertEquals(dto.getConds_code(), selected.getConds_code());
            assertEquals(dto.getName(), selected.getName());
            assertEquals(dto.getCond_code1(), selected.getCond_code1());
            assertEquals(dto.getChk_cond_code1(), selected.getChk_cond_code1());
            assertEquals(dto.getCond_code2(), selected.getCond_code2());
            assertEquals(dto.getChk_cond_code2(), selected.getChk_cond_code2());
            assertEquals(dto.getCond_code3(), selected.getCond_code3());
            assertEquals(dto.getChk_cond_code3(), selected.getChk_cond_code3());
            assertEquals(dto.getCond_code4(), selected.getCond_code4());
            assertEquals(dto.getChk_cond_code4(), selected.getChk_cond_code4());
            assertEquals(dto.getChk_use(), selected.getChk_use());
            assertEquals(dto.getComt(), selected.getComt());
        }
    }

    @DisplayName("검색 기반 데이터 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 검색_기반_데이터_조회_테스트(int cnt) {
        List<ServiceUserConditionsDto> dummy = new ArrayList<>();

        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            dummy.add(dto);
        }

        SearchCondition sc = createSearchCondition();
        List<ServiceUserConditionsDto> selected = serviceUserConditionsDao.selectBySearchCondition(sc);

        int expectedCnt = Math.min(cnt, pageSize);
        assertEquals(expectedCnt, selected.size());


        dummy.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
        selected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

        for (int i=0; i<expectedCnt; i++) {
            ServiceUserConditionsDto expected = dummy.get(i);
            ServiceUserConditionsDto actual = selected.get(i);

            assertEquals(expected.getConds_code(), actual.getConds_code());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getCond_code1(), actual.getCond_code1());
            assertEquals(expected.getChk_cond_code1(), actual.getChk_cond_code1());
            assertEquals(expected.getCond_code2(), actual.getCond_code2());
            assertEquals(expected.getChk_cond_code2(), actual.getChk_cond_code2());
            assertEquals(expected.getCond_code3(), actual.getCond_code3());
            assertEquals(expected.getChk_cond_code3(), actual.getChk_cond_code3());
            assertEquals(expected.getCond_code4(), actual.getCond_code4());
            assertEquals(expected.getChk_cond_code4(), actual.getChk_cond_code4());
            assertEquals(expected.getChk_use(), actual.getChk_use());
            assertEquals(expected.getComt(), actual.getComt());
        }
    }

    @DisplayName("전체 데이터 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_데이터_조회_테스트(int cnt) {
        List<ServiceUserConditionsDto> dummy = new ArrayList<>();

        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            dummy.add(dto);
        }

        List<ServiceUserConditionsDto> selected = serviceUserConditionsDao.selectAll();

        assertEquals(cnt, selected.size());

        dummy.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
        selected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsDto expected = dummy.get(i);
            ServiceUserConditionsDto actual = selected.get(i);

            assertEquals(expected.getConds_code(), actual.getConds_code());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getCond_code1(), actual.getCond_code1());
            assertEquals(expected.getChk_cond_code1(), actual.getChk_cond_code1());
            assertEquals(expected.getCond_code2(), actual.getCond_code2());
            assertEquals(expected.getChk_cond_code2(), actual.getChk_cond_code2());
            assertEquals(expected.getCond_code3(), actual.getCond_code3());
            assertEquals(expected.getChk_cond_code3(), actual.getChk_cond_code3());
            assertEquals(expected.getCond_code4(), actual.getCond_code4());
            assertEquals(expected.getChk_cond_code4(), actual.getChk_cond_code4());
            assertEquals(expected.getChk_use(), actual.getChk_use());
            assertEquals(expected.getComt(), actual.getComt());
        }
    }

    @DisplayName("코드 기반 데이터 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드_기반_데이터_조회_테스트(int cnt) {
        List<ServiceUserConditionsDto> dummy = new ArrayList<>();

        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto expected = dummy.get(i-1);
            ServiceUserConditionsDto actual = serviceUserConditionsDao.selectByCondsCode(expected.getConds_code());

            assertNotNull(actual);
            assertEquals(expected.getConds_code(), actual.getConds_code());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getCond_code1(), actual.getCond_code1());
            assertEquals(expected.getChk_cond_code1(), actual.getChk_cond_code1());
            assertEquals(expected.getCond_code2(), actual.getCond_code2());
            assertEquals(expected.getChk_cond_code2(), actual.getChk_cond_code2());
            assertEquals(expected.getCond_code3(), actual.getCond_code3());
            assertEquals(expected.getChk_cond_code3(), actual.getChk_cond_code3());
            assertEquals(expected.getCond_code4(), actual.getCond_code4());
            assertEquals(expected.getChk_cond_code4(), actual.getChk_cond_code4());
            assertEquals(expected.getChk_use(), actual.getChk_use());
            assertEquals(expected.getComt(), actual.getComt());
        }
    }

    @DisplayName("시퀀시 기반 데이터 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 시퀀스_기반_데이터_조회_테스트(int cnt) {
        List<ServiceUserConditionsDto> dummy = new ArrayList<>();

        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto expected = dummy.get(i-1);
            ServiceUserConditionsDto actual = serviceUserConditionsDao.select(expected.getSeq());

            assertNotNull(actual);
            assertEquals(expected.getConds_code(), actual.getConds_code());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getCond_code1(), actual.getCond_code1());
            assertEquals(expected.getChk_cond_code1(), actual.getChk_cond_code1());
            assertEquals(expected.getCond_code2(), actual.getCond_code2());
            assertEquals(expected.getChk_cond_code2(), actual.getChk_cond_code2());
            assertEquals(expected.getCond_code3(), actual.getCond_code3());
            assertEquals(expected.getChk_cond_code3(), actual.getChk_cond_code3());
            assertEquals(expected.getCond_code4(), actual.getCond_code4());
            assertEquals(expected.getChk_cond_code4(), actual.getChk_cond_code4());
            assertEquals(expected.getChk_use(), actual.getChk_use());
            assertEquals(expected.getComt(), actual.getComt());
        }
    }

    @DisplayName("데이터 정책 조회(조인 처리) 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 데이터_정책_조회_조인_처리_테스트(int cnt) {
        List<ServiceUserConditionsDto> dummy = new ArrayList<>();

        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            dummy.add(dto);

            ServiceUserConditionsDetailDto detailDto = serviceUserConditionsDao.selectForUserConditions(dummy.get(i-1).getConds_code());

            assertNotNull(detailDto);
            assertEquals(dummy.get(i-1).getConds_code(), detailDto.getConds_code());
            assertEquals(dummy.get(i-1).getName(), detailDto.getName());
            assertEquals(dummy.get(i-1).getCond_code1(), detailDto.getCond_code1());
            assertEquals(dummy.get(i-1).getChk_cond_code1(), detailDto.getChk_cond_code1());

            assertEquals(dummy.get(i-1).getCond_code2(), detailDto.getCond_code2());
            assertEquals(dummy.get(i-1).getChk_cond_code2(), detailDto.getChk_cond_code2());

            assertEquals(dummy.get(i-1).getCond_code3(), detailDto.getCond_code3());
            assertEquals(dummy.get(i-1).getChk_cond_code3(), detailDto.getChk_cond_code3());

            assertEquals(dummy.get(i-1).getCond_code4(), detailDto.getCond_code4());
            assertEquals(dummy.get(i-1).getChk_cond_code4(), detailDto.getChk_cond_code4());

            log.info("detailDto => {}", detailDto);
        }
    }

    @DisplayName("데이터 생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 데이터_생성_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));

            ServiceUserConditionsDto selected = serviceUserConditionsDao.select(dto.getSeq());

            assertNotNull(selected);

            assertEquals(dto.getConds_code(), selected.getConds_code());
            assertEquals(dto.getName(), selected.getName());
            assertEquals(dto.getCond_code1(), selected.getCond_code1());
            assertEquals(dto.getChk_cond_code1(), selected.getChk_cond_code1());
            assertEquals(dto.getCond_code2(), selected.getCond_code2());
            assertEquals(dto.getChk_cond_code2(), selected.getChk_cond_code2());
            assertEquals(dto.getCond_code3(), selected.getCond_code3());
            assertEquals(dto.getChk_cond_code3(), selected.getChk_cond_code3());
            assertEquals(dto.getCond_code4(), selected.getCond_code4());
            assertEquals(dto.getChk_cond_code4(), selected.getChk_cond_code4());
            assertEquals(dto.getChk_use(), selected.getChk_use());
            assertEquals(dto.getComt(), selected.getComt());

        }
    }

    @DisplayName("데이터 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 데이터_수정_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));

            dto.setName("수정된 이름");
            dto.setCond_code1("SUC0001");
            dto.setChk_cond_code1("N");
            dto.setCond_code2("SUC0002");
            dto.setChk_cond_code2("N");
            dto.setCond_code3("SUC0003");
            dto.setChk_cond_code3("N");
            dto.setCond_code4("SUC0004");
            dto.setChk_cond_code4("N");
            dto.setChk_use("N");
            dto.setComt("수정된 비고");

            assertEquals(1, serviceUserConditionsDao.update(dto));

            ServiceUserConditionsDto selected = serviceUserConditionsDao.select(dto.getSeq());

            assertNotNull(selected);

            assertEquals(dto.getConds_code(), selected.getConds_code());
            assertEquals(dto.getName(), selected.getName());
            assertEquals(dto.getCond_code1(), selected.getCond_code1());
            assertEquals(dto.getChk_cond_code1(), selected.getChk_cond_code1());
            assertEquals(dto.getCond_code2(), selected.getCond_code2());
            assertEquals(dto.getChk_cond_code2(), selected.getChk_cond_code2());
            assertEquals(dto.getCond_code3(), selected.getCond_code3());
            assertEquals(dto.getChk_cond_code3(), selected.getChk_cond_code3());
            assertEquals(dto.getCond_code4(), selected.getCond_code4());
            assertEquals(dto.getChk_cond_code4(), selected.getChk_cond_code4());
            assertEquals(dto.getChk_use(), selected.getChk_use());
            assertEquals(dto.getComt(), selected.getComt());
        }
    }

    @DisplayName("사용 여부 변경 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 사용_여부_변경_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));

            dto.setChk_use("N");
            assertEquals(1, serviceUserConditionsDao.updateChkUse(dto));

            ServiceUserConditionsDto selected = serviceUserConditionsDao.select(dto.getSeq());

            assertNotNull(selected);
            assertEquals("N", selected.getChk_use());
        }
    }

    @DisplayName("데이터 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 데이터_삭제_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            assertTrue(serviceUserConditionsDao.existsBySeq(dto.getSeq()));

            assertEquals(1, serviceUserConditionsDao.delete(dto.getSeq()));
            assertFalse(serviceUserConditionsDao.existsBySeq(dto.getSeq()));
        }
    }

    @DisplayName("전체 데이터 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_데이터_삭제_테스트(int cnt) {
        for (int i=1; i<=cnt; i++) {
            ServiceUserConditionsDto dto = createUserConditionsDto(i);
            assertEquals(1, serviceUserConditionsDao.insert(dto));
            assertTrue(serviceUserConditionsDao.existsBySeq(dto.getSeq()));
        }

        assertEquals(cnt, serviceUserConditionsDao.count());
        assertEquals(cnt, serviceUserConditionsDao.deleteAll());
        assertEquals(0, serviceUserConditionsDao.count());
    }

    private ServiceUserConditionDto createUserConditionDto(int i) {
        ServiceUserConditionDto dto = new ServiceUserConditionDto();

        dto.setCond_code("SUC000" + i);
        dto.setName("테스트용 항목" + i);
        dto.setShort_exp("짧은 설명" + i);
        dto.setLong_exp("긴 설명" + i);
        dto.setChk_use("Y");
        dto.setLaw1("법안 내용 " + i);
        dto.setLaw2("법안 내용 " + i);
        dto.setLaw3("법안 내용 " + i);
        dto.setComt("비고" + i);
        dto.setReg_date(currentDateFormat);
        dto.setReg_user_seq(managerSeq);
        dto.setUp_date(currentDateFormat);
        dto.setUp_user_seq(managerSeq);

        return dto;
    }

    private ServiceUserConditionsDto createUserConditionsDto(int i) {
        ServiceUserConditionsDto dto = new ServiceUserConditionsDto();

        dto.setConds_code("SUC000" + i);
        dto.setName("테스트용 정책" + i);
        dto.setCond_code1("SUC0001");
        dto.setChk_cond_code1("Y");

        dto.setCond_code2("SUC0002");
        dto.setChk_cond_code2("Y");

        dto.setCond_code3("SUC0003");
        dto.setChk_cond_code3("Y");

        dto.setCond_code4("SUC0004");
        dto.setChk_cond_code4("Y");

        dto.setChk_use("Y");
        dto.setComt("비고란");

        dto.setReg_date(currentDateFormat);
        dto.setReg_user_seq(managerSeq);
        dto.setUp_date(currentDateFormat);
        dto.setUp_user_seq(managerSeq);


        return dto;
    }

    private SearchCondition createSearchCondition() {
        SearchCondition sc = new SearchCondition();
        sc.setPage(page);
        sc.setPageSize(pageSize);
        sc.setSearchOption("NM");
        sc.setSearchKeyword("테스트용");
        sc.setSortOption("1");
        return sc;
    }

}