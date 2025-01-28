package com.example.demo.service.reply;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.reply.ReplyChangeHistoryDto;
import com.example.demo.dto.reply.ReplyChangeHistoryRequest;
import com.example.demo.dto.reply.ReplyChangeHistoryResponse;
import com.example.demo.global.error.exception.business.reply.ReplyChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.reply.ReplyChangeHistoryRepository;
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
class ReplyChangeHistoryServiceImplTest {

    @InjectMocks
    private ReplyChangeHistoryService sut;

    @Mock
    private ReplyChangeHistoryRepository replyChangeHistoryDao;

    @Mock
    private CustomFormatter formatter;


    @Nested
    @DisplayName("대댓글 변경 이력 생성 처리")
    class sut_create_test {

        @Test
        @DisplayName("사용자가 대댓글을 등록하면 초기에 대댓글 변경 이력을 생성한다.")
        void it_correctly_creates_reply_change_history_when_user_registers_reply() {
            // given
            Integer rcno = 1;
            ReplyChangeHistoryRequest request = ReplyChangeHistoryRequest.builder()
                                                                        .rcno(rcno)
                                                                        .bno(1)
                                                                        .cno(1)
                                                                        .bef_cont("테스트용 변경전 내용")
                                                                        .aft_cont("테스트용 변경후 내용")
                                                                        .appl_time("2021-08-01 00:00:00")
                                                                        .comt("테스트용 변경 이력 생성")
                                                                        .build();


            ReplyChangeHistoryResponse expected = ReplyChangeHistoryResponse.builder()
                                                                            .rcno(rcno)
                                                                            .bno(1)
                                                                            .cno(1)
                                                                            .bef_cont("테스트용 변경전 내용")
                                                                            .aft_cont("테스트용 변경후 내용")
                                                                            .appl_time("2021-08-01 00:00:00")
                                                                            .comt("테스트용 변경 이력 생성")
                                                                            .build();


            // when
            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(replyChangeHistoryDao.insert(any())).thenReturn(1);

            // then
            ReplyChangeHistoryResponse actual = sut.create(request);
            assertEquals(expected, actual);

            assertEquals(expected.getRcno(), actual.getRcno());
            assertEquals(expected.getBno(), actual.getBno());
            assertEquals(expected.getCno(), actual.getCno());
            assertEquals(expected.getBef_cont(), actual.getBef_cont());
            assertEquals(expected.getAft_cont(), actual.getAft_cont());
            assertEquals(expected.getAppl_time(), actual.getAppl_time());
            assertEquals(expected.getComt(), actual.getComt());
        }

        @Test
        @DisplayName("대댓글 변경 이력 등록시 DBMS 적용 실패시 예외를 던진다.")
        void it_throws_exception_when_dbms_application_fails_when_registering_reply_change_history() {
            // given
            ReplyChangeHistoryRequest request = ReplyChangeHistoryRequest.builder()
                                                                        .rcno(1)
                                                                        .bno(1)
                                                                        .cno(1)
                                                                        .bef_cont("테스트용 변경전 내용")
                                                                        .aft_cont("테스트용 변경후 내용")
                                                                        .appl_time("2021-08-01 00:00:00")
                                                                        .comt("테스트용 변경 이력 생성")
                                                                        .build();

            // when
            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(replyChangeHistoryDao.insert(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.create(request));
        }
    }

    @Nested
    @DisplayName("대댓글 변경 이력 수정 처리")
    class sut_modify_test {

        @Test
        @DisplayName("대댓글 변경 이력 내용을 수정한다")
        void it_correctly_modifies_reply_change_history() {
            // given
            Integer rcno = 1;
            ReplyChangeHistoryRequest request = ReplyChangeHistoryRequest.builder()
                    .rcno(rcno)
                    .bno(1)
                    .cno(1)
                    .bef_cont("테스트용 변경전 내용")
                    .aft_cont("테스트용 변경후 내용")
                    .appl_time("2021-08-01 00:00:00")
                    .comt("테스트용 변경 이력 생성")
                    .build();


            // when
            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(replyChangeHistoryDao.existsBySeqForUpdate(rcno)).thenReturn(true);
            when(replyChangeHistoryDao.update(any())).thenReturn(1);

            // then
            assertDoesNotThrow(() -> sut.modify(request));
        }

        @Test
        @DisplayName("대댓글 변경 이력이 존재하지 않으면 예외를 던진다.")
        void it_throws_exception_when_reply_change_history_does_not_exist() {
            // given
            ReplyChangeHistoryRequest request = ReplyChangeHistoryRequest.builder()
                    .rcno(1)
                    .bno(1)
                    .cno(1)
                    .bef_cont("테스트용 변경전 내용")
                    .aft_cont("테스트용 변경후 내용")
                    .appl_time("2021-08-01 00:00:00")
                    .comt("테스트용 변경 이력 생성")
                    .build();

            // when
            when(replyChangeHistoryDao.existsBySeqForUpdate(1)).thenReturn(false);

            // then
            assertThrows(ReplyChangeHistoryNotFoundException.class, () -> sut.modify(request));
        }



        @Test
        @DisplayName("대댓글 변경 이력 수정시 DBMS 적용 실패시 예외를 던진다.")
        void it_throws_exception_when_dbms_application_fails_when_modifying_reply_change_history() {
            // given
            ReplyChangeHistoryRequest request = ReplyChangeHistoryRequest.builder()
                    .rcno(1)
                    .bno(1)
                    .cno(1)
                    .bef_cont("테스트용 변경전 내용")
                    .aft_cont("테스트용 변경후 내용")
                    .appl_time("2021-08-01 00:00:00")
                    .comt("테스트용 변경 이력 생성")
                    .build();

            // when
            when(formatter.getCurrentDateFormat()).thenReturn("2021-08-01 00:00:00");
            when(replyChangeHistoryDao.existsBySeqForUpdate(1)).thenReturn(true);
            when(replyChangeHistoryDao.update(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modify(request));
        }
    }

    @Nested
    @DisplayName("대댓글 변경 이력 삭제 처리")
    class sut_delete_test {

        @Test
        @DisplayName("특정 대댓글 변경 이력을 삭제한다")
        void it_correctly_deletes_reply_change_history() {
            Integer seq = 1;
            when(replyChangeHistoryDao.deleteBySeq(seq)).thenReturn(1);
            assertDoesNotThrow(() -> sut.remove(seq));
        }


        @Test
        @DisplayName("특정 대댓글 변경 이력 삭제시 DBMS 적용 실패시 예외를 던진다.")
        void it_throws_exception_when_dbms_application_fails_when_deleting_reply_change_history() {
            Integer seq = 1;
            when(replyChangeHistoryDao.deleteBySeq(seq)).thenReturn(0);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.remove(seq));
        }

        @Test
        @DisplayName("사용자가 rcno로 대댓글 변경 이력을 모두 삭제한다")
        void it_correctly_deletes_all_reply_change_history_when_user_delete_rcno() {
            Integer rcno = 1;
            when(replyChangeHistoryDao.countByRcno(rcno)).thenReturn(5);
            when(replyChangeHistoryDao.deleteByRcno(rcno)).thenReturn(5);
            assertDoesNotThrow(() -> sut.removeByRcno(rcno));
        }

        @Test
        @DisplayName("사용자가 rcno로 대댓글 변경 이력을 모두 삭제시 DBMS 적용 실패시 예외를 던진다.")
        void it_throws_exception_when_dbms_application_fails_when_deleting_all_reply_change_history_when_user_delete_rcno() {
            Integer rcno = 1;
            when(replyChangeHistoryDao.countByRcno(rcno)).thenReturn(5);
            when(replyChangeHistoryDao.deleteByRcno(rcno)).thenReturn(4);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.removeByRcno(rcno));
        }

        @Test
        @DisplayName("사용자가 모든 대댓글 변경 이력을 삭제한다")
        void it_correctly_deletes_all_reply_change_history() {
            when(replyChangeHistoryDao.count()).thenReturn(5);
            when(replyChangeHistoryDao.deleteAll()).thenReturn(5);
            assertDoesNotThrow(() -> sut.removeAll());
        }

        @Test
        @DisplayName("사용자가 모든 대댓글 변경 이력을 삭제시 DBMS 적용 실패시 예외를 던진다.")
        void it_throws_exception_when_dbms_application_fails_when_deleting_all_reply_change_history() {
            when(replyChangeHistoryDao.count()).thenReturn(5);
            when(replyChangeHistoryDao.deleteAll()).thenReturn(4);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.removeAll());
        }
    }

    @Nested
    @DisplayName("대댓글 변경 이력 조회 처리")
    class sut_read_test {

        @Test
        @DisplayName("특정 대댓글 변경 이력을 조회한다")
        void it_correctly_reads_reply_change_history_seq() {
            Integer seq = 1;
            ReplyChangeHistoryDto dto = new ReplyChangeHistoryDto();
            when(replyChangeHistoryDao.selectBySeq(seq)).thenReturn(dto);
            assertDoesNotThrow(() -> sut.readBySeq(seq));
        }

        @Test
        @DisplayName("특정 대댓글 변경 이력 조회시 대댓글 변경 이력이 존재하지 않으면 예외를 던진다.")
        void it_throws_exception_when_reply_change_history_does_not_exist_seq() {
            Integer seq = 1;
            when(replyChangeHistoryDao.selectBySeq(seq)).thenReturn(null);
            assertThrows(ReplyChangeHistoryNotFoundException.class, () -> sut.readBySeq(seq));
        }

        @Test
        @DisplayName("사용자가 rcno로 대댓글 변경 이력을 조회한다")
        void it_correctly_reads_reply_change_history_rcno() {
            Integer rcno = 1;
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            when(replyChangeHistoryDao.selectByRcno(rcno)).thenReturn(expected);
            assertDoesNotThrow(() -> sut.readByRcno(rcno));
        }

        @Test
        @DisplayName("특정 대댓글 변경 이력을 조회한다")
        void it_correctly_reads_all_reply_change_history() {
            List<ReplyChangeHistoryDto> expected = new ArrayList<>();
            when(replyChangeHistoryDao.selectAll()).thenReturn(expected);
            assertDoesNotThrow(() -> sut.readAll());
        }

    }
}