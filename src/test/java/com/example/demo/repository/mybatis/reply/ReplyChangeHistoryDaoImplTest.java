package com.example.demo.repository.mybatis.reply;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.BoardCategory;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.dto.reply.ReplyChangeHistoryDto;
import com.example.demo.dto.reply.ReplyDto;
import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
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
class ReplyChangeHistoryDaoImplTest {

    @Autowired
    private ReplyChangeHistoryDaoImpl sut;

    @Autowired
    private ReplyDaoImpl replyDao;

    @Autowired
    private CommentDaoImpl commentDao;

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardCategoryDaoImpl boardCategoryDao;

    private BoardDto boardDto;
    private BoardCategoryDto boardCategoryDto;
    private CommentDto commentDto;
    private ReplyDto replyDto;

    private final Integer REG_USER_SEQ = 1;
    private final Integer UP_USER_SEQ = 1;
    private final String REG_DATE = "2025-01-14 00:00:00";
    private final String UP_DATE = "2025-01-14 00:00:00";
    private final String CATE_CODE = "BC101001";
    private final Integer TEST_USER_SEQ = 1;

    @BeforeEach
    void setUp() {
        assertNotNull(sut);
        assertNotNull(replyDao);
        assertNotNull(commentDao);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryDao);

        sut.deleteAll();
        replyDao.deleteAll();
        commentDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, sut.count());
        assertEquals(0, replyDao.count());
        assertEquals(0, commentDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());

        // 임시 카테고리 생성 및 등록
        createCategoryDto();
        assertEquals(1, boardCategoryDao.insert(boardCategoryDto));

        // 임시 게시글 등록
        createBoardDto();
        assertEquals(1, boardDao.insert(boardDto));

        // 임시 댓글 등록
        createCommentDto(boardDto.getBno());
        assertEquals(1, commentDao.insert(commentDto));

        // 임시 답글 등록
        createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
        assertEquals(1, replyDao.insert(replyDto));
    }

    @AfterEach
    void clean() {
        sut.deleteAll();
        replyDao.deleteAll();
        commentDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, sut.count());
        assertEquals(0, replyDao.count());
        assertEquals(0, commentDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());
    }

    @Nested
    @DisplayName("카운팅 및 존재 여부 확인 관련 테스트")
    class countAndExists {


        @DisplayName("n개 답글 변경 이력 등록 후 카운팅 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_답글_변경_이력_등록_후_카운팅(int cnt) {
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
            }

            assertEquals(cnt, sut.count());
        }

        @DisplayName("답글 변경 이력이 존재하는지 확인")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 답글_변경_이력이_존재하는지_확인(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (ReplyChangeHistoryDto dto : expected) {
                assertTrue(sut.existsBySeq(dto.getSeq()));
                assertTrue(sut.existsBySeqForUpdate(dto.getSeq()));
            }
        }
    }

    @Nested
    @DisplayName("답글 변경 이력 등록 관련 테스트")
    class InsertTest {

        @DisplayName("n개 답글 변경 이력 등록 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_답글_변경_이력_등록(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReplyChangeHistoryDto> actual = sut.selectAll();
            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq() - b.getSeq());
            actual.sort((a, b) -> a.getSeq() - b.getSeq());

            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto e = expected.get(i);
                ReplyChangeHistoryDto a = actual.get(i);
                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getBef_cont(), a.getBef_cont());
                assertEquals(e.getAft_cont(), a.getAft_cont());
                assertEquals(e.getComt(), a.getComt());
                assertEquals(e.getReg_user_seq(), a.getReg_user_seq());
                assertEquals(e.getReg_date(), a.getReg_date());
                assertEquals(e.getUp_user_seq(), a.getUp_user_seq());
                assertEquals(e.getUp_date(), a.getUp_date());
            }
        }
    }

    @Nested
    @DisplayName("답글 변경 이력 조회 관련 테스트")
    class SelectTest {


        @DisplayName("n개 답글 변경 이력 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_답글_변경_이력_전체_조회(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReplyChangeHistoryDto> actual = sut.selectAll();
            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq() - b.getSeq());
            actual.sort((a, b) -> a.getSeq() - b.getSeq());

            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto e = expected.get(i);
                ReplyChangeHistoryDto a = actual.get(i);
                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getBef_cont(), a.getBef_cont());
                assertEquals(e.getAft_cont(), a.getAft_cont());
                assertEquals(e.getComt(), a.getComt());
                assertEquals(e.getReg_user_seq(), a.getReg_user_seq());
                assertEquals(e.getReg_date(), a.getReg_date());
                assertEquals(e.getUp_user_seq(), a.getUp_user_seq());
                assertEquals(e.getUp_date(), a.getUp_date());
            }
        }

        @DisplayName("답글 변경 이력 seq 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 답글_변경_이력_seq_조회(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            for (ReplyChangeHistoryDto dto : expected) {
                ReplyChangeHistoryDto actual = sut.selectBySeq(dto.getSeq());
                assertNotNull(actual);
                assertEquals(dto.getSeq(), actual.getSeq());
                assertEquals(dto.getCno(), actual.getCno());
                assertEquals(dto.getBno(), actual.getBno());
                assertEquals(dto.getBef_cont(), actual.getBef_cont());
                assertEquals(dto.getAft_cont(), actual.getAft_cont());
                assertEquals(dto.getComt(), actual.getComt());
                assertEquals(dto.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(dto.getReg_date(), actual.getReg_date());
                assertEquals(dto.getUp_user_seq(), actual.getUp_user_seq());
                assertEquals(dto.getUp_date(), actual.getUp_date());
            }
        }

        @DisplayName("답글 변경 이력 rcno 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 답글_변경_이력_rcno_조회(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReplyChangeHistoryDto> actual = sut.selectByRcno(replyDto.getRcno());

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getSeq() - b.getSeq());
            actual.sort((a, b) -> a.getSeq() - b.getSeq());

            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto e = expected.get(i);
                ReplyChangeHistoryDto a = actual.get(i);

                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getBef_cont(), a.getBef_cont());
                assertEquals(e.getAft_cont(), a.getAft_cont());
                assertEquals(e.getComt(), a.getComt());
                assertEquals(e.getReg_user_seq(), a.getReg_user_seq());
                assertEquals(e.getReg_date(), a.getReg_date());
                assertEquals(e.getUp_user_seq(), a.getUp_user_seq());
                assertEquals(e.getUp_date(), a.getUp_date());
            }
        }
    }

    @Nested
    @DisplayName("답글 변경 이력 수정 관련 테스트")
    class UpdateTest {

        @DisplayName("답글 변경 이력 수정 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 답글_변경_이력_수정(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReplyChangeHistoryDto> actual = sut.selectAll();
            assertEquals(cnt, actual.size());

            for (ReplyChangeHistoryDto dto : expected) {
                dto.setBef_cont("수정된 대댓글입니다.");
                dto.setAft_cont("수정된 대댓글입니다.");
                dto.setComt("수정된 변경 이력입니다.");
                assertEquals(1, sut.update(dto));
            }

            List<ReplyChangeHistoryDto> updated = sut.selectAll();
            assertEquals(cnt, updated.size());

            expected.sort((a, b) -> a.getSeq() - b.getSeq());
            updated.sort((a, b) -> a.getSeq() - b.getSeq());

            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto e = expected.get(i);
                ReplyChangeHistoryDto a = updated.get(i);

                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getBef_cont(), a.getBef_cont());
                assertEquals(e.getAft_cont(), a.getAft_cont());
                assertEquals(e.getComt(), a.getComt());
                assertEquals(e.getReg_user_seq(), a.getReg_user_seq());
                assertEquals(e.getReg_date(), a.getReg_date());
                assertEquals(e.getUp_user_seq(), a.getUp_user_seq());
                assertEquals(e.getUp_date(), a.getUp_date());
            }
        }
    }

    @Nested
    @DisplayName("답글 변경 이력 삭제 관련 테스트")
    class DeleteTest {

        @DisplayName("n개 답글 변경 이력 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_답글_변경_이력_삭제(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReplyChangeHistoryDto> actual = sut.selectAll();
            assertEquals(cnt, actual.size());

            for (ReplyChangeHistoryDto dto : expected) {
                assertEquals(1, sut.deleteBySeq(dto.getSeq()));
            }

            assertEquals(0, sut.count());
        }

        @DisplayName("답글 변경 이력 rcno 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 답글_변경_이력_rcno_삭제(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReplyChangeHistoryDto> actual = sut.selectAll();
            assertEquals(cnt, actual.size());

            assertEquals(cnt, sut.deleteByRcno(replyDto.getRcno()));
            assertEquals(0, sut.count());
        }

        @DisplayName("모든 답글 변경 이력 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 모든_답글_변경_이력_삭제(int cnt) {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyChangeHistoryDto dto = createReplyChangeHistoryDto(replyDto.getRcno(), replyDto.getCno(), replyDto.getBno(), i);
                assertEquals(1, sut.insert(dto));
                expected.add(dto);
            }

            List<ReplyChangeHistoryDto> actual = sut.selectAll();
            assertEquals(cnt, actual.size());

            assertEquals(cnt, sut.deleteAll());
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

    private void createReplyDto(Integer bno, Integer cno, Integer user_seq) {
        replyDto = ReplyDto.builder()
                            .cno(cno)
                            .bno(bno)
                            .user_seq(user_seq)
                            .cont("테스트용 대댓글입니다.")
                            .user_seq(TEST_USER_SEQ)
                            .writer("홍길동")
                            .reg_user_seq(REG_USER_SEQ)
                            .reg_date(REG_DATE)
                            .up_user_seq(UP_USER_SEQ)
                            .up_date(UP_DATE)
                            .build();
    }

    private ReplyChangeHistoryDto createReplyChangeHistoryDto(Integer rcno, Integer cno, Integer bno, int i) {
        return ReplyChangeHistoryDto.builder()
                                    .rcno(rcno)
                                    .cno(cno)
                                    .bno(bno)
                                    .bef_cont("테스트용 대댓글입니다.")
                                    .aft_cont("테스트용 수정된 대댓글입니다. " + i)
                                    .appl_time(REG_DATE)
                                    .comt("테스트용 변경 이력입니다.")
                                    .reg_user_seq(REG_USER_SEQ)
                                    .reg_date(REG_DATE)
                                    .up_user_seq(UP_USER_SEQ)
                                    .up_date(UP_DATE)
                                    .build();
    }

}