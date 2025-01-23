package com.example.demo.repository.mybatis.reply;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.BoardCategory;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.dto.reply.ReplyDto;
import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
class ReplyDaoImplTest {

    @Autowired
    private ReplyDaoImpl sut;

    @Autowired
    private CommentDaoImpl commentDao;

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardCategoryDaoImpl boardCategoryDao;

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
    void setUp() {
        assertNotNull(sut);
        assertNotNull(commentDao);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryDao);

        sut.deleteAll();
        commentDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, sut.count());
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

    }

    @AfterEach
    void clean() {
        sut.deleteAll();
        commentDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, sut.count());
        assertEquals(0, commentDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());
    }


    @Nested
    @DisplayName("대댓글 카운팅 및 존재 여부 테스트")
    class CountAndExistsTest {

        @DisplayName("n개 등록 후 카운팅 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록_후_카운팅_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
            }

            assertEquals(cnt, sut.count());
        }

        @DisplayName("n개 등록 후 존재 여부 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_등록_후_rcon_존재_여부_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            for (ReplyDto replyDto : expected) {
                assertTrue(sut.existsByRcno(replyDto.getRcno()));
                assertTrue(sut.existsByRcnoForUpdate(replyDto.getRcno()));

            }
        }

    }

    @Nested
    @DisplayName("대댓글 등록 테스트")
    class InsertTest {

        @DisplayName("n개 대댓글 등록 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_대댓글_등록_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            List<ReplyDto> actual = sut.selectAll();

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));
            actual.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));

            for (int i=0; i<cnt; i++) {
                ReplyDto e = expected.get(i);
                ReplyDto a = actual.get(i);

                assertEquals(e.getRcno(), a.getRcno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getWriter(), a.getWriter());
            }
        }
    }

    @Nested
    @DisplayName("대댓글 조회 테스트")
    class SelectTest {


        @DisplayName("n개 대댓글 등록후 rcno로 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_대댓글_등록후_rcno로_조회_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            for (ReplyDto replyDto : expected) {
                ReplyDto actual = sut.select(replyDto.getRcno());
                assertEquals(replyDto.getRcno(), actual.getRcno());
                assertEquals(replyDto.getBno(), actual.getBno());
                assertEquals(replyDto.getCno(), actual.getCno());
                assertEquals(replyDto.getUser_seq(), actual.getUser_seq());
                assertEquals(replyDto.getCont(), actual.getCont());
                assertEquals(replyDto.getWriter(), actual.getWriter());
            }
        }

        @DisplayName("n개 대댓글 등록후 bno로 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_대댓글_등록후_bno로_조회_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            List<ReplyDto> actual = sut.selectByBno(boardDto.getBno());

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));
            actual.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));

            for (int i=0; i<cnt; i++) {
                ReplyDto e = expected.get(i);
                ReplyDto a = actual.get(i);

                assertEquals(e.getRcno(), a.getRcno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getWriter(), a.getWriter());
            }
        }

        @DisplayName("n개 대댓글 등록후 cno로 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_대댓글_등록후_cno로_조회_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            List<ReplyDto> actual = sut.selectByCno(commentDto.getCno());

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));
            actual.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));

            for (int i=0; i<cnt; i++) {
                ReplyDto e = expected.get(i);
                ReplyDto a = actual.get(i);

                assertEquals(e.getRcno(), a.getRcno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getWriter(), a.getWriter());
            }
        }

        @DisplayName("n개 대댓글 등록후 bno, cno로 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_대댓글_등록후_bno_cno로_조회_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("bno", boardDto.getBno());
            map.put("cno", commentDto.getCno());
            List<ReplyDto> actual = sut.selectByBnoAndCno(map);

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));
            actual.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));

            for (int i=0; i<cnt; i++) {
                ReplyDto e = expected.get(i);
                ReplyDto a = actual.get(i);

                assertEquals(e.getRcno(), a.getRcno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getWriter(), a.getWriter());
            }
        }
    }

    @Nested
    @DisplayName("대댓글 수정 테스트")
    class UpdateTest {

        @DisplayName("대댓글 수정 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 대댓글_수정_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            for (ReplyDto replyDto : expected) {
                replyDto.setCont("수정된 대댓글입니다.");
                replyDto.setWriter("임꺽정");
                assertEquals(1, sut.update(replyDto));
            }

            List<ReplyDto> actual = sut.selectAll();

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));
            actual.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));

            for (int i=0; i<cnt; i++) {
                ReplyDto e = expected.get(i);
                ReplyDto a = actual.get(i);

                assertEquals(e.getRcno(), a.getRcno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getWriter(), a.getWriter());
            }
        }

        @DisplayName("대댓글 추천수 증가 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 대댓글_추천수_증가_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            for (ReplyDto replyDto : expected) {
                assertEquals(1, sut.increaseRecoCnt(replyDto.getRcno()));
            }

            List<ReplyDto> actual = sut.selectAll();

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));
            actual.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));

            for (int i=0; i<cnt; i++) {
                ReplyDto e = expected.get(i);
                ReplyDto a = actual.get(i);

                assertEquals(e.getRcno(), a.getRcno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getWriter(), a.getWriter());
                assertEquals( 1, a.getLike_cnt());
            }
        }

        @DisplayName("대댓글 비추천수 증가 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 대댓글_비추천수_증가_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(), TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            for (ReplyDto replyDto : expected) {
                assertEquals(1, sut.increaseNotRecoCnt(replyDto.getRcno()));
            }

            List<ReplyDto> actual = sut.selectAll();

            assertEquals(cnt, actual.size());

            expected.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));
            actual.sort((a, b) -> a.getRcno().compareTo(b.getRcno()));

            for (int i=0; i<cnt; i++) {
                ReplyDto e = expected.get(i);
                ReplyDto a = actual.get(i);

                assertEquals(e.getRcno(), a.getRcno());
                assertEquals(e.getBno(), a.getBno());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getUser_seq(), a.getUser_seq());
                assertEquals(e.getCont(), a.getCont());
                assertEquals(e.getWriter(), a.getWriter());
                assertEquals( 1, a.getDislike_cnt());
            }
        }
    }

    @Nested
    @DisplayName("대댓글 삭제 테스트")
    class DeleteTest {

        @DisplayName("n개 대댓글 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_대댓글_삭제_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i = 0; i < cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(),
                        TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            for (ReplyDto replyDto : expected) {
                assertEquals(1, sut.delete(replyDto.getRcno()));
            }

            assertEquals(0, sut.count());
        }

        @DisplayName("n개 대댓글 삭제 후 bno로 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_대댓글_삭제_후_bno로_삭제_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i = 0; i < cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(),
                        TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            assertEquals(cnt, sut.deleteByBno(boardDto.getBno()));

            assertEquals(0, sut.count());
        }

        @DisplayName("n개 대댓글 삭제 후 cno로 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void n개_대댓글_삭제_후_cno로_삭제_테스트(int cnt) {
            List<ReplyDto> expected = new ArrayList<>();
            for (int i = 0; i < cnt; i++) {
                ReplyDto replyDto = createReplyDto(boardDto.getBno(), commentDto.getCno(),
                        TEST_USER_SEQ);
                assertEquals(1, sut.insert(replyDto));
                expected.add(replyDto);
            }

            assertEquals(cnt, sut.deleteByCno(commentDto.getCno()));

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

    private ReplyDto createReplyDto(Integer bno, Integer cno, Integer user_seq) {
        return ReplyDto.builder()
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

}