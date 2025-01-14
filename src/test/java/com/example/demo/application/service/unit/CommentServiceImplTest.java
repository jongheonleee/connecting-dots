package com.example.demo.application.service.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.demo.global.error.exception.business.comment.CommentFormInvalidException;
import com.example.demo.global.error.exception.business.comment.CommentNotFoundException;
import com.example.demo.global.error.exception.technology.InternalServerException;
import com.example.demo.application.comment.CommentServiceImpl;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.dto.comment.CommentRequestDto;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;


@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentDaoImpl commentDao;


    @InjectMocks
    private CommentServiceImpl target;


    private List<CommentResponseDto> fixture = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        assertNotNull(commentDao);
        assertNotNull(target);
    }

    @DisplayName("댓글 등록과정에서 DB에 정상적으로 반영되지 않은 경우 -> InternalServerError")
    @Test
    void test1() {
        var commentRequestDto = createCommentRequestDto(1, 1);
        when(commentDao.insert(commentRequestDto)).thenReturn(0);
        assertThrows(InternalServerException.class, () -> target.create(commentRequestDto));
    }

    @DisplayName("댓글 등록 입력 폼이 잘못된 경우 -> CommentFormInvalidException")
    @Test
    void test2() {
        var commentRequestDto = createCommentRequestDto(1, 1);
        when(commentDao.insert(commentRequestDto)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(CommentFormInvalidException.class, () -> target.create(commentRequestDto));
    }

    @DisplayName("존재하지 않는 댓글 번호로 댓글을 조회할 경우 -> CommentNotFoundException")
    @Test
    void test3() {
        int cno = 1;
        when(commentDao.selectByCno(cno)).thenReturn(null);
        assertThrows(CommentNotFoundException.class, () -> target.findByCno(cno));
    }

    @DisplayName("댓글 수정 과정에서 DB에 정상적으로 반영되지 않은 경우 -> InternalServerError")
    @Test
    void test4() {
        var commentRequestDto = createCommentRequestDto(1, 1);
        when(commentDao.update(commentRequestDto)).thenReturn(0);
        assertThrows(InternalServerException.class, () -> target.update(commentRequestDto));
    }

    @DisplayName("댓글 수정 입력 폼이 잘못된 경우 -> CommentFormInvalidException")
    @Test
    void test5() {
        var commentRequestDto = createCommentRequestDto(1, 1);
        when(commentDao.update(commentRequestDto)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(CommentFormInvalidException.class, () -> target.update(commentRequestDto));
    }

    private CommentResponseDto createCommentDto(int bno, int i) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setBno(bno);
        commentResponseDto.setWriter("writer" + i);
        commentResponseDto.setContent("content" + i);
        commentResponseDto.setReg_id("reg_id");
        commentResponseDto.setUp_id("up_id");
        return commentResponseDto;
    }

    private CommentRequestDto createCommentRequestDto(int bno, int i) {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setBno(bno);
        commentRequestDto.setWriter("writer" + i);
        commentRequestDto.setComment("content" + i);
        return commentRequestDto;
    }
}