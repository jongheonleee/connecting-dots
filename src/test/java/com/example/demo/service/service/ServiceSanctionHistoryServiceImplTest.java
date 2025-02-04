package com.example.demo.service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.repository.service.ServiceSanctionHistoryRepository;
import com.example.demo.service.service.impl.ServiceSanctionHistoryServiceImpl;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceSanctionHistoryDto;
import com.example.demo.dto.service.ServiceSanctionHistoryRequest;
import com.example.demo.dto.service.ServiceSanctionHistoryResponse;
import com.example.demo.global.error.exception.business.service.ServiceSanctionHistoryAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceSanctionHistoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
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
class ServiceSanctionHistoryServiceImplTest {


    @InjectMocks
    private ServiceSanctionHistoryServiceImpl serviceSanctionHistoryService;

    @Mock
    private ServiceSanctionHistoryRepository serviceSanctionHistoryDao;

    @Mock
    private CustomFormatter formatter;

    private final String poli_stat = "PS100";

    private final String appl_begin = "2025-01-01 00:00:00";
    private final String appl_end = "2025-02-01 00:00:00";

    private final String currentDateFormat = "2025-01-08 00:00:00";
    private final Integer managerSeq = 1;

    private final Integer page = 1;
    private final Integer pageSize = 10;

    // ===================== 지원 기능 단순 성공 테스트 =====================

    @BeforeEach
    void setUp() {
        assertNotNull(serviceSanctionHistoryService);
        assertNotNull(serviceSanctionHistoryDao);
        assertNotNull(formatter);
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        when(serviceSanctionHistoryDao.count()).thenReturn(cnt);
        assertEquals(cnt, serviceSanctionHistoryService.count());
    }

    @DisplayName("유저 시퀀스로 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 유저_시퀀스로_카운팅_테스트(int cnt) {
        Integer user_seq = 1;
        when(serviceSanctionHistoryDao.countByUserSeq(user_seq)).thenReturn(cnt);
        assertEquals(cnt, serviceSanctionHistoryService.countByUserSeq(user_seq));
    }

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryRequest request = createRequest(i);
            ServiceSanctionHistoryDto dto = createDto(request);

            when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
            when(formatter.getManagerSeq()).thenReturn(managerSeq);

            when(serviceSanctionHistoryDao.insert(dto)).thenReturn(1);
            when(serviceSanctionHistoryDao.selectBySeq(dto.getSeq())).thenReturn(dto);

            ServiceSanctionHistoryResponse response = serviceSanctionHistoryService.create(request);

            assertEquals(request.getPoli_stat(), response.getPoli_stat());
            assertEquals(request.getShort_exp(), response.getShort_exp());
            assertEquals(request.getLong_exp(), response.getLong_exp());
            assertEquals(request.getComt(), response.getComt());
            assertEquals(request.getAppl_begin(), response.getAppl_begin());
            assertEquals(request.getAppl_end(), response.getAppl_end());
            assertEquals(request.getUser_seq(), response.getUser_seq());
        }
    }

    @DisplayName("유저 시퀀스로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 유저_시퀀스로_조회_테스트(int cnt) {
        Integer user_seq = 1;
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryRequest request = createRequest(i);
            request.setUser_seq(user_seq);
            ServiceSanctionHistoryDto dto = createDto(request);
            dto.setSeq(i);

            dummy.add(dto);
        }

        when(serviceSanctionHistoryDao.selectByUserSeq(user_seq)).thenReturn(dummy);

        List<ServiceSanctionHistoryResponse> responses = serviceSanctionHistoryService.readByUserSeq(user_seq);

        assertEquals(cnt, responses.size());
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto expectedDto = dummy.get(i);
            ServiceSanctionHistoryResponse actualResponse = responses.get(i);

            assertEquals(expectedDto.getPoli_stat(), actualResponse.getPoli_stat());
            assertEquals(expectedDto.getShort_exp(), actualResponse.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualResponse.getLong_exp());
            assertEquals(expectedDto.getComt(), actualResponse.getComt());
            assertEquals(expectedDto.getAppl_begin(), actualResponse.getAppl_begin());
            assertEquals(expectedDto.getAppl_end(), actualResponse.getAppl_end());
            assertEquals(expectedDto.getUser_seq(), actualResponse.getUser_seq());
        }
    }

    @DisplayName("시퀀스로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 시퀀스로_조회_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryRequest request = createRequest(i);
            ServiceSanctionHistoryDto dto = createDto(request);
            dto.setSeq(i);

            when(serviceSanctionHistoryDao.existsBySeq(i)).thenReturn(true);
            when(serviceSanctionHistoryDao.selectBySeq(i)).thenReturn(dto);

            ServiceSanctionHistoryResponse response = serviceSanctionHistoryService.readBySeq(i);

            assertEquals(dto.getPoli_stat(), response.getPoli_stat());
            assertEquals(dto.getShort_exp(), response.getShort_exp());
            assertEquals(dto.getLong_exp(), response.getLong_exp());
            assertEquals(dto.getComt(), response.getComt());
            assertEquals(dto.getAppl_begin(), response.getAppl_begin());
            assertEquals(dto.getAppl_end(), response.getAppl_end());
            assertEquals(dto.getUser_seq(), response.getUser_seq());
        }
    }

    @DisplayName("조건으로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 조건으로_조회_테스트(int cnt) {
        SearchCondition sc = createSearchCondition();
        List<ServiceSanctionHistoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryRequest request = createRequest(i);
            ServiceSanctionHistoryDto dto = createDto(request);
            dto.setSeq(i);

            dummy.add(dto);
        }

        when(serviceSanctionHistoryDao.countBySearchCondition(sc)).thenReturn(cnt);
        when(serviceSanctionHistoryDao.selectBySearchCondition(sc)).thenReturn(dummy);

        List<ServiceSanctionHistoryResponse> responses = serviceSanctionHistoryService.readBySearchCondition(sc).getResponses();

        assertEquals(cnt, responses.size());
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryDto expectedDto = dummy.get(i);
            ServiceSanctionHistoryResponse actualResponse = responses.get(i);

            assertEquals(expectedDto.getPoli_stat(), actualResponse.getPoli_stat());
            assertEquals(expectedDto.getShort_exp(), actualResponse.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualResponse.getLong_exp());
            assertEquals(expectedDto.getComt(), actualResponse.getComt());
            assertEquals(expectedDto.getAppl_begin(), actualResponse.getAppl_begin());
            assertEquals(expectedDto.getAppl_end(), actualResponse.getAppl_end());
            assertEquals(expectedDto.getUser_seq(), actualResponse.getUser_seq());
        }
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryRequest request = createRequest(i);
            ServiceSanctionHistoryDto dto = createDto(request);
            dto.setSeq(i);

            when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
            when(formatter.getManagerSeq()).thenReturn(managerSeq);

            when(serviceSanctionHistoryDao.existsBySeqForUpdate(request.getSeq())).thenReturn(true);
            when(serviceSanctionHistoryDao.update(any())).thenReturn(1);

            assertDoesNotThrow(() -> serviceSanctionHistoryService.modify(request));
        }
    }

    @DisplayName("삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            when(serviceSanctionHistoryDao.existsBySeqForUpdate(i)).thenReturn(true);
            when(serviceSanctionHistoryDao.deleteBySeq(i)).thenReturn(1);

            serviceSanctionHistoryService.remove(i);
        }
    }

    @DisplayName("유저 시퀀스로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 유저_시퀀스로_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            Integer user_seq = i;
            when(serviceSanctionHistoryDao.countByUserSeq(user_seq)).thenReturn(cnt);
            when(serviceSanctionHistoryDao.deleteByUserSeq(user_seq)).thenReturn(cnt);

            serviceSanctionHistoryService.removeByUserSeq(user_seq);
        }
    }

    @DisplayName("전체 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_삭제_테스트(int cnt) {
        when(serviceSanctionHistoryDao.count()).thenReturn(cnt);
        when(serviceSanctionHistoryDao.deleteAll()).thenReturn(cnt);

        serviceSanctionHistoryService.removeAll();
    }

    // ===================== 예외 처리 테스트 =====================
    @Test
    @DisplayName("create() -> 중복된 키 값으로 인한 예외 발생")
    void create_중복된_키_값으로_인한_예외_발생() {
        ServiceSanctionHistoryRequest request = createRequest(1);
        when(serviceSanctionHistoryDao.existsByPoliStat(request.getPoli_stat())).thenReturn(true);
        assertThrows(ServiceSanctionHistoryAlreadyExistsException.class, () -> serviceSanctionHistoryService.create(request));
    }

    @Test
    @DisplayName("create() -> DBMS 반영되지 않아서 예외 발생")
    void create_DBMS_반영되지_않아서_예외_발생() {
        ServiceSanctionHistoryRequest request = createRequest(1);
        ServiceSanctionHistoryDto dto = createDto(request);

        when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
        when(formatter.getManagerSeq()).thenReturn(managerSeq);

        when(serviceSanctionHistoryDao.insert(dto)).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceSanctionHistoryService.create(request));
    }

    @Test
    @DisplayName("readBySeq() -> 존재하지 않는 seq로 조회하여 예외 발생")
    void readBySeq_존재하지_않는_seq로_조회하여_예외_발생() {
        Integer seq = 1;
        when(serviceSanctionHistoryDao.existsBySeq(seq)).thenReturn(false);
        assertThrows(ServiceSanctionHistoryNotFoundException.class, () -> serviceSanctionHistoryService.readBySeq(seq));
    }

    @Test
    @DisplayName("modify() -> 존재하지 않는 seq로 수정하여 예외 발생")
    void modify_존재하지_않는_seq로_수정하여_예외_발생() {
        ServiceSanctionHistoryRequest request = createRequest(1);
        when(serviceSanctionHistoryDao.existsBySeqForUpdate(request.getSeq())).thenReturn(false);
        assertThrows(ServiceSanctionHistoryNotFoundException.class, () -> serviceSanctionHistoryService.modify(request));
    }

    @Test
    @DisplayName("modify() -> DBMS 반영되지 않아서 예외 발생")
    void modify_DBMS_반영되지_않아서_예외_발생() {
        ServiceSanctionHistoryRequest request = createRequest(1);
        ServiceSanctionHistoryDto dto = createDto(request);

        when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
        when(formatter.getManagerSeq()).thenReturn(managerSeq);

        when(serviceSanctionHistoryDao.existsBySeqForUpdate(request.getSeq())).thenReturn(true);
        when(serviceSanctionHistoryDao.update(any())).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceSanctionHistoryService.modify(request));
    }

    @Test
    @DisplayName("remove() -> 존재하지 않는 seq로 삭제하여 예외 발생")
    void remove_존재하지_않는_seq로_삭제하여_예외_발생() {
        Integer seq = 1;
        when(serviceSanctionHistoryDao.existsBySeqForUpdate(seq)).thenReturn(false);
        assertThrows(ServiceSanctionHistoryNotFoundException.class, () -> serviceSanctionHistoryService.remove(seq));
    }


    @Test
    @DisplayName("removeAll() -> DBMS 반영되지 않아서 예외 발생")
    void removeAll_DBMS_반영되지_않아서_예외_발생() {
        when(serviceSanctionHistoryDao.count()).thenReturn(10);
        when(serviceSanctionHistoryDao.deleteAll()).thenReturn(0);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceSanctionHistoryService.removeAll());
    }



    private ServiceSanctionHistoryRequest createRequest(int i) {
        ServiceSanctionHistoryRequest request = new ServiceSanctionHistoryRequest();

        request.setPoli_stat(poli_stat + i);
        request.setShort_exp("짧은 설명");
        request.setLong_exp("긴 설명");
        request.setComt("비고란");
        request.setAppl_begin(appl_begin);
        request.setAppl_end(appl_end);
        request.setUser_seq(i);

        return request;
    }

    private ServiceSanctionHistoryDto createDto(ServiceSanctionHistoryRequest request) {
        return new ServiceSanctionHistoryDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);
    }


    private SearchCondition createSearchCondition() {
        SearchCondition sc = new SearchCondition();
        sc.setPage(page);
        sc.setPageSize(pageSize);
        return sc;
    }


}