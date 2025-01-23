package com.example.demo.repository.mybatis.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.BoardCategory;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardChangeHistoryDto;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.ord_category.CategoryDto;
import java.util.ArrayList;
import java.util.List;
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
class BoardChangeHistoryDaoImplTest {


    @Autowired
    private BoardChangeHistoryDaoImpl boardChangeHistoryDao;

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardCategoryDaoImpl boardCategoryDao;


    private BoardDto boardDto;
    private BoardCategoryDto categoryDto;


    private final Integer REG_USER_SEQ = 1;
    private final Integer UP_USER_SEQ = 1;
    private final String REG_DATE = "2025-01-14";
    private final String UP_DATE = "2025-01-14";
    private final String CATE_CODE = "BC101001";


    private final Integer TEST_USER_SEQ = 1;

    @BeforeEach
    void setUp() {
        assertNotNull(boardChangeHistoryDao);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryDao);

        boardDao.deleteAll();

        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        boardChangeHistoryDao.deleteAll();

        assertEquals(0, boardChangeHistoryDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());

        categoryDto = createCategoryDto();
        assertEquals(1, boardCategoryDao.insert(categoryDto));

        boardDto = createBoardDto();
        assertEquals(1, boardDao.insert(boardDto));
    }

    @AfterEach
    void tearDown() {
        boardChangeHistoryDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, boardChangeHistoryDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());
    }

    @Nested
    @DisplayName("카운트 및 존재여부 처리 테스트")
    class CountAndExistTest {

        @DisplayName("n개 등록후 전체 카운트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_전체_카운트(int n) {
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
            }

            assertEquals(n, boardChangeHistoryDao.count());
        }

        @DisplayName("등록없이 전체 카운트")
        @Test
        void 등록없이_전체_카운트() {
            assertEquals(0, boardChangeHistoryDao.count());
        }

        @DisplayName("n개 등록후 bno로 카운트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_bno로_카운트(int n) {
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
            }

            assertEquals(n, boardChangeHistoryDao.countByBno(boardDto.getBno()));
        }

        @DisplayName("n개 등록후 seq로 존재여부 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_seq로_존재여부_확인(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardChangeHistoryDto dto : dummy) {
                assertTrue(boardChangeHistoryDao.existsBySeq(dto.getSeq()));
            }
        }

        @DisplayName("n개 등록후 seq로 존재 여부 확인 - FOR UPDATE")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_seq로_존재_여부_확인_FOR_UPDATE(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardChangeHistoryDto dto : dummy) {
                assertTrue(boardChangeHistoryDao.existsBySeqForUpdate(dto.getSeq()));
            }
        }

        @DisplayName("n개 등록후 bno로 존재여부 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_bno로_존재여부_확인(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardChangeHistoryDto dto : dummy) {
                assertTrue(boardChangeHistoryDao.existsByBno(dto.getBno()));
            }
        }

        @DisplayName("n개 등록후 bno로 존재 여부 확인 - FOR UPDATE")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_bno로_존재_여부_확인_FOR_UPDATE(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardChangeHistoryDto dto : dummy) {
                assertTrue(boardChangeHistoryDao.existsByBnoForUpdate(dto.getBno()));
            }
        }
    }

    @Nested
    @DisplayName("등록 처리 테스트")
    class InsertTest {

        @DisplayName("n개 등록")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록(int n) {
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                assertTrue(boardChangeHistoryDao.existsBySeq(dto.getSeq()));
            }

            assertEquals(n, boardChangeHistoryDao.count());
        }
    }

    @Nested
    @DisplayName("수정 처리 관련 테스트")
    class UpdateTest {

        @DisplayName("n개 등록후 수정")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_수정(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardChangeHistoryDto dto : dummy) {
                dto.setTitle("수정된 제목");
                dto.setCont("수정된 내용");
                dto.setComt("수정된 내용입니다.");

                assertEquals(1, boardChangeHistoryDao.update(dto));

                BoardChangeHistoryDto updated = boardChangeHistoryDao.selectBySeq(dto.getSeq());
                assertEquals(dto.getTitle(), updated.getTitle());
                assertEquals(dto.getCont(), updated.getCont());
                assertEquals(dto.getComt(), updated.getComt());
            }
        }
    }

    @Nested
    @DisplayName("삭제 처리 관련 테스트")
    class DeleteTest {

        @DisplayName("n개 등록후 seq로 삭제")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_삭제(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardChangeHistoryDto dto : dummy) {
                assertEquals(1, boardChangeHistoryDao.deleteBySeq(dto.getSeq()));
                assertFalse(boardChangeHistoryDao.existsBySeq(dto.getSeq()));
            }

            assertEquals(0, boardChangeHistoryDao.count());
        }

        @DisplayName("n개 등록후 bno로 삭제")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_bno로_삭제(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            int totalCnt = boardChangeHistoryDao.countByBno(boardDto.getBno());
            int rowCnt = boardChangeHistoryDao.deleteByBno(boardDto.getBno());
            assertEquals(totalCnt, rowCnt);
        }

        @DisplayName("n개 등록후 전체 삭제")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_전체_삭제(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            int totalCnt = boardChangeHistoryDao.count();
            int rowCnt = boardChangeHistoryDao.deleteAll();
            assertEquals(totalCnt, rowCnt);
        }
    }

    @Nested
    @DisplayName("조회 처리 관련 테스트")
    class SelectTest {

        @DisplayName("n개 등록후 seq로 조회")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_seq로_조회(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardChangeHistoryDto dto : dummy) {
                BoardChangeHistoryDto selected = boardChangeHistoryDao.selectBySeq(dto.getSeq());
                assertNotNull(selected);
                assertEquals(dto.getSeq(), selected.getSeq());
            }
        }

        @DisplayName("n개 등록후 bno로 조회")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_bno로_조회(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            List<BoardChangeHistoryDto> selected = boardChangeHistoryDao.selectByBno(boardDto.getBno());
            assertNotNull(selected);
            assertEquals(n, selected.size());
        }

        @DisplayName("n개 등록후 전체 조회")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록후_전체_조회(int n) {
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<n; i++) {
                BoardChangeHistoryDto dto = createChangeHistoryDto(i);
                assertEquals(1, boardChangeHistoryDao.insert(dto));
                dummy.add(dto);
            }

            List<BoardChangeHistoryDto> selected = boardChangeHistoryDao.selectAll();
            assertNotNull(selected);
            assertEquals(n, selected.size());
        }
    }

    private BoardChangeHistoryDto createChangeHistoryDto(int i) {
        return BoardChangeHistoryDto.builder()
                                    .bno(boardDto.getBno())
                                    .title("테스트용 제목" + i)
                                    .cont("테스트용 내용" + i)
                                    .comt("테스트용입니다.")
                                    .appl_begin("2025-01-14")
                                    .appl_end("2025-01-14")
                                    .reg_user_seq(REG_USER_SEQ)
                                    .reg_date(REG_DATE)
                                    .up_user_seq(UP_USER_SEQ)
                                    .up_date(UP_DATE)
                                    .build();
    }

    private BoardCategoryDto createCategoryDto() {
        return BoardCategoryDto.builder()
                            .cate_code(CATE_CODE)
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

    private BoardDto createBoardDto() {
        return BoardDto.builder()
                        .cate_code(CATE_CODE)
                        .user_seq(TEST_USER_SEQ)
                        .writer("홍길동")
                        .title("테스트용 제목")
                        .cont("테스트용 내용")
                        .reg_user_seq(REG_USER_SEQ)
                        .reg_date(REG_DATE)
                        .up_user_seq(UP_USER_SEQ)
                        .up_date(UP_DATE)
                        .build();
    }

}