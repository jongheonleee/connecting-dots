package com.example.demo.controller.report;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import com.example.demo.dto.report.ReportCategoryResponse;
import com.example.demo.dto.report.ReportDetailResponse;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.dto.report.ReportRequest;
import com.example.demo.dto.report.ReportResponse;
import com.example.demo.global.error.exception.business.report.ReportCategoryNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.service.report.impl.ReportServiceImpl;
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
@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportServiceImpl reportServiceImpl;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(reportServiceImpl);
    }

    @Nested
    @DisplayName("GET /api/report/... 형태 요청 테스트")
    class sut_read_report_test {


        @Test
        @DisplayName("사용자가 특정 리포트를 상세 조회할 때, 해당 rno에 해당하는 리포트가 없으면 404 NOT FOUND를 반환한다.")
        void it_returns_404_not_found_when_the_report_does_not_exist() throws Exception {
            // given
            Integer rno = 1;
            willThrow(new ReportNotFoundException())
                    .given(reportServiceImpl)
                    .readReportDetailsByRno(rno);

            // when & then
            mockMvc.perform(get("/api/report/detail/{rno}", rno))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("사용자가 특정 리포트를 성공적으로 상세 조회한다.")
        void it_correctly_work_when_user_read_report() throws Exception {
            // given
            Integer rno = 1;
            ReportCategoryResponse category = ReportCategoryResponse.builder()
                                                                    .cate_code("TS010101")
                                                                    .top_cate("TS010100")
                                                                    .name("테스트용 카테고리")
                                                                    .ord(1)
                                                                    .chk_use("Y")
                                                                    .level(1)
                                                                    .build();

            ReportProcessDetailsResponse process = ReportProcessDetailsResponse.builder()
                                                                                .seq(1)
                                                                                .rno(rno)
                                                                                .pros_code("5001")
                                                                                .appl_begin("2025/02/06 00:00:00")
                                                                                .appl_end("2025/02/06 00:00:00")
                                                                                .chk_use("Y")
                                                                                .build();

            ReportDetailResponse reportDetailResponse = ReportDetailResponse.builder()
                                                                            .rno(rno)
                                                                            .category(category)
                                                                            .process(process)
                                                                            .title("테스트용 제목")
                                                                            .cont("테스트용 내용")
                                                                            .reg_date("2025/02/06 00:00:00")
                                                                            .repo_seq(1)
                                                                            .resp_seq(2)
                                                                            .build();

            given(reportServiceImpl.readReportDetailsByRno(rno)).willReturn(reportDetailResponse);

            // when & then
            mockMvc.perform(get("/api/report/detail/{rno}", rno))
                    .andDo(print())
                    .andExpectAll(status().isOk(),
                            jsonPath("$.rno").value(rno),
                            jsonPath("$.category.cate_code").value(category.getCate_code()),
                            jsonPath("$.category.top_cate").value(category.getTop_cate()),
                            jsonPath("$.category.name").value(category.getName()),
                            jsonPath("$.category.ord").value(category.getOrd()),
                            jsonPath("$.category.chk_use").value(category.getChk_use()),
                            jsonPath("$.category.level").value(category.getLevel()),
                            jsonPath("$.process.seq").value(process.getSeq()),
                            jsonPath("$.process.rno").value(process.getRno()),
                            jsonPath("$.process.pros_code").value(process.getPros_code()),
                            jsonPath("$.process.appl_begin").value(process.getAppl_begin()),
                            jsonPath("$.process.appl_end").value(process.getAppl_end()),
                            jsonPath("$.process.chk_use").value(process.getChk_use()),
                            jsonPath("$.title").value(reportDetailResponse.getTitle()),
                            jsonPath("$.cont").value(reportDetailResponse.getCont()),
                            jsonPath("$.reg_date").value(reportDetailResponse.getReg_date()),
                            jsonPath("$.repo_seq").value(reportDetailResponse.getRepo_seq()),
                            jsonPath("$.resp_seq").value(reportDetailResponse.getResp_seq())
                            );
        }
    }

    @Nested
    @DisplayName("POST /api/report/... 형태 요청 테스트")
    class sut_create_report_test {

        @Test
        @DisplayName("사용자가 리포트를 생성하는 과정에서 유효성 검증에 걸려서 실패한다.")
        void it_returns_400_bad_request_when_validation_fails() throws Exception {
            // given
            Integer bno = 1;
            ReportRequest request = ReportRequest.builder()
                    .rno(1)
                    .cate_code("TS010101")
                    .title(null) // title은 필수값이다.
                    .cont("테스트용 내용")
                    .comt("테스트용 코멘트")
                    .repo_seq(1)
                    .resp_seq(2)
                    .boar(bno)
                    .cmnt(null)
                    .repl(null)
                    .type(0)
                    .build();

            // when & then
            mockMvc.perform(post("/api/report/create")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("사용자가 리포트를 생성하는 과정에서 전달한 카테고리가 존재하지 않는 값인 경우 예외가 발생하여 404 NOT FOUND를 반환한다.")
        void it_returns_404_not_found_when_the_category_does_not_exist() throws Exception {
            // given
            Integer bno = 1;
            ReportRequest request = ReportRequest.builder()
                    .rno(1)
                    .cate_code("TS010101")
                    .title("테스트용 제목")
                    .cont("테스트용 내용")
                    .comt("테스트용 코멘트")
                    .repo_seq(1)
                    .resp_seq(2)
                    .boar(bno)
                    .cmnt(null)
                    .repl(null)
                    .type(0)
                    .build();

            willThrow(new ReportCategoryNotFoundException())
                    .given(reportServiceImpl)
                    .create(request);

            // when & then
            mockMvc.perform(post("/api/report/create")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("사용자가 리포트를 생성하는 과정에서 RDBMS에 문제가 발생하여 예외가 발생하여 500 INTERNAL SERVER ERROR를 반환한다.")
        void it_returns_500_internal_server_error_when_database_error_occurs() throws Exception {
            // given
            Integer bno = 1;
            ReportRequest request = ReportRequest.builder()
                    .rno(1)
                    .cate_code("TS010101")
                    .title("테스트용 제목")
                    .cont("테스트용 내용")
                    .comt("테스트용 코멘트")
                    .repo_seq(1)
                    .resp_seq(2)
                    .boar(bno)
                    .cmnt(null)
                    .repl(null)
                    .type(0)
                    .build();

            doThrow(new NotApplyOnDbmsException())
                    .when(reportServiceImpl)
                    .create(request);

            // when & then
            mockMvc.perform(post("/api/report/create")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("사용자가 리포트를 생성하는 과정에서 내부적으로 RuntimeException이 발생하여 500 INTERNAL SERVER ERROR를 반환한다.")
        void it_returns_500_internal_server_error_when_report_change_history_creation_fails()
                throws Exception {
            // given
            Integer bno = 1;
            ReportRequest request = ReportRequest.builder()
                    .rno(1)
                    .cate_code("TS010101")
                    .title("테스트용 제목")
                    .cont("테스트용 내용")
                    .comt("테스트용 코멘트")
                    .repo_seq(1)
                    .resp_seq(2)
                    .boar(bno)
                    .cmnt(null)
                    .repl(null)
                    .type(0)
                    .build();

            doThrow(new RuntimeException())
                    .when(reportServiceImpl)
                    .create(request);

            // when & then
            mockMvc.perform(post("/api/report/create")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("사용자가 성공적으로 리포트를 생성한다.")
        void it_correctly_work_when_user_create_report() throws Exception {
            // given
            Integer bno = 1;
            ReportRequest request = ReportRequest.builder()
                                                .rno(1)
                                                .cate_code("TS010101")
                                                .title("테스트용 제목")
                                                .cont("테스트용 내용")
                                                .comt("테스트용 코멘트")
                                                .repo_seq(1)
                                                .resp_seq(2)
                                                .boar(bno)
                                                .cmnt(null)
                                                .repl(null)
                                                .type(0)
                                                .build();

            ReportResponse response = ReportResponse.builder()
                    .rno(1)
                    .cate_code("TS010101")

                    .build();

            given(reportServiceImpl.create(request)).willReturn(response);

            // when & then
            mockMvc.perform(post("/api/report/create")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpectAll(status().isCreated(),
                            jsonPath("$.rno").value(response.getRno()),
                            jsonPath("$.cate_code").value(response.getCate_code()),
                            jsonPath("$.title").value(response.getTitle()),
                            jsonPath("$.cont").value(response.getCont()),
                            jsonPath("$.chk_change").value(response.getChk_change()),
                            jsonPath("$.comt").value(response.getComt()),
                            jsonPath("$.repo_seq").value(response.getRepo_seq()),
                            jsonPath("$.resp_seq").value(response.getResp_seq()),
                            jsonPath("$.boar").value(response.getBoar()),
                            jsonPath("$.cmnt").value(response.getCmnt()),
                            jsonPath("$.repl").value(response.getRepl()),
                            jsonPath("$.type").value(response.getType())
                    );
        }

        @Nested
        @DisplayName("PUT /api/report/... 형태 요청 테스트")
        class sut_modify_report_test {

            @Test
            @DisplayName("사용자가 특정 리포트를 수정하는 과정에서 유효성 검증에 걸려서 실패한다.")
            void it_returns_400_bad_request_when_validation_fails() throws Exception {
                // given
                Integer bno = 1;
                ReportRequest request = ReportRequest.builder()
                        .rno(1)
                        .cate_code("TS010101")
                        .title(null) // title은 필수값이다.
                        .cont("테스트용 내용")
                        .comt("테스트용 코멘트")
                        .repo_seq(1)
                        .resp_seq(2)
                        .boar(bno)
                        .cmnt(null)
                        .repl(null)
                        .type(0)
                        .build();


                // when & then
                mockMvc.perform(put("/api/report/modify")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("사용자가 특정 리포트를 수정하는 과정에서 해당 리포트가 존재하지 않는 경우 404 NOT FOUND를 반환한다.")
            void it_returns_404_not_found_when_the_report_does_not_exist() throws Exception {
                // given
                Integer bno = 1;
                ReportRequest request = ReportRequest.builder()
                        .rno(1)
                        .cate_code("TS010101")
                        .title("테스트용 제목")
                        .cont("테스트용 내용")
                        .comt("테스트용 코멘트")
                        .repo_seq(1)
                        .resp_seq(2)
                        .boar(bno)
                        .cmnt(null)
                        .repl(null)
                        .type(0)
                        .build();

                willThrow(new ReportNotFoundException())
                        .given(reportServiceImpl)
                        .modify(request);

                // when & then
                mockMvc.perform(put("/api/report/modify")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isNotFound());
            }

            @Test
            @DisplayName("사용자가 특정 리포트를 수정하는 과정에서 RDBMS에 문제가 발생하여 예외가 발생하여 500 INTERNAL SERVER ERROR를 반환한다.")
            void it_returns_500_internal_server_error_when_database_error_occurs() throws Exception {
                // given
                Integer bno = 1;
                ReportRequest request = ReportRequest.builder()
                        .rno(1)
                        .cate_code("TS010101")
                        .title("테스트용 제목")
                        .cont("테스트용 내용")
                        .comt("테스트용 코멘트")
                        .repo_seq(1)
                        .resp_seq(2)
                        .boar(bno)
                        .cmnt(null)
                        .repl(null)
                        .type(0)
                        .build();

                doThrow(new NotApplyOnDbmsException())
                        .when(reportServiceImpl)
                        .modify(request);

                // when & then
                mockMvc.perform(put("/api/report/modify")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isInternalServerError());
            }

            @Test
            @DisplayName("사용자가 특정 리포트를 수정하는 과정에서 내부적으로 RuntimeException이 발생하여 500 INTERNAL SERVER ERROR를 반환한다.")
            void it_returns_500_internal_server_error_when_report_change_history_creation_fails()
                    throws Exception {
                // given
                Integer bno = 1;
                ReportRequest request = ReportRequest.builder()
                                                    .rno(1)
                                                    .cate_code("TS010101")
                                                    .title("테스트용 제목")
                                                    .cont("테스트용 내용")
                                                    .comt("테스트용 코멘트")
                                                    .repo_seq(1)
                                                    .resp_seq(2)
                                                    .boar(bno)
                                                    .cmnt(null)
                                                    .repl(null)
                                                    .type(0)
                                                    .build();

                doThrow(new RuntimeException())
                        .when(reportServiceImpl)
                        .modify(request);

                // when & then
                mockMvc.perform(put("/api/report/modify")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isInternalServerError());
            }

            @Test
            @DisplayName("사용자가 성공적으로 리포트를 수정한다.")
            void it_correctly_work_when_user_modify_report() throws Exception {
                // given
                Integer bno = 1;
                ReportRequest request = ReportRequest.builder()
                                                    .rno(1)
                                                    .cate_code("TS010101")
                                                    .title("테스트용 제목")
                                                    .cont("테스트용 내용")
                                                    .comt("테스트용 코멘트")
                                                    .repo_seq(1)
                                                    .resp_seq(2)
                                                    .boar(bno)
                                                    .cmnt(null)
                                                    .repl(null)
                                                    .type(0)
                                                    .build();


                willDoNothing()
                        .given(reportServiceImpl)
                        .modify(request);

                // when & then
                mockMvc.perform(put("/api/report/modify")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                        .andDo(print())
                        .andExpectAll(status().isNoContent());
            }

        }

        @Nested
        @DisplayName("DELETE /api/report/... 형태 요청 테스트")
        class sut_remove_report_test {

            @Test
            @DisplayName("사용자가 특정 리포트를 삭제하는 과정에서 RDBMS에 문제가 발생하여 예외가 발생하여 500 INTERNAL SERVER ERROR를 반환한다.")
            void it_returns_500_internal_server_error_when_database_error_occurs() throws Exception {
                // given
                Integer rno = 1;
                doThrow(new NotApplyOnDbmsException())
                        .when(reportServiceImpl)
                        .removeByRno(rno);

                // when & then
                mockMvc.perform(delete("/api/report/{rno}", rno)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isInternalServerError());
            }


            @Test
            @DisplayName("사용자가 특정 리포트를 삭제하는 과정에서 내부적으로 RuntimeException이 발생하여 500 INTERNAL SERVER ERROR를 반환한다.")
            void it_returns_500_internal_server_error_when_report_change_history_creation_fails()
                    throws Exception {
                // given
                Integer rno = 1;
                doThrow(new RuntimeException())
                        .when(reportServiceImpl)
                        .removeByRno(rno);

                // when & then
                mockMvc.perform(delete("/api/report/{rno}", rno)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isInternalServerError());
            }

            @Test
            @DisplayName("사용자가 성공적으로 리포트를 삭제한다.")
            void it_correctly_work_when_user_remove_report() throws Exception {
                // given
                Integer rno = 1;
                willDoNothing()
                        .given(reportServiceImpl)
                        .removeByRno(rno);

                // when & then
                mockMvc.perform(delete("/api/report/{rno}", rno)
                                .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isNoContent());
            }
        }
    }
}