package com.example.demo.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.dto.service.ServiceUserConditionDto;
import com.example.demo.dto.service.ServiceUserConditionRequest;
import com.example.demo.dto.service.ServiceUserConditionResponse;
import com.example.demo.repository.mybatis.service.ServiceUserConditionDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceUserConditionServiceImplTest {

    @InjectMocks
    private ServiceUserConditionServiceImpl serviceUserConditionService;

    @Mock
    private ServiceUserConditionDaoImpl serviceUserConditionDao;

    @Mock
    private CustomFormatter formatter;

    private final String reg_date = "2025-01-11";
    private final Integer reg_user_seq = 1;
    private final String up_date = "2025-01-11";
    private final Integer up_user_seq = 1;


    @BeforeEach
    void setUp() {
        assertNotNull(serviceUserConditionService);
        assertNotNull(serviceUserConditionDao);
        assertNotNull(formatter);
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        when(serviceUserConditionDao.count()).thenReturn(cnt);
        assertEquals(cnt, serviceUserConditionService.count());
    }

    @DisplayName("조건 항목 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 조건_항목_코드_조회_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            ServiceUserConditionDto dto = createDto(request);

            when(serviceUserConditionDao.existsByCondCode(request.getCond_code())).thenReturn(true);
            when(serviceUserConditionDao.select(request.getCond_code())).thenReturn(dto);

            ServiceUserConditionResponse response = serviceUserConditionService.readByCondCode(request.getCond_code());

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
        when(serviceUserConditionDao.selectAll()).thenReturn(dummy);

        List<ServiceUserConditionResponse> responses = serviceUserConditionService.readAll();

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
            when(serviceUserConditionDao.insert(dto)).thenReturn(1);
            when(serviceUserConditionDao.select(dto.getCond_code())).thenReturn(dto);

            ServiceUserConditionResponse response = serviceUserConditionService.create(request);

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

            when(serviceUserConditionDao.existsByCondCodeForUpdate(request.getCond_code())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);
            when(serviceUserConditionDao.update(dto)).thenReturn(1);

            serviceUserConditionService.modify(request);
        }
    }

    @DisplayName("사용 가능 여부 변경 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 사용_가능_여부_변경_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            ServiceUserConditionDto dto = createDto(request);

            when(serviceUserConditionDao.existsByCondCodeForUpdate(request.getCond_code())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);
            when(serviceUserConditionDao.updateChkUse(dto)).thenReturn(1);

            assertDoesNotThrow(() -> serviceUserConditionService.modifyChkUse(request));
        }
    }

    @DisplayName("삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 삭제_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionRequest request = createRequest(i);
            when(serviceUserConditionDao.delete(request.getCond_code())).thenReturn(1);
            assertDoesNotThrow(() -> serviceUserConditionService.remove(request.getCond_code()));
        }
    }

    @DisplayName("모두 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 모두_삭제_테스트(int cnt) {
        when(serviceUserConditionDao.count()).thenReturn(cnt);
        when(serviceUserConditionDao.deleteAll()).thenReturn(cnt);
        assertDoesNotThrow(() -> serviceUserConditionService.removeAll());
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