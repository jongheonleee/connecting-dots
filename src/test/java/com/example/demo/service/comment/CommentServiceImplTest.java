package com.example.demo.service.comment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import com.example.demo.dto.comment.CommentDto;
import com.example.demo.dto.comment.CommentRequest;
import com.example.demo.dto.comment.CommentResponse;
import com.example.demo.global.error.exception.business.comment.CommentNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.board.impl.BoardDaoImpl;
import com.example.demo.repository.comment.impl.CommentDaoImpl;
import com.example.demo.repository.reply.impl.ReplyDaoImpl;
import com.example.demo.service.comment.impl.CommentChangeHistoryServiceImpl;
import com.example.demo.service.comment.impl.CommentServiceImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl sut;

    @Mock
    private BoardDaoImpl boardDaoImpl;

    @Mock
    private ReplyDaoImpl replyDaoImpl;

    @Mock
    private CommentChangeHistoryServiceImpl commentChangeHistoryServiceImpl;


    @Mock
    private CommentDaoImpl commentDaoImpl;

    @Mock
    private CustomFormatter formatter;

    @BeforeEach
    public void setUp() {
        assertNotNull(sut);
        assertNotNull(commentDaoImpl);
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
            when(boardDaoImpl.existsByBno(any())).thenReturn(true);
            when(commentDaoImpl.insert(any())).thenReturn(1);

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

            when(boardDaoImpl.existsByBno(any())).thenReturn(false);

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

            when(boardDaoImpl.existsByBno(any())).thenReturn(true);
            when(commentDaoImpl.insert(any())).thenReturn(0);

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
            // given
            CommentRequest request = CommentRequest.builder()
                    .cno(1)
                    .bno(1)
                    .cont("테스트용 댓글 내용")
                    .user_seq(1)
                    .writer("테스트용 작성자")
                    .build();

            CommentDto dto = new CommentDto();
            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDaoImpl.selectByCno(any())).thenReturn(dto);
            when(commentDaoImpl.update(any())).thenReturn(1);
            doNothing().when(commentChangeHistoryServiceImpl).modify(any());

            assertDoesNotThrow(() -> sut.modify(request));
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
            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(false);

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
            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDaoImpl.update(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modify(request));
        }

        // 댓글의 좋아요, 싫어요 업데이트 하는 경우

        @DisplayName("사용자가 특정 댓글의 좋아요 업데이트한다")
        @Test
        void it_correctly_work_when_user_click_like_button() {
            // given
            Integer cno = 1;

            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDaoImpl.increaseLikeCnt(any())).thenReturn(1);

            assertDoesNotThrow(
                    () -> sut.increaseLikeCnt(cno)
            );
        }

        @DisplayName("존재하지 않는 댓글인 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_not_exists_for_like() {
            // given
            Integer cno = 1;

            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(false);

            // then
            assertThrows(CommentNotFoundException.class, () -> sut.increaseLikeCnt(cno));
        }

        @DisplayName("RDBMS에 댓글 좋아요 업데이트가 실패한 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_like_update_failed() {
            // given
            Integer cno = 1;

            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDaoImpl.increaseLikeCnt(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.increaseLikeCnt(cno));
        }

        @DisplayName("사용자가 특정 댓글의 싫어요 업데이트한다")
        @Test
        void it_correctly_work_when_user_click_dislike_button() {
            // given
            Integer cno = 1;

            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDaoImpl.increaseDislikeCnt(any())).thenReturn(1);

            assertDoesNotThrow(
                    () -> sut.increaseDislikeCnt(cno)
            );
        }

        @DisplayName("존재하지 않는 댓글인 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_not_exists_for_dislike() {
            // given
            Integer cno = 1;

            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(false);

            // then
            assertThrows(CommentNotFoundException.class, () -> sut.increaseDislikeCnt(cno));
        }

        @DisplayName("RDBMS에 댓글 싫어요 업데이트가 실패한 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_dislike_update_failed() {
            // given
            Integer cno = 1;

            when(commentDaoImpl.existsByCnoForUpdate(any())).thenReturn(true);
            when(commentDaoImpl.increaseDislikeCnt(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.increaseDislikeCnt(cno));
        }
    }

    @Nested
    @DisplayName("사용자가 댓글을 삭제하는 경우")
    class sut_delete_comment_test {

        @DisplayName("사용자가 특정 댓글을 삭제한다")
        @Test
        void it_correctly_work_when_user_delete_comment() {
            // given
            Integer cno = 1;

            when(commentDaoImpl.deleteByCno(any())).thenReturn(1);

            assertDoesNotThrow(
                    () -> sut.remove(cno)
            );
        }


        @DisplayName("RDBMS에 댓글 삭제가 실패한 경우 예외가 발생한다")
        @Test
        void it_throws_exception_when_comment_delete_failed() {
            // given
            Integer cno = 1;

            when(commentDaoImpl.deleteByCno(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.remove(cno));
        }

        @DisplayName("사용자가 특정 게시글의 모든 댓글을 삭제한다")
        @Test
        void it_correctly_work_when_user_delete_all_comments() {
            // given
            Integer bno = 1;

            when(commentDaoImpl.countByBno(bno)).thenReturn(5);
            when(commentDaoImpl.deleteByBno(bno)).thenReturn(5);

            assertDoesNotThrow(
                    () -> sut.removeByBno(bno)
            );
        }

    }

    @Nested
    @DisplayName("사용자가 댓글을 조회 하는 경우")
    class sut_read_comment_test {


        @DisplayName("특정 게시글의 모든 댓글& 대댓글을 조회한다")
        @Test
        void it_correctly_work_when_user_read_all_comments() {

        }

        @DisplayName("특정 댓글을 조회한다")
        @Test
        void it_correctly_work_when_user_read_comment() {
            // given
            Integer cno = 1;
            CommentDto dto = CommentDto.builder()
                                        .cno(cno)
                                        .bno(1)
                                        .cont("테스트용 댓글 내용")
                                        .user_seq(1)
                                        .writer("테스트용 작성자")
                                        .like_cnt(0)
                                        .dislike_cnt(0)
                                        .build();

            CommentResponse expected = CommentResponse.builder()
                                                      .cno(cno)
                                                      .bno(1)
                                                      .cont("테스트용 댓글 내용")
                                                      .user_seq(1)
                                                      .writer("테스트용 작성자")
                                                      .like_cnt(0)
                                                      .dislike_cnt(0)
                                                      .build();
            when(commentDaoImpl.existsByCno(any())).thenReturn(true);
            when(commentDaoImpl.selectByCno(cno)).thenReturn(dto);

            CommentResponse actual = sut.readByCno(cno);

            assertNotNull(actual);

            assertEquals(expected.getCno(), actual.getCno());
            assertEquals(expected.getBno(), actual.getBno());
            assertEquals(expected.getCont(), actual.getCont());
            assertEquals(expected.getUser_seq(), actual.getUser_seq());
            assertEquals(expected.getWriter(), actual.getWriter());
            assertEquals(expected.getLike_cnt(), actual.getLike_cnt());
            assertEquals(expected.getDislike_cnt(), actual.getDislike_cnt());
        }
    }
}