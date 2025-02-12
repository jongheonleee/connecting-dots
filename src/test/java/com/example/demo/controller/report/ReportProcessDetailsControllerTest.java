package com.example.demo.controller.report;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.report.ReportProcessDetailsRequest;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.service.report.impl.ReportProcessDetailsServiceImpl;
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
@WebMvcTest(ReportProcessDetailsController.class)
class ReportProcessDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportProcessDetailsServiceImpl reportProcessDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(reportProcessDetailsServiceImpl);
    }


    @Nested
    @DisplayName("POST /api/report-process-details ... 형태 요청 테스트")
    class sut_create_report_process_details_test {

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하는 과정에서 rno에 해당하는 리포트가 존재하지 않는 경우 404 NOT FOUND를 응답한다.")
        void it_responds_404_when_report_not_found() throws Exception {
            // given
            ReportProcessDetailsRequest request = ReportProcessDetailsRequest.builder()
                                                                            .rno(1)
                                                                            .pros_code("5001")
                                                                            .chk_use("Y")
                                                                            .build();

            willThrow(new ReportNotFoundException())
                    .given(reportProcessDetailsServiceImpl)
                    .renew(request);

            // when & then
            mockMvc.perform(post("/api/report-process-details/renew")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하는 과정에서 pros_code 유효성 검사에 실패한 경우 400 BAD REQUEST를 응답한다.")
        void it_responds_400_when_pros_code_validation_failed() throws Exception {
            // given
            ReportProcessDetailsRequest request = ReportProcessDetailsRequest.builder()
                                                                            .rno(1)
                                                                            .pros_code("500")
                                                                            .chk_use("Y")
                                                                            .build();

            // when & then
            mockMvc.perform(post("/api/report-process-details/renew")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하는 과정에서 pros_code와 관련된 코드가 존재하지 않는 경우 404 BAD REQUEST를 응답한다.")
        void it_responds_404_when_pros_code_not_found() throws Exception {
            // given
            ReportProcessDetailsRequest request = ReportProcessDetailsRequest.builder()
                                                                            .rno(1)
                                                                            .pros_code("5001")
                                                                            .chk_use("Y")
                                                                            .build();

            willThrow(new CodeNotFoundException())
                    .given(reportProcessDetailsServiceImpl)
                    .renew(request);

            // when & then
            mockMvc.perform(post("/api/report-process-details/renew")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하는 과정에서 내부적으로 예외가 발생한 경우 500 INTERNAL SERVER ERROR를 응답한다.")
        void it_responds_500_when_internal_exception_occurred() throws Exception {
            // given
            ReportProcessDetailsRequest request = ReportProcessDetailsRequest.builder()
                                                                            .rno(1)
                                                                            .pros_code("5001")
                                                                            .chk_use("Y")
                                                                            .build();

            willThrow(new RuntimeException())
                    .given(reportProcessDetailsServiceImpl)
                    .renew(request);

            // when & then
            mockMvc.perform(post("/api/report-process-details/renew")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("사용자가 성공적으로 리포트 처리 내역을 갱신하는 경우 201 CREATED를 응답한다.")
        void it_responds_201_when_report_process_details_renewed() throws Exception {
            // given
            ReportProcessDetailsRequest request = ReportProcessDetailsRequest.builder()
                                                                            .rno(1)
                                                                            .pros_code("5001")
                                                                            .chk_use("Y")
                                                                            .build();

            ReportProcessDetailsResponse response = ReportProcessDetailsResponse.builder()
                                                                                .seq(1)
                                                                                .rno(1)
                                                                                .pros_code("5001")
                                                                                .chk_use("Y")
                                                                                .build();

            given(reportProcessDetailsServiceImpl.renew(request))
                    .willReturn(response);

            // when & then
            mockMvc.perform(post("/api/report-process-details/renew")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.seq").value(response.getSeq()),
                            jsonPath("$.rno").value(response.getRno()),
                            jsonPath("$.pros_code").value(response.getPros_code()),
                            jsonPath("$.chk_use").value(response.getChk_use())
                    );

        }
    }
}