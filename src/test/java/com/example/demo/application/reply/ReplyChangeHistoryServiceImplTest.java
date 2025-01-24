package com.example.demo.application.reply;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.reply.ReplyChangeHistoryRequest;
import com.example.demo.dto.reply.ReplyChangeHistoryResponse;
import com.example.demo.repository.mybatis.reply.ReplyChangeHistoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
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
    private ReplyChangeHistoryServiceImpl sut;

    @Mock
    private ReplyChangeHistoryDaoImpl replyChangeHistoryDao;

    @Mock
    private CustomFormatter formatter;


    @Nested
    @DisplayName("대댓글 변경 이력 생성 처리")
    class sut_create_test {

        @Test
        @DisplayName("사용자가 대댓글을 등록하면 초기에 대댓글 변경 이력을 생성한다.")
        void it_correctly_creates_reply_change_history_when_user_registers_reply() {
            // given
            ReplyChangeHistoryRequest request = ReplyChangeHistoryRequest.builder()
                                                                            .build();

            ReplyChangeHistoryResponse expected = new ReplyChangeHistoryResponse();

            // when

            // then
            ReplyChangeHistoryResponse actual = sut.create(request);
            assertEquals(expected, actual);


        }
    }
}