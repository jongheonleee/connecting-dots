package com.example.demo.repository.report.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.report.ReportCategoryDto;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.dto.report.ReportProcessDetailsDto;
import com.example.demo.repository.report.ReportCategoryRepository;
import com.example.demo.repository.report.ReportProcessDetailsRepository;
import com.example.demo.repository.report.ReportRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ReportProcessDetailsDaoImplTest {

    private static final Integer MAX_LEVEL = 3;

    @Autowired
    private ReportProcessDetailsRepository sut;

    @Autowired
    private ReportCategoryRepository reportCategoryRepository;

    @Autowired
    private ReportRepository reportRepository;

    private ReportDto reportDto;
    private ReportCategoryDto reportCategoryDto;


    @BeforeEach
    void setUp() {
        assertNotNull(sut);
        assertNotNull(reportRepository);
        assertNotNull(reportCategoryRepository);

        reportRepository.deleteAll();
        assertEquals(0, reportRepository.count());

        for (int i=MAX_LEVEL; i>0; i--) {
            reportCategoryRepository.deleteByLevel(i);
        }
        assertEquals(0, reportCategoryRepository.count());

        sut.deleteAll();
        assertEquals(0, sut.count());

        createCategoryDto();
        assertEquals(1, reportCategoryRepository.insert(reportCategoryDto));

        createReportDto();
        assertEquals(1, reportRepository.insert(reportDto));
    }

    @AfterEach
    void clean() {
        reportRepository.deleteAll();
        assertEquals(0, reportRepository.count());

        for (int i=MAX_LEVEL; i>0; i--) {
            reportCategoryRepository.deleteByLevel(i);
        }
        assertEquals(0, reportCategoryRepository.count());

        sut.deleteAll();
        assertEquals(0, sut.count());
    }

    @Nested
    @DisplayName("카운팅 및 존재 여부 관련 테스트")
    class sut_count_exists_test {

        @DisplayName("여러개 생성하고 그만큼 카운팅 되는지 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_count(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
        }

        @DisplayName("여러개 생성하고 rno로 카운팅 되는지 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_countByRno(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByRno(reportDto.getRno()));
        }

        @DisplayName("여러개 생성하고 seq로 존재 여부 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_existsBySeq(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsBySeq(dto.getSeq()));
            }
        }

        @DisplayName("여러개 생성하고 rno로 존재 여부 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_existsByRno(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsByRno(reportDto.getRno()));
            }
        }

        @DisplayName("여러개 생성하고 seq로 for update 문 형식으로 존재 여부 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_existsBySeqForUpdate(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsBySeqForUpdate(dto.getSeq()));
            }
        }

        @DisplayName("여러개 생성하고 rno로 for update 문 형식으로 존재 여부 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_existsByRnoForUpdate(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsByRnoForUpdate(reportDto.getRno()));
            }
        }
    }

    @Nested
    @DisplayName("조회 관련 테스트")
    class sut_select_test {

        @DisplayName("여러개 생성하고 각각 seq로 조회하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_selectBySeq(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertNotNull(sut.selectBySeq(dto.getSeq()));
            }
        }

        @DisplayName("여러개 생성하고 rno로 조회하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_selectByRno(int cnt) {
            List<ReportProcessDetailsDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReportProcessDetailsDto> actual = sut.selectByRno(reportDto.getRno());

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
            actual.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            for (int i=0; i<cnt; i++) {
                var e = expected.get(i);
                var a = actual.get(i);

                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getRno(), a.getRno());
                assertEquals(e.getPros_code(), a.getPros_code());
                assertEquals(e.getAppl_begin(), a.getAppl_begin());
                assertEquals(e.getAppl_end(), a.getAppl_end());
                assertEquals(e.getReg_date(), a.getReg_date());
                assertEquals(e.getReg_user_seq(), a.getReg_user_seq());
                assertEquals(e.getUp_date(), a.getUp_date());
                assertEquals(e.getUp_user_seq(), a.getUp_user_seq());
                assertEquals(e.getChk_use(), a.getChk_use());
            }
        }

        @DisplayName("여러개 생성하고 모두 조회하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_selectAll(int cnt) {
            List<ReportProcessDetailsDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReportProcessDetailsDto> actual = sut.selectAll();

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
            actual.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            for (int i=0; i<cnt; i++) {
                var e = expected.get(i);
                var a = actual.get(i);

                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getRno(), a.getRno());
                assertEquals(e.getPros_code(), a.getPros_code());
                assertEquals(e.getAppl_begin(), a.getAppl_begin());
                assertEquals(e.getAppl_end(), a.getAppl_end());
                assertEquals(e.getReg_date(), a.getReg_date());
                assertEquals(e.getReg_user_seq(), a.getReg_user_seq());
                assertEquals(e.getUp_date(), a.getUp_date());
                assertEquals(e.getUp_user_seq(), a.getUp_user_seq());
                assertEquals(e.getChk_use(), a.getChk_use());
            }
        }

        @DisplayName("여러개 생성하고 rno로 현재 상태 조회하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_selectByRnoAtPresent(int cnt) {
            List<ReportProcessDetailsDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);

                if (i == cnt -1) {
                    dto.setAppl_begin("2021-01-01 00:00:00");
                    dto.setAppl_end("9999-12-31 23:59:59");
                    assertEquals(1, sut.update(dto));
                }
            }

            ReportProcessDetailsDto actual = sut.selectByRnoAtPresent(reportDto.getRno());

            assertNotNull(actual);

            expected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            var e = expected.get(cnt-1);

            assertEquals(e.getSeq(), actual.getSeq());
            assertEquals(e.getRno(), actual.getRno());
            assertEquals(e.getPros_code(), actual.getPros_code());
            assertEquals(e.getAppl_begin(), actual.getAppl_begin());
            assertEquals(e.getAppl_end(), actual.getAppl_end());
            assertEquals(e.getReg_date(), actual.getReg_date());
            assertEquals(e.getReg_user_seq(), actual.getReg_user_seq());
            assertEquals(e.getUp_date(), actual.getUp_date());
            assertEquals(e.getUp_user_seq(), actual.getUp_user_seq());
            assertEquals(e.getChk_use(), actual.getChk_use());
        }
    }

    @Nested
    @DisplayName("생성 관련 테스트")
    class sut_insert_test {

        @DisplayName("여러개 생성하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_insert(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }
        }
    }

    @Nested
    @DisplayName("수정 관련 테스트")
    class sut_update_test {

        @DisplayName("여러개 생성하고 각각 수정하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_update(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));

                dto.setAppl_begin("2021-01-01 00:00:00");
                dto.setAppl_end("2021-01-01 00:00:00");
                dto.setReg_date("2021-01-01 00:00:00");
                dto.setReg_user_seq(1);
                dto.setUp_date("2021-01-01 00:00:00");
                dto.setUp_user_seq(1);
                dto.setChk_use("Y");

                assertEquals(1, sut.update(dto));

                var actual = sut.selectBySeq(dto.getSeq());

                assertEquals(dto.getSeq(), actual.getSeq());
                assertEquals(dto.getRno(), actual.getRno());
                assertEquals(dto.getPros_code(), actual.getPros_code());
                assertEquals(dto.getAppl_begin(), actual.getAppl_begin());
                assertEquals(dto.getAppl_end(), actual.getAppl_end());
                assertEquals(dto.getReg_date(), actual.getReg_date());
                assertEquals(dto.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(dto.getUp_date(), actual.getUp_date());
                assertEquals(dto.getUp_user_seq(), actual.getUp_user_seq());
                assertEquals(dto.getChk_use(), actual.getChk_use());
            }
        }
    }

    @Nested
    @DisplayName("삭제 관련 테스트")
    class sut_delete_test {

        @DisplayName("여러개 생성하고 모두 삭제하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_deleteAll(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
            assertEquals(cnt, sut.deleteAll());
            assertEquals(0, sut.count());
        }

        @DisplayName("여러개 생성하고 seq로 각각 삭제하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_deleteBySeq(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertEquals(1, sut.deleteBySeq(dto.getSeq()));
                assertFalse(sut.existsBySeq(dto.getSeq()));
            }


            assertEquals(0, sut.count());
        }

        @DisplayName("여러개 생성하고 rno로 삭제하기")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_works_when_deleteByRno(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
            assertEquals(cnt, sut.deleteByRno(reportDto.getRno()));
            assertEquals(0, sut.count());
        }
    }

    private void createCategoryDto() {
        reportCategoryDto =  ReportCategoryDto.builder()
                .cate_code("RC010101")
                .top_cate("RC010100")
                .name("테스트용 리포트 카테고리")
                .comt("테스트용 리포트 카테고리입니다.")
                .ord(1)
                .chk_use("Y")
                .level(1)
                .reg_date("2021-01-01 00:00:00")
                .reg_user_seq(1)
                .up_date("2021-01-01 00:00:00")
                .up_user_seq(1)
                .build();
    }

    private void createReportDto() {
        reportDto =  ReportDto.builder()
                .cate_code("RC010101")
                .title("테스트용 리포트 제목")
                .cont("테스트용 리포트 내용입니다.")
                .chk_change("N")
                .comt("테스트용 리포트입니다.")
                .repo_seq(1)
                .resp_seq(1)
                .boar(1)
                .cmnt(1)
                .repl(1)
                .type(0)
                .reg_date("2021-01-01 00:00:00")
                .reg_user_seq(1)
                .up_date("2021-01-01 00:00:00")
                .up_user_seq(1)
                .build();
    }

    private ReportProcessDetailsDto createDto(int i) {
        return ReportProcessDetailsDto.builder()
                .rno(reportDto.getRno())
                .pros_code("PRC010101")
                .appl_begin("2021-01-01 00:00:00")
                .appl_end("2021-01-01 00:00:00")
                .reg_date("2021-01-01 00:00:00")
                .reg_user_seq(1)
                .up_date("2021-01-01 00:00:00")
                .up_user_seq(1)
                .chk_use("Y")
                .build();
    }
}