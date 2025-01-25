package com.example.demo.presentation.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.application.service.impl.ServiceUserGradeServiceImpl;
import com.example.demo.dto.service.ServiceUserGradeRequest;
import com.example.demo.dto.service.ServiceUserGradeResponse;
import com.example.demo.global.error.GlobalExceptionHandler;
import com.example.demo.global.error.exception.business.service.ServiceUserGradeAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceUserGradeNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

class ServiceUserGradeControllerTest {

    private MockMvc mockMvc;
    private ServiceUserGradeServiceImpl serviceUserGradeService;


    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                                                                        .serializers(
                                                                                LocalTimeSerializer.INSTANCE)
                                                                        .featuresToDisable(
                                                                                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                                                        .modules(new JavaTimeModule())
                                                                        .build();


    private final String stat_code = "ABC0001";

    @BeforeEach
    void setUp() {
        // 목킹 설정
        serviceUserGradeService = Mockito.mock(ServiceUserGradeServiceImpl.class);

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setDefaultCharset(StandardCharsets.UTF_8);

        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceUserGradeController(serviceUserGradeService))
                .alwaysDo(print())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(messageConverter)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

    }

    @Nested
    @DisplayName("GET /api/service-user-grade/{stat_code}")
    class Describe_read {

        @Test
        @DisplayName("정상적인 stat_code가 주어지면, 해당 ServiceUserGrade를 반환한다.")
        void 존재하는_stat_code_회원_등급_반환() throws Exception {
            // given
            ServiceUserGradeResponse response = createResponse();
            Mockito.when(serviceUserGradeService.readByStatCode(stat_code)).thenReturn(response);

            // when, then
            mockMvc.perform(get("/api/service-user-grade/{stat_code}", stat_code))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }

        @Test
        @DisplayName("존재하지 않는 stat_code가 주어지면, 404 NOT_FOUND를 반환한다.")
        void 존재하지_않는_stat_code_404_반환() throws Exception {
            // given
            ServiceUserGradeNotFoundException serviceUserGradeNotFoundException = new ServiceUserGradeNotFoundException();
            Mockito.doThrow(serviceUserGradeNotFoundException)
                    .when(serviceUserGradeService)
                    .readByStatCode(stat_code);
            // when, then
            mockMvc.perform(get("/api/service-user-grade/{stat_code}", stat_code))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/service-user-grade/create")
    class Describe_create {

        @Test
        @DisplayName("정상적인 ServiceUserGradeRequest가 주어지면, ServiceUserGrade를 생성한다.")
        void 유효한_입력값_전달되면_정상적으로_생성() throws Exception {
            // given
            ServiceUserGradeRequest request = createRequest();
            ServiceUserGradeResponse response = createResponse();
            Mockito.when(serviceUserGradeService.create(request)).thenReturn(response);

            // when, then
            mockMvc.perform(post("/api/service-user-grade/create")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }

        @Test
        @DisplayName("이미 존재하는 stat_code가 주어지면, 409 CONFLICT를 반환한다.")
        void 이미_존재하는_stat_code_409_응답() throws Exception {
            // given
            ServiceUserGradeAlreadyExistsException serviceUserGradeAlreadyExistsException = new ServiceUserGradeAlreadyExistsException();
            ServiceUserGradeRequest request = createRequest();
            Mockito.when(serviceUserGradeService.create(request))
                    .thenThrow(serviceUserGradeAlreadyExistsException);

            // when, then
            mockMvc.perform(post("/api/service-user-grade/create")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("유효하지 않은 값이 전달되면 400 BAD_REQUEST를 반환한다.")
        void 유효하지_않은_값_400_응답() throws Exception {
            // given
            ServiceUserGradeRequest request = createRequest();
            request.setStat_code("invalid_stat_code");

            // when, then
            mockMvc.perform(post("/api/service-user-grade/create")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PATCH /api/service-user-grade/{stat_code}")
    class Describe_modify {

        @Test
        @DisplayName("정상적인 ServiceUserGradeRequest가 주어지면, ServiceUserGrade를 수정한다.")
        void 유효한_값_전달되면_정상_처리() throws Exception {
            // given
            ServiceUserGradeRequest request = createRequest();
            Mockito.doNothing().when(serviceUserGradeService).modify(request);

            // when, then
            mockMvc.perform(patch("/api/service-user-grade/{stat_code}", stat_code)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 stat_code가 주어지면, 404 NOT_FOUND를 반환한다.")
        void 존재하지_않는_stat_code_404_응답() throws Exception {
            // given
            ServiceUserGradeNotFoundException serviceUserGradeNotFoundException = new ServiceUserGradeNotFoundException();
            ServiceUserGradeRequest request = createRequest();
            Mockito.doThrow(serviceUserGradeNotFoundException)
                    .when(serviceUserGradeService)
                    .modify(request);

            // when, then
            mockMvc.perform(patch("/api/service-user-grade/{stat_code}", stat_code)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("유효하지 않은 값이 전달되면 400 BAD_REQUEST를 반환한다.")
        void 유효하지_않는_값_전달되면_400_응답() throws Exception {
            // given
            ServiceUserGradeRequest request = createRequest();
            request.setStat_code("!@322");

            // when, then
            mockMvc.perform(patch("/api/service-user-grade/{stat_code}", stat_code)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /api/service-user-grade/{stat_code}")
    class Describe_remove {

        @Test
        @DisplayName("정상적인 stat_code가 주어지면, ServiceUserGrade를 삭제한다.")
        void 존재하는_stat_code_전달되면_정상_처리() throws Exception {
            // given
            Mockito.doNothing().when(serviceUserGradeService).removeByStatCode(stat_code);

            // when, then
            mockMvc.perform(delete("/api/service-user-grade/{stat_code}", stat_code))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("존재하지 않는 stat_code가 주어지면, 404 NOT_FOUND를 반환한다.")
        void 존재하지_않는_stat_code_404_응답() throws Exception {
            // given
            ServiceUserGradeNotFoundException serviceUserGradeNotFoundException = new ServiceUserGradeNotFoundException();
            Mockito.doThrow(serviceUserGradeNotFoundException)
                    .when(serviceUserGradeService)
                    .removeByStatCode(stat_code);
            // when, then
            mockMvc.perform(delete("/api/service-user-grade/{stat_code}", stat_code))
                    .andExpect(status().isNotFound());
        }
    }


    private ServiceUserGradeRequest createRequest() {
        return ServiceUserGradeRequest.builder()
                                    .stat_code(stat_code)
                                    .name("테스트용")
                                    .ord(1)
                                    .short_exp("짧은 설명")
                                    .long_exp("긴 설명")
                                    .img("이미지")
                                    .chk_use("Y")
                                    .build();
    }

    private ServiceUserGradeResponse createResponse() {
        return ServiceUserGradeResponse.builder()
                                    .stat_code(stat_code)
                                    .name("테스트용")
                                    .ord(1)
                                    .short_exp("짧은 설명")
                                    .long_exp("긴 설명")
                                    .img("이미지")
                                    .chk_use("Y")
                                    .build();
    }
}