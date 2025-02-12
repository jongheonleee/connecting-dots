package com.example.demo.service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserConditionsDetailDto;
import com.example.demo.dto.service.ServiceUserConditionsDetailResponse;
import com.example.demo.dto.service.ServiceUserConditionsDto;
import com.example.demo.dto.service.ServiceUserConditionsRequest;
import com.example.demo.dto.service.ServiceUserConditionsResponse;
import com.example.demo.global.error.exception.business.service.ServiceUserConditionsAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceUserConditionsNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.service.impl.ServiceUserConditionsDaoImpl;
import com.example.demo.service.service.impl.ServiceUserConditionsServiceImpl;
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

// 모든 서비스 테스트 코드가 동일한 CustomFormatter를 사용하므로 Mock으로 생성하여 주입
// 이 부분 템플릿 메서드 패턴을 활용해서 코드 중복 개선해 나가도 좋지 않을까?
@ExtendWith(MockitoExtension.class)
class ServiceUserConditionsServiceImplTest {

    @InjectMocks
    private ServiceUserConditionsServiceImpl serviceUserConditionsServiceImpl;

    @Mock
    private ServiceUserConditionsDaoImpl serviceUserConditionsDaoImpl;

    @Mock
    private CustomFormatter formatter;

    private final String reg_date = "2025-01-11";
    private final Integer reg_user_seq = 1;
    private final String up_date = "2025-01-11";
    private final Integer up_user_seq = 1;

    private final Integer page = 1;
    private final Integer pageSize = 10;

    private final String condCode1 = "CD0001";
    private final String condCode2 = "CD0002";
    private final String condCode3 = "CD0003";
    private final String condCode4 = "CD0004";


    @BeforeEach
    void setUp() {
        assertNotNull(serviceUserConditionsServiceImpl);
        assertNotNull(serviceUserConditionsDaoImpl);
        assertNotNull(formatter);
    }

    // ===================== 지원 기능 단순 성공 테스트 =====================

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        when(serviceUserConditionsDaoImpl.count()).thenReturn(cnt);
        assertEquals(cnt, serviceUserConditionsServiceImpl.count());
    }

    @DisplayName("검색 조건에 따른 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 검색_조건에_따른_카운팅_테스트(int cnt) {
        SearchCondition sc = new SearchCondition();
        when(serviceUserConditionsDaoImpl.countBySearchCondition(sc)).thenReturn(cnt);
        assertEquals(cnt, serviceUserConditionsServiceImpl.countBySearchCondition(sc));
    }

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsRequest request = createRequest(i);
            ServiceUserConditionsDto dto = createDto(request);

            when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
            when(formatter.getManagerSeq()).thenReturn(reg_user_seq);

            when(serviceUserConditionsDaoImpl.existsByCondsCode(request.getConds_code())).thenReturn(false);
            when(serviceUserConditionsDaoImpl.insert(dto)).thenReturn(1);
            when(serviceUserConditionsDaoImpl.selectByCondsCode(dto.getConds_code())).thenReturn(dto);

            ServiceUserConditionsResponse response = serviceUserConditionsServiceImpl.create(request);

            assertEquals(dto.getConds_code(), response.getConds_code());
            assertEquals(dto.getName(), response.getName());
            assertEquals(dto.getCond_code1(), response.getCond_code1());
            assertEquals(dto.getChk_cond_code1(), response.getChk_cond_code1());
            assertEquals(dto.getCond_code2(), response.getCond_code2());
            assertEquals(dto.getChk_cond_code2(), response.getChk_cond_code2());
            assertEquals(dto.getCond_code3(), response.getCond_code3());
            assertEquals(dto.getChk_cond_code3(), response.getChk_cond_code3());
            assertEquals(dto.getCond_code4(), response.getCond_code4());
            assertEquals(dto.getChk_cond_code4(), response.getChk_cond_code4());
            assertEquals(dto.getChk_use(), response.getChk_use());
        }
    }

    @DisplayName("조건 항목 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 조건_항목_코드로_조회_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsRequest request = createRequest(i);
            ServiceUserConditionsDto dto = createDto(request);

            when(serviceUserConditionsDaoImpl.existsByCondsCode(request.getConds_code())).thenReturn(true);
            when(serviceUserConditionsDaoImpl.selectByCondsCode(request.getConds_code())).thenReturn(dto);

            ServiceUserConditionsResponse response = serviceUserConditionsServiceImpl.readByCondsCode(request.getConds_code());

            assertEquals(dto.getConds_code(), response.getConds_code());
            assertEquals(dto.getName(), response.getName());
            assertEquals(dto.getCond_code1(), response.getCond_code1());
            assertEquals(dto.getChk_cond_code1(), response.getChk_cond_code1());
            assertEquals(dto.getCond_code2(), response.getCond_code2());
            assertEquals(dto.getChk_cond_code2(), response.getChk_cond_code2());
            assertEquals(dto.getCond_code3(), response.getCond_code3());
            assertEquals(dto.getChk_cond_code3(), response.getChk_cond_code3());
            assertEquals(dto.getCond_code4(), response.getCond_code4());
            assertEquals(dto.getChk_cond_code4(), response.getChk_cond_code4());
            assertEquals(dto.getChk_use(), response.getChk_use());
        }
    }

    @DisplayName("검색 조건에 따른 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 검색_조건에_따른_조회_테스트(int cnt) {
        List<ServiceUserConditionsDto> dummy = new ArrayList<>();
        SearchCondition sc = new SearchCondition();

        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsRequest request = createRequest(i);
            ServiceUserConditionsDto dto = createDto(request);
            dummy.add(dto);
        }

        when(serviceUserConditionsDaoImpl.countBySearchCondition(sc)).thenReturn(cnt);
        when(serviceUserConditionsDaoImpl.selectBySearchCondition(sc)).thenReturn(dummy);

        PageResponse<ServiceUserConditionsResponse> response = serviceUserConditionsServiceImpl.readBySearchCondition(sc);

        assertEquals(cnt, response.getTotalCnt());
        assertEquals(cnt, response.getResponses().size());
    }

    @DisplayName("조건 항목 코드로 정책 내용 상세 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 조건_항목_코드로_정책_내용_상세_조회_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsRequest request = createRequest(i);
            ServiceUserConditionsDto dto = createDto(request);
            ServiceUserConditionsDetailDto detailDto = createDetailDto(request);

            when(serviceUserConditionsDaoImpl.existsByCondsCode(request.getConds_code())).thenReturn(true);
            when(serviceUserConditionsDaoImpl.selectForUserConditions(request.getConds_code())).thenReturn(detailDto);

            ServiceUserConditionsDetailResponse response = serviceUserConditionsServiceImpl.readByCondsCodeForUserConditions(request.getConds_code());

            assertEquals(dto.getConds_code(), response.getConds_code());
            assertEquals(dto.getName(), response.getName());
            assertEquals(dto.getCond_code1(), response.getCond_code1());
            assertEquals(dto.getChk_cond_code1(), response.getChk_cond_code1());
            assertEquals(dto.getCond_code2(), response.getCond_code2());
            assertEquals(dto.getChk_cond_code2(), response.getChk_cond_code2());
            assertEquals(dto.getCond_code3(), response.getCond_code3());
            assertEquals(dto.getChk_cond_code3(), response.getChk_cond_code3());
            assertEquals(dto.getCond_code4(), response.getCond_code4());
            assertEquals(dto.getChk_cond_code4(), response.getChk_cond_code4());
        }
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsRequest request = createRequest(i);
            ServiceUserConditionsDto dto = createDto(request);

            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            when(serviceUserConditionsDaoImpl.existsByCondsCodeForUpdate(request.getConds_code())).thenReturn(true);
            when(serviceUserConditionsDaoImpl.update(dto)).thenReturn(1);

            assertDoesNotThrow(() -> serviceUserConditionsServiceImpl.modify(request));
        }
    }

    @DisplayName("사용 가능 여부 변경 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 사용_가능_여부_변경_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsRequest request = createRequest(i);
            ServiceUserConditionsDto dto = createDto(request);

            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            when(serviceUserConditionsDaoImpl.existsByCondsCodeForUpdate(request.getConds_code())).thenReturn(true);
            when(serviceUserConditionsDaoImpl.updateChkUse(dto)).thenReturn(1);

            assertDoesNotThrow(() -> serviceUserConditionsServiceImpl.modifyChkUse(request));
        }
    }

    @DisplayName("코드로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsRequest request = createRequest(i);
            when(serviceUserConditionsDaoImpl.deleteByCondsCode(request.getConds_code())).thenReturn(1);
            assertDoesNotThrow(() -> serviceUserConditionsServiceImpl.removeByCondsCode(request.getConds_code()));
        }
    }

    @DisplayName("시퀀스로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 시퀀스로_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceUserConditionsRequest request = createRequest(i);
            ServiceUserConditionsDto dto = createDto(request);
            when(serviceUserConditionsDaoImpl.delete(dto.getSeq())).thenReturn(1);
            assertDoesNotThrow(() -> serviceUserConditionsServiceImpl.remove(dto.getSeq()));
        }
    }

    @DisplayName("모두 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 모두_삭제_테스트(int cnt) {
        when(serviceUserConditionsDaoImpl.count()).thenReturn(cnt);
        when(serviceUserConditionsDaoImpl.deleteAll()).thenReturn(cnt);
        assertDoesNotThrow(() -> serviceUserConditionsServiceImpl.removeAll());
    }

    // ===================== 예외 처리 테스트 =====================
    @Test
    @DisplayName("create() -> 중복된 키 값으로 인한 예외 발생")
    void create_중복된_키_값으로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        when(serviceUserConditionsDaoImpl.existsByCondsCode(request.getConds_code())).thenReturn(true);
        assertThrows(ServiceUserConditionsAlreadyExistsException.class, () -> serviceUserConditionsServiceImpl.create(request));
    }

    @Test
    @DisplayName("create() -> DBMS 적용 실패로 인한 예외 발생")
    void create_DBMS_적용_실패로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        ServiceUserConditionsDto dto = createDto(request);

        when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
        when(formatter.getManagerSeq()).thenReturn(reg_user_seq);

        when(serviceUserConditionsDaoImpl.existsByCondsCode(request.getConds_code())).thenReturn(false);
        when(serviceUserConditionsDaoImpl.insert(dto)).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionsServiceImpl.create(request));
    }

    @Test
    @DisplayName("readByCondsCode() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void readByCondsCode_존재하지_않는_키_값으로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        when(serviceUserConditionsDaoImpl.existsByCondsCode(request.getConds_code())).thenReturn(false);
        assertThrows(ServiceUserConditionsNotFoundException.class, () -> serviceUserConditionsServiceImpl.readByCondsCode(request.getConds_code()));
    }


    @Test
    @DisplayName("readByCondsCodeForUserConditions() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void readByCondsCodeForUserConditions_존재하지_않는_키_값으로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        when(serviceUserConditionsDaoImpl.existsByCondsCode(request.getConds_code())).thenReturn(false);
        assertThrows(ServiceUserConditionsNotFoundException.class, () -> serviceUserConditionsServiceImpl.readByCondsCodeForUserConditions(request.getConds_code()));
    }

    @Test
    @DisplayName("modify() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void modify_존재하지_않는_키_값으로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        when(serviceUserConditionsDaoImpl.existsByCondsCodeForUpdate(request.getConds_code())).thenReturn(false);
        assertThrows(ServiceUserConditionsNotFoundException.class, () -> serviceUserConditionsServiceImpl.modify(request));
    }

    @Test
    @DisplayName("modify() -> DBMS 적용 실패로 인한 예외 발생")
    void modify_DBMS_적용_실패로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        ServiceUserConditionsDto dto = createDto(request);

        when(formatter.getCurrentDateFormat()).thenReturn(up_date);
        when(formatter.getManagerSeq()).thenReturn(up_user_seq);

        when(serviceUserConditionsDaoImpl.existsByCondsCodeForUpdate(request.getConds_code())).thenReturn(true);
        when(serviceUserConditionsDaoImpl.update(dto)).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionsServiceImpl.modify(request));
    }

    @Test
    @DisplayName("modifyChkUse() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void modifyChkUse_존재하지_않는_키_값으로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        when(serviceUserConditionsDaoImpl.existsByCondsCodeForUpdate(request.getConds_code())).thenReturn(false);
        assertThrows(ServiceUserConditionsNotFoundException.class, () -> serviceUserConditionsServiceImpl.modifyChkUse(request));
    }

    @Test
    @DisplayName("modifyChkUse() -> DBMS 적용 실패로 인한 예외 발생")
    void modifyChkUse_DBMS_적용_실패로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        ServiceUserConditionsDto dto = createDto(request);

        when(formatter.getCurrentDateFormat()).thenReturn(up_date);
        when(formatter.getManagerSeq()).thenReturn(up_user_seq);

        when(serviceUserConditionsDaoImpl.existsByCondsCodeForUpdate(request.getConds_code())).thenReturn(true);
        when(serviceUserConditionsDaoImpl.updateChkUse(dto)).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionsServiceImpl.modifyChkUse(request));
    }

    @Test
    @DisplayName("removeByCondsCode() -> DBMS 적용 실패로 인한 예외 발생")
    void removeByCondsCode_DBMS_적용_실패로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        when(serviceUserConditionsDaoImpl.deleteByCondsCode(request.getConds_code())).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionsServiceImpl.removeByCondsCode(request.getConds_code()));
    }

    @Test
    @DisplayName("remove() -> DBMS 적용 실패로 인한 예외 발생")
    void remove_DBMS_적용_실패로_인한_예외_발생() {
        ServiceUserConditionsRequest request = createRequest(1);
        ServiceUserConditionsDto dto = createDto(request);
        when(serviceUserConditionsDaoImpl.delete(dto.getSeq())).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionsServiceImpl.remove(dto.getSeq()));
    }

    @Test
    @DisplayName("removeAll() -> DBMS 적용 실패로 인한 예외 발생")
    void removeAll_DBMS_적용_실패로_인한_예외_발생() {
        when(serviceUserConditionsDaoImpl.count()).thenReturn(10);
        when(serviceUserConditionsDaoImpl.deleteAll()).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserConditionsServiceImpl.removeAll());
    }


    private ServiceUserConditionsRequest createRequest(int i) {
        ServiceUserConditionsRequest request = new ServiceUserConditionsRequest();

        request.setConds_code("테스트용 코드" + i);
        request.setName("테스트용" + i);
        request.setCond_code1(condCode1);
        request.setChk_cond_code1("Y");
        request.setCond_code2(condCode2);
        request.setChk_cond_code2("Y");
        request.setCond_code3(condCode3);
        request.setChk_cond_code3("Y");
        request.setCond_code4(condCode4);
        request.setChk_cond_code4("Y");
        request.setChk_use("Y");
        request.setComt("비고");

        return request;
    }

    private ServiceUserConditionsDto createDto(ServiceUserConditionsRequest request) {
        return new ServiceUserConditionsDto(request, reg_date, reg_user_seq, up_date, up_user_seq);
    }

    private ServiceUserConditionsDetailDto createDetailDto(ServiceUserConditionsRequest request) {
        ServiceUserConditionsDetailDto detailDto = new ServiceUserConditionsDetailDto();

        detailDto.setConds_code(request.getConds_code());
        detailDto.setName(request.getName());

        detailDto.setCond_code1(request.getCond_code1());
        detailDto.setChk_cond_code1(request.getChk_cond_code1());
        detailDto.setShort_exp1("짧은 설명1");
        detailDto.setLong_exp1("긴 설명1");

        detailDto.setCond_code2(request.getCond_code2());
        detailDto.setChk_cond_code2(request.getChk_cond_code2());
        detailDto.setShort_exp2("짧은 설명2");
        detailDto.setLong_exp2("긴 설명2");

        detailDto.setCond_code3(request.getCond_code3());
        detailDto.setChk_cond_code3(request.getChk_cond_code3());
        detailDto.setShort_exp3("짧은 설명3");
        detailDto.setLong_exp3("긴 설명3");

        detailDto.setCond_code4(request.getCond_code4());
        detailDto.setChk_cond_code4(request.getChk_cond_code4());
        detailDto.setShort_exp4("짧은 설명4");
        detailDto.setLong_exp4("긴 설명4");


        return detailDto;
    }


}