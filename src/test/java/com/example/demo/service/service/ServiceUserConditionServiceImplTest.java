package com.example.demo.service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.dto.service.ServiceUserConditionDto;
import com.example.demo.dto.service.ServiceUserConditionRequest;
import com.example.demo.dto.service.ServiceUserConditionResponse;
import com.example.demo.global.error.exception.business.service.ServiceUserConditionAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceUserConditionNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.service.impl.ServiceUserConditionDaoImpl;
import com.example.demo.service.service.impl.ServiceUserConditionServiceImpl;
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
class ServiceUserConditionServiceImplTest {

    @InjectMocks
    private ServiceUserConditionServiceImpl serviceUserConditionServiceImpl;

    @Mock
    private ServiceUserConditionDaoImpl serviceUserConditionDaoImpl;

    @Mock
    private CustomFormatter formatter;

    private final String reg_date = "2025-01-11";
    private final Integer reg_user_seq = 1;
    private final String up_date = "2025-01-11";
    private final Integer up_user_seq = 1;


    @BeforeEach
    void setUp() {
        assertNotNull(serviceUserConditionServiceImpl);
        assertNotNull(serviceUserConditionDaoImpl);
        assertNotNull(formatter);
    }

    // ===================== 지원 기능 단순 성공 테스트 =====================

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        when(serviceUserConditionDaoImpl.count()).thenReturn(cnt);
        assertEquals(cnt, serviceUserConditionServiceImpl.count());
    }

    @DisplayName("조건 항목 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 조건_항목_코드_조회_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            ServiceUserConditionDto dto = createDto(request);

            when(serviceUserConditionDaoImpl.existsByCondCode(request.getCond_code())).thenReturn(true);
            when(serviceUserConditionDaoImpl.select(request.getCond_code())).thenReturn(dto);

            ServiceUserConditionResponse response = serviceUserConditionServiceImpl.readByCondCode(request.getCond_code());

            assertEquals(request.getCond_code(), response.getCond_code());
            assertEquals(request.getName(), response.getName());
            assertEquals(request.getShort_exp(), response.getShort_exp());
            assertEquals(request.getLong_exp(), response.getLong_exp());
            assertEquals(request.getChk_use(), response.getChk_use());
            assertEquals(request.getLaw1(), response.getLaw1());
            assertEquals(request.getLaw2(), response.getLaw2());
            assertEquals(request.getLaw3(), response.getLaw3());
            assertEquals(request.getComt(), response.getComt());
        }
    }

    @DisplayName("전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_조회_테스트(int cnt) {
        List<ServiceUserConditionDto> dummy = new ArrayList<>();

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            ServiceUserConditionDto dto = createDto(request);
            dummy.add(dto);
        }
        when(serviceUserConditionDaoImpl.selectAll()).thenReturn(dummy);

        List<ServiceUserConditionResponse> responses = serviceUserConditionServiceImpl.readAll();

        assertEquals(cnt, responses.size());

        for (int i=0; i < cnt; i++) {
            assertEquals(dummy.get(i).getCond_code(), responses.get(i).getCond_code());
            assertEquals(dummy.get(i).getName(), responses.get(i).getName());
            assertEquals(dummy.get(i).getShort_exp(), responses.get(i).getShort_exp());
            assertEquals(dummy.get(i).getLong_exp(), responses.get(i).getLong_exp());
            assertEquals(dummy.get(i).getChk_use(), responses.get(i).getChk_use());
            assertEquals(dummy.get(i).getLaw1(), responses.get(i).getLaw1());
            assertEquals(dummy.get(i).getLaw2(), responses.get(i).getLaw2());
            assertEquals(dummy.get(i).getLaw3(), responses.get(i).getLaw3());
            assertEquals(dummy.get(i).getComt(), responses.get(i).getComt());
        }
    }

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            ServiceUserConditionDto dto = createDto(request);

            when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
            when(formatter.getManagerSeq()).thenReturn(reg_user_seq);
            when(serviceUserConditionDaoImpl.insert(dto)).thenReturn(1);
            when(serviceUserConditionDaoImpl.select(dto.getCond_code())).thenReturn(dto);

            ServiceUserConditionResponse response = serviceUserConditionServiceImpl.create(request);

            assertEquals(request.getCond_code(), response.getCond_code());
            assertEquals(request.getName(), response.getName());
            assertEquals(request.getShort_exp(), response.getShort_exp());
            assertEquals(request.getLong_exp(), response.getLong_exp());
            assertEquals(request.getChk_use(), response.getChk_use());
            assertEquals(request.getLaw1(), response.getLaw1());
            assertEquals(request.getLaw2(), response.getLaw2());
            assertEquals(request.getLaw3(), response.getLaw3());
            assertEquals(request.getComt(), response.getComt());
        }
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            ServiceUserConditionDto dto = createDto(request);

            when(serviceUserConditionDaoImpl.existsByCondCodeForUpdate(request.getCond_code())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);
            when(serviceUserConditionDaoImpl.update(dto)).thenReturn(1);

            serviceUserConditionServiceImpl.modify(request);
        }
    }

    @DisplayName("사용 가능 여부 변경 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 사용_가능_여부_변경_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            ServiceUserConditionDto dto = createDto(request);

            when(serviceUserConditionDaoImpl.existsByCondCodeForUpdate(request.getCond_code())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);
            when(serviceUserConditionDaoImpl.updateChkUse(dto)).thenReturn(1);

            assertDoesNotThrow(() -> serviceUserConditionServiceImpl.modifyChkUse(request));
        }
    }

    @DisplayName("삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 삭제_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            when(serviceUserConditionDaoImpl.delete(request.getCond_code())).thenReturn(1);
            assertDoesNotThrow(() -> serviceUserConditionServiceImpl.remove(request.getCond_code()));
        }
    }

    @DisplayName("모두 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 모두_삭제_테스트(int cnt) {
        when(serviceUserConditionDaoImpl.count()).thenReturn(cnt);
        when(serviceUserConditionDaoImpl.deleteAll()).thenReturn(cnt);
        assertDoesNotThrow(() -> serviceUserConditionServiceImpl.removeAll());
    }

    // ===================== 예외 처리 테스트 =====================
    @Test
    @DisplayName("create() -> 중복된 키값으로 인한 예외 발생")
    void create_중복된_키_값으로_인한_예외_발생() {
        ServiceUserConditionRequest request = createRequest(1);
        ServiceUserConditionDto dto = createDto(request);

        when(serviceUserConditionDaoImpl.existsByCondCode(request.getCond_code())).thenReturn(true);

        assertThrows(ServiceUserConditionAlreadyExistsException.class, () -> serviceUserConditionServiceImpl.create(request));
    }

    @Test
    @DisplayName("create() -> DBMS 정상 반영 실패로 인한 예외 발생")
    void create_DBMS_정상_반영_실패로_인한_예외_발생() {
        ServiceUserConditionRequest request = createRequest(1);
        ServiceUserConditionDto dto = createDto(request);

        when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
        when(formatter.getManagerSeq()).thenReturn(reg_user_seq);
        when(serviceUserConditionDaoImpl.existsByCondCode(request.getCond_code())).thenReturn(false);
        when(serviceUserConditionDaoImpl.insert(dto)).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionServiceImpl.create(request));
    }

    @Test
    @DisplayName("readByCondCode() -> 조건 코드 조회 실패로 인한 예외 발생")
    void readByCondCode_조건_코드_조회_실패로_인한_예외_발생() {
        ServiceUserConditionRequest request = createRequest(1);
        ServiceUserConditionDto dto = createDto(request);

        when(serviceUserConditionDaoImpl.existsByCondCode(request.getCond_code())).thenReturn(false);

        assertThrows(ServiceUserConditionNotFoundException.class, () -> serviceUserConditionServiceImpl.readByCondCode(request.getCond_code()));
    }

    @Test
    @DisplayName("modify() -> 존재하지 않는 키 수정 시도할 때 예외 발생")
    void modify_존재하지_않는_키_수정_시도할_때_예외_발생() {
        ServiceUserConditionRequest request = createRequest(1);
        ServiceUserConditionDto dto = createDto(request);

        when(serviceUserConditionDaoImpl.existsByCondCodeForUpdate(request.getCond_code())).thenReturn(false);

        assertThrows(ServiceUserConditionNotFoundException.class, () -> serviceUserConditionServiceImpl.modify(request));
    }

    @Test
    @DisplayName("modify() -> DBMS 정상 반영 실패로 인한 예외 발생")
    void modify_DBMS_정상_반영_실패로_인한_예외_발생() {
        ServiceUserConditionRequest request = createRequest(1);
        ServiceUserConditionDto dto = createDto(request);

        when(serviceUserConditionDaoImpl.existsByCondCodeForUpdate(request.getCond_code())).thenReturn(true);
        when(formatter.getCurrentDateFormat()).thenReturn(up_date);
        when(formatter.getManagerSeq()).thenReturn(up_user_seq);
        when(serviceUserConditionDaoImpl.update(dto)).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionServiceImpl.modify(request));
    }

    @Test
    @DisplayName("modifyChkUse() -> 존재하지 않는 키 수정 시도할 때 예외 발생")
    void modifyChkUse_존재하지_않는_키_수정_시도할_때_예외_발생() {
        ServiceUserConditionRequest request = createRequest(1);
        ServiceUserConditionDto dto = createDto(request);

        when(serviceUserConditionDaoImpl.existsByCondCodeForUpdate(request.getCond_code())).thenReturn(false);

        assertThrows(ServiceUserConditionNotFoundException.class, () -> serviceUserConditionServiceImpl.modifyChkUse(request));
    }

    @Test
    @DisplayName("modifyChkUse() -> DBMS 정상 반영 실패로 인한 예외 발생")
    void modifyChkUse_DBMS_정상_반영_실패로_인한_예외_발생() {
        ServiceUserConditionRequest request = createRequest(1);
        ServiceUserConditionDto dto = createDto(request);

        when(serviceUserConditionDaoImpl.existsByCondCodeForUpdate(request.getCond_code())).thenReturn(true);
        when(formatter.getCurrentDateFormat()).thenReturn(up_date);
        when(formatter.getManagerSeq()).thenReturn(up_user_seq);
        when(serviceUserConditionDaoImpl.updateChkUse(dto)).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionServiceImpl.modifyChkUse(request));
    }

    @Test
    @DisplayName("remove() -> DBMS 정상 반영 실패로 인한 예외 발생")
    void remove_DBMS_정상_반영_실패로_인한_예외_발생() {
        ServiceUserConditionRequest request = createRequest(1);

        when(serviceUserConditionDaoImpl.delete(request.getCond_code())).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionServiceImpl.remove(request.getCond_code()));
    }

    @Test
    @DisplayName("removaAll() -> DBMS 정상 반영 실패로 인한 예외 발생")
    void removeAll_DBMS_정상_반영_실패로_인한_예외_발생() {
        when(serviceUserConditionDaoImpl.count()).thenReturn(10);
        when(serviceUserConditionDaoImpl.deleteAll()).thenReturn(5);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionServiceImpl.removeAll());
    }

    private ServiceUserConditionRequest createRequest(int i) {
        ServiceUserConditionRequest request = new ServiceUserConditionRequest();

        request.setCond_code("CD" + i);
        request.setName("테스트용" + i);
        request.setShort_exp("짧은 설명" + i);
        request.setLong_exp("긴 설명" + i);
        request.setChk_use("Y");
        request.setLaw1("관련 법안 1");
        request.setLaw2("관련 법안 2");
        request.setLaw3("관련 법안 3");
        request.setComt("비고");

        return request;
    }

    private ServiceUserConditionDto createDto(ServiceUserConditionRequest request) {
        ServiceUserConditionDto dto = new ServiceUserConditionDto(request, reg_date, reg_user_seq, up_date, up_user_seq);
        return dto;
    }
}