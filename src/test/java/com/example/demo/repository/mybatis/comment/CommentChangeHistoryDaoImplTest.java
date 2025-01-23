package com.example.demo.repository.mybatis.comment;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.BoardCategory;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.comment.CommentChangeHistoryDto;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.reply.ReplyChangeHistoryDaoImpl;
import com.example.demo.repository.mybatis.reply.ReplyDaoImpl;
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
class CommentChangeHistoryDaoImplTest {

    @Autowired
    private CommentChangeHistoryDaoImpl sut;

    @Autowired
    private CommentDaoImpl commentDao;

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardCategoryDaoImpl boardCategoryDao;

    @Autowired
    private ReplyChangeHistoryDaoImpl replyChangeHistoryDao;

    @Autowired
    private ReplyDaoImpl replyDao;

    private BoardDto boardDto;
    private BoardCategoryDto boardCategoryDto;
    private CommentDto commentDto;

    private final Integer REG_USER_SEQ = 1;
    private final Integer UP_USER_SEQ = 1;
    private final String REG_DATE = "2025-01-14";
    private final String UP_DATE = "2025-01-14";
    private final String CATE_CODE = "BC101001";


    private final Integer TEST_USER_SEQ = 1;


    @BeforeEach
    public void setUp() {
        assertNotNull(sut);
        assertNotNull(commentDao);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryDao);
        assertNotNull(replyChangeHistoryDao);
        assertNotNull(replyDao);

        replyChangeHistoryDao.deleteAll();
        replyDao.deleteAll();

        commentDao.deleteAll();
        boardDao.deleteAll();
        sut.deleteAll();

        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, boardCategoryDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, commentDao.count());
        assertEquals(0, sut.count());

        // 임시 카테고리 생성 및 등록
        createCategoryDto();
        assertEquals(1, boardCategoryDao.insert(boardCategoryDto));

        // 임시 게시글 등록
        createBoardDto();
        assertEquals(1, boardDao.insert(boardDto));

        // 임시 댓글 등록
        createCommentDto(boardDto.getBno());
        assertEquals(1, commentDao.insert(commentDto));

    }

    @AfterEach
    public void tearDown() {
        replyChangeHistoryDao.deleteAll();
        replyDao.deleteAll();

        commentDao.deleteAll();
        boardDao.deleteAll();
        sut.deleteAll();

        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, boardCategoryDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, commentDao.count());
        assertEquals(0, sut.count());
    }

    @Nested
    @DisplayName("댓글 변경 이력 카운팅 및 존재 여부 테스트")
    class CountAndExistsTest {
        @DisplayName("댓글 변경 이력 n개 등록 후 카운팅 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_카운팅(int cnt) {
            Integer cno = commentDto.getCno();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
        }

        @DisplayName("댓글 변경 이력 n개 등록 후 cno로 카운팅 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_cno로_카운팅(int cnt) {
            Integer cno = commentDto.getCno();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByCno(cno));
        }

        @DisplayName("댓글 변경 이력 n개 등록 후 존재 여부 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_존재_여부(int cnt) {
            Integer cno = commentDto.getCno();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
            }

            assertTrue(sut.existsByCno(cno));
        }
    }

    @Nested
    @DisplayName("댓글 변경 이력 조회 테스트")
    class SelectTest {

        @DisplayName("댓글 변경 이력 n개 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_조회(int cnt) {
            Integer cno = commentDto.getCno();
            List<CommentChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<CommentChangeHistoryDto> actual = sut.selectByCno(cno);
            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
            actual.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto e = expected.get(i);
                CommentChangeHistoryDto a = actual.get(i);

                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getBef_cont(), a.getBef_cont());
                assertEquals(e.getAft_cont(), a.getAft_cont());
                assertEquals(e.getComt(), a.getComt());
            }
        }

        @DisplayName("댓글 변경 이력 n개 조회 후 최신 댓글 변경 이력 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_조회_후_최신_댓글_변경_이력_조회(int cnt) {
            Integer cno = commentDto.getCno();
            List<CommentChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            CommentChangeHistoryDto actual = sut.selectLatestByCno(cno);
            CommentChangeHistoryDto e = expected.get(cnt - 1);

            assertEquals(e.getSeq(), actual.getSeq());
            assertEquals(e.getBef_cont(), actual.getBef_cont());
            assertEquals(e.getAft_cont(), actual.getAft_cont());
            assertEquals(e.getComt(), actual.getComt());
        }

        @DisplayName("댓글 변경 이력 n개 조회 후 seq로 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_조회_후_seq로_조회(int cnt) {
            Integer cno = commentDto.getCno();
            List<CommentChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (CommentChangeHistoryDto e : expected) {
                CommentChangeHistoryDto actual = sut.select(e.getSeq());
                assertEquals(e.getSeq(), actual.getSeq());
                assertEquals(e.getBef_cont(), actual.getBef_cont());
                assertEquals(e.getAft_cont(), actual.getAft_cont());
                assertEquals(e.getComt(), actual.getComt());
            }
        }
    }

    @Nested
    @DisplayName("댓글 변경 이력 생성 테스트")
    class InsertTest {

        @DisplayName("댓글 변경 이력 n개 생성 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_생성(int cnt) {
            Integer cno = commentDto.getCno();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByCno(cno));
        }
    }

    @Nested
    @DisplayName("댓글 변경 이력 수정 테스트")
    class UpdateTest {

        @DisplayName("댓글 변경 이력 n개 수정 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_수정(int cnt) {
            Integer cno = commentDto.getCno();
            List<CommentChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (CommentChangeHistoryDto e : expected) {
                e.setBef_cont("수정된 댓글입니다.");
                e.setAft_cont("수정된 댓글입니다.");
                e.setComt("수정된 댓글입니다.");
                assertEquals(1, sut.update(e));
            }

            List<CommentChangeHistoryDto> actual = sut.selectByCno(cno);
            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
            actual.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto e = expected.get(i);
                CommentChangeHistoryDto a = actual.get(i);

                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getBef_cont(), a.getBef_cont());
                assertEquals(e.getAft_cont(), a.getAft_cont());
                assertEquals(e.getComt(), a.getComt());
            }
        }
    }

    @Nested
    @DisplayName("댓글 변경 이력 삭제 테스트")
    class DeleteTest {

        @DisplayName("댓글 변경 이력 n개 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_n_개_삭제(int cnt) {
            Integer cno = commentDto.getCno();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.countByCno(cno));

            assertEquals(cnt, sut.deleteByCno(cno));
            assertEquals(0, sut.countByCno(cno));
        }

        @DisplayName("댓글 변경 이력 seq 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 댓글_변경_이력_seq_삭제(int cnt) {
            Integer cno = commentDto.getCno();
            List<CommentChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (CommentChangeHistoryDto dto : expected) {
                assertEquals(1, sut.delete(dto.getSeq()));
            }

            assertEquals(0, sut.countByCno(cno));
            assertEquals(0, sut.count());
        }

        @DisplayName("댓글 변경 이력 전체 삭제 테스트")
        @Test
        void 댓글_변경_이력_전체_삭제() {
            Integer cno = commentDto.getCno();
            for (int i=0; i<10; i++) {
                CommentChangeHistoryDto dto = createCommentChangeHistoryDto(cno, i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(10, sut.countByCno(cno));
            assertEquals(10, sut.count());

            assertEquals(10, sut.deleteAll());
            assertEquals(0, sut.countByCno(cno));
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

    private void createCommentDto(Integer bno) {
        commentDto =  CommentDto.builder()
                                .bno(bno)
                                .cont("테스트용 댓글입니다.")
                                .user_seq(REG_USER_SEQ)
                                .writer("홍길동")
                                .reg_user_seq(REG_USER_SEQ)
                                .reg_date(REG_DATE)
                                .up_user_seq(UP_USER_SEQ)
                                .up_date(UP_DATE)
                                .build();
    }

    private CommentChangeHistoryDto createCommentChangeHistoryDto(Integer cno, Integer i) {
        return CommentChangeHistoryDto.builder()
                                    .bef_cont("테스트용 댓글입니다1." + i)
                                    .aft_cont("테스트용 댓글입니다2." + i)
                                    .appl_time(REG_DATE)
                                    .comt("테스트용입니다.")
                                    .reg_date(REG_DATE)
                                    .reg_user_seq(REG_USER_SEQ)
                                    .up_date(UP_DATE)
                                    .up_user_seq(UP_USER_SEQ)
                                    .cno(cno)
                                    .bno(1)
                                    .build();
    }
}