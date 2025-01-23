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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
    private final String REG_DATE = "2025-01-14";
    private final String UP_DATE = "2025-01-14";
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

    }

    @Nested
    @DisplayName("답글 변경 이력 등록 관련 테스트")
    class InsertTest {

    }

    @Nested
    @DisplayName("답글 변경 이력 조회 관련 테스트")
    class SelectTest {

    }

    @Nested
    @DisplayName("답글 변경 이력 수정 관련 테스트")
    class UpdateTest {

    }

    @Nested
    @DisplayName("답글 변경 이력 삭제 관련 테스트")
    class DeleteTest {

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

}