package com.example.demo.controller.comment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.comment.CommentRequest;
import com.example.demo.dto.comment.CommentResponse;
import com.example.demo.service.comment.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(commentService);
    }

    @Nested
    @DisplayName("GET /api/comment... 형태의 요청 테스트 ")
    class sut_get_api_comment_test {

        @Test
        @DisplayName("특정 댓글 상세 조회 : GET /api/comment/{cno}")
        void it_correctly_work_when_read_detail_comment() throws Exception {
            // given
            Integer cno = 1;
            CommentResponse response = CommentResponse.builder()
                    .cno(cno)
                    .bno(1)
                    .like_cnt(10)
                    .dislike_cnt(5)
                    .cont("테스트용 댓글 내용")
                    .user_seq(1)
                    .writer("테스트용 작성자")
                    .build();
            // when
            given(commentService.readByCno(cno)).willReturn(response);

            // then
            mockMvc.perform(get("/api/comment/{cno}", cno))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cno").value(cno))
                    .andExpect(jsonPath("$.bno").value(1))
                    .andExpect(jsonPath("$.like_cnt").value(10))
                    .andExpect(jsonPath("$.dislike_cnt").value(5))
                    .andExpect(jsonPath("$.cont").value("테스트용 댓글 내용"))
                    .andExpect(jsonPath("$.user_seq").value(1))
                    .andExpect(jsonPath("$.writer").value("테스트용 작성자"));
        }

        @Test
        @DisplayName("특정 댓글 좋아요 처리 : GET /api/comment/like/{cno}")
        void it_correctly_work_when_increase_like() throws Exception {
            // given
            Integer cno = 1;
            // when
            willDoNothing().given(commentService).increaseLikeCnt(cno);

            // then
            mockMvc.perform(get("/api/comment/like/{cno}", cno))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("특정 댓글 싫어요 처리 : GET /api/comment/dislike/{cno}")
        void it_correctly_work_when_increase_dislike() throws Exception {
            // given
            Integer cno = 1;
            // when
            willDoNothing().given(commentService).increaseDislikeCnt(cno);

            // then
            mockMvc.perform(get("/api/comment/dislike/{cno}", cno))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("POST /api/comment... 형태의 요청 테스트 ")
    class sut_post_api_comment_test {

        @Test
        @DisplayName("댓글 생성 : POST /api/comment/create")
        void it_correctly_work_when_create_comment() throws Exception {
            // given
            CommentResponse response = CommentResponse.builder()
                    .cno(1)
                    .bno(1)
                    .like_cnt(0)
                    .dislike_cnt(0)
                    .cont("테스트용 댓글 내용")
                    .user_seq(1)
                    .writer("테스트용 작성자")
                    .build();

            CommentRequest request = CommentRequest.builder()
                    .bno(1)
                    .cont("테스트용 댓글 내용")
                    .user_seq(1)
                    .writer("테스트용 작성자")
                    .build();
            // when
            given(commentService.create(request)).willReturn(response);

            // then
            mockMvc.perform(post("/api/comment/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.cno").value(1))
                    .andExpect(jsonPath("$.bno").value(1))
                    .andExpect(jsonPath("$.like_cnt").value(0))
                    .andExpect(jsonPath("$.dislike_cnt").value(0))
                    .andExpect(jsonPath("$.cont").value("테스트용 댓글 내용"))
                    .andExpect(jsonPath("$.user_seq").value(1))
                    .andExpect(jsonPath("$.writer").value("테스트용 작성자"));
        }

        @Test
        @DisplayName("댓글 수정 : POST /api/comment/modify")
        void it_correctly_work_when_modify_comment() throws Exception {
            // given
            CommentRequest request = CommentRequest.builder()
                    .cno(1)
                    .bno(1)
                    .cont("테스트용 댓글 내용 수정")
                    .user_seq(1)
                    .writer("테스트용 작성자 수정")
                    .build();
            // when
            willDoNothing().given(commentService).modify(request);

            // then
            mockMvc.perform(post("/api/comment/modify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    @DisplayName("DELETE /api/comment... 형태의 요청 테스트 ")
    class sut_delete_api_comment_test {

        @Test
        @DisplayName("댓글 삭제 : DELETE /api/comment/{cno}")
        void it_correctly_work_when_remove_comment() throws Exception {
            // given
            Integer cno = 1;
            // when
            willDoNothing().given(commentService).remove(cno);

            // then
            mockMvc.perform(delete("/api/comment/{cno}", cno)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }
}