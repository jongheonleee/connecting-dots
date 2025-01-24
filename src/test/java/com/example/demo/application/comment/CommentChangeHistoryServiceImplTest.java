package com.example.demo.application.comment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.comment.CommentChangeHistoryDto;
import com.example.demo.dto.comment.CommentChangeHistoryRequest;
import com.example.demo.dto.comment.CommentChangeHistoryResponse;
import com.example.demo.global.error.exception.business.comment.CommentChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.comment.CommentChangeHistoryDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.List;
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
class CommentChangeHistoryServiceImplTest {

    @InjectMocks
    private CommentChangeHistoryServiceImpl sut;

    @Mock
    private CommentDaoImpl commentDao;

    @Mock
    private CommentChangeHistoryDaoImpl commentChangeHistoryDao;

    @Mock
    private CustomFormatter formatter;


    @Nested
    @DisplayName("댓글 변경 이력 등록 처리")
    class sut_create_comment_change_history_test {

        @DisplayName("사용자가 댓글을 등록하면 새로운 댓글 변경 이력을 등록한다.")
        @Test
        void it_correctly_creates_new_comment_change_history_when_user_add_comment() {
            // given
            CommentChangeHistoryRequest request = CommentChangeHistoryRequest.builder()
                                                                            .bef_cont("수정전 댓글 내용")
                                                                            .aft_cont("수정후 댓글 내용")
                                                                            .appl_time("2021-08-01 00:00:00")
                                                                            .cno(1)
                                                                            .bno(1)
                                                                            .build();

            CommentChangeHistoryResponse expected = CommentChangeHistoryResponse.builder()
                                                                               .seq(1)
                                                                               .bef_cont("수정전 댓글 내용")
                                                                               .aft_cont("수정후 댓글 내용")
                                                                               .appl_time("2021-08-01 00:00:00")
                                                                               .cno(1)
                                                                               .bno(1)
                                                                               .build();
            // when
            when(commentChangeHistoryDao.insert(any())).thenReturn(1);

            CommentChangeHistoryResponse actual = sut.create(request);

            // then
            assertNotNull(actual);

            assertEquals(expected.getBef_cont(), actual.getBef_cont());
            assertEquals(expected.getAft_cont(), actual.getAft_cont());
            assertEquals(expected.getAppl_time(), actual.getAppl_time());
            assertEquals(expected.getCno(), actual.getCno());
            assertEquals(expected.getBno(), actual.getBno());
        }

        @DisplayName("DBMS 등록 실패시 예외를 던진다")
        @Test
        void it_throws_exception_when_insert_fails() {
            // given
            CommentChangeHistoryRequest request = CommentChangeHistoryRequest.builder()
                                                                            .bef_cont("수정전 댓글 내용")
                                                                            .aft_cont("수정후 댓글 내용")
                                                                            .appl_time("2021-08-01 00:00:00")
                                                                            .cno(1)
                                                                            .bno(1)
                                                                            .build();

            // when
            when(commentChangeHistoryDao.insert(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.create(request));
        }

    }

    @Nested
    @DisplayName("댓글 변경 이력 조회 처리")
    class sut_read_comment_change_history_test {

        @DisplayName("사용자가 cno로 해당 댓글의 모든 변경 이력을 조회한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_read_comment_change_history_when_user_find_cno(int cnt) {
            List<CommentChangeHistoryDto> dummy = new ArrayList<>();
            List<CommentChangeHistoryResponse> expected = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                dummy.add(CommentChangeHistoryDto.builder()
                                                 .seq(i)
                                                 .bef_cont("수정전 댓글 내용")
                                                 .aft_cont("수정후 댓글 내용")
                                                 .appl_time("2021-08-01 00:00:00")
                                                 .cno(1)
                                                 .bno(1)
                                                 .build());
                expected.add(CommentChangeHistoryResponse.builder()
                                                         .seq(i)
                                                         .bef_cont("수정전 댓글 내용")
                                                         .aft_cont("수정후 댓글 내용")
                                                         .appl_time("2021-08-01 00:00:00")
                                                         .cno(1)
                                                         .bno(1)
                                                         .build());
            }

            Integer cno = 1;
            when(commentChangeHistoryDao.selectByCno(cno)).thenReturn(dummy);
            List<CommentChangeHistoryResponse> actual = sut.readByCno(cno);

            assertEquals(cnt, actual.size());
            for (int i=0; i<cnt; i++) {
                CommentChangeHistoryResponse e = expected.get(i);
                CommentChangeHistoryResponse a = actual.get(i);

                assertEquals(e.getSeq(), a.getSeq());
                assertEquals(e.getBef_cont(), a.getBef_cont());
                assertEquals(e.getAft_cont(), a.getAft_cont());
                assertEquals(e.getAppl_time(), a.getAppl_time());
                assertEquals(e.getCno(), a.getCno());
                assertEquals(e.getBno(), a.getBno());
            }
        }

        @DisplayName("사용자가 특정 seq로 해당 댓글의 변경 이력을 조회한다.")
        @Test
        void it_correctly_read_comment_change_history_when_user_find_seq() {
            Integer seq = 1;

            CommentChangeHistoryDto dto = new CommentChangeHistoryDto();
            when(commentChangeHistoryDao.select(seq)).thenReturn(dto);

            CommentChangeHistoryResponse actual = sut.readBySeq(seq);

            assertNotNull(actual);
        }

        @DisplayName("null로 조회가 되면 예외를 던진다")
        @Test
        void it_throws_exception_when_select_fails() {
            Integer seq = 1;
            when(commentChangeHistoryDao.select(seq)).thenReturn(null);
            assertThrows(CommentChangeHistoryNotFoundException.class, () -> sut.readBySeq(seq));
        }

    }

    @Nested
    @DisplayName("댓글 변경 이력 수정 처리")
    class sut_update_comment_change_history_test {

        @DisplayName("사용자가 댓글 변경 이력을 수정하면 해당 댓글 변경 이력을 수정한다.")
        @Test
        void it_correctly_updates_comment_change_history_when_user_update_comment_change_history() {
            // given
            CommentChangeHistoryRequest request = CommentChangeHistoryRequest.builder()
                    .seq(1)
                    .bef_cont("수정전 댓글 내용")
                    .aft_cont("수정후 댓글 내용")
                    .appl_time("2021-08-01 00:00:00")
                    .cno(1)
                    .bno(1)
                    .build();


            // when & then
            when(commentChangeHistoryDao.existsByCnoForUpdate(request.getCno())).thenReturn(true);
            when(commentChangeHistoryDao.update(any())).thenReturn(1);

            assertDoesNotThrow(()->sut.modify(request));
        }

        @DisplayName("수정할 댓글 변경 이력이 없으면 예외를 던진다")
        @Test
        void it_throws_exception_when_comment_change_history_not_found() {
            // given
            CommentChangeHistoryRequest request = CommentChangeHistoryRequest.builder()
                    .seq(1)
                    .bef_cont("수정전 댓글 내용")
                    .aft_cont("수정후 댓글 내용")
                    .appl_time("2021-08-01 00:00:00")
                    .cno(1)
                    .bno(1)
                    .build();

            // when
            when(commentChangeHistoryDao.existsByCnoForUpdate(request.getCno())).thenReturn(false);

            // then
            assertThrows(CommentChangeHistoryNotFoundException.class, () -> sut.modify(request));
        }

        @DisplayName("DBMS 수정 실패시 예외를 던진다")
        @Test
        void it_throws_exception_when_update_fails() {
            // given
            CommentChangeHistoryRequest request = CommentChangeHistoryRequest.builder()
                    .seq(1)
                    .bef_cont("수정전 댓글 내용")
                    .aft_cont("수정후 댓글 내용")
                    .appl_time("2021-08-01 00:00:00")
                    .cno(1)
                    .bno(1)
                    .build();

            // when
            when(commentChangeHistoryDao.existsByCnoForUpdate(request.getCno())).thenReturn(true);
            when(commentChangeHistoryDao.update(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modify(request));
        }
    }

    @Nested
    @DisplayName("댓글 변경 이력 삭제 처리")
    class sut_delete_comment_change_history_test {

        @DisplayName("사용자가 특정 seq로 해당 댓글 변경 이력을 삭제한다.")
        @Test
        void it_correctly_delete_comment_change_history_when_user_delete_comment_change_history() {
            // given
            Integer seq = 1;

            // when & then
            when(commentChangeHistoryDao.delete(seq)).thenReturn(1);

            assertDoesNotThrow(() -> sut.delete(seq));
        }

        @DisplayName("삭제할 댓글 변경 이력이 없으면 예외를 던진다")
        @Test
        void it_throws_exception_when_comment_change_history_not_found() {
            // given
            Integer seq = 1;

            // when
            when(commentChangeHistoryDao.delete(seq)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.delete(seq));
        }

        @DisplayName("사용자가 cno로 해당 댓글의 모든 변경 이력을 삭제한다.")
        @Test
        void it_correctly_delete_all_comment_change_history_when_user_delete_all_comment_change_history() {
            // given
            Integer cno = 1;

            // when & then
            when(commentChangeHistoryDao.countByCno(cno)).thenReturn(5);
            when(commentChangeHistoryDao.deleteByCno(cno)).thenReturn(5);

            assertDoesNotThrow(() -> sut.deleteByCno(cno));
        }

        @DisplayName("삭제할 댓글 변경 이력이 없으면 예외를 던진다")
        @Test
        void it_throws_exception_when_comment_change_history_not_found_when_delete_by_cno() {
            // given
            Integer cno = 1;

            // when
            when(commentChangeHistoryDao.countByCno(cno)).thenReturn(3);
            when(commentChangeHistoryDao.deleteByCno(cno)).thenReturn(10);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.deleteByCno(cno));
        }
    }

}