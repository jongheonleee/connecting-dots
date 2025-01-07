package com.example.demo.repository.mybatis.code;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.code.CodeRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        int count = commonCodeDaoImpl.count();
        assertEquals(0, count);
    }

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_테스트(int cnt) {
         CodeRequestDto dto = new CodeRequestDto();
         dto.setLevel(1);
         dto.setCode("code");
         dto.setName("name");
         dto.setChk_use("Y");
         dto.setReg_date("2021-01-01");
         dto.setReg_user_seq(1);
         dto.setUp_user_seq(1);
         dto.setUp_date("2021-01-01");
         int result = commonCodeDaoImpl.insert(dto);
         assertEquals(1, result);
    }
}