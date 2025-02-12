package com.example.demo.service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceTermsConditionDto;
import com.example.demo.dto.service.ServiceTermsDto;
import com.example.demo.dto.service.ServiceTermsRequest;
import com.example.demo.dto.service.ServiceTermsResponse;
import com.example.demo.global.error.exception.business.service.ServiceTermsAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceTermsNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.service.impl.ServiceTermsDaoImpl;
import com.example.demo.service.service.impl.ServiceTermsServiceImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ServiceTermsServiceImplTest {

    @InjectMocks
    private ServiceTermsServiceImpl serviceTermsServiceImpl;

    @Mock
    private ServiceTermsDaoImpl serviceTermsDaoImpl;

    @Mock
    private CustomFormatter formatter;

    private final String reg_date = "2025-01-11";
    private final Integer reg_user_seq = 1;
    private final String up_date = "2025-01-11";
    private final Integer up_user_seq = 1;
    private final Integer page = 1;
    private final Integer pageSize = 10;


    @BeforeEach
    void setUp() {
        assertNotNull(serviceTermsServiceImpl);
        assertNotNull(serviceTermsDaoImpl);
        assertNotNull(formatter);
    }

    // ===================== 지원 기능 단순 성공 테스트 =====================

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void count(int cnt) {
        when(serviceTermsDaoImpl.count()).thenReturn(cnt);
        assertEquals(cnt, serviceTermsServiceImpl.count());
    }

    @DisplayName("검색 조건에 따른 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void countBySearchCondition(int cnt) {
        SearchCondition sc = new SearchCondition();
        when(serviceTermsDaoImpl.countBySearchCondition(sc)).thenReturn(cnt);
        assertEquals(cnt, serviceTermsServiceImpl.countBySearchCondition(sc));
    }

    @DisplayName("서비스 정책 생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void create(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsRequest request = createRequest(i);
            when(serviceTermsDaoImpl.existsByPoliStat(request.getPoli_stat())).thenReturn(false);
            when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
            when(formatter.getManagerSeq()).thenReturn(reg_user_seq);

            ServiceTermsDto dto = createDto(i);
            when(serviceTermsDaoImpl.insert(dto)).thenReturn(1);
            when(serviceTermsDaoImpl.select(request.getPoli_stat())).thenReturn(dto);

            ServiceTermsResponse response = serviceTermsServiceImpl.create(request);

            assertEquals(request.getPoli_stat(), response.getPoli_stat());
            assertEquals(request.getName(), response.getName());
            assertEquals(request.getRule_stat1(), response.getRule_stat1());
            assertEquals(request.getOp1(), response.getOp1());
            assertEquals(request.getRule_stat2(), response.getRule_stat2());
            assertEquals(request.getOp2(), response.getOp2());
            assertEquals(request.getRule_stat3(), response.getRule_stat3());
            assertEquals(request.getComt(), response.getComt());
            assertEquals(request.getChk_use(), response.getChk_use());
        }
    }

    @DisplayName("서비스 정책 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void readByPoliStat(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createDto(i);
            when(serviceTermsDaoImpl.existsByPoliStat(dto.getPoli_stat())).thenReturn(true);
            when(serviceTermsDaoImpl.select(dto.getPoli_stat())).thenReturn(dto);

            ServiceTermsResponse response = serviceTermsServiceImpl.readByPoliStat(dto.getPoli_stat());

            assertEquals(dto.getPoli_stat(), response.getPoli_stat());
            assertEquals(dto.getName(), response.getName());
            assertEquals(dto.getRule_stat1(), response.getRule_stat1());
            assertEquals(dto.getOp1(), response.getOp1());
            assertEquals(dto.getRule_stat2(), response.getRule_stat2());
            assertEquals(dto.getOp2(), response.getOp2());
            assertEquals(dto.getRule_stat3(), response.getRule_stat3());
            assertEquals(dto.getComt(), response.getComt());
            assertEquals(dto.getChk_use(), response.getChk_use());
        }
    }

    @DisplayName("서비스 정책 검색 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void readBySearchCondition(int cnt) {
        SearchCondition sc = new SearchCondition();
        sc.setPage(page);
        sc.setPageSize(pageSize);

        List<ServiceTermsDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createDto(i);
            dummy.add(dto);
        }

        when(serviceTermsDaoImpl.countBySearchCondition(sc)).thenReturn(cnt);
        when(serviceTermsDaoImpl.selectBySearchCondition(sc)).thenReturn(dummy);

        PageResponse<ServiceTermsResponse> response = serviceTermsServiceImpl.readBySearchCondition(sc);
        assertEquals(cnt, response.getResponses().size());
    }

    @DisplayName("서비스 정책 전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void readAll(int cnt) {
        List<ServiceTermsDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceTermsDto dto = createDto(i);
            dummy.add(dto);
        }

        when(serviceTermsDaoImpl.selectAll()).thenReturn(dummy);

        List<ServiceTermsResponse> responses = serviceTermsServiceImpl.readAll();
        assertEquals(cnt, responses.size());
    }

    @Test
    @DisplayName("서비스 정책 조건 생성 테스트")
    void getServiceTermsCondition() {
        String poli_stat = "테스트용";
        String name = "테스트용 조건문";
        String expectedCond = "col1 >= 5 AND col2 < 10";
        ServiceTermsConditionDto dto = new ServiceTermsConditionDto(poli_stat, name, expectedCond);

        when(serviceTermsDaoImpl.existsByPoliStat(poli_stat)).thenReturn(true);
        when(serviceTermsDaoImpl.selectForCondition(poli_stat)).thenReturn(dto);

        String cond = serviceTermsServiceImpl.getServiceTermsCondition(poli_stat);
        assertEquals(expectedCond, cond);
    }

    @DisplayName("서비스 정책 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void modify(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsRequest request = createRequest(i);
            when(serviceTermsDaoImpl.existsByPoliStatForUpdate(request.getPoli_stat())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            ServiceTermsDto dto = createDto(i);
            when(serviceTermsDaoImpl.update(dto)).thenReturn(1);

            assertDoesNotThrow(() -> serviceTermsServiceImpl.modify(request));
        }
    }

    @DisplayName("서비스 정책 사용여부 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void modifyChkUse(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsRequest request = createRequest(i);
            when(serviceTermsDaoImpl.existsByPoliStatForUpdate(request.getPoli_stat())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            ServiceTermsDto dto = createDto(i);
            when(serviceTermsDaoImpl.updateChkUse(dto)).thenReturn(1);

            assertDoesNotThrow(() -> serviceTermsServiceImpl.modifyChkUse(request));
        }
    }

    @DisplayName("서비스 정책 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void remove(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceTermsRequest request = createRequest(i);
            when(serviceTermsDaoImpl.delete(request.getPoli_stat())).thenReturn(1);

            assertDoesNotThrow(() -> serviceTermsServiceImpl.remove(request.getPoli_stat()));
        }
    }

    @DisplayName("서비스 정책 전체 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void removeByCode(int cnt) {
        when(serviceTermsDaoImpl.count()).thenReturn(cnt);
        when(serviceTermsDaoImpl.deleteAll()).thenReturn(cnt);

        assertDoesNotThrow(() -> serviceTermsServiceImpl.removeAll());
    }

    // ===================== 예외 처리 테스트 =====================
    @Test
    @DisplayName("create() -> 중복된 키 값으로 인한 예외 발생")
    void create_중복된_키_값으로_인한_예외_발생() {
        ServiceTermsRequest request = createRequest(1);
        when(serviceTermsDaoImpl.existsByPoliStat(request.getPoli_stat())).thenReturn(true);
        assertThrows(ServiceTermsAlreadyExistsException.class, () -> serviceTermsServiceImpl.create(request));
    }

    @Test
    @DisplayName("create() -> DBMS 적용 실패로 인한 예외 발생")
    void create_DBMS_적용_실패로_인한_예외_발생() {
        ServiceTermsRequest request = createRequest(1);
        when(serviceTermsDaoImpl.existsByPoliStat(request.getPoli_stat())).thenReturn(false);
        when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
        when(formatter.getManagerSeq()).thenReturn(reg_user_seq);

        ServiceTermsDto dto = createDto(1);
        when(serviceTermsDaoImpl.insert(dto)).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceTermsServiceImpl.create(request));
    }

    @Test
    @DisplayName("readByPoliStat() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void readByPoliStat_존재하지_않는_키_값으로_인한_예외_발생() {
        String poli_stat = "테스트용";
        when(serviceTermsDaoImpl.existsByPoliStat(poli_stat)).thenReturn(false);
        assertThrows(ServiceTermsNotFoundException.class, () -> serviceTermsServiceImpl.readByPoliStat(poli_stat));
    }

    @Test
    @DisplayName("modify() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void modify_존재하지_않는_키_값으로_인한_예외_발생() {
        ServiceTermsRequest request = createRequest(1);
        when(serviceTermsDaoImpl.existsByPoliStatForUpdate(request.getPoli_stat())).thenReturn(false);
        assertThrows(ServiceTermsNotFoundException.class, () -> serviceTermsServiceImpl.modify(request));
    }

    @Test
    @DisplayName("modify() -> DBMS 적용 실패로 인한 예외 발생")
    void modify_DBMS_적용_실패로_인한_예외_발생() {
        ServiceTermsRequest request = createRequest(1);
        when(serviceTermsDaoImpl.existsByPoliStatForUpdate(request.getPoli_stat())).thenReturn(true);
        when(formatter.getCurrentDateFormat()).thenReturn(up_date);
        when(formatter.getManagerSeq()).thenReturn(up_user_seq);

        ServiceTermsDto dto = createDto(1);
        when(serviceTermsDaoImpl.update(dto)).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceTermsServiceImpl.modify(request));
    }

    @Test
    @DisplayName("modifyChkUse() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void modifyChkUse_존재하지_않는_키_값으로_인한_예외_발생() {
        ServiceTermsRequest request = createRequest(1);
        when(serviceTermsDaoImpl.existsByPoliStatForUpdate(request.getPoli_stat())).thenReturn(false);
        assertThrows(ServiceTermsNotFoundException.class, () -> serviceTermsServiceImpl.modifyChkUse(request));
    }

    @Test
    @DisplayName("modifyChkUse() -> DBMS 적용 실패로 인한 예외 발생")
    void modifyChkUse_DBMS_적용_실패로_인한_예외_발생() {
        ServiceTermsRequest request = createRequest(1);
        when(serviceTermsDaoImpl.existsByPoliStatForUpdate(request.getPoli_stat())).thenReturn(true);
        when(formatter.getCurrentDateFormat()).thenReturn(up_date);
        when(formatter.getManagerSeq()).thenReturn(up_user_seq);

        ServiceTermsDto dto = createDto(1);
        when(serviceTermsDaoImpl.updateChkUse(dto)).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceTermsServiceImpl.modifyChkUse(request));
    }


    @Test
    @DisplayName("removeAll() -> DBMS 적용 실패로 인한 예외 발생")
    void removeAll_DBMS_적용_실패로_인한_예외_발생() {
        when(serviceTermsDaoImpl.count()).thenReturn(10);
        when(serviceTermsDaoImpl.deleteAll()).thenReturn(5);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceTermsServiceImpl.removeAll());
    }

    private ServiceTermsRequest createRequest(int i) {
        ServiceTermsRequest request = new ServiceTermsRequest();
        request.setPoli_stat("테스트용" + i);
        request.setName("테스트용" + i);
        request.setRule_stat1("rule_stat1" + i);
        request.setOp1("op1" + i);
        request.setRule_stat2("rule_stat2" + i);
        request.setOp2("op2" + i);
        request.setRule_stat3("rule_stat3" + i);
        request.setComt("comt" + i);
        request.setChk_use("Y");
        request.setCode("000" + i);
        return request;
    }

    private ServiceTermsDto createDto(int i) {
        ServiceTermsDto dto = new ServiceTermsDto();
        dto.setPoli_stat("테스트용" + i);
        dto.setName("테스트용" + i);
        dto.setRule_stat1("rule_stat1" + i);
        dto.setOp1("op1" + i);
        dto.setRule_stat2("rule_stat2" + i);
        dto.setOp2("op2" + i);
        dto.setRule_stat3("rule_stat3" + i);
        dto.setComt("comt" + i);
        dto.setChk_use("Y");
        dto.setCode("000" + i);
        dto.setReg_date(reg_date);
        dto.setReg_user_seq(reg_user_seq);
        dto.setUp_date(up_date);
        dto.setUp_user_seq(up_user_seq);
        return dto;
    }

}