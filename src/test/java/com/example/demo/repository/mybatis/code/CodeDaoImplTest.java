package com.example.demo.repository.mybatis.code;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.repository.code.CommonCodeRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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
class CodeDaoImplTest {

    private final Integer MAX_LEVEL = 3;

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @BeforeEach
    void setUp() {
        assertNotNull(commonCodeRepository);
        for (int i=MAX_LEVEL; i>=0; i--) {
            commonCodeRepository.deleteByLevel(i);
        }
    }

    @AfterEach
    void tearDown() {
        for (int i=MAX_LEVEL; i>=0; i--) {
            commonCodeRepository.deleteByLevel(i);
        }

        assertEquals(0, commonCodeRepository.count());
    }


    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeRepository.insert(request));
        }

        int totalCnt = commonCodeRepository.count();
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("상위 코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 상위_코드로_조회_테스트(int cnt) {
        String top_code = "100";
        CodeDto request = createRequest(top_code, "테스트용", null);
        assertEquals(1, commonCodeRepository.insert(request));

        for (int i=1; i<=cnt; i++) {
            request = createRequest("100" + i, "테스트용" + i, top_code);
            assertEquals(1, commonCodeRepository.insert(request));
        }


        List<CodeDto> responses = commonCodeRepository.selectByTopCode(top_code);
        assertEquals(cnt, responses.size());
        responses.stream().forEach(response -> {
            assertEquals(top_code, response.getTop_code());
        });
    }

    @Test
    @DisplayName("코드로 조회 테스트")
    void 코드로_조회_테스트() {
        CodeDto request = createRequest("100", "테스트용", null);
        assertEquals(1, commonCodeRepository.insert(request));

        CodeDto response = commonCodeRepository.selectByCode("100");
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
        assertEquals(1, commonCodeRepository.insert(request));
        CodeDto response = commonCodeRepository.selectByCode("100");
        response = commonCodeRepository.selectBySeq(response.getSeq());

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
            assertEquals(1, commonCodeRepository.insert(request));
        }

        List<CodeDto> responses = commonCodeRepository.selectAll();

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
            assertEquals(1, commonCodeRepository.insert(request));

            CodeDto response = commonCodeRepository.selectByCode("100" + i);
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
            assertEquals(1, commonCodeRepository.insert(request));

            CodeDto response = commonCodeRepository.selectByCode("100" + i);

            CodeDto updateRequest = createRequest(response.getCode(), "수정된 테스트용" + i, response.getTop_code());
            updateRequest.setSeq(response.getSeq());

            assertEquals(1, commonCodeRepository.update(updateRequest));

            CodeDto updatedResponse = commonCodeRepository.selectByCode("100" + i);
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
            assertEquals(1, commonCodeRepository.insert(request));

            CodeDto response = commonCodeRepository.selectByCode("100" + i);

            CodeDto updateRequest = createRequest(response.getCode(), response.getName(), response.getTop_code());
            updateRequest.setSeq(response.getSeq());
            updateRequest.setChk_use("N");

            assertEquals(1, commonCodeRepository.updateUse(updateRequest));

            CodeDto updatedResponse = commonCodeRepository.selectByCode("100" + i);
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
            assertEquals(1, commonCodeRepository.insert(request));
        }

        for (int i=0; i<cnt; i++) {
            CodeDto response = commonCodeRepository.selectByCode("100" + i);
            assertEquals(1, commonCodeRepository.delete(response.getSeq()));
        }

        List<CodeDto> responses = commonCodeRepository.selectAll();
        assertEquals(0, responses.size());
    }

    @DisplayName("레벨로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 레벨로_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeRepository.insert(request));
        }

        assertEquals(cnt, commonCodeRepository.deleteByLevel(1));
        List<CodeDto> responses = commonCodeRepository.selectAll();
        assertEquals(0, responses.size());
    }

    @DisplayName("코드로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_삭제_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeRepository.insert(request));
        }

        assertEquals(1, commonCodeRepository.deleteByCode("100" + (cnt-1)));
        List<CodeDto> responses = commonCodeRepository.selectAll();
        assertEquals(cnt-1, responses.size());
    }

    @DisplayName("페이징 처리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {10, 50, 100, 150, 200})
    void 페이징_처리_테스트(int cnt) {
        SearchCondition sc = createSearchCondition(1, 10, "NM", "테스트용", "1");

        for (int i=0; i<cnt; i++) {
            CodeDto request = createRequest("100" + i, "테스트용" + i, null);
            assertEquals(1, commonCodeRepository.insert(request));
        }

        int totalCnt = commonCodeRepository.countBySearchCondition(sc);
        assertEquals(cnt, totalCnt);
        List<CodeDto> codeDtos = commonCodeRepository.selectBySearchCondition(sc);
        assertEquals(10, codeDtos.size());
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

    private SearchCondition createSearchCondition(int page, int pageSize, String searchOption, String searchKeyword, String sortOption) {
        return new SearchCondition(page, pageSize, searchOption, searchKeyword, sortOption);
    }

}