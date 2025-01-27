package com.example.demo.controller.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.example.demo.service.board.BoardCategoryService;
import com.example.demo.dto.board.BoardCategoryRequest;
import com.example.demo.global.error.exception.business.board.BoardCategoryAlreadyExistsException;
import com.example.demo.global.error.exception.business.board.BoardCategoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import org.springframework.security.test.context.support.WithMockUser;
import com.example.demo.dto.board.BoardCategoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

@WithMockUser
@WebMvcTest(BoardCategoryController.class)
class BoardCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardCategoryService boardCategoryService;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(boardCategoryService);
    }

    @Nested
    @DisplayName("GET /api/board-category/{cate_code}")
    class GetBoardCategory {

        @Test
        @DisplayName("존재하는 코드로 상세 조회 성공")
        void 존재하는_코드로_상세_조회_성공() throws Exception {
            given(boardCategoryService.readByCateCode(any()))
                    .willReturn(createResponse(1));

            mockMvc.perform(get("/api/board-category/AB010101"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.cate_code").value("AB010101"),
                            jsonPath("$.top_cate").value("AB010100"),
                            jsonPath("$.name").value("테스트용 카테고리1"),
                            jsonPath("$.comt").value("..."),
                            jsonPath("$.ord").value(2),
                            jsonPath("$.chk_use").value("Y"),
                            jsonPath("$.level").value(2)
                 );
        }

        @Test
        @DisplayName("존재하지 않는 코드로 상세 조회 실패")
        void 존재하지_않는_코드로_상세_조회_실패() throws Exception {
            given(boardCategoryService.readByCateCode(any()))
                    .willThrow(new BoardCategoryNotFoundException());

            mockMvc.perform(get("/api/board-category/AB010101"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/board-category/top/{top_cate}")
    class GetBoardCategoryList {

        @Test
        @DisplayName("존재하는 코드로 리스트 조회 성공")
        void 존재하는_코드로_리스트_조회_성공() throws Exception {
            given(boardCategoryService.readByTopCate(any()))
                    .willReturn(List.of(createResponse(1), createResponse(2)));

            mockMvc.perform(get("/api/board-category/top/AB010100"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$[0].cate_code").value("AB010101"),
                            jsonPath("$[0].top_cate").value("AB010100"),
                            jsonPath("$[0].name").value("테스트용 카테고리1"),
                            jsonPath("$[0].comt").value("..."),
                            jsonPath("$[0].ord").value(2),
                            jsonPath("$[0].chk_use").value("Y"),
                            jsonPath("$[0].level").value(2),
                            jsonPath("$[1].cate_code").value("AB010102"),
                            jsonPath("$[1].top_cate").value("AB010100"),
                            jsonPath("$[1].name").value("테스트용 카테고리2"),
                            jsonPath("$[1].comt").value("..."),
                            jsonPath("$[1].ord").value(3),
                            jsonPath("$[1].chk_use").value("Y"),
                            jsonPath("$[1].level").value(3)
                    );
        }

        @Test
        @DisplayName("존재하지 않는 코드로 리스트 조회 실패")
        void 존재하지_않는_코드로_리스트_조회_실패() throws Exception {
            given(boardCategoryService.readByTopCate(any()))
                    .willThrow(new BoardCategoryNotFoundException());

            mockMvc.perform(get("/api/board-category/top/AB010100"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/board-category/all")
    class GetAllBoardCategory {

        @Test
        @DisplayName("모든 카테고리 조회 성공")
        void 모든_카테고리_조회_성공() throws Exception {
            given(boardCategoryService.readAll())
                    .willReturn(List.of(createResponse(1), createResponse(2)));

            mockMvc.perform(get("/api/board-category/all"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$[0].cate_code").value("AB010101"),
                            jsonPath("$[0].top_cate").value("AB010100"),
                            jsonPath("$[0].name").value("테스트용 카테고리1"),
                            jsonPath("$[0].comt").value("..."),
                            jsonPath("$[0].ord").value(2),
                            jsonPath("$[0].chk_use").value("Y"),
                            jsonPath("$[0].level").value(2),
                            jsonPath("$[1].cate_code").value("AB010102"),
                            jsonPath("$[1].top_cate").value("AB010100"),
                            jsonPath("$[1].name").value("테스트용 카테고리2"),
                            jsonPath("$[1].comt").value("..."),
                            jsonPath("$[1].ord").value(3),
                            jsonPath("$[1].chk_use").value("Y"),
                            jsonPath("$[1].level").value(3)
                    );
        }

        @Test
        @DisplayName("카테고리가 존재하지 않는 경우, 빈 리스트 반환")
        void 카테고리가_존재하지_않는_경우_빈_리스트_반환() throws Exception {
            given(boardCategoryService.readAll())
                    .willReturn(List.of());

            mockMvc.perform(get("/api/board-category/all"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isEmpty()
                    );
        }
    }

    @Nested
    @DisplayName("POST /api/board-category/create")
    class PostBoardCategory {

        @Test
        @DisplayName("카테고리 생성 성공")
        void 카테고리_생성_성공() throws Exception {
            given(boardCategoryService.create(any()))
                    .willReturn(createResponse(1));

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(post("/api/board-category/create")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.cate_code").value("AB010101"),
                            jsonPath("$.top_cate").value("AB010100"),
                            jsonPath("$.name").value("테스트용 카테고리1"),
                            jsonPath("$.comt").value("..."),
                            jsonPath("$.ord").value(2),
                            jsonPath("$.chk_use").value("Y"),
                            jsonPath("$.level").value(2)
                    );
        }

        @Test
        @DisplayName("전달받은 코드가 이미 존재하는 코드인 경우, 예외 발생 -> 409 CONFLICT")
        void 전달받은_코드가_이미_존재하는_코드인_경우_예외_발생() throws Exception {
            given(boardCategoryService.create(any()))
                    .willThrow(new BoardCategoryAlreadyExistsException());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(post("/api/board-category/create")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("유효하지 않은 값들로 전달받은 경우, 예외 발생 -> 400 BAD REQUEST")
        void 유효하지_않은_값들로_전달받은_경우_예외_발생() throws Exception {
            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "!@33",
                            "top_cate", "2 ",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(post("/api/board-category/create")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("DBMS 반영 실패로 인한 예외 발생 -> 500 INTERNAL SERVER ERROR")
        void DBMS_반영_실패로_인한_예외_발생() throws Exception {
            given(boardCategoryService.create(any()))
                    .willThrow(new NotApplyOnDbmsException());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(post("/api/board-category/create")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("PATCH /api/board-category/{cate_code}")
    class PatchBoardCategory {
        @Test
        @DisplayName("카테고리 수정 성공")
        void 카테고리_수정_성공() throws Exception {
            willDoNothing()
                    .given(boardCategoryService)
                    .modify(any());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(put("/api/board-category/AB010101")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(boardCategoryService).modify(any());
        }

        @Test
        @DisplayName("존재하지 않는 코드로 수정 시, 예외 발생 -> 404 NOT FOUND")
        void 존재하지_않는_코드로_수정_시_예외_발생() throws Exception {
            willThrow(new BoardCategoryNotFoundException())
                    .given(boardCategoryService)
                    .modify(any());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(put("/api/board-category/AB010101")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DBMS 반영 실패로 인한 예외 발생 -> 500 INTERNAL SERVER ERROR")
        void DBMS_반영_실패로_인한_예외_발생() throws Exception {
            willThrow(new NotApplyOnDbmsException())
                    .given(boardCategoryService)
                    .modify(any());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(put("/api/board-category/AB010101")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("유효하지 않은 값들로 전달받은 경우, 예외 발생 -> 400 BAD REQUEST")
        void 유효하지_않은_값들로_전달받은_경우_예외_발생() throws Exception {
            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "!@33",
                            "top_cate", "2 ",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(put("/api/board-category/AB010101")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("PATCH /api/board-category/usey/{cate_code}")
    class PatchBoardCategoryUseY {

        @Test
        @DisplayName("사용 중인 카테고리로 변경 성공")
        void 사용_중인_카테고리로_변경_성공() throws Exception {
            willDoNothing()
                    .given(boardCategoryService)
                    .modifyChkUseY(any());

            mockMvc.perform(patch("/api/board-category/usey/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(boardCategoryService).modifyChkUseY(any());
        }

        @Test
        @DisplayName("존재하지 않는 코드로 사용 중인 카테고리 변경 시, 예외 발생 -> 404 NOT FOUND")
        void 존재하지_않는_코드로_사용_중인_카테고리_변경_시_예외_발생() throws Exception {
            willThrow(new BoardCategoryNotFoundException())
                    .given(boardCategoryService)
                    .modifyChkUseY(any());

            mockMvc.perform(patch("/api/board-category/usey/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DBMS 반영 실패로 인한 예외 발생 -> 500 INTERNAL SERVER ERROR")
        void DBMS_반영_실패로_인한_예외_발생() throws Exception {
            willThrow(new NotApplyOnDbmsException())
                    .given(boardCategoryService)
                    .modifyChkUseY(any());

            mockMvc.perform(patch("/api/board-category/usey/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("DELETE /api/board-category")
    class DeleteBoardCategory {

        @Test
        @DisplayName("코드로 카테고리 삭제 성공")
        void 코드로_카테고리_삭제_성공() throws Exception {
            willDoNothing()
                    .given(boardCategoryService)
                    .remove(any());

            mockMvc.perform(delete("/api/board-category/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(boardCategoryService).remove(any());
        }

        @Test
        @DisplayName("존재하지 않는 코드로 삭제 시, 예외 발생 -> 404 NOT FOUND")
        void 존재하지_않는_코드로_삭제_시_예외_발생() throws Exception {
            willThrow(new BoardCategoryNotFoundException())
                    .given(boardCategoryService)
                    .remove(any());

            mockMvc.perform(delete("/api/board-category/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("모든 카테고리 삭제 성공")
        void 모든_카테고리_삭제_성공() throws Exception {
            willDoNothing()
                    .given(boardCategoryService)
                    .removeAll();

            mockMvc.perform(delete("/api/board-category/all")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(boardCategoryService).removeAll();
        }
    }

    private BoardCategoryResponse createResponse(int i) {
        return BoardCategoryResponse.builder()
                                    .cate_code("AB01010" + i)
                                    .top_cate("AB010100")
                                    .name("테스트용 카테고리" + i)
                                    .comt("...")
                                    .ord(1 + i)
                                    .chk_use("Y")
                                    .level(1 + i)
                                    .build();

    }

    private BoardCategoryRequest createRequest(int i) {
        return BoardCategoryRequest.builder()
                                    .cate_code("AB01010" + i)
                                    .top_cate("AB010100")
                                    .name("테스트용 카테고리" + i)
                                    .comt("...")
                                    .ord(1 + i)
                                    .chk_use("Y")
                                    .level(1 + i)
                                    .build();
    }
}