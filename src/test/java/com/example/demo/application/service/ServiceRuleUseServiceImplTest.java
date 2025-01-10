package com.example.demo.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.dto.service.ServiceRuleUseRequest;
import com.example.demo.dto.service.ServiceRuleUseResponse;
import com.example.demo.repository.mybatis.service.ServiceRuleUseDaoImpl;
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
class ServiceRuleUseServiceImplTest {

    @InjectMocks
    private ServiceRuleUseServiceImpl serviceRuleUseService;

    @Mock
    private ServiceRuleUseDaoImpl serviceRuleUseDao;

    @Mock
    private CustomFormatter formatter;

    private final String currentDateFormat = "2025/01/09";
    private final Integer managerSeq = 1;

    @BeforeEach
    void setUp() {
        assertNotNull(serviceRuleUseService);
        assertNotNull(serviceRuleUseDao);
        assertNotNull(formatter);
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        when(serviceRuleUseDao.count()).thenReturn(cnt);
        int totalCnt = serviceRuleUseService.count();
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("코드로 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"100", "200", "300", "400", "500"})
    void 코드로_카운팅_테스트(String code) {
        when(serviceRuleUseDao.countByCode(code)).thenReturn(5);
        int totalCnt = serviceRuleUseService.countByCode(code);
        assertEquals(5, totalCnt);
    }

    @DisplayName("서비스 룰 사용 생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_룰_사용_생성_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            // given
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);
            when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
            when(formatter.getManagerSeq()).thenReturn(managerSeq);
            when(serviceRuleUseDao.insert(dto)).thenReturn(1);
            when(serviceRuleUseDao.selectByRuleStat(dto.getRule_stat())).thenReturn(dto);

            // when
            ServiceRuleUseResponse response = serviceRuleUseService.create(request);

            // then
            assertEquals(dto.getRule_stat(), response.getRule_stat());
            assertEquals(dto.getName(), response.getName());
            assertEquals(dto.getTar_name(), response.getTar_name());
            assertEquals(dto.getOp1(), response.getOp1());
            assertEquals(dto.getVal1(), response.getVal1());
            assertEquals(dto.getChk_use(), response.getChk_use());
        }
    }

    @DisplayName("서비스 룰 사용 조회 테스트")
    @Test
    void 서비스_룰_사용_조회_테스트() {
        // given
        ServiceRuleUseRequest request = createRequest(1);
        String rule_stat = request.getRule_stat();

        ServiceRuleUseDto dto = new ServiceRuleUseDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);
        when(serviceRuleUseDao.selectByRuleStat(rule_stat)).thenReturn(dto);

        // when
        ServiceRuleUseResponse response = serviceRuleUseService.readByRuleStat(rule_stat);

        // then
        assertEquals(dto.getRule_stat(), response.getRule_stat());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getTar_name(), response.getTar_name());
        assertEquals(dto.getOp1(), response.getOp1());
        assertEquals(dto.getVal1(), response.getVal1());
        assertEquals(dto.getChk_use(), response.getChk_use());
    }

    @DisplayName("코드로 서비스 룰 사용 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_서비스_룰_사용_조회_테스트(int cnt) {
        // given
        List<ServiceRuleUseDto> dummy = new ArrayList<>();
        String code = "2002";

        for (int i=0; i<cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);
            dummy.add(dto);
        }

        // when
        List<ServiceRuleUseResponse> expectedResponses = dummy.stream()
                                                            .map(ServiceRuleUseResponse::new)
                                                            .toList();
        when(serviceRuleUseDao.selectByCode(code)).thenReturn(dummy);
        List<ServiceRuleUseResponse> responses = serviceRuleUseService.readByCode(code);

        // then
        assertEquals(expectedResponses.size(), responses.size());
        for (int i=0; i<cnt; i++) {
            assertEquals(expectedResponses.get(i).getRule_stat(), responses.get(i).getRule_stat());
            assertEquals(expectedResponses.get(i).getName(), responses.get(i).getName());
            assertEquals(expectedResponses.get(i).getTar_name(), responses.get(i).getTar_name());
            assertEquals(expectedResponses.get(i).getOp1(), responses.get(i).getOp1());
            assertEquals(expectedResponses.get(i).getVal1(), responses.get(i).getVal1());
        }
    }

    @DisplayName("전체 서비스 룰 사용 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_서비스_룰_사용_조회_테스트(int cnt) {
        // given
        List<ServiceRuleUseDto> dummy = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);
            dummy.add(dto);
        }

        // when
        List<ServiceRuleUseResponse> expectedResponses = dummy.stream()
                                                            .map(ServiceRuleUseResponse::new)
                                                            .toList();
        when(serviceRuleUseDao.selectAll()).thenReturn(dummy);
        List<ServiceRuleUseResponse> responses = serviceRuleUseService.readAll();

        // then
        assertEquals(expectedResponses.size(), responses.size());
        for (int i=0; i<cnt; i++) {
            assertEquals(expectedResponses.get(i).getRule_stat(), responses.get(i).getRule_stat());
            assertEquals(expectedResponses.get(i).getName(), responses.get(i).getName());
            assertEquals(expectedResponses.get(i).getTar_name(), responses.get(i).getTar_name());
            assertEquals(expectedResponses.get(i).getOp1(), responses.get(i).getOp1());
            assertEquals(expectedResponses.get(i).getVal1(), responses.get(i).getVal1());
        }
    }

    @DisplayName("서비스 룰 사용 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_룰_사용_수정_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            // given
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);
            when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
            when(formatter.getManagerSeq()).thenReturn(managerSeq);
            when(serviceRuleUseDao.update(dto)).thenReturn(1);

            // when
            assertDoesNotThrow(() -> serviceRuleUseService.modify(request));
        }
    }

    @DisplayName("서비스 룰 사용 사용여부 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_룰_사용_사용여부_수정_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            // given
            ServiceRuleUseRequest request = createRequest(i);
            ServiceRuleUseDto dto = new ServiceRuleUseDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);
            when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
            when(formatter.getManagerSeq()).thenReturn(managerSeq);
            when(serviceRuleUseDao.updateUse(dto)).thenReturn(1);

            // when
            assertDoesNotThrow(() -> serviceRuleUseService.modifyChkUse(request));
        }
    }

    @DisplayName("서비스 룰 사용 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 서비스_룰_사용_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            // given
            ServiceRuleUseRequest request = createRequest(i);
            when(serviceRuleUseDao.deleteByRuleStat(request.getRule_stat())).thenReturn(1);

            // when
            assertDoesNotThrow(() -> serviceRuleUseService.removeByRuleStat(request.getRule_stat()));
        }
    }

    @DisplayName("전체 서비스 룰 사용 삭제 테스트")
    @Test
    void 전체_서비스_룰_사용_삭제_테스트() {
        // given
        when(serviceRuleUseDao.count()).thenReturn(20);
        when(serviceRuleUseDao.deleteAll()).thenReturn(20);

        // when
        assertDoesNotThrow(() -> serviceRuleUseService.removeAll());
    }

    @DisplayName("코드로 전체 서비스 룰 사용 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_전체_서비스_룰_사용_삭제_테스트(int cnt) {
        // given
        String code = "2002";
        when(serviceRuleUseDao.countByCode(code)).thenReturn(cnt);
        when(serviceRuleUseDao.deleteByCode(code)).thenReturn(cnt);

        // when
        assertDoesNotThrow(() -> serviceRuleUseService.removeByCode(code));
    }

    @Test
    @DisplayName("코드 페이지네이션 조회 테스트")
    void 코드_페이지네이션_조회_테스트() {
        // given
        SearchCondition sc = createSearchCondition(1, 10, "NM", "테스트용", "1");
        List<ServiceRuleUseDto> dummy = List.of(
                new ServiceRuleUseDto(createRequest(1), "2025/01/05", 1, "2025/01/05", 1),
                new ServiceRuleUseDto(createRequest(2), "2025/01/05", 1, "2025/01/05", 1),
                new ServiceRuleUseDto(createRequest(3), "2025/01/05", 1, "2025/01/05", 1)
        );
        when(serviceRuleUseDao.selectBySearchCondition(any())).thenReturn(dummy);

        // when
        List<ServiceRuleUseResponse> expectedResponses = dummy.stream()
                                                            .map(ServiceRuleUseResponse::new)
                                                            .toList();
        PageResponse<ServiceRuleUseResponse> responses = serviceRuleUseService.readBySearchCondition(sc);

        // then
        assertNotNull(responses);
        assertEquals(expectedResponses.size(), responses.getResponses().size());

        for (int i = 0; i < expectedResponses.size(); i++) {
            ServiceRuleUseResponse response = responses.getResponses().get(i);
            assertEquals(expectedResponses.get(i).getRule_stat(), response.getRule_stat());
            assertEquals(expectedResponses.get(i).getName(), response.getName());
            assertEquals(expectedResponses.get(i).getTar_name(), response.getTar_name());
        }
    }

    private ServiceRuleUseRequest createRequest(int i) {
        ServiceRuleUseRequest request = new ServiceRuleUseRequest();
        request.setRule_stat("SA000" + i);
        request.setName("테스트용");
        request.setTar_name("orig_cnt");
        request.setOp1(">=");
        request.setVal1("1");
        request.setChk_use("Y");
        request.setCode("2002");
        return request;
    }

    private SearchCondition createSearchCondition(int page, int pageSize, String searchOption, String searchKeyword, String sortOption) {
        return new SearchCondition(page, pageSize, searchOption, searchKeyword, sortOption);
    }

}