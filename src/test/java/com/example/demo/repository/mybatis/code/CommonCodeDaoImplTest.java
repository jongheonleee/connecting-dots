package com.example.demo.repository.mybatis.code;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.code.CodeResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class CommonCodeDaoImplTest {

    private final Integer MAX_LEVEL = 3;

    @Autowired
    private CommonCodeDaoImpl commonCodeDaoImpl;

    @BeforeEach
    void setUp() {
        assertNotNull(commonCodeDaoImpl);
        for (int i=MAX_LEVEL; i>=0; i--) {
            commonCodeDaoImpl.deleteByLevel(i);
        }
    }


    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeDaoImpl.insert(request));
        }

        int totalCnt = commonCodeDaoImpl.count();
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("상위 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 상위_코드로_조회_테스트(int cnt) {
        String top_code = "100";
        CodeDto request = createRequest(top_code, "테스트용", null);
        assertEquals(1, commonCodeDaoImpl.insert(request));

        for (int i=1; i<=cnt; i++) {
            request = createRequest("100" + i, "테스트용" + i, top_code);
            assertEquals(1, commonCodeDaoImpl.insert(request));
        }


        List<CodeDto> responses = commonCodeDaoImpl.selectByTopCode(top_code);
        assertEquals(cnt, responses.size());
        responses.stream().forEach(response -> {
            assertEquals(top_code, response.getTop_code());
        });
    }

    @Test
    @DisplayName("코드로 조회 테스트")
    void 코드로_조회_테스트() {
        CodeDto request = createRequest("100", "테스트용", null);
        assertEquals(1, commonCodeDaoImpl.insert(request));

        CodeDto response = commonCodeDaoImpl.selectByCode("100");
        assertNotNull(response);
        assertEquals(request.getCode(), response.getCode());
        assertEquals(request.getLevel(), response.getLevel());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getTop_code(), response.getTop_code());
    }

    @Test
    @DisplayName("시퀀스로 조회 테스트")
    void 시퀀스로_조회_테스트() {
        CodeDto request = createRequest("100", "테스트용", null);
        assertEquals(1, commonCodeDaoImpl.insert(request));
        CodeDto response = commonCodeDaoImpl.selectByCode("100");
        response = commonCodeDaoImpl.selectBySeq(response.getSeq());

        assertNotNull(response);
        assertEquals(request.getCode(), response.getCode());
        assertEquals(request.getLevel(), response.getLevel());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getTop_code(), response.getTop_code());
    }

    @DisplayName("전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_조회_테스트(int cnt) {
        List<CodeDto> dummy = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            dummy.add(request);
            assertEquals(1, commonCodeDaoImpl.insert(request));
        }

        List<CodeDto> responses = commonCodeDaoImpl.selectAll();

        // 정렬로 데이터 순서 보장
        dummy.sort((a, b) -> a.getCode().compareTo(b.getCode()));
        responses.sort((a, b) -> a.getCode().compareTo(b.getCode()));

        assertEquals(cnt, responses.size());
        for (int i=0; i<cnt; i++) {
            assertEquals(dummy.get(i).getCode(), responses.get(i).getCode());
            assertEquals(dummy.get(i).getName(), responses.get(i).getName());
            assertEquals(dummy.get(i).getTop_code(), responses.get(i).getTop_code());
        }
    }

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeDaoImpl.insert(request));

            CodeDto response = commonCodeDaoImpl.selectByCode("100" + i);
            assertEquals(request.getCode(), response.getCode());
            assertEquals(request.getLevel(), response.getLevel());
            assertEquals(request.getName(), response.getName());
            assertEquals(request.getTop_code(), response.getTop_code());
        }
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeDaoImpl.insert(request));

            CodeDto response = commonCodeDaoImpl.selectByCode("100" + i);

            CodeDto updateRequest = createRequest(response.getCode(), "수정된 테스트용" + i, response.getTop_code());
            updateRequest.setSeq(response.getSeq());

            assertEquals(1, commonCodeDaoImpl.update(updateRequest));

            CodeDto updatedResponse = commonCodeDaoImpl.selectByCode("100" + i);
            assertEquals(updateRequest.getCode(), updatedResponse.getCode());
            assertEquals(updateRequest.getName(), updatedResponse.getName());
            assertEquals(updateRequest.getChk_use(), updatedResponse.getChk_use());
        }
    }

    @DisplayName("사용여부 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 사용여부_수정_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeDaoImpl.insert(request));

            CodeDto response = commonCodeDaoImpl.selectByCode("100" + i);

            CodeDto updateRequest = createRequest(response.getCode(), response.getName(), response.getTop_code());
            updateRequest.setSeq(response.getSeq());
            updateRequest.setChk_use("N");

            assertEquals(1, commonCodeDaoImpl.updateUse(updateRequest));

            CodeDto updatedResponse = commonCodeDaoImpl.selectByCode("100" + i);
            assertEquals(updateRequest.getCode(), updatedResponse.getCode());
            assertEquals(updateRequest.getName(), updatedResponse.getName());
            assertEquals(updateRequest.getChk_use(), updatedResponse.getChk_use());
        }
    }

    @DisplayName("삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeDaoImpl.insert(request));
        }

        for (int i=0; i<cnt; i++) {
            CodeDto response = commonCodeDaoImpl.selectByCode("100" + i);
            assertEquals(1, commonCodeDaoImpl.delete(response.getSeq()));
        }

        List<CodeDto> responses = commonCodeDaoImpl.selectAll();
        assertEquals(0, responses.size());
    }

    @DisplayName("레벨로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 레벨로_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeDaoImpl.insert(request));
        }

        assertEquals(cnt, commonCodeDaoImpl.deleteByLevel(1));
        List<CodeDto> responses = commonCodeDaoImpl.selectAll();
        assertEquals(0, responses.size());
    }

    @DisplayName("코드로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeDaoImpl.insert(request));
        }

        assertEquals(1, commonCodeDaoImpl.deleteByCode("100" + (cnt-1)));
        List<CodeDto> responses = commonCodeDaoImpl.selectAll();
        assertEquals(cnt-1, responses.size());
    }

    private CodeDto createRequest(String code, String name, String top_code) {
        CodeDto dto = new CodeDto();
        dto.setLevel(1);
        dto.setCode(code);
        dto.setName(name);
        dto.setTop_code(top_code);
        dto.setChk_use("Y");
        dto.setReg_date("2025-01-01");
        dto.setReg_user_seq(1);
        dto.setUp_user_seq(1);
        dto.setUp_date("2025-01-01");
        return dto;
    }

    private CodeResponse createResponse(String code, String name, String top_code) {
        CodeResponse dto = new CodeResponse();
        dto.setLevel(1);
        dto.setCode(code);
        dto.setName(name);
        dto.setTop_code(top_code);
        dto.setChk_use("Y");
        return dto;
    }
}