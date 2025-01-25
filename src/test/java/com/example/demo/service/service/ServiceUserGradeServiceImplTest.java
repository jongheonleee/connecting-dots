package com.example.demo.service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.service.service.impl.ServiceUserGradeServiceImpl;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserGradeDto;
import com.example.demo.dto.service.ServiceUserGradeRequest;
import com.example.demo.dto.service.ServiceUserGradeResponse;
import com.example.demo.global.error.exception.business.service.ServiceUserGradeAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceUserGradeNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.service.ServiceUserGradeDaoImpl;
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
class ServiceUserGradeServiceImplTest {


    @InjectMocks
    private ServiceUserGradeServiceImpl serviceUserGradeService;

    @Mock
    private ServiceUserGradeDaoImpl serviceUserGradeDao;

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
        assertNotNull(serviceUserGradeService);
        assertNotNull(serviceUserGradeDao);
        assertNotNull(formatter);
    }

    // ===================== 지원 기능 단순 성공 테스트 =====================


    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        when(serviceUserGradeDao.count()).thenReturn(cnt);
        assertEquals(cnt, serviceUserGradeService.count());
    }

    @DisplayName("등록 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 등록_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeRequest request = createRequest(i);
            ServiceUserGradeDto dto = createDto(request);

            when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
            when(formatter.getManagerSeq()).thenReturn(reg_user_seq);

            when(serviceUserGradeDao.existsByStatCode(request.getStat_code())).thenReturn(false);
            when(serviceUserGradeDao.insert(dto)).thenReturn(1);
            when(serviceUserGradeDao.selectByStatCode(dto.getStat_code())).thenReturn(dto);

            ServiceUserGradeResponse response = serviceUserGradeService.create(request);

            assertEquals(request.getStat_code(), response.getStat_code());
            assertEquals(request.getName(), response.getName());
            assertEquals(request.getShort_exp(), response.getShort_exp());
            assertEquals(request.getLong_exp(), response.getLong_exp());
            assertEquals(request.getChk_use(), response.getChk_use());
            assertEquals(request.getComt(), response.getComt());
        }
    }

    @DisplayName("조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 조회_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeRequest request = createRequest(i);
            ServiceUserGradeDto dto = createDto(request);

            when(serviceUserGradeDao.existsByStatCode(request.getStat_code())).thenReturn(true);
            when(serviceUserGradeDao.selectByStatCode(dto.getStat_code())).thenReturn(dto);

            ServiceUserGradeResponse response = serviceUserGradeService.readByStatCode(dto.getStat_code());

            assertEquals(request.getStat_code(), response.getStat_code());
            assertEquals(request.getName(), response.getName());
            assertEquals(request.getShort_exp(), response.getShort_exp());
            assertEquals(request.getLong_exp(), response.getLong_exp());
            assertEquals(request.getChk_use(), response.getChk_use());
            assertEquals(request.getComt(), response.getComt());
        }
    }

    @DisplayName("전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_조회_테스트(int cnt) {
        List<ServiceUserGradeDto> dummy = new ArrayList<>();

        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeRequest request = createRequest(i);
            ServiceUserGradeDto dto = createDto(request);
            dummy.add(dto);
        }
        dummy.sort((a, b) -> a.getStat_code().compareTo(b.getStat_code()));
        when(serviceUserGradeDao.selectAll()).thenReturn(dummy);

        List<ServiceUserGradeResponse> responses = serviceUserGradeService.readAll();

        assertEquals(cnt, responses.size());
        for (int i=0; i<cnt; i++) {
            ServiceUserGradeDto expectedDto = dummy.get(i);
            ServiceUserGradeResponse actualResponse = responses.get(i);

            assertEquals(expectedDto.getStat_code(), actualResponse.getStat_code());
            assertEquals(expectedDto.getName(), actualResponse.getName());
            assertEquals(expectedDto.getShort_exp(), actualResponse.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualResponse.getLong_exp());
            assertEquals(expectedDto.getChk_use(), actualResponse.getChk_use());
            assertEquals(expectedDto.getComt(), actualResponse.getComt());
        }
    }

    @DisplayName("조건 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 조건_조회_테스트(int cnt) {
        SearchCondition sc = createSearchCondition();
        List<ServiceUserGradeDto> dummy = new ArrayList<>();

        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeRequest req = createRequest(i);
            ServiceUserGradeDto dto = createDto(req);
            dummy.add(dto);
        }

        dummy.sort((a, b) -> a.getStat_code().compareTo(b.getStat_code()));
        when(serviceUserGradeDao.countBySearchCondition(sc)).thenReturn(cnt);
        when(serviceUserGradeDao.selectBySearchCondition(sc)).thenReturn(dummy);

        PageResponse<ServiceUserGradeResponse> response = serviceUserGradeService.readBySearchCondition(sc);

        assertEquals(cnt, response.getTotalCnt());
        assertEquals(page, response.getPage());
        assertEquals(pageSize, response.getPageSize());

        for (int i=0; i<cnt; i++) {
            ServiceUserGradeDto expectedDto = dummy.get(i);
            ServiceUserGradeResponse actualResponse = response.getResponses().get(i);

            assertEquals(expectedDto.getStat_code(), actualResponse.getStat_code());
            assertEquals(expectedDto.getName(), actualResponse.getName());
            assertEquals(expectedDto.getShort_exp(), actualResponse.getShort_exp());
            assertEquals(expectedDto.getLong_exp(), actualResponse.getLong_exp());
            assertEquals(expectedDto.getChk_use(), actualResponse.getChk_use());
            assertEquals(expectedDto.getComt(), actualResponse.getComt());
        }
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeRequest request = createRequest(i);
            ServiceUserGradeDto dto = createDto(request);

            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            when(serviceUserGradeDao.existsByStatCodeForUpdate(request.getStat_code())).thenReturn(true);
            when(serviceUserGradeDao.update(dto)).thenReturn(1);

            assertDoesNotThrow(() -> serviceUserGradeService.modify(request));
        }
    }

    @DisplayName("코드 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 조건_삭제_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserGradeRequest request = createRequest(i);
            ServiceUserGradeDto dto = createDto(request);

            when(serviceUserGradeDao.deleteByStatCode(dto.getStat_code())).thenReturn(1);

            assertDoesNotThrow(() -> serviceUserGradeService.removeByStatCode(dto.getStat_code()));
        }
    }

    @DisplayName("전체 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_삭제_테스트(int cnt) {
        when(serviceUserGradeDao.count()).thenReturn(cnt);
        when(serviceUserGradeDao.deleteAll()).thenReturn(cnt);
        assertDoesNotThrow(() -> serviceUserGradeService.removeAll());
    }

    // ===================== 예외 처리 테스트 =====================
    @Test
    @DisplayName("create() -> 중복된 키 값으로 인한 예외 발생")
    void create_중복된_키_값으로_인한_예외_발생() {
        ServiceUserGradeRequest request = createRequest(1);
        when(serviceUserGradeDao.existsByStatCode(request.getStat_code())).thenReturn(true);
        assertThrows(ServiceUserGradeAlreadyExistsException.class, () -> serviceUserGradeService.create(request));
    }

    @Test
    @DisplayName("create() -> DBMS 적용 실패로 인한 예외 발생")
    void create_DBMS_적용_실패로_인한_예외_발생() {
        ServiceUserGradeRequest request = createRequest(1);
        ServiceUserGradeDto dto = createDto(request);

        when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
        when(formatter.getManagerSeq()).thenReturn(reg_user_seq);

        when(serviceUserGradeDao.existsByStatCode(request.getStat_code())).thenReturn(false);
        when(serviceUserGradeDao.insert(dto)).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserGradeService.create(request));
    }

    @Test
    @DisplayName("readByStatCode() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void readByStatCode_존재하지_않는_키_값으로_인한_예외_발생() {
        String stat_code = "TET0001";
        when(serviceUserGradeDao.existsByStatCode(stat_code)).thenReturn(false);
        assertThrows(ServiceUserGradeNotFoundException.class, () -> serviceUserGradeService.readByStatCode(stat_code));
    }

    @Test
    @DisplayName("modify() -> 존재하지 않는 키 값으로 인한 예외 발생")
    void modify_존재하지_않는_키_값으로_인한_예외_발생() {
        ServiceUserGradeRequest request = createRequest(1);
        when(serviceUserGradeDao.existsByStatCodeForUpdate(request.getStat_code())).thenReturn(false);
        assertThrows(ServiceUserGradeNotFoundException.class, () -> serviceUserGradeService.modify(request));
    }

    @Test
    @DisplayName("modify() -> DBMS 적용 실패로 인한 예외 발생")
    void modify_DBMS_적용_실패로_인한_예외_발생() {
        ServiceUserGradeRequest request = createRequest(1);
        ServiceUserGradeDto dto = createDto(request);

        when(formatter.getCurrentDateFormat()).thenReturn(up_date);
        when(formatter.getManagerSeq()).thenReturn(up_user_seq);

        when(serviceUserGradeDao.existsByStatCodeForUpdate(request.getStat_code())).thenReturn(true);
        when(serviceUserGradeDao.update(dto)).thenReturn(0);

        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserGradeService.modify(request));
    }


    @Test
    @DisplayName("removeByStatCode() -> DBMS 적용 실패로 인한 예외 발생")
    void removeByStatCode_DBMS_적용_실패로_인한_예외_발생() {
        String stat_code = "TET0001";
        when(serviceUserGradeDao.deleteByStatCode(stat_code)).thenReturn(5);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserGradeService.removeByStatCode(stat_code));
    }


    @Test
    @DisplayName("removeAll() -> DBMS 적용 실패로 인한 예외 발생")
    void removeAll_DBMS_적용_실패로_인한_예외_발생() {
        when(serviceUserGradeDao.count()).thenReturn(10);
        when(serviceUserGradeDao.deleteAll()).thenReturn(5);
        assertThrows(NotApplyOnDbmsException.class, () -> serviceUserGradeService.removeAll());
    }





    private ServiceUserGradeRequest createRequest(int i) {
        ServiceUserGradeRequest request = new ServiceUserGradeRequest();

        request.setStat_code("TET000" + i);
        request.setName("테스트용" + i);
        request.setOrd(i + 1);
        request.setChk_use("Y");
        request.setShort_exp("테스트용 짧은 설명" + i);
        request.setLong_exp("테스트용 긴 설명" + i);
        request.setImg("테스트용 이미지" + i);
        request.setComt("테스트용 비고" + i);

        return request;
    }

    private ServiceUserGradeDto createDto(ServiceUserGradeRequest request) {
        return new ServiceUserGradeDto(request, reg_date, reg_user_seq, up_date, up_user_seq);
    }

    private SearchCondition createSearchCondition() {
        SearchCondition sc = new SearchCondition();
        sc.setPage(page);
        sc.setPageSize(pageSize);
        sc.setSortOption("1");
        sc.setSearchOption("NM");
        sc.setSearchKeyword("테스트");
        return sc;
    }
}