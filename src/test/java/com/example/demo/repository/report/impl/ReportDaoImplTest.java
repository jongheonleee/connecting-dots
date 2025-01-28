package com.example.demo.repository.report.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.report.ReportCategoryDto;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.repository.report.ReportCategoryRepository;
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
class ReportDaoImplTest {

    private static final Integer MAX_LEVEL = 3;

    @Autowired
    private ReportRepository sut;

    @Autowired
    private ReportCategoryRepository reportCategoryRepository;

    @BeforeEach
    void setUp() {
        assertNotNull(sut);
        assertNotNull(reportCategoryRepository);

        sut.deleteAll();
        assertEquals(0, sut.count());

        for (int i=MAX_LEVEL; i>0; i--) {
            reportCategoryRepository.deleteByLevel(i);
        }

        assertEquals(0, reportCategoryRepository.count());

        ReportCategoryDto dto = createCategoryDto();
        assertEquals(1, reportCategoryRepository.insert(dto));
    }

    @AfterEach
    void clean() {
        sut.deleteAll();
        assertEquals(0, sut.count());

        for (int i=MAX_LEVEL; i>0; i--) {
            reportCategoryRepository.deleteByLevel(i);
        }

        assertEquals(0, reportCategoryRepository.count());
    }

    @Nested
    @DisplayName("카운트 및 존재 여부 확인 관련 테스트")
    class sut_count_exists_test {

        @DisplayName("여러개를 등록하고 그만큼 카운팅 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_count(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
        }

        @DisplayName("여러개를 등록하고 카테고리로 카운팅 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_count_category(int cnt) {
            String cate_code = "RC010101";
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByCateCode(cate_code));
        }

        @DisplayName("여러개를 등록하고 리포트 시퀀스로 카운팅 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_count_repo_seq(int cnt) {
            Integer repo_seq = 1;
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByRepoSeq(repo_seq));
        }

        @DisplayName("여러개를 등록하고 리포트 대상자 시퀀스로 카운팅 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_count_resp_seq(int cnt) {
            Integer resp_seq = 2;
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByRespSeq(resp_seq));
        }

        @DisplayName("여러개를 등록하고 검색 조건으로 카운팅 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_count_by_search_condition(int cnt) {
            SearchCondition sc = new SearchCondition(1, 10, "TT", "테스트용", "1");
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countBySearchCondition(sc));
        }

        @DisplayName("여러개를 등록하고 rno로 존재하는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_existsByRno(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsByRno(dto.getRno()));
            }
        }

        @DisplayName("여러개를 등록하고 rno로 for update 형식의 존재여부 확인이 잘 이루어지는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_existsByRnoForUpdate(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsByRnoForUpdate(dto.getRno()));
            }
        }

        @DisplayName("여러개를 등록하고 리포트 시퀀스로 존재하는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_existsByRepoSeq(int cnt) {
            Integer repo_seq = 1;
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsByRepoSeq(repo_seq));
            }
        }

        @DisplayName("여러개를 등록하고 리포트 대상자 시퀀스로 존재하는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_existsByRespSeq(int cnt) {
            Integer resp_seq = 2;
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                assertTrue(sut.existsByRespSeq(resp_seq));
            }
        }

    }


    @Nested
    @DisplayName("조회 관련 테스트")
    class sut_select_test {

        @DisplayName("여러개를 등록하고 전체 조회가 잘 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_selectAll(int cnt) {
            List<ReportDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReportDto> actual = sut.selectAll();
            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getRno().compareTo(b.getRno()));
            actual.sort((a, b) -> a.getRno().compareTo(b.getRno()));

            for (int i=0; i<cnt; i++) {
                ReportDto e = expected.get(i);
                ReportDto a = actual.get(i);

                assertEquals(e.getRno(), a.getRno());
                assertEquals(e.getCate_code(), a.getCate_code());
                assertEquals(e.getTitle(), a.getTitle());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getChk_change(), a.getChk_change());
                assertEquals(e.getComt(), a.getComt());
                assertEquals(e.getRepo_seq(), a.getRepo_seq());
                assertEquals(e.getResp_seq(), a.getResp_seq());
                assertEquals(e.getBoar(), a.getBoar());
                assertEquals(e.getCmnt(), a.getCmnt());
                assertEquals(e.getRepl(), a.getRepl());
                assertEquals(e.getType(), a.getType());
            }
        }

        @DisplayName("여러개 등록하고 그 중에서 rno로 조회가 잘 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_selectByRno(int cnt) {
            List<ReportDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (int i=0; i<cnt; i++) {
                ReportDto e = expected.get(i);
                ReportDto a = sut.selectByRno(e.getRno());

                assertEquals(e.getRno(), a.getRno());
                assertEquals(e.getCate_code(), a.getCate_code());
                assertEquals(e.getTitle(), a.getTitle());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getChk_change(), a.getChk_change());
                assertEquals(e.getComt(), a.getComt());
                assertEquals(e.getRepo_seq(), a.getRepo_seq());
                assertEquals(e.getResp_seq(), a.getResp_seq());
                assertEquals(e.getBoar(), a.getBoar());
                assertEquals(e.getCmnt(), a.getCmnt());
                assertEquals(e.getRepl(), a.getRepl());
                assertEquals(e.getType(), a.getType());
            }
        }

        @DisplayName("여러개 등록하고 검색 조건으로 조회가 잘 되는지 확인한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_selectBySearchCondition(int cnt) {
            List<ReportDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            Integer pageSize = 10;
            SearchCondition sc = new SearchCondition(1, pageSize, "TT", "테스트용", "1");
            List<ReportDto> actual = sut.selectBySearchCondition(sc);
            assertEquals(cnt > pageSize ? pageSize : cnt, actual.size());
        }

    }

    @Nested
    @DisplayName("등록 관련 테스트")
    class sut_insert_test {

        @DisplayName("여러개를 등록한다.")
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

        @DisplayName("기존에 등록되어 있는 것들을 성공적으로 수정 처리한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_update(int cnt) {
            for (int i=0; i<cnt; i++) {
                var dto = createDto(i);
                assertEquals(1, sut.insert(dto));

                dto.setTitle("수정된 테스트용 리포트 제목" + i);
                dto.setCont("수정된 테스트용 리포트 내용" + i);
                dto.setComt("수정된 테스트용 리포트 코멘트" + i);

                assertEquals(1, sut.update(dto));

                var updated = sut.selectByRno(dto.getRno());
                assertEquals(dto.getTitle(), updated.getTitle());
                assertEquals(dto.getCont(), updated.getCont());
                assertEquals(dto.getComt(), updated.getComt());
            }
        }

    }

    private ReportCategoryDto createCategoryDto() {
        return ReportCategoryDto.builder()
                                .cate_code("RC010101")
                                .top_cate("RC010100")
                                .name("테스트용 리포트 카테고리")
                                .comt("테스트용 리포트 카테고리입니다.")
                                .ord(1)
                                .chk_use("Y")
                                .level(1)
                                .reg_date("2021-01-01")
                                .reg_user_seq(1)
                                .up_date("2021-01-01")
                                .up_user_seq(1)
                                .build();
    }

    private ReportDto createDto(int i) {
        return ReportDto.builder()
                        .rno(i)
                        .cate_code("RC010101")
                        .title("테스트용 리포트 제목" + i)
                        .cont("테스트용 리포트 내용" + i)
                        .chk_change("N")
                        .comt("테스트용 리포트 코멘트" + i)
                        .repo_seq(1)
                        .resp_seq(2)
                        .boar(1)
                        .type(0)
                        .reg_date("2021-01-01")
                        .reg_user_seq(1)
                        .up_date("2021-01-01")
                        .up_user_seq(1)
                        .build();
    }

}