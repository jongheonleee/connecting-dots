package com.example.demo.presentation.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.application.service.ServiceSanctionHistoryServiceImpl;
import com.example.demo.dto.service.ServiceSanctionHistoryRequest;
import com.example.demo.dto.service.ServiceSanctionHistoryResponse;
import com.example.demo.global.error.GlobalExceptionHandler;
import com.example.demo.global.error.exception.business.service.ServiceSanctionHistoryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 컨트롤러에서 테스트해야할 내용
 * - 1. HTTP Request와 handler mapping 여부
 * - 2. Request Parameter, Body 값 검증
 * - 3. Service Layer 호출 여부
 * - 4. 기대한 HTTP Response 값 반환 여부
 */
class ServiceSanctionHistoryControllerTest {
    private MockMvc mockMvc;
    private ServiceSanctionHistoryServiceImpl serviceSanctionHistoryService;


    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                                                                        .serializers(LocalTimeSerializer.INSTANCE)
                                                                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                                                        .modules(new JavaTimeModule())
                                                                        .build();

    @BeforeEach
    void setUp() {
        // 목킹 설정
        serviceSanctionHistoryService = Mockito.mock(ServiceSanctionHistoryServiceImpl.class);

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setDefaultCharset(StandardCharsets.UTF_8);

        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceSanctionHistoryController(serviceSanctionHistoryService))
                .alwaysDo(print())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(messageConverter)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

    }

    @Nested
    @DisplayName("GET /api/service-sanction-history/{seq}")
    class GetServiceSanctionHistory {
        @Test
        @DisplayName("등록된 seq를 전달하면 해당 seq로 서비스 제재 이력을 반환한다.")
        void 등록된_seq_전달하면_서비스_제재_이력_반환() throws Exception {
            // given
            int i = 1;
            ServiceSanctionHistoryResponse expected = createResponse(i);

            // when
            Mockito.when(serviceSanctionHistoryService.readBySeq(i)).thenReturn(expected);

            // then
            mockMvc.perform(
                        get("/api/service-sanction-history/{seq}", i)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(expected)));
        }

        @Test
        @DisplayName("존재하지 않는 seq를 전달하면 404 NOT FOUND를 반환한다.")
        void 등록되지_않은_seq_전달하면_예외_발생() throws Exception {
            // given
            int i = 1;

            // when
            Mockito.when(serviceSanctionHistoryService.readBySeq(i)).thenThrow(new ServiceSanctionHistoryNotFoundException());

            // then
            mockMvc.perform(
                        get("/api/service-sanction-history/{seq}", i)
                    )
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/service-sanction-history/list/{user_seq}")
    class GetServiceSanctionHistoryListByUserSeq {
        @Test
        @DisplayName("등록된 user_seq를 전달하면 해당 user_seq로 서비스 제재 이력을 반환한다.")
        void 등록된_user_seq_서비스_제재_이력_반환 () throws Exception {
            // given
            int user_seq = 1;
            List<ServiceSanctionHistoryResponse> expected = createResponses(5, user_seq);

            // when
            Mockito.when(serviceSanctionHistoryService.readByUserSeq(user_seq)).thenReturn(expected);


            // then
            mockMvc.perform(
                            get("/api/service-sanction-history/list/{user_seq}", user_seq)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(expected)));
        }

        /**
         * 이 부분 회원 서비스 구현시 추가해야할 부분 []
         */
        @Test
        @DisplayName("등록되지 않은 user_seq를 전달하면 404 NOT FOUND를 반환한다.")
        void 등록되지_않은_user_seq_예외_발생() {

        }
    }

    @Nested
    @DisplayName("POST /api/service-sanction-history/create")
    class CreateServiceSanctionHistory {
        @Test
        @DisplayName("정상적인 요청 값이 주어지면 생성 성공")
        void 정상적인_요청_값이_주어지면_생성_성공() throws Exception {
            // given
            ServiceSanctionHistoryRequest request = createRequest(1);
            ServiceSanctionHistoryResponse expected = createResponse(1);

            // when
            Mockito.when(serviceSanctionHistoryService.create(request))
                    .thenReturn(expected);

            // then
            mockMvc.perform(
                        post("/api/service-sanction-history/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().string(objectMapper.writeValueAsString(expected)));
        }

        /**
         * 이 부분 회원 서비스 구현시 추가해야할 부분 []
         */
        @Test
        @DisplayName("존재하지 않는 user_seq 전달되면 404 NOT FOUND 반환")
        void 존재하지_않는_user_seq_전달되면_예외_발생() throws Exception {

        }

        @Test
        @DisplayName("유효하지 않은 요청 값이 주어지면 예외 발생")
        void 유효하지_않은_요청_값이_주어지면_예외_발생() throws Exception {
            // given
            ServiceSanctionHistoryRequest request = createRequest(1);

            request.setAppl_begin("231!@$");
            request.setPoli_stat("!@#");

            // when

            // then
            mockMvc.perform(
                        post("/api/service-sanction-history/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest()
            );
        }
    }

    @Nested
    @DisplayName("PATCH /api/service-sanction-history/modify/{seq}")
    class ModifyServiceSanctionHistory {
        @Test
        @DisplayName("정상적인 요청 값이 주어지면 수정 성공")
        void 정상적인_요청_값이_주어지면_수정_성공() throws Exception {
            // given
            int i = 1;
            ServiceSanctionHistoryRequest request = createRequest(i);

            // when
            Mockito.doNothing().when(serviceSanctionHistoryService).modify(request);

            // then
            mockMvc.perform(
                        patch("/api/service-sanction-history/modify/{seq}", i)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNoContent()
            );

        }

        @Test
        @DisplayName("유효하지 않은 요청 값이 주어지면 예외 발생")
        void 유효하지_않은_요청_값이_주어지면_예외_발생() throws Exception {
            // given
            int i = 1;
            ServiceSanctionHistoryRequest request = createRequest(i);

            request.setAppl_begin("231!@$");
            request.setPoli_stat("!@#");

            // when
            // then
            mockMvc.perform(
                        patch("/api/service-sanction-history/modify/{seq}", i)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest()
            );
        }
    }

    @Nested
    @DisplayName("DELETE /api/service-sanction-history/{seq}")
    class RemoveServiceSanctionHistory {
        @Test
        @DisplayName("등록된 seq 전달하면 삭제 성공")
        void 등록된_seq_전달_삭제_성공() throws Exception {
            // given
            int i = 1;

            // when
            Mockito.doNothing().when(serviceSanctionHistoryService).remove(i);

            // then
            mockMvc.perform(
                        delete("/api/service-sanction-history/{seq}", i)
                    )
                    .andExpect(status().isNoContent()
            );
        }
    }

    @Nested
    @DisplayName("DELETE /api/service-sanction-history/list/{user_seq}")
    class RemoveServiceSanctionHistoryByUserSeq {
        @Test
        @DisplayName("등록된 user_seq 전달하면 해당 이력 전체 삭제 성공")
        void 등록된_user_seq_전달하면_이력_전체_삭제_성공() throws Exception {
            // given
            int user_seq = 1;

            // when
            Mockito.doNothing().when(serviceSanctionHistoryService).removeByUserSeq(user_seq);

            // then
            mockMvc.perform(
                        delete("/api/service-sanction-history/{user_seq}", user_seq)
                    )
                    .andExpect(status().isNoContent()
            );
        }

        @Test
        @DisplayName("등록되지 않은 user_seq 전달하면 삭제 실패")
        void 등로되지_않은_user_seq_전달하면_삭제_실패() {
            // given
            // when
            // then
        }
    }

    private ServiceSanctionHistoryRequest createRequest(int i) {
        return ServiceSanctionHistoryRequest.builder()
                                            .seq(i)
                                            .user_seq(i)
                                            .poli_stat("ABC0001")
                                            .appl_begin("2025/01/13")
                                            .appl_end("2025/01/13")
                                            .short_exp("짧은 설명")
                                            .long_exp("긴 설명")
                                            .comt("비고")
                                            .build();
    }


    private ServiceSanctionHistoryResponse createResponse(int i) {
        return ServiceSanctionHistoryResponse.builder()
                                            .seq(i)
                                            .user_seq(i)
                                            .poli_stat("ABC0001")
                                            .appl_begin("2025/01/13")
                                            .appl_end("2025/01/13")
                                            .short_exp("짧은 설명")
                                            .long_exp("긴 설명")
                                            .comt("비고")
                                            .build();
    }

    private List<ServiceSanctionHistoryResponse> createResponses(int cnt, int user_seq) {
        List<ServiceSanctionHistoryResponse> responses = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            ServiceSanctionHistoryResponse response = createResponse(user_seq);
            responses.add(response);
        }

        return responses;
    }
}