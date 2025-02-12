package com.example.demo.repository.report.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.report.ReportCategoryDto;
import com.example.demo.dto.report.ReportChangeHistoryDto;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.repository.report.ReportCategoryRepository;
import com.example.demo.repository.report.ReportChangeHistoryRepository;
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
class ReportChangeHistoryDaoImplTest {

    private static final Integer MAX_LEVEL = 3;

    @Autowired
    private ReportChangeHistoryRepository sut;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportCategoryRepository reportCategoryRepository;

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
    @DisplayName("카운트 및 존재 여부 확인 관련 테스트")
    class sut_count_exists_test {

        @DisplayName("여러개 등록하고 그만큼 카운팅되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_count(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
        }

        @DisplayName("여러개 등록하고 rno로 카운팅되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_countByRno(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByRno(reportDto.getRno()));
        }

        @DisplayName("여러개 등록하고 seq로 존재 여부 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_existsBySeq(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsBySeq(dto.getSeq()));
            }
        }

        @DisplayName("여러개 등록하고 rno로 존재 여부 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_existsByRno(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsByRno(dto.getRno()));
            }
        }

        @DisplayName("여러개 등록하고 seq로 for update 문으로 존재 여부 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_exists_seq_for_update(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsBySeqForUpdate(dto.getSeq()));
            }
        }

        @DisplayName("여러개 등록하고 rno로 for update 문으로 존재 여부 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_exists_rno_for_update(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsByRnoForUpdate(dto.getRno()));
            }
        }

    }


    @Nested
    @DisplayName("조회 관련 테스트")
    class sut_select_test {

        @DisplayName("여러개 등록하고 seq로 각각 조회한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_select_seq(int cnt) {
            for (int i=0; i<cnt; i++) {
                var expected = createDto(i);
                assertEquals(1, sut.insert(expected));

                var selected = sut.selectBySeq(expected.getSeq());
                assertNotNull(selected);

                assertEquals(expected.getRno(), selected.getRno());
                assertEquals(expected.getTitle(), selected.getTitle());
                assertEquals(expected.getCont(), selected.getCont());
                assertEquals(expected.getAppl_begin(), selected.getAppl_begin());
                assertEquals(expected.getAppl_end(), selected.getAppl_end());
                assertEquals(expected.getComt(), selected.getComt());
                assertEquals(expected.getReg_date(), selected.getReg_date());
                assertEquals(expected.getReg_user_seq(), selected.getReg_user_seq());
                assertEquals(expected.getUp_date(), selected.getUp_date());
                assertEquals(expected.getUp_user_seq(), selected.getUp_user_seq());
            }
        }

        @DisplayName("여러개 등록하고 rno로 조회한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_select_rno(int cnt) {
            List<ReportChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            var actual = sut.selectByRno(reportDto.getRno());

            assertNotNull(actual);
            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
            actual.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            for (int i=0; i<cnt; i++) {
                var e = expected.get(i);
                var a = actual.get(i);

                assertEquals(e.getRno(), a.getRno());
                assertEquals(e.getTitle(), a.getTitle());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getAppl_begin(), a.getAppl_begin());
                assertEquals(e.getAppl_end(), a.getAppl_end());
                assertEquals(e.getComt(), a.getComt());
                assertEquals(e.getReg_date(), a.getReg_date());
                assertEquals(e.getReg_user_seq(), a.getReg_user_seq());
                assertEquals(e.getUp_date(), a.getUp_date());
                assertEquals(e.getUp_user_seq(), a.getUp_user_seq());
            }
        }

        @DisplayName("여러개 등록하고 전체 조회한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_select_all(int cnt) {
            List<ReportChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            var actual = sut.selectAll();

            assertNotNull(actual);
            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
            actual.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            for (int i=0; i<cnt; i++) {
                var e = expected.get(i);
                var a = actual.get(i);

                assertEquals(e.getRno(), a.getRno());
                assertEquals(e.getTitle(), a.getTitle());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getAppl_begin(), a.getAppl_begin());
                assertEquals(e.getAppl_end(), a.getAppl_end());
                assertEquals(e.getComt(), a.getComt());
                assertEquals(e.getReg_date(), a.getReg_date());
                assertEquals(e.getReg_user_seq(), a.getReg_user_seq());
                assertEquals(e.getUp_date(), a.getUp_date());
                assertEquals(e.getUp_user_seq(), a.getUp_user_seq());
            }
        }

        @DisplayName("여러개 등록하고 rno로 최신 데이터 조회한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_select_latest_by_rno(int cnt) {
            List<ReportChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                dummy.add(dto);
            }

            ReportChangeHistoryDto expected = dummy.stream()
                                                    .max((a, b) -> a.getSeq().compareTo(b.getSeq()))
                                                    .orElseThrow();
            var actual = sut.selectLatestByRno(reportDto.getRno());

            assertNotNull(actual);

            assertEquals(expected.getRno(), actual.getRno());
            assertEquals(expected.getTitle(), actual.getTitle());
            assertEquals(expected.getCont(), actual.getCont());
            assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
            assertEquals(expected.getAppl_end(), actual.getAppl_end());
            assertEquals(expected.getComt(), actual.getComt());
            assertEquals(expected.getReg_date(), actual.getReg_date());
            assertEquals(expected.getReg_user_seq(), actual.getReg_user_seq());
            assertEquals(expected.getUp_date(), actual.getUp_date());
            assertEquals(expected.getUp_user_seq(), actual.getUp_user_seq());
        }
    }

    @Nested
    @DisplayName("생성 관련 테스트")
    class sut_insert_test {

        @DisplayName("여러개 생성한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_insert(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }
        }
    }

    @Nested
    @DisplayName("수정 관련 테스트")
    class sut_update_test {

        @DisplayName("여러개 생성하고 수정한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_update(int cnt) {
            List<ReportChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (int i=0; i<cnt; i++) {
                var e = expected.get(i);
                e.setTitle(e.getTitle() + "수정");
                e.setCont(e.getCont() + "수정");
                e.setAppl_begin("2021-01-01 00:00:00");
                e.setAppl_end("2021-01-01 00:00:00");
                e.setComt(e.getComt() + "수정");
                e.setReg_date("2021-01-01 00:00:00");
                e.setReg_user_seq(e.getReg_user_seq());
                e.setUp_date("2021-01-01 00:00:00");
                e.setUp_user_seq(e.getUp_user_seq());

                assertEquals(1, sut.update(e));

                var actual = sut.selectBySeq(e.getSeq());
                assertNotNull(actual);

                assertEquals(e.getRno(), actual.getRno());
                assertEquals(e.getTitle(), actual.getTitle());
                assertEquals(e.getCont(), actual.getCont());
                assertEquals(e.getAppl_begin(), actual.getAppl_begin());
                assertEquals(e.getAppl_end(), actual.getAppl_end());
                assertEquals(e.getComt(), actual.getComt());
                assertEquals(e.getReg_date(), actual.getReg_date());
                assertEquals(e.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(e.getUp_date(), actual.getUp_date());
                assertEquals(e.getUp_user_seq(), actual.getUp_user_seq());
            }
        }
    }

    @Nested
    @DisplayName("삭제 관련 테스트")
    class sut_delete_test {

        @DisplayName("여러개 생성하고 seq로 삭제한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_delete_by_seq(int cnt) {
            List<ReportChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (int i=0; i<cnt; i++) {
                var e = expected.get(i);
                assertEquals(1, sut.delete(e.getSeq()));
                assertFalse(sut.existsBySeq(e.getSeq()));
            }
        }

        @DisplayName("여러개 생성하고 rno로 삭제한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_delete_by_rno(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.deleteByRno(reportDto.getRno()));
        }

        @DisplayName("여러개 생성하고 전체 삭제한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_delete_all(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
            assertEquals(cnt, sut.deleteAll());
            assertEquals(0, sut.count());
        }
    }

    private ReportChangeHistoryDto createDto(int i) {
        return ReportChangeHistoryDto.builder()
                                    .rno(reportDto.getRno())
                                    .title(reportDto.getTitle() + i )
                                    .cont(reportDto.getCont() + i)
                                    .appl_begin("2021-01-01 00:00:00")
                                    .appl_end("2021-01-01 00:00:00")
                                    .comt(reportDto.getComt())
                                    .reg_date("2021-01-01 00:00:00")
                                    .reg_user_seq(reportDto.getReg_user_seq())
                                    .up_date("2021-01-01 00:00:00")
                                    .up_user_seq(reportDto.getUp_user_seq())
                                    .build();
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
}