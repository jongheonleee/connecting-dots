package com.example.demo.repository.report.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.report.ReportCategoryDto;
import com.example.demo.repository.report.ReportCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ReportCategoryDaoImplTest {

    private static final Integer MAX_LEVEL = 3;

    @Autowired
    private ReportCategoryRepository sut;

    @BeforeEach
    void setUp() {
        assertNotNull(sut);

        for (int i=MAX_LEVEL; i>0; i--) {
            sut.deleteByLevel(i);
        }

        assertEquals(0, sut.count());
    }

    @AfterEach
    void clean() {
        for (int i=MAX_LEVEL; i>0; i--) {
            sut.deleteByLevel(i);
        }

        assertEquals(0, sut.count());
    }

    @Nested
    @DisplayName("카운트 및 존재 여부 확인 관련 테스트")
    class sut_count_exists_test {

        @DisplayName("여러개를 등록하고 그만큼 카운팅 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_count(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
        }

        @DisplayName("여러개를 등록하고 레벨별로 카운팅 되는지 확인한다.")
        @Test
        void it_correctly_work_when_countByLevel() {
            int countOfLevel1 = 10, countOfLevel2 = 10, countOfLevel3 = 10;

            for (int i=0; i<countOfLevel1; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.insert(dto));
            }
            assertEquals(countOfLevel1, sut.countByLevel(1));

            for (int i=countOfLevel1; i<countOfLevel1 + countOfLevel2; i++) {
                var dto = createDto(i, 2);
                assertEquals(1, sut.insert(dto));
            }
            assertEquals(countOfLevel2, sut.countByLevel(2));

            for (int i=countOfLevel1 + countOfLevel2; i<countOfLevel1 + countOfLevel2 + countOfLevel3; i++) {
                var dto = createDto(i, 3);
                assertEquals(1, sut.insert(dto));
            }
            assertEquals(countOfLevel3, sut.countByLevel(3));
        }

        @DisplayName("여러개를 등록하고 존재하는지 확인한다.")
        @ParameterizedTest
        @ValueSource(strings = {"RC010101", "RC010102", "RC010103", "RC010104", "RC010105"})
        void it_correctly_work_when_existsByCateCode(String cate_code) {
            var dto = createDto(cate_code);
            assertEquals(1, sut.insert(dto));
            assertTrue(sut.existsByCateCode(cate_code));
        }

        @DisplayName("여러개를 등록하고 for update 형식의 존재여부 확인이 잘 이루어지는지 확인한다.")
        @ParameterizedTest
        @ValueSource(strings = {"RC010101", "RC010102", "RC010103", "RC010104", "RC010105"})
        void it_correctly_work_when_existsByCateCodeForUpdate(String cate_code) {
            var dto = createDto(cate_code);
            assertEquals(1, sut.insert(dto));
            assertTrue(sut.existsByCateCodeForUpdate(cate_code));
        }

    }

    @Nested
    @DisplayName("조회 관련 테스트")
    class sut_select_test {

    }

    @Nested
    @DisplayName("생성 관련 테스트")
    class sut_insert_test {

        @DisplayName("여러개를 생성한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_insert(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.insert(dto));
            }
        }
    }

    @Nested
    @DisplayName("수정 관련 테스트")
    class sut_update_test {

        @DisplayName("기존에 등록되어 있는 것들을 성공적으로 수정 처리한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_update(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.insert(dto));
            }

            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                dto.setName("수정된 테스트용 카테고리" + i);
                dto.setComt("수정된 테스트용 카테고리 설명" + i);

                assertEquals(1, sut.update(dto));

                var updated = sut.selectByCateCode(dto.getCate_code());
                assertEquals(dto.getName(), updated.getName());
                assertEquals(dto.getComt(), updated.getComt());
            }
        }

        @DisplayName("기존에 등록된 카테고리의 사용여부를 Y로 수정한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_updateChkUseY(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.insert(dto));
            }

            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.updateChkUseY(dto.getCate_code()));

                var updated = sut.selectByCateCode(dto.getCate_code());
                assertEquals("Y", updated.getChk_use());
            }
        }

        @DisplayName("기존에 등록된 카테고리의 사용여부를 N으로 수정한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_updateChkUseN(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.insert(dto));
            }

            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.updateChkUseN(dto.getCate_code()));

                var updated = sut.selectByCateCode(dto.getCate_code());
                assertEquals("N", updated.getChk_use());
            }
        }

    }

    @Nested
    @DisplayName("삭제 관련 테스트")
    class sut_delete_test {

        @DisplayName("특정 카테고리를 삭제한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_delete(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.insert(dto));
            }

            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.deleteByCateCode(dto.getCate_code()));
            }

            assertEquals(0, sut.count());
        }

        @DisplayName("특정 레벨의 카테고리를 삭제한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_deleteByLevel(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 1);
                assertEquals(1, sut.insert(dto));
            }
            assertEquals(cnt, sut.deleteByLevel(1));

            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 2);
                assertEquals(1, sut.insert(dto));
            }
            assertEquals(cnt, sut.deleteByLevel(2));


            for (int i=0; i<cnt; i++) {
                var dto = createDto(i, 3);
                assertEquals(1, sut.insert(dto));
            }
            assertEquals(cnt, sut.deleteByLevel(3));

            assertEquals(0, sut.count());
        }
    }

    private ReportCategoryDto createDto(int i, int level) {
        return ReportCategoryDto.builder()
                                .cate_code("RC01010" + i)
                                .top_cate("RC01010" )
                                .name("테스트용 카테고리" + i)
                                .comt("테스트용 카테고리 설명" + i)
                                .ord(i)
                                .chk_use("Y")
                                .level(level)
                                .reg_date("2021-09-01 00:00:00")
                                .reg_user_seq(1)
                                .up_date("2021-09-01 00:00:00")
                                .up_user_seq(1)
                                .build();

    }

    private ReportCategoryDto createDto(String cate_code) {
        return ReportCategoryDto.builder()
                                .cate_code(cate_code)
                                .top_cate("RC01010" )
                                .name("테스트용 카테고리")
                                .comt("테스트용 카테고리 설명")
                                .ord(1)
                                .chk_use("Y")
                                .level(1)
                                .reg_date("2021-09-01 00:00:00")
                                .reg_user_seq(1)
                                .up_date("2021-09-01 00:00:00")
                                .up_user_seq(1)
                                .build();
    }

}