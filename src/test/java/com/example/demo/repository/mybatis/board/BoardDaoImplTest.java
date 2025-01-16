package com.example.demo.repository.mybatis.board;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.domain.BoardCategory;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardDaoImplTest {

    private final Integer REG_USER_SEQ = 1;
    private final Integer UP_USER_SEQ = 1;
    private final String REG_DATE = "2025-01-14";
    private final String UP_DATE = "2025-01-14";
    private final String CATE_CODE = "BC101001";

    private final Integer page = 1;
    private final Integer pageSize = 10;

    private final Integer TEST_USER_SEQ = 1;

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardCategoryDaoImpl boardCategoryDao;

    @BeforeEach
    public void setUp() {
        // 자동 주입 확인
        assertNotNull(boardDao);
        assertNotNull(boardCategoryDao);

        // 테스트 서버 초기화
        boardDao.deleteAll();

        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());

        // 임시 카테고리 생성 및 등록
        BoardCategoryDto dto = createCategoryDto();
        assertEquals(1, boardCategoryDao.insert(dto));
    }

    @Nested
    @DisplayName("게시글 카운팅 및 존재 여부 테스트")
    class CountAndExistsTest {

        @DisplayName("카운팅 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카운팅_처리_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));
            }

            assertEquals(cnt, boardDao.count());
        }

        @DisplayName("검색 기반으로 카운팅 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 검색_기반으로_카운팅_처리_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));
            }

            SearchCondition sc = createSearchCondition(page, pageSize, "TT", "테스트용", "1");
            assertEquals(cnt, boardDao.countBySearchCondition(sc));
        }

        @DisplayName("존재하는 bno로 존재 여부 확인 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void bno로_존재_여부_확인_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));
                assertTrue(boardDao.existsByBno(dto.getBno()));
            }
        }

        @DisplayName("존재하지 않는 bno로 존재 여부 확인 테스트")
        @Test
        void 존재하지_않는_bno로_존재_여부_확인_테스트() {
            Integer bno = 99999999;
            assertFalse(boardDao.existsByBno(bno));
        }

        @DisplayName("존재하는 bno로 존재 여부 확인 - FOR UPDATE")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void bno로_존재_여부_확인_FOR_UPDATE_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));
                assertTrue(boardDao.existsByBnoForUpdate(dto.getBno()));
            }
        }

        @DisplayName("존재하지 않는 bno로 존재 여부 확인 - FOR UPDATE")
        @Test
        void 존재하지_않는_bno로_존재_여부_확인_FOR_UPDATE_테스트() {
            Integer bno = 99999999;
            assertFalse(boardDao.existsByBnoForUpdate(bno));
        }
    }

    @Nested
    @DisplayName("게시글 생성 처리 테스트")
    class InsertTest {

        @DisplayName("게시글 생성 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_생성_처리_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));
            }
        }
    }

    @Nested
    @DisplayName("게시글 조회 처리 테스트")
    class SelectTest {

        @DisplayName("게시글 단건 조회 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_단건_조회_처리_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto expected = createBoardDto(i);
                assertEquals(1, boardDao.insert(expected));

                BoardDto actual = boardDao.select(expected.getBno());

                assertNotNull(actual);
                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getTitle(), actual.getTitle());
                assertEquals(expected.getCont(), actual.getCont());
                assertEquals(expected.getWriter(), actual.getWriter());
                assertEquals(expected.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(expected.getUp_user_seq(), actual.getUp_user_seq());
            }
        }

        @DisplayName("게시글 전체 조회 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_전체_조회_처리_테스트(int cnt) {
            List<BoardDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));
                dummy.add(dto);
            }

            List<BoardDto> result = boardDao.selectAll();

            dummy.sort((o1, o2) -> o2.getBno() - o1.getBno());
            result.sort((o1, o2) -> o2.getBno() - o1.getBno());

            assertEquals(cnt, result.size());

            for (int i=0; i<cnt; i++) {
                BoardDto expected = dummy.get(i);
                BoardDto actual = result.get(i);

                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getTitle(), actual.getTitle());
                assertEquals(expected.getCont(), actual.getCont());
                assertEquals(expected.getWriter(), actual.getWriter());
                assertEquals(expected.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(expected.getUp_user_seq(), actual.getUp_user_seq());
            }
        }

        @DisplayName("게시글 검색 조회 처리 테스트")
        @ParameterizedTest
        @CsvSource({"TT, 테스트용, 1", "WR, 홍길동, 1", "CT, BC101001, 1",
                    "TT, 테스트용, 5", "WR, 홍길동, 5", "CT, BC101001, 5",
                    "TT, 테스트용, 10", "WR, 홍길동, 10", "CT, BC101001, 10",
                    "TT, 테스트용, 15", "WR, 홍길동, 15", "CT, BC101001, 15",
                    "TT, 테스트용, 20", "WR, 홍길동, 20", "CT, BC101001, 20"})
        void 게시글_검색_조회_처리_테스트(String searchOption, String searchKeyword, int cnt) {
            List<BoardDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));
                dummy.add(dto);
            }

            SearchCondition sc = createSearchCondition(page, pageSize, searchOption, searchKeyword, "1");
            List<BoardDto> result = boardDao.selectBySearchCondition(sc);

            int expectedSize = cnt > pageSize ? pageSize : cnt; // 현재 페이지가 1로 고정되어 있으므로 cnt가 pageSize보다 크면 pageSize
            assertEquals(expectedSize, result.size());

            dummy.sort((o1, o2) -> o2.getBno() - o1.getBno());
            result.sort((o1, o2) -> o2.getBno() - o1.getBno());

            for (int i=0; i<expectedSize; i++) {
                BoardDto expected = dummy.get(i);
                BoardDto actual = result.get(i);

                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getTitle(), actual.getTitle());
                assertEquals(expected.getCont(), actual.getCont());
                assertEquals(expected.getWriter(), actual.getWriter());
                assertEquals(expected.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(expected.getUp_user_seq(), actual.getUp_user_seq());
            }
        }
    }

    @Nested
    @DisplayName("게시글 수정과 과련된 작업 테스트")
    class UpdateTest {

        @DisplayName("게시글 수정 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_수정_처리_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));

                dto.setTitle("수정된 제목" + i);
                dto.setCont("수정된 내용" + i);
                dto.setWriter("수정된 작성자" + i);

                assertEquals(1, boardDao.update(dto));

                BoardDto actual = boardDao.select(dto.getBno());

                assertNotNull(actual);
                assertEquals(dto.getBno(), actual.getBno());
                assertEquals(dto.getTitle(), actual.getTitle());
                assertEquals(dto.getCont(), actual.getCont());
                assertEquals(dto.getWriter(), actual.getWriter());
                assertEquals(dto.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(dto.getUp_user_seq(), actual.getUp_user_seq());
            }
        }

        @DisplayName("게시글 조회수 증가 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_조회수_증가_처리_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));

                dto.setUp_user_seq(UP_USER_SEQ);
                assertEquals(1, boardDao.increaseViewCnt(dto));
                BoardDto actual = boardDao.select(dto.getBno());
                assertEquals(1, actual.getView_cnt());
            }
        }

        @DisplayName("게시글 조회수 증가 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_좋아요_증가_처리_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));

                dto.setUp_user_seq(UP_USER_SEQ);
                assertEquals(1, boardDao.increaseRecoCnt(dto));

                BoardDto actual = boardDao.select(dto.getBno());

                assertNotNull(actual);
                assertEquals(dto.getBno(), actual.getBno());
                assertEquals(dto.getTitle(), actual.getTitle());
                assertEquals(dto.getCont(), actual.getCont());
                assertEquals(dto.getWriter(), actual.getWriter());
                assertEquals(dto.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(dto.getUp_user_seq(), actual.getUp_user_seq());
                assertEquals(1, actual.getReco_cnt());
            }
        }

        @DisplayName("게시글 좋아요/싫어요 증가 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_좋아요_싫어요_증가_처리_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardDto dto = createBoardDto(i);
                assertEquals(1, boardDao.insert(dto));

                dto.setUp_user_seq(UP_USER_SEQ);
                assertEquals(1, boardDao.increaseRecoCnt(dto));
                assertEquals(1, boardDao.increaseNotRecoCnt(dto));

                BoardDto actual = boardDao.select(dto.getBno());

                assertNotNull(actual);
                assertEquals(dto.getBno(), actual.getBno());
                assertEquals(dto.getTitle(), actual.getTitle());
                assertEquals(dto.getCont(), actual.getCont());
                assertEquals(dto.getWriter(), actual.getWriter());
                assertEquals(dto.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(dto.getUp_user_seq(), actual.getUp_user_seq());
                assertEquals(1, actual.getReco_cnt());
                assertEquals(1, actual.getNot_reco_cnt());
            }
        }
    }

    private BoardDto createBoardDto(int i) {
        return BoardDto.builder()
                        .cate_code(CATE_CODE)
                        .user_seq(TEST_USER_SEQ)
                        .writer("홍길동")
                        .title("테스트용 제목" + i)
                        .cont("테스트용 내용" + i)
                        .reg_user_seq(REG_USER_SEQ)
                        .reg_date(REG_DATE)
                        .up_user_seq(UP_USER_SEQ)
                        .up_date(UP_DATE)
                        .build();
    }

    private BoardCategoryDto createCategoryDto() {
        return BoardCategoryDto.builder()
                                .cate_code("BC101001")
                                .top_cate("BC101000")
                                .name("테스트용 카테고리")
                                .comt("테스트용입니다.")
                                .ord(1)
                                .chk_use("Y")
                                .level(1)
                                .reg_user_seq(REG_USER_SEQ)
                                .reg_date(REG_DATE)
                                .up_user_seq(UP_USER_SEQ)
                                .up_date(UP_DATE)
                                .build();
    }

    private SearchCondition createSearchCondition(int page, int pageSize, String searchOption, String searchKeyword, String sortOption) {
        return new SearchCondition(page, pageSize, searchOption, searchKeyword, sortOption);
    }

}