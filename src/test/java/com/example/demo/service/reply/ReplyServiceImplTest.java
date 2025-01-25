package com.example.demo.service.reply;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.reply.ReplyChangeHistoryResponse;
import com.example.demo.dto.reply.ReplyDto;
import com.example.demo.dto.reply.ReplyRequest;
import com.example.demo.dto.reply.ReplyResponse;
import com.example.demo.global.error.exception.business.reply.ReplyNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.reply.ReplyDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReplyServiceImplTest {

    @InjectMocks
    private ReplyService sut;

    @Mock
    private ReplyDaoImpl replyDao;

    @Mock
    private ReplyChangeHistoryService replyChangeHistoryService;

    @Mock
    private CustomFormatter formatter;


    @Nested
    @DisplayName("대댓글 작성 관련 테스트")
    class sut_create_reply_test {


        @Test
        @DisplayName("사용자가 특정 댓글에 대댓글을 작성한다")
        void it_correctly_work_when_user_writes_reply() {
            // given
            ReplyRequest request = ReplyRequest.builder()
                                                .rcno(1)
                                                .cno(1)
                                                .bno(1)
                                                .cont("테스트용 대댓글입니다.")
                                                .writer("테스트용 유저")
                                                .user_seq(1)
                                                .build();
            ReplyResponse expected = ReplyResponse.builder()
                                                  .rcno(1)
                                                  .cno(1)
                                                  .bno(1)
                                                  .cont("테스트용 대댓글입니다.")
                                                  .writer("테스트용 유저")
                                                  .user_seq(1)
                                                  .build();
            // when
            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(formatter.getManagerSeq()).thenReturn(1);
            when(replyDao.insert(any())).thenReturn(1);

            // then
            ReplyResponse actual = sut.create(request);

            assertNotNull(actual);

            assertEquals(expected.getRcno(), actual.getRcno());
            assertEquals(expected.getCno(), actual.getCno());
            assertEquals(expected.getBno(), actual.getBno());
            assertEquals(expected.getCont(), actual.getCont());
            assertEquals(expected.getWriter(), actual.getWriter());
            assertEquals(expected.getUser_seq(), actual.getUser_seq());

        }

        @Test
        @DisplayName("DBMS에 정상적으로 적용되지 않는 경우 예외를 발생시킨다")
        void it_throws_exception_when_dbms_not_applied_correctly() {
            // given
            ReplyRequest request = ReplyRequest.builder()
                                                .rcno(1)
                                                .cno(1)
                                                .bno(1)
                                                .cont("테스트용 대댓글입니다.")
                                                .writer("테스트용 유저")
                                                .user_seq(1)
                                                .build();
            // when
            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(formatter.getManagerSeq()).thenReturn(1);
            when(replyDao.insert(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.create(request));
        }


    }

    @Nested
    @DisplayName("대댓글 수정 관련 테스트")
    class sut_modify_reply_test {

        @Test
        @DisplayName("사용자가 특정 대댓글을 수정한다")
        void it_correctly_work_when_user_modifies_reply() {
            // given
            ReplyRequest request = ReplyRequest.builder()
                                                .rcno(1)
                                                .cno(1)
                                                .bno(1)
                                                .cont("수정된 테스트용 대댓글입니다.")
                                                .writer("테스트용 유저")
                                                .user_seq(1)
                                                .build();
            ReplyDto dto = ReplyDto.builder()
                                   .rcno(1)
                                   .cno(1)
                                   .bno(1)
                                   .cont("수정된 테스트용 대댓글입니다.")
                                   .writer("테스트용 유저")
                                   .user_seq(1)
                                   .build();

            ReplyChangeHistoryResponse replyChangeHistoryResponse = ReplyChangeHistoryResponse.builder()
                                                                           .rcno(1)
                                                                           .bno(1)
                                                                           .cno(1)
                                                                           .bef_cont("테스트용 대댓글입니다.")
                                                                           .aft_cont("수정된 테스트용 대댓글입니다.")
                                                                           .appl_time("2021-08-01 00:00:00")
                                                                           .comt(null)
                                                                           .build();
            // when
            when(replyDao.existsByRcnoForUpdate(any())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(formatter.getManagerSeq()).thenReturn(1);
            when(replyDao.select(request.getRcno())).thenReturn(dto);
            when(replyChangeHistoryService.create(any())).thenReturn(replyChangeHistoryResponse);
            when(replyDao.update(any())).thenReturn(1);

            // then
            assertDoesNotThrow(() -> sut.modify(request));
        }

        @Test
        @DisplayName("사용자가 수정하려는 대댓글이 존재하지 않는 경우 예외를 발생시킨다.")
        void it_throws_exception_when_reply_doesnt_exists() {
            // given
            ReplyRequest request = ReplyRequest.builder()
                    .rcno(1)
                    .cno(1)
                    .bno(1)
                    .cont("수정된 테스트용 대댓글입니다.")
                    .writer("테스트용 유저")
                    .user_seq(1)
                    .build();
            // when
            when(replyDao.existsByRcnoForUpdate(any())).thenReturn(false);

            // then
            assertThrows(ReplyNotFoundException.class, () -> sut.modify(request));
        }

        @Test
        @DisplayName("DBMS에 정상적으로 적용되지 않는 경우 예외를 발생시킨다")
        void it_throws_exception_when_dbms_not_applied_correctly() {
            // given
            ReplyRequest request = ReplyRequest.builder()
                    .rcno(1)
                    .cno(1)
                    .bno(1)
                    .cont("수정된 테스트용 대댓글입니다.")
                    .writer("테스트용 유저")
                    .user_seq(1)
                    .build();
            ReplyDto dto = ReplyDto.builder()
                    .rcno(1)
                    .cno(1)
                    .bno(1)
                    .cont("수정된 테스트용 대댓글입니다.")
                    .writer("테스트용 유저")
                    .user_seq(1)
                    .build();

            ReplyChangeHistoryResponse replyChangeHistoryResponse = ReplyChangeHistoryResponse.builder()
                    .rcno(1)
                    .bno(1)
                    .cno(1)
                    .bef_cont("테스트용 대댓글입니다.")
                    .aft_cont("수정된 테스트용 대댓글입니다.")
                    .appl_time("2021-08-01 00:00:00")
                    .comt(null)
                    .build();
            // when
            when(replyDao.existsByRcnoForUpdate(any())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(formatter.getManagerSeq()).thenReturn(1);
            when(replyDao.select(request.getRcno())).thenReturn(dto);
            when(replyChangeHistoryService.create(any())).thenReturn(replyChangeHistoryResponse);
            when(replyDao.update(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modify(request));
        }

    }

    @Nested
    @DisplayName("대댓글 삭제 관련 테스트")
    class sut_delete_reply_test {

        @Test
        @DisplayName("사용자가 rcno로 특정 대댓글을 삭제한다")
        void it_correctly_work_when_user_deletes_reply_rcno() {
            Integer rcno = 1;
            when(replyDao.delete(rcno)).thenReturn(1);
            assertDoesNotThrow(() -> sut.remove(rcno));
        }

        @Test
        @DisplayName("DBMS에 정상적으로 적용되지 않는 경우 예외를 발생시킨다")
        void it_throws_exception_when_dbms_not_applied_correctly_rcno() {
            Integer rcno = 1;
            when(replyDao.delete(rcno)).thenReturn(0);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.remove(rcno));
        }

        @Test
        @DisplayName("사용자가 cno로 특정 댓글에 달려있는 모든 대댓글을 삭제한다")
        void it_correctly_work_when_user_deletes_all_replies_cno() {
            Integer cno = 1;
            when(replyDao.countByCno(cno)).thenReturn(5);
            when(replyDao.deleteByCno(cno)).thenReturn(5);
            assertDoesNotThrow(() -> sut.removeByCno(cno));
        }

        @Test
        @DisplayName("DBMS에 정상적으로 적용되지 않는 경우 예외를 발생시킨다")
        void it_throws_exception_when_dbms_not_applied_correctly_cno() {
            Integer cno = 1;
            when(replyDao.countByCno(cno)).thenReturn(5);
            when(replyDao.deleteByCno(cno)).thenReturn(4);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.removeByCno(cno));
        }

        @Test
        @DisplayName("사용자가 bno로 특정 게시글에 달린 모든 대댓글을 삭제한다")
        void it_correctly_work_when_user_deletes_all_replies_bno() {
            Integer bno = 1;
            when(replyDao.countByBno(bno)).thenReturn(5);
            when(replyDao.deleteByBno(bno)).thenReturn(5);
            assertDoesNotThrow(() -> sut.removeByBno(bno));
        }

        @Test
        @DisplayName("DBMS에 정상적으로 적용되지 않는 경우 예외를 발생시킨다")
        void it_throws_exception_when_dbms_not_applied_correctly_bno() {
            Integer bno = 1;
            when(replyDao.deleteByBno(bno)).thenReturn(5);
            when(replyDao.deleteByBno(bno)).thenReturn(4);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.removeByBno(bno));
        }

        @Test
        @DisplayName("사용자가 모든 대댓글을 삭제한다")
        void it_correctly_work_when_user_deletes_all_replies() {
            when(replyDao.count()).thenReturn(5);
            when(replyDao.deleteAll()).thenReturn(5);
            assertDoesNotThrow(() -> sut.removeAll());
        }

        @Test
        @DisplayName("DBMS에 정상적으로 적용되지 않는 경우 예외를 발생시킨다")
        void it_throws_exception_when_dbms_not_applied_correctly_all() {
            when(replyDao.count()).thenReturn(5);
            when(replyDao.deleteAll()).thenReturn(4);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.removeAll());
        }

    }

    @Nested
    @DisplayName("대댓글 조회 관련 테스트")
    class sut_select_reply_test {

        @Test
        @DisplayName("사용자가 rcno로 특정 대댓글을 조회한다.")
        void it_correctly_work_when_user_select_one_rcno() {
            Integer rcno = 1;
            ReplyDto dto = ReplyDto.builder()
                                   .rcno(1)
                                   .cno(1)
                                   .bno(1)
                                   .cont("테스트용 대댓글입니다.")
                                   .writer("테스트용 유저")
                                   .user_seq(1)
                                   .build();
            when(replyDao.select(rcno)).thenReturn(dto);
            ReplyResponse expected = ReplyResponse.builder()
                                                  .rcno(1)
                                                  .cno(1)
                                                  .bno(1)
                                                  .cont("테스트용 대댓글입니다.")
                                                  .writer("테스트용 유저")
                                                  .user_seq(1)
                                                  .build();


            when(replyDao.select(rcno)).thenReturn(dto);

            ReplyResponse actual = sut.readByRcno(rcno);

            assertNotNull(actual);

            assertEquals(expected.getRcno(), actual.getRcno());
            assertEquals(expected.getCno(), actual.getCno());
            assertEquals(expected.getBno(), actual.getBno());
            assertEquals(expected.getCont(), actual.getCont());
            assertEquals(expected.getWriter(), actual.getWriter());
            assertEquals(expected.getUser_seq(), actual.getUser_seq());
        }

        @Test
        @DisplayName("사용자가 rcno로 특정 대댓글을 조회할 때 대댓글이 존재하지 않는 경우 예외를 발생시킨다.")
        void it_throws_exception_when_reply_doesnt_exists_rcno() {
            Integer rcno = 1;
            when(replyDao.select(rcno)).thenReturn(null);
            assertThrows(ReplyNotFoundException.class, () -> sut.readByRcno(rcno));
        }

        @Test
        @DisplayName("사용자가 cno로 특정 댓글에 달린 모든 대댓글을 조회한다")
        void it_correctly_work_when_user_select_all_replies_cno() {
            Integer cno = 1;
            List<ReplyDto> dummy = new ArrayList<>();
            List<ReplyResponse> expected = new ArrayList<>();
            for (int i=0; i<5; i++) {
                ReplyDto dto = new ReplyDto();
                ReplyResponse response = new ReplyResponse();
                dummy.add(dto);
                expected.add(response);
            }

            when(replyDao.selectByCno(cno)).thenReturn(dummy);
            List<ReplyResponse> actual = sut.readByCno(cno);

            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
        }

        @Test
        @DisplayName("사용자가 모든 대댓글을 조회한다")
        void it_correctly_work_when_user_select_all_replies() {
            List<ReplyDto> dummy = new ArrayList<>();
            List<ReplyResponse> expected = new ArrayList<>();
            for (int i=0; i<5; i++) {
                ReplyDto dto = new ReplyDto();
                ReplyResponse response = new ReplyResponse();
                dummy.add(dto);
                expected.add(response);
            }

            when(replyDao.selectAll()).thenReturn(dummy);
            List<ReplyResponse> actual = sut.readAll();

            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());
        }
    }


}