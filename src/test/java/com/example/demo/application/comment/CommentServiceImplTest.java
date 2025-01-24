package com.example.demo.application.comment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.comment.CommentDto;
import com.example.demo.dto.comment.CommentRequest;
import com.example.demo.dto.comment.CommentResponse;
import com.example.demo.global.error.exception.business.comment.CommentNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentChangeHistoryDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import com.example.demo.repository.mybatis.reply.ReplyDaoImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl sut;

    @Mock
    private BoardDaoImpl boardDao;

    @Mock
    private ReplyDaoImpl replyDao;

    @Mock
    private CommentChangeHistoryServiceImpl commentChangeHistoryService;


    @Mock
    private CommentDaoImpl commentDao;

    @Mock
    private CustomFormatter formatter;

    @BeforeEach
    public void setUp() {
        assertNotNull(sut);
        assertNotNull(commentDao);
        assertNotNull(formatter);
    }

    @Nested
    @DisplayName("사용자가 댓글을 등록하는 경우")
    class sut_create_comment_test {

        @DisplayName("한명의 사용자가 특정 게시글에 댓글을 등록한다")
        @Test
        void it_correctly_work_when_user_add_comment() {
            // given
            CommentRequest request = CommentRequest.builder()
                                                    .bno(1)
                                                    .cont("테스트용 댓글 내용")
                                                    .user_seq(1)
                                                    .writer("테스트용 작성자")
                                                    .build();
            CommentResponse expected = CommentResponse.builder()
                                                      .cno(1)
                                                      .bno(1)
                                                      .cont("테스트용 댓글 내용")
                                                      .user_seq(1)
                                                      .writer("테스트용 작성자")
                                                      .like_cnt(0)
                                                      .dislike_cnt(0)
                                                      .build();


            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(formatter.getManagerSeq()).thenReturn(1);
            when(boardDao.existsByBno(any())).thenReturn(true);
            when(commentDao.insert(any())).thenReturn(1);

            // when
            CommentResponse actual = sut.create(request);

            // then
            assertNotNull(actual);
            assertEquals(expected.getBno(), actual.getBno());
            assertEquals(expected.getCont(), actual.getCont());
            assertEquals(expected.getCont(), actual.getCont());
            assertEquals(expected.getWriter(), actual.getWriter());

        }

        @DisplayName("존재하지 않는 게시글인 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_board_not_exists() {
            // given
            CommentRequest request = CommentRequest.builder()
                                                    .bno(1)
                                                    .cont("테스트용 댓글 내용")
                                                    .user_seq(1)
                                                    .writer("테스트용 작성자")
                                                    .build();

            when(boardDao.existsByBno(any())).thenReturn(false);

            // when
            // then
            assertThrows(RuntimeException.class, () -> sut.create(request));
        }

        @DisplayName("RDBMS에 댓글 등록이 실패한 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_insert_failed() {
            // given
            CommentRequest request = CommentRequest.builder()
                                                    .bno(1)
                                                    .cont("테스트용 댓글 내용")
                                                    .user_seq(1)
                                                    .writer("테스트용 작성자")
                                                    .build();

            when(boardDao.existsByBno(any())).thenReturn(true);
            when(commentDao.insert(any())).thenReturn(0);

            // when
            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.create(request));
        }
    }

    @Nested
    @DisplayName("사용자가 댓글을 수정하는 경우")
    class sut_update_comment_test {

        // 댓글 내용 수정하는 경우
        @DisplayName("사용자가 특정 댓글을 수정한다")
        @Test
        void it_correctly_work_when_user_update_comment() {

        }

        @DisplayName("존재하지 않는 댓글인 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_not_exists() {
            // given
            CommentRequest request = CommentRequest.builder()
                                                    .cno(1)
                                                    .bno(1)
                                                    .cont("테스트용 댓글 내용")
                                                    .user_seq(1)
                                                    .writer("테스트용 작성자")
                                                    .build();

            // when
            when(commentDao.existsByCnoForUpdate(any())).thenReturn(false);

            // then
            assertThrows(CommentNotFoundException.class, () -> sut.modify(request));
        }

        @DisplayName("RDBMS에 댓글 수정이 실패한 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_update_failed() {
            // given
            CommentRequest request = CommentRequest.builder()
                                                    .cno(1)
                                                    .bno(1)
                                                    .cont("테스트용 댓글 내용")
                                                    .user_seq(1)
                                                    .writer("테스트용 작성자")
                                                    .build();

            // when
            when(commentDao.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDao.update(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modify(request));
        }

        // 댓글의 좋아요, 싫어요 업데이트 하는 경우

        @DisplayName("사용자가 특정 댓글의 좋아요 업데이트한다")
        @Test
        void it_correctly_work_when_user_click_like_button() {
            // given
            Integer cno = 1;

            when(commentDao.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDao.increaseLikeCnt(any())).thenReturn(1);

            assertDoesNotThrow(
                    () -> sut.increaseLikeCnt(cno)
            );
        }

        @DisplayName("존재하지 않는 댓글인 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_not_exists_for_like() {
            // given
            Integer cno = 1;

            when(commentDao.existsByCnoForUpdate(any())).thenReturn(false);

            // then
            assertThrows(CommentNotFoundException.class, () -> sut.increaseLikeCnt(cno));
        }

        @DisplayName("RDBMS에 댓글 좋아요 업데이트가 실패한 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_like_update_failed() {
            // given
            Integer cno = 1;

            when(commentDao.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDao.increaseLikeCnt(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.increaseLikeCnt(cno));
        }

        @DisplayName("사용자가 특정 댓글의 싫어요 업데이트한다")
        @Test
        void it_correctly_work_when_user_click_dislike_button() {
            // given
            Integer cno = 1;

            when(commentDao.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDao.increaseDislikeCnt(any())).thenReturn(1);

            assertDoesNotThrow(
                    () -> sut.increaseDislikeCnt(cno)
            );
        }

        @DisplayName("존재하지 않는 댓글인 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_not_exists_for_dislike() {
            // given
            Integer cno = 1;

            when(commentDao.existsByCnoForUpdate(any())).thenReturn(false);

            // then
            assertThrows(CommentNotFoundException.class, () -> sut.increaseDislikeCnt(cno));
        }

        @DisplayName("RDBMS에 댓글 싫어요 업데이트가 실패한 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_dislike_update_failed() {
            // given
            Integer cno = 1;

            when(commentDao.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDao.increaseDislikeCnt(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.increaseDislikeCnt(cno));
        }
    }
}