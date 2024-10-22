package com.example.demo.repository.mybatis.learning;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
class CommentDaoImplLearningTest {

    @Autowired
    private CommentDaoImpl target;

    @Autowired
    private BoardDaoImpl boardDao;

    private BoardFormDto boardFormDto = new BoardFormDto();

    private List<CommentResponseDto> fixture = new ArrayList<>();

    @BeforeEach
    void setUp() {
        assertNotNull(target);
        assertNotNull(boardDao);

        fixture.clear();
        target.deleteAll();

        createBoardFormDto();
        assertTrue(1 == boardDao.insert(boardFormDto));
    }


    /**
     *
     * 0. 카운팅
     * - 0. 예외 - x
     * - 1. 실패 - x
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 전체 카운팅
     *
     * 1. 등록
     * - 0. 예외
     *  - 0-0. 게시글 번호가 없는 경우
     *
     * - 1. 실패
     * - 2. 성공
     *
     * 2. 특정 게시글과 과련된 댓글 조회
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     * 3. 특정 댓글 조회
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     * 4. 전체 조회
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     * 5. 댓글 수정
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     * 6. 좋아요 증가
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     * 7. 싫어요 증가
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     * 8. 특정 게시글과 관련된 댓글 전체 삭제
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     * 9. 특정 댓글 삭제
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     * 10. 전체 삭제
     * - 0. 예외
     * - 1. 실패
     * - 2. 성공
     *
     */

    @DisplayName("게시글의 번호가 없는 상태로 댓글이 등록되면 어떻게 될까? -> DataIntegrityViolationException이 발생함")
    @Test
    public void learn1() {
        /**
         * - fk 제약 조건 위배로 인한 DataIntegrityViolationException 발생
         */
        var commentDto = createCommentDto(0);
        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(commentDto)
        );
    }

    @DisplayName("작성자가 없는 댓글이 등록되면 어떻게 될까? -> DataIntegrityViolationException이 발생함")
    @Test
    public void learn2() {
        /**
         * - not null 제약 조건 위배로 인한 DataIntegrityViolationException 발생
         */
        createBoardFormDto();
        assertTrue(1 == boardDao.insert(boardFormDto));
        var commentDto = createCommentDto(boardFormDto.getBno());
        commentDto.setWriter(null);

        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(commentDto)
        );
    }


    private CommentResponseDto createCommentDto(int bno) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setBno(bno);
        commentResponseDto.setWriter("writer");
        commentResponseDto.setContent("content");
        commentResponseDto.setReg_id("reg_id");
        commentResponseDto.setUp_id("up_id");
        return commentResponseDto;
    }

    private void createFixture(int bno, int cnt) {
        for (int i = 0; i < cnt; i++) {
            var commentDto = createCommentDto(bno);
            fixture.add(commentDto);
        }
    }


    private void createBoardFormDto() {
        boardFormDto = new BoardFormDto();

        boardFormDto.setCate_code("0101");
        boardFormDto.setId("id");
        boardFormDto.setTitle("title");
        boardFormDto.setWriter("writer");
        boardFormDto.setView_cnt(0);
        boardFormDto.setReco_cnt(0);
        boardFormDto.setNot_reco_cnt(0);
        boardFormDto.setContent("content");
        boardFormDto.setComt("comt");
        boardFormDto.setReg_date("2021-01-01");
        boardFormDto.setReg_id("reg_id");
        boardFormDto.setUp_date("2021-01-01");
        boardFormDto.setUp_id("up_id");

    }
}