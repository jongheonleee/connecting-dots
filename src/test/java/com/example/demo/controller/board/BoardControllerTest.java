package com.example.demo.controller.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardCategoryResponse;
import com.example.demo.dto.board.BoardDetailResponse;
import com.example.demo.dto.board.BoardMainDto;
import com.example.demo.dto.board.BoardMainResponse;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse;
import com.example.demo.dto.board.BoardUpdateRequest;
import com.example.demo.service.board.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WithMockUser
@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(boardService);
    }

    @Nested
    @DisplayName("GET /api/board/... 형태 요청 테스트")
    class sut_read_board_test {

        @Test
        @DisplayName("메인 페이지에서 조회 : GET api/board")
        void it_correctly_work_when_user_request_read_board_for_main() throws Exception {
            // given
            Integer page = 1, pageSize = 10;
            PageResponse response = createMainResponse(100, new SearchCondition(), pageSize);
            given(boardService.readForMain(page, pageSize))
                              .willReturn(response);

            // when & then
            mockMvc.perform(get("/api/board")
                            .param("page", "1")
                            .param("pageSize", "10")
                    )
                    .andDo(print())
                    .andExpectAll(status().isOk(),
                            jsonPath("$.totalCnt").value(100),
                            jsonPath("$.responses").isArray(),
                            jsonPath("$.responses[0].bno").exists(),
                            jsonPath("$.responses[0].title").exists(),
                            jsonPath("$.responses[0].writer").exists(),
                            jsonPath("$.responses[0].cate_name").exists(),
                            jsonPath("$.responses[0].reg_date").exists(),
                            jsonPath("$.responses[0].view_cnt").exists(),
                            jsonPath("$.responses[0].reco_cnt").exists(),
                            jsonPath("$.responses[0].thumb").exists(),
                            jsonPath("$.responses[0].comment_cnt").exists()
                    );
        }

        @Test
        @DisplayName("카테고리 별로 게시글 메인 페이지에서 조회 : GET api/board/{cate_code}")
        void it_correctly_work_when_user_request_read_board_for_category() throws Exception {
            // given
            Integer page = 1, pageSize = 10;
            String cate_code = "C001";

            PageResponse response = createMainResponse(100, new SearchCondition(), pageSize);
            given(boardService.readByCategoryForMain(cate_code, page, pageSize))
                              .willReturn(response);

            // when & then
            mockMvc.perform(get("/api/board/{cate_code}", cate_code)
                            .param("page", "1")
                            .param("pageSize", "10")
                            .param("cate_code", cate_code)
                    )
                    .andDo(print())
                    .andExpectAll(status().isOk(),
                            jsonPath("$.totalCnt").value(100),
                            jsonPath("$.responses").isArray(),
                            jsonPath("$.responses[0].bno").exists(),
                            jsonPath("$.responses[0].title").exists(),
                            jsonPath("$.responses[0].writer").exists(),
                            jsonPath("$.responses[0].cate_name").exists(),
                            jsonPath("$.responses[0].reg_date").exists(),
                            jsonPath("$.responses[0].view_cnt").exists(),
                            jsonPath("$.responses[0].reco_cnt").exists(),
                            jsonPath("$.responses[0].thumb").exists(),
                            jsonPath("$.responses[0].comment_cnt").exists()
                    );

        }

        @Test
        @DisplayName("검색으로 메인 페이지에서 조회 : GET api/board/search?...")
        void it_correctly_work_when_user_request_read_board_for_search() throws Exception {
            // given
            Integer page = 1, pageSize = 10;
            String searchOption = "TT", searchKeyword = "백엔드", sortOption = "1";

            PageResponse response = createMainResponse(100, new SearchCondition(), pageSize);
            given(boardService.readBySearchConditionForMain(any()))
                              .willReturn(response);

            // when & then
            mockMvc.perform(get("/api/board/search")
                            .param("page", "1")
                            .param("pageSize", "10")
                            .param("searchOption", searchOption)
                            .param("searchKeyword", searchKeyword)
                            .param("sortOption", sortOption)
                    )
                    .andDo(print())
                    .andExpectAll(status().isOk(),
                            jsonPath("$.totalCnt").value(100),
                            jsonPath("$.responses").isArray(),
                            jsonPath("$.responses[0].bno").exists(),
                            jsonPath("$.responses[0].title").exists(),
                            jsonPath("$.responses[0].writer").exists(),
                            jsonPath("$.responses[0].cate_name").exists(),
                            jsonPath("$.responses[0].reg_date").exists(),
                            jsonPath("$.responses[0].view_cnt").exists(),
                            jsonPath("$.responses[0].reco_cnt").exists(),
                            jsonPath("$.responses[0].thumb").exists(),
                            jsonPath("$.responses[0].comment_cnt").exists()
                    );

        }

        @Test
        @DisplayName("게시글 상세 페이지에서 조회 : GET api/board/detail/{bno}")
        void it_correctly_work_when_user_request_read_board_for_detail() throws Exception {
            // given
            Integer bno = 1;
            BoardDetailResponse response = createDetailResponse(bno, 1);

            given(boardService.readDetailByBno(bno))
                              .willReturn(response);

            // when & then
            mockMvc.perform(get("/api/board/detail/{bno}", bno))
                    .andDo(print())
                    .andExpectAll(status().isOk(),
                            jsonPath("$.bno").value(bno),
                            jsonPath("$.title").exists(),
                            jsonPath("$.writer").exists(),
                            jsonPath("$.view_cnt").exists(),
                            jsonPath("$.reco_cnt").exists(),
                            jsonPath("$.not_reco_cnt").exists(),
                            jsonPath("$.cont").exists(),
                            jsonPath("$.category").exists(),
                            jsonPath("$.boardImgs").isArray(),
                            jsonPath("$.comments").isArray()
                    );
        }

        @Test
        @DisplayName("프로필 페이지 전용 조회 : GET api/board/user")
        void it_correctly_work_when_user_request_read_board_for_profile() {
            // given
            // when
            // then
        }

        @Test
        @DisplayName("게시글 추천수 증가 : GET api/board/like/{bno}")
        void it_correctly_work_when_user_request_read_board_for_like() throws Exception {
            // given
            Integer bno = 1;
            willDoNothing().given(boardService).increaseReco(bno);

            // when & then
            mockMvc.perform(get("/api/board/like/{bno}", bno))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("게시글 비추천수 증가 : GET api/board/dislike/{bno}")
        void it_correctly_work_when_user_request_read_board_for_dislike() throws Exception {
            // given
            Integer bno = 1;
            willDoNothing().given(boardService).increaseNotReco(bno);

            // when & then
            mockMvc.perform(get("/api/board/dislike/{bno}", bno))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    @DisplayName("POST /api/board/... 형태 요청 테스트")
    class sut_create_board_test {

        @Test
        @DisplayName("게시글 작성 : POST api/board/create")
        void it_correctly_work_when_user_request_create_board() throws Exception {
            // given
            List<MultipartFile> files = new ArrayList<>();
            String boardData = objectMapper.writeValueAsString(
                    Map.of("title", "테스트용 게시글",
                            "writer", "최여늘",
                            "cate_code", "BC010101",
                            "cont", "테스트용 게시글 내용",
                            "user_seq", 1)
            );

            MockMultipartFile file1 = new MockMultipartFile("files", "image1.jpg", "image/jpeg", "image data".getBytes());
            MockMultipartFile file2 = new MockMultipartFile("files", "image2.jpg", "image/jpeg", "image data".getBytes());

            MockMultipartFile boardDataPart = new MockMultipartFile("boardData", "", "application/json", boardData.getBytes());

            BoardResponse response = BoardResponse.builder()
                    .bno(1)
                    .title("테스트용 게시글")
                    .writer("최여늘")
                    .user_seq(1)
                    .cate_code("BC010101")
                    .cont("테스트용 게시글 내용")
                    .build();

            given(boardService.create(any(BoardRequest.class), anyList()))
                    .willReturn(response);

            // when & then
            mockMvc.perform(multipart("/api/board/create")
                            .file(file1)
                            .file(file2)
                            .file(boardDataPart) // boardData를 part로 전달
                            .with(csrf())
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.bno").value(1))
                    .andExpect(jsonPath("$.title").value("테스트용 게시글"))
                    .andExpect(jsonPath("$.writer").value("최여늘"))
                    .andExpect(jsonPath("$.cate_code").value("BC010101"))
                    .andExpect(jsonPath("$.cont").value("테스트용 게시글 내용"));
        }



    }

    @Nested
    @DisplayName("PUT /api/board/... 형태 요청 테스트")
    class sut_update_board_test {

        @Test
        @DisplayName("게시글 수정 : PUT api/board/update")
        void it_correctly_work_when_user_request_update_board() throws Exception {
            // given
            List<MultipartFile> files = new ArrayList<>();
            String boardData = objectMapper.writeValueAsString(
                    Map.of( "bno", "1",
                            "title", "테스트용 게시글",
                            "writer", "최여늘",
                            "cate_code", "BC010101",
                            "cont", "테스트용 게시글 내용",
                            "user_seq", 1)
            );

            BoardResponse response = BoardResponse.builder()
                    .bno(1)
                    .title("테스트용 게시글")
                    .writer("최여늘")
                    .user_seq(1)
                    .cate_code("BC010101")
                    .cont("테스트용 게시글 내용")
                    .build();

            MockMultipartFile file1 = new MockMultipartFile("files", "image1.jpg", "image/jpeg", "image data".getBytes());
            MockMultipartFile file2 = new MockMultipartFile("files", "image2.jpg", "image/jpeg", "image data".getBytes());
            MockMultipartFile boardDataPart = new MockMultipartFile("boardData", "", "application/json", boardData.getBytes());

            // when
            willDoNothing().given(boardService).modify(any(BoardUpdateRequest.class), anyList());

            // then
            mockMvc.perform(multipart("/api/board/modify")
                            .file(file1)
                            .file(file2)
                            .file(boardDataPart) // boardData를 part로 전달
                            .with(csrf())
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    @DisplayName("DELETE /api/board/... 형태 요청 테스트")
    class sut_delete_board_test {

        @Test
        @DisplayName("게시글 삭제 : DELETE api/board/{bno}")
        void it_correctly_work_when_user_request_delete_board() {
            // given
            Integer bno = 1;

            // when
            willDoNothing().given(boardService).remove(bno);

            // then
            assertDoesNotThrow(() -> mockMvc.perform(delete("/api/board/{bno}", bno)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent()));
        }

    }



    private PageResponse createMainResponse(Integer totalCnt, SearchCondition sc, Integer pageSize) {
        List<BoardMainDto> dtos = new ArrayList<>();
        for (int i=0; i<pageSize; i++) {
            BoardMainDto dto = BoardMainDto.builder()
                                            .bno(i)
                                            .title("테스트용 게시글 " + i)
                                            .writer("최여늘" + i)
                                            .cate_name("테스트용 카테고리" + i)
                                            .reg_date("2025/01/27 12:00:00")
                                            .view_cnt(i * 5 + 10)
                                            .reco_cnt(i * 5 + 10)
                                            .thumb("thumb" + i)
                                            .comment_cnt(i * 3)
                                            .build();
            dtos.add(dto);
        }

        List<BoardMainResponse> responses = dtos.stream()
                .map(dto -> BoardMainResponse.builder()
                        .bno(dto.getBno())
                        .title(dto.getTitle())
                        .writer(dto.getWriter())
                        .cate_name(dto.getCate_name())
                        .reg_date(dto.getReg_date())
                        .view_cnt(dto.getView_cnt())
                        .reco_cnt(dto.getReco_cnt())
                        .thumb(dto.getThumb())
                        .comment_cnt(dto.getComment_cnt())
                        .build())
                .toList();

        return new PageResponse<BoardMainResponse>(totalCnt, sc, responses);
    }

    private BoardDetailResponse createDetailResponse(Integer bno, Integer i) {
        return BoardDetailResponse.builder()
                // 기본 게시글 정보
                .bno(bno)
                .user_seq(i)
                .title("테스트용 게시글 " + i)
                .writer("최여늘" + i)
                .view_cnt(i * 5 + 10)
                .reco_cnt(i * 5 + 10)
                .not_reco_cnt(i * 5 + 10)
                .cont("테스트용 게시글 내용 " + i)

                // 게시글 카테고리
                .category(BoardCategoryResponse.builder()
                        .cate_code("C001")
                        .name("테스트용 카테고리" + i)
                        .build())

                // 게시글 이미지
                .boardImgs(new ArrayList<>())

                // 게시글 댓글 & 대댓글
                .comments(new ArrayList<>())
                .build();
    }
}