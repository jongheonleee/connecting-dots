package com.example.demo.application.service.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.demo.application.exception.comment.CommentFormInvalidException;
import com.example.demo.application.exception.comment.CommentNotFoundException;
import com.example.demo.application.exception.global.InternalServerError;
import com.example.demo.application.service.comment.CommentServiceImpl;
import com.example.demo.dto.comment.CommentDto;
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


    private List<CommentDto> fixture = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        assertNotNull(commentDao);
        assertNotNull(target);
    }

    @DisplayName("댓글 등록과정에서 DB에 정상적으로 반영되지 않은 경우 -> InternalServerError")
    @Test
    void test1() {
        CommentDto commentDto = createCommentDto(1, 1);
        when(commentDao.insert(commentDto)).thenReturn(0);
        assertThrows(InternalServerError.class, () -> target.insert(commentDto));
    }

    @DisplayName("댓글 등록 입력 폼이 잘못된 경우 -> CommentFormInvalidException")
    @Test
    void test2() {
        CommentDto commentDto = createCommentDto(1, 1);
        when(commentDao.insert(commentDto)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(CommentFormInvalidException.class, () -> target.insert(commentDto));
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
        CommentDto commentDto = createCommentDto(1, 1);
        when(commentDao.update(commentDto)).thenReturn(0);
        assertThrows(InternalServerError.class, () -> target.update(commentDto));
    }

    @DisplayName("댓글 수정 입력 폼이 잘못된 경우 -> CommentFormInvalidException")
    @Test
    void test5() {
        CommentDto commentDto = createCommentDto(1, 1);
        when(commentDao.update(commentDto)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(CommentFormInvalidException.class, () -> target.update(commentDto));
    }

    private CommentDto createCommentDto(int bno, int i) {
        CommentDto commentDto = new CommentDto();
        commentDto.setBno(bno);
        commentDto.setWriter("writer" + i);
        commentDto.setContent("content" + i);
        commentDto.setReg_id("reg_id");
        commentDto.setUp_id("up_id");
        return commentDto;
    }
}