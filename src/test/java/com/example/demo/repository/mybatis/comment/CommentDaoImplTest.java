package com.example.demo.repository.mybatis.comment;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.BoardCategory;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
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
class CommentDaoImplTest {

    @Autowired
    private CommentDaoImpl sut;

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardCategoryDaoImpl boardCategoryDao;

    private BoardDto boardDto;
    private BoardCategoryDto boardCategoryDto;

    private final Integer REG_USER_SEQ = 1;
    private final Integer UP_USER_SEQ = 1;
    private final String REG_DATE = "2025-01-14";
    private final String UP_DATE = "2025-01-14";
    private final String CATE_CODE = "BC101001";

    private final Integer page = 1;
    private final Integer pageSize = 10;

    private final Integer TEST_USER_SEQ = 1;

    @BeforeEach
    void setUp() {
        assertNotNull(sut);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryDao);

        boardDao.deleteAll();
        sut.deleteAll();

        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, boardDao.count());
        assertEquals(0, sut.count());
        assertEquals(0, boardCategoryDao.count());

        // 임시 카테고리 생성 및 등록
        createCategoryDto();
        assertEquals(1, boardCategoryDao.insert(boardCategoryDto));

        // 임시 게시글 등록
        createBoardDto();
        assertEquals(1, boardDao.insert(boardDto));
    }

    @AfterEach
    void clean() {
        sut.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, sut.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());
    }
    

    @Nested
    @DisplayName("댓글 조회 및 카운트 처리")
    class ReadAndCountTest {

        @DisplayName("댓글 전체 조회")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_전체_조회(int cnt) {
            List<CommentDto> expected = new ArrayList<>();
            Integer bno = boardDto.getBno();

            for (int i=0; i<cnt; i++) {
                CommentDto dto = createCommentDto(bno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<CommentDto> actual = sut.selectAll();
            assertEquals(expected.size(), actual.size());

            expected.sort((a, b) -> a.getCno().compareTo(b.getCno()));
            actual.sort((a, b) -> a.getCno().compareTo(b.getCno()));

            for (int i=0; i<expected.size(); i++) {
                CommentDto e = expected.get(i);
                CommentDto a = actual.get(i);

                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getWriter(), a.getWriter());
            }
        }

        @DisplayName("댓글 전체 카운트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_전체_카운트(int cnt) {
            Integer bno = boardDto.getBno();

            for (int i=0; i<cnt; i++) {
                CommentDto dto = createCommentDto(bno, i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
        }

        @DisplayName("게시글별 댓글 조회")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글별_댓글_조회(int cnt) {
            List<CommentDto> expected = new ArrayList<>();
            Integer bno = boardDto.getBno();

            for (int i=0; i<cnt; i++) {
                CommentDto dto = createCommentDto(bno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<CommentDto> actual = sut.selectByBno(bno);
            assertEquals(expected.size(), actual.size());

            expected.sort((a, b) -> a.getCno().compareTo(b.getCno()));
            actual.sort((a, b) -> a.getCno().compareTo(b.getCno()));

            for (int i=0; i<expected.size(); i++) {
                CommentDto e = expected.get(i);
                CommentDto a = actual.get(i);

                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getWriter(), a.getWriter());
            }
        }

        @DisplayName("게시글별 댓글 카운트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글별_댓글_카운트(int cnt) {
            Integer bno = boardDto.getBno();

            for (int i=0; i<cnt; i++) {
                CommentDto dto = createCommentDto(bno, i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByBno(bno));
        }



    }

    @Nested
    @DisplayName("댓글 등록 처리")
    class CreateTest {

        @DisplayName("댓글 n개 정상 등록")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_n개_정상_등록(int n) {
            Integer bno = boardDto.getBno();
            for (int i=0; i<n; i++) {
                CommentDto dto = createCommentDto(bno, i);
                assertEquals(1, sut.insert(dto));
            }
        }


    }

    @Nested
    @DisplayName("댓글 수정 관련 작업 처리")
    class UpdateTest {

        @DisplayName("댓글 n개 등록하고 n개 수정")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_n개_등록하고_n개_수정(int cnt) {
            Integer bno = boardDto.getBno();
            List<CommentDto> expected = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                CommentDto dto = createCommentDto(bno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<CommentDto> actual = sut.selectAll();

            for (int i=0; i<cnt; i++) {
                CommentDto dto = actual.get(i);
                dto.setCont("수정된 댓글입니다." + i);
                assertEquals(1, sut.update(dto));
            }

            List<CommentDto> updated = sut.selectAll();
            assertEquals(expected.size(), updated.size());

            expected.sort((a, b) -> a.getCno().compareTo(b.getCno()));
            updated.sort((a, b) -> a.getCno().compareTo(b.getCno()));

            for (int i=0; i<expected.size(); i++) {
                CommentDto e = expected.get(i);
                CommentDto a = updated.get(i);

                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getBno(), a.getBno());
                assertNotEquals(e.getCont(), a.getCont());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getWriter(), a.getWriter());
            }
        }
    }

    @Nested
    @DisplayName("댓글 삭제 관련 작업 처리")
    class DeleteTest {

        @DisplayName("댓글 n개 등록하고 n개 삭제")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_n개_등록하고_n개_삭제(int cnt) {
            Integer bno = boardDto.getBno();
            List<CommentDto> expected = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                CommentDto dto = createCommentDto(bno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (CommentDto commentDto : expected) {
                assertEquals(1, sut.deleteByCno(commentDto.getCno()));
            }

            assertEquals(0, sut.count());
        }


    }




    private void createCategoryDto() {
        boardCategoryDto = BoardCategoryDto.builder()
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

    private void createBoardDto() {
        boardDto = BoardDto.builder()
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

    private CommentDto createCommentDto(Integer bno, int i) {
        return CommentDto.builder()
                        .bno(bno)
                        .cont("테스트용 댓글입니다." + i)
                        .user_seq(REG_USER_SEQ)
                        .writer("홍길동")
                        .reg_user_seq(REG_USER_SEQ)
                        .reg_date(REG_DATE)
                        .up_user_seq(UP_USER_SEQ)
                        .up_date(UP_DATE)
                        .build();
    }

}