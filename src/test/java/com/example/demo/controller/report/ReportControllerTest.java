package com.example.demo.controller.report;

import static org.junit.jupiter.api.Assertions.*;


import com.example.demo.service.report.impl.ReportServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    }

    @Nested
    @DisplayName("POST /api/report/... 형태 요청 테스트")
    class sut_create_report_test {

    }

    @Nested
    @DisplayName("PUT /api/report/... 형태 요청 테스트")
    class sut_modify_report_test {

    }

    @Nested
    @DisplayName("DELETE /api/report/... 형태 요청 테스트")
    class sut_remove_report_test {

    }

}