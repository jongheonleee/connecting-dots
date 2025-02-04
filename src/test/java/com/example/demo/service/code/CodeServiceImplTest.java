package com.example.demo.service.code;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.domain.Code;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.code.CodeRequest;
import com.example.demo.dto.code.CodeResponse;
import com.example.demo.repository.code.impl.CommonCodeDaoImpl;
import com.example.demo.service.code.impl.CommonCodeServiceImpl;
import com.example.demo.utils.CustomFormatter;
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
class CodeServiceImplTest {

    @InjectMocks
    private CommonCodeServiceImpl codeServiceImpl;

    @Mock
    private CommonCodeDaoImpl codeDaoImpl;

    @Mock
    private CustomFormatter formatter;

    @BeforeEach
    void setUp() {
        assertNotNull(codeServiceImpl);
        assertNotNull(codeDaoImpl);
        assertNotNull(formatter);
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        when(codeDaoImpl.count()).thenReturn(cnt);
        int totalCnt = codeServiceImpl.count();
        assertEquals(cnt, totalCnt);
    }

    @Test
    @DisplayName("상위 코드로 조회 테스트")
    void 상위_코드로_조회_테스트() {
        // given
        String top_code = "100";
        List<CodeDto> dummy = List.of(
            new CodeDto(1, 1, "101", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1),
            new CodeDto(2, 2, "102", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1),
            new CodeDto(3, 3, "103", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1)
        );
        when(codeDaoImpl.selectByTopCode(top_code)).thenReturn(dummy);

        // when
        List<CodeResponse> expectedResponses = dummy.stream().map(CodeResponse::new).toList();
        var responses = codeServiceImpl.readByTopCode(top_code);

        // then
        assertNotNull(responses);
        assertEquals(expectedResponses.size(), responses.size());

        for (int i = 0; i < expectedResponses.size(); i++) {
            assertEquals(expectedResponses.get(i).getSeq(), responses.get(i).getSeq());
            assertEquals(expectedResponses.get(i).getCode(), responses.get(i).getCode());
            assertEquals(expectedResponses.get(i).getName(), responses.get(i).getName());
            assertEquals(expectedResponses.get(i).getLevel(), responses.get(i).getLevel());
        }
    }

    @Test
    @DisplayName("코드로 조회 테스트")
    void 코드로_조회_테스트() {
        // given
        String code = "100";
        CodeDto dto = new CodeDto(1, 1, code, "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1);
        when(codeDaoImpl.selectByCode(code)).thenReturn(dto);

        // when
        CodeResponse expectedResponse = new CodeResponse(dto);
        var response = codeServiceImpl.readByCode(code);

        // then
        assertNotNull(response);
        assertEquals(expectedResponse.getSeq(), response.getSeq());
        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getName(), response.getName());
        assertEquals(expectedResponse.getLevel(), response.getLevel());
    }

    @Test
    @DisplayName("시퀀스로 조회 테스트")
    void 시퀀스로_조회_테스트() {
        // given
        String code = "100";
        CodeDto dto = new CodeDto(1, 1, code, "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1);
        when(codeDaoImpl.selectBySeq(dto.getSeq())).thenReturn(dto);

        // when
        CodeResponse expectedResponse = new CodeResponse(dto);
        var response = codeServiceImpl.readBySeq(dto.getSeq());

        // then
        assertNotNull(response);
        assertEquals(expectedResponse.getSeq(), response.getSeq());
        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getName(), response.getName());
        assertEquals(expectedResponse.getLevel(), response.getLevel());
    }

    @Test
    @DisplayName("전체 조회 테스트")
    void 전체_조회_테스트() {
        // given
        List<CodeDto> dummy = List.of(
            new CodeDto(1, 1, "101", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1),
            new CodeDto(2, 2, "102", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1),
            new CodeDto(3, 3, "103", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1)
        );
        when(codeDaoImpl.selectAll()).thenReturn(dummy);

        // when
        List<CodeResponse> expectedResponses = dummy.stream().map(CodeResponse::new).toList();
        var responses = codeServiceImpl.readAll();

        // then
        assertNotNull(responses);
        assertEquals(expectedResponses.size(), responses.size());

        for (int i = 0; i < expectedResponses.size(); i++) {
            assertEquals(expectedResponses.get(i).getSeq(), responses.get(i).getSeq());
            assertEquals(expectedResponses.get(i).getCode(), responses.get(i).getCode());
            assertEquals(expectedResponses.get(i).getName(), responses.get(i).getName());
            assertEquals(expectedResponses.get(i).getLevel(), responses.get(i).getLevel());
        }
    }

    @Test
    @DisplayName("코드 생성 테스트")
    void 코드_생성_테스트() {
        // given
        String currentDateFormat = "2025/01/09";
        Integer managerSeq = 1;

        CodeRequest request = new CodeRequest(1, "1001", "회원가입", "Y", "1000");
        CodeDto dto = new CodeDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);

        when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
        when(formatter.getManagerSeq()).thenReturn(managerSeq);
        when(codeDaoImpl.insert(dto)).thenReturn(1);
        when(codeDaoImpl.selectBySeq(dto.getSeq())).thenReturn(dto);

        // when
        assertDoesNotThrow(() -> codeServiceImpl.create(request));
    }

    @Test
    @DisplayName("코드 수정 테스트")
    void 코드_수정_테스트() {
        // given
        String currentDateFormat = "2025/01/09";
        Integer managerSeq = 1;

        CodeRequest request = new CodeRequest(1, "1001", "회원가입", "Y", "1000");
        CodeDto dto = new CodeDto(request, currentDateFormat, managerSeq, currentDateFormat, managerSeq);

        when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
        when(formatter.getManagerSeq()).thenReturn(managerSeq);
        when(codeDaoImpl.update(dto)).thenReturn(1);

        // when
        assertDoesNotThrow(() -> codeServiceImpl.modify(request));
    }

    @Test
    @DisplayName("사용여부 수정 테스트")
    void 사용여부_수정_테스트() {
        // given
        String currentDateFormat = "2025/01/09";
        Integer managerSeq = 1;

        Code code = Code.of("1001");
        CodeRequest request = new CodeRequest(code.getLevel(), code.getCode(), code.getName(), "N", code.getTopCode());
        CodeDto dto = code.toDto("N", currentDateFormat, managerSeq, currentDateFormat, managerSeq);

        when(formatter.getCurrentDateFormat()).thenReturn(currentDateFormat);
        when(formatter.getManagerSeq()).thenReturn(managerSeq);
        when(codeDaoImpl.updateUse(dto)).thenReturn(1);

        // when
        assertDoesNotThrow(() -> codeServiceImpl.modifyUse(request));
    }

    @Test
    @DisplayName("레벨로 삭제 테스트")
    void 레벨로_삭제_테스트() {
        // given
        int level = 1;
        when(codeDaoImpl.deleteByLevel(level)).thenReturn(1);

        // when
        assertDoesNotThrow(() -> codeServiceImpl.removeByLevel(level));
    }

    @Test
    @DisplayName("코드로 삭제 테스트")
    void 코드로_삭제_테스트() {
        // given
        String code = "100";
        when(codeDaoImpl.deleteByCode(code)).thenReturn(1);

        // when
        assertDoesNotThrow(() -> codeServiceImpl.removeByCode(code));
    }

    @Test
    @DisplayName("시퀀스로 삭제 테스트")
    void 시퀀스로_삭제_테스트() {
        // given
        int seq = 1;
        when(codeDaoImpl.delete(seq)).thenReturn(1);

        // when
        assertDoesNotThrow(() -> codeServiceImpl.removeBySeq(seq));
    }

    @Test
    @DisplayName("코드 삭제 테스트")
    void 코드_삭제_테스트() {
        // given
        int seq = 1;
        when(codeDaoImpl.delete(seq)).thenReturn(1);

        // when
        assertDoesNotThrow(() -> codeServiceImpl.removeBySeq(seq));
    }

    @Test
    @DisplayName("코드 모두 삭제 테스트")
    void 코드_모두_삭제_테스트() {
        // given
        when(codeDaoImpl.deleteByLevel(any())).thenReturn(1);
        when(codeDaoImpl.count()).thenReturn(0);

        // when
        assertDoesNotThrow(() -> codeServiceImpl.removeAll());
    }

    @Test
    @DisplayName("코드 페이지네이션 조회 테스트")
    void 코드_페이지네이션_조회_테스트() {
        // given
        SearchCondition sc = createSearchCondition(1, 10, "NM", "테스트용", "1");
        List<CodeDto> dummy = List.of(
            new CodeDto(1, 1, "101", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1),
            new CodeDto(2, 2, "102", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1),
            new CodeDto(3, 3, "103", "테스트용", "Y", "100", "2025/01/05", 1, "2025/01/05", 1)
        );
        when(codeDaoImpl.selectBySearchCondition(any())).thenReturn(dummy);

        // when
        List<CodeResponse> expectedResponses = dummy.stream().map(CodeResponse::new).toList();
        PageResponse<CodeResponse> responses = codeServiceImpl.readBySearchCondition(sc);

        // then
        assertNotNull(responses);
        assertEquals(expectedResponses.size(), responses.getResponses().size());

        for (int i = 0; i < expectedResponses.size(); i++) {
            CodeResponse response = responses.getResponses().get(i);
            assertEquals(expectedResponses.get(i).getSeq(), response.getSeq());
            assertEquals(expectedResponses.get(i).getCode(), response.getCode());
            assertEquals(expectedResponses.get(i).getName(), response.getName());
            assertEquals(expectedResponses.get(i).getLevel(), response.getLevel());
        }
    }

    private SearchCondition createSearchCondition(int page, int pageSize, String searchOption, String searchKeyword, String sortOption) {
        return new SearchCondition(page, pageSize, searchOption, searchKeyword, sortOption);
    }



}