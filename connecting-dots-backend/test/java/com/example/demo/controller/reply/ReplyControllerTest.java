package com.example.demo.controller.reply;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.will;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.example.demo.dto.reply.ReplyRequest;
import com.example.demo.dto.reply.ReplyResponse;
import com.example.demo.service.reply.ReplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(ReplyController.class)
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReplyService replyService;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(replyService);
    }

    @Nested
    @DisplayName("GET /api/reply... 형태의 요청 테스트 ")
    class sut_get_api_reply_test {

        @Test
        @DisplayName("특정 대댓글 상세 조회 : GET /api/reply/{rcno}")
        void it_correctly_work_when_read_detail_reply() throws Exception {
            // given
            Integer rcno = 1;
            ReplyResponse response = ReplyResponse.builder()
                    .rcno(rcno)
                    .cno(1)
                    .bno(1)
                    .cont("테스트용 대댓글")
                    .like_cnt(0)
                    .dislike_cnt(0)
                    .user_seq(1)
                    .writer("테스트용 작성자")
                    .build();

            // when
            given(replyService.readByRcno(rcno)).willReturn(response);

            // then
            mockMvc.perform(get("/api/reply/{rcno}", rcno))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.rcno").value(rcno),
                            jsonPath("$.cno").value(1),
                            jsonPath("$.bno").value(1),
                            jsonPath("$.cont").value("테스트용 대댓글"),
                            jsonPath("$.like_cnt").value(0),
                            jsonPath("$.dislike_cnt").value(0),
                            jsonPath("$.user_seq").value(1),
                            jsonPath("$.writer").value("테스트용 작성자")
                    );
        }

        @Test
        @DisplayName("특정 대댓글 좋아요 처리 : GET /api/reply/like/{rcno}")
        void it_correctly_work_when_increase_like_cnt() throws Exception {
            // given
            Integer rcno = 1;

            // when
            willDoNothing().given(replyService).increaseLikeCnt(rcno);

            // then
            mockMvc.perform(get("/api/reply/like/{rcno}", rcno))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("특정 대댓글 싫어요 처리 : GET /api/reply/dislike/{rcno}")
        void it_correctly_work_when_increase_dislike_cnt() throws Exception {
            // given
            Integer rcno = 1;

            // when
            willDoNothing().given(replyService).increaseDislikeCnt(rcno);

            // then
            mockMvc.perform(get("/api/reply/dislike/{rcno}", rcno))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("POST /api/reply... 형태의 요청 테스트 ")
    class sut_post_api_reply_test {

        @Test
        @DisplayName("대댓글 생성 : POST /api/reply/create")
        void it_correctly_work_when_create_reply() throws Exception {
            // given
            ReplyRequest request = ReplyRequest.builder()
                    .bno(1)
                    .cno(1)
                    .cont("테스트용 대댓글")
                    .user_seq(1)
                    .writer("테스트용 작성자")
                    .build();

            ReplyResponse response = ReplyResponse.builder()
                    .rcno(1)
                    .cno(1)
                    .bno(1)
                    .cont("테스트용 대댓글")
                    .like_cnt(0)
                    .dislike_cnt(0)
                    .user_seq(1)
                    .writer("테스트용 작성자")
                    .build();

            // when
            given(replyService.create(request)).willReturn(response);

            // then
            mockMvc.perform(post("/api/reply/create")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request))
                    .with(csrf()))
                    .andDo(print())
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.rcno").value(1),
                            jsonPath("$.cno").value(1),
                            jsonPath("$.bno").value(1),
                            jsonPath("$.cont").value("테스트용 대댓글"),
                            jsonPath("$.like_cnt").value(0),
                            jsonPath("$.dislike_cnt").value(0),
                            jsonPath("$.user_seq").value(1),
                            jsonPath("$.writer").value("테스트용 작성자")
                    );
        }

        @Test
        @DisplayName("대댓글 수정 : POST /api/reply/modify")
        void it_correctly_work_when_modify_reply() throws Exception {
            // given
            ReplyRequest request = ReplyRequest.builder()
                    .rcno(1)
                    .bno(1)
                    .cno(1)
                    .cont("테스트용 대댓글 수정")
                    .user_seq(1)
                    .writer("테스트용 작성자")
                    .build();

            // when
            willDoNothing().given(replyService).modify(request);

            // then
            mockMvc.perform(post("/api/reply/modify")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request))
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("DELETE /api/reply... 형태의 요청 테스트 ")
    class sut_delete_api_reply_test {
        @Test
        @DisplayName("대댓글 삭제 : DELETE /api/reply/{rcno}")
        void it_correctly_work_when_remove_reply() throws Exception {
            // given
            Integer rcno = 1;

            // when
            willDoNothing().given(replyService).remove(rcno);

            // then
            mockMvc.perform(delete("/api/reply/{rcno}", rcno)
                    .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }


}