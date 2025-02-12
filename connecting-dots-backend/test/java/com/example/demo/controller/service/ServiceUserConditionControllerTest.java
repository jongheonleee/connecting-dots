package com.example.demo.controller.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.service.service.impl.ServiceUserConditionServiceImpl;
import com.example.demo.dto.service.ServiceUserConditionRequest;
import com.example.demo.dto.service.ServiceUserConditionResponse;
import com.example.demo.global.error.GlobalExceptionHandler;
import com.example.demo.global.error.exception.business.service.ServiceUserConditionAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceUserConditionNotFoundException;
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


class ServiceUserConditionControllerTest {

    private MockMvc mockMvc;
    private ServiceUserConditionServiceImpl serviceUserConditionService;


    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                                                                        .serializers(
                                                                                LocalTimeSerializer.INSTANCE)
                                                                        .featuresToDisable(
                                                                                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                                                        .modules(new JavaTimeModule())
                                                                        .build();

    private final String cond_code = "USC0001";

    @BeforeEach
    void setUp() {
        // 목킹 설정
        serviceUserConditionService = Mockito.mock(ServiceUserConditionServiceImpl.class);

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setDefaultCharset(StandardCharsets.UTF_8);

        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceUserConditionController(serviceUserConditionService))
                                .alwaysDo(print())
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .setMessageConverters(messageConverter)
                                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                                .build();

    }

    @Nested
    @DisplayName("GET /api/service-user-condition/{cond_code}")
    class Describe_getServiceUserCondition {

        @Test
        @DisplayName("존재하는 cond_code로 조회할 때, 해당 회원 약관 항목을 반환한다")
        void 존재하는_cond_code로_조회_성공() throws Exception {
            ServiceUserConditionResponse expected = createResponse();

            Mockito.when(serviceUserConditionService.readByCondCode(cond_code))
                   .thenReturn(expected);

            mockMvc.perform(
                        get("/api/service-user-condition/{cond_code}", cond_code)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }

        @Test
        @DisplayName("존재하지 않는 cond_code로 조회할 때, 404 NOT FOUND를 반환한다")
        void 존재하지_않는_cond_code로_조회_실패() throws Exception {
            ServiceUserConditionNotFoundException serviceUserConditionNotFoundException = new ServiceUserConditionNotFoundException();

            Mockito.when(serviceUserConditionService.readByCondCode(cond_code))
                   .thenThrow(serviceUserConditionNotFoundException);

            mockMvc.perform(
                        get("/api/service-user-condition/{cond_code}", cond_code)
                    )
                    .andExpect(status().isNotFound()
                    );
        }
    }

    @Nested
    @DisplayName("POST /api/service-user-condition/create")
    class Describe_postServiceUserCondition {

        @Test
        @DisplayName("회원 약관 항목을 생성한다")
        void 회원_약관_항목_생성_성공() throws Exception {
            ServiceUserConditionRequest request = createRequest();
            ServiceUserConditionResponse expected = createResponse();

            Mockito.when(serviceUserConditionService.create(request))
                   .thenReturn(expected);

            mockMvc.perform(
                        post("/api/service-user-condition/create")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected))
                    );
        }

        @Test
        @DisplayName("이미 존재하는 cond_code로 생성할 때, 409 CONFLICT를 반환한다")
        void 이미_존재하는_cond_code로_생성_실패() throws Exception {
            ServiceUserConditionRequest request = createRequest();
            ServiceUserConditionAlreadyExistsException serviceUserConditionAlreadyExistsException = new ServiceUserConditionAlreadyExistsException();

            Mockito.when(serviceUserConditionService.create(request))
                   .thenThrow(serviceUserConditionAlreadyExistsException);

            mockMvc.perform(
                        post("/api/service-user-condition/create")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isConflict()
                    );
        }

        @Test
        @DisplayName("유효하지 않는 값으로 생성시 400 BAD REQUEST를 반환한다")
        void 유효하지_않는_값으로_생성_실패() throws Exception {
            ServiceUserConditionRequest request = createRequest();
            request.setCond_code("21#@222");

            mockMvc.perform(
                        post("/api/service-user-condition/create")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest()
                    );
        }
    }

    @Nested
    @DisplayName("PATCH /api/service-user-condition/modify/{cond_code}")
    class Describe_patchServiceUserCondition {

        @Test
        @DisplayName("존재하는 cond_code로 수정할 때, 해당 회원 약관 항목을 수정한다")
        void 존재하는_cond_code로_수정_성공() throws Exception {
            ServiceUserConditionRequest request = createRequest();

            Mockito.doNothing()
                   .when(serviceUserConditionService)
                   .modify(request);

            mockMvc.perform(
                        patch("/api/service-user-condition/modify/{cond_code}", cond_code)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNoContent()
                    );
        }

        @Test
        @DisplayName("존재하지 않는 cond_code로 수정할 때, 404 NOT FOUND를 반환한다")
        void 존재하지_않는_cond_code로_수정_실패() throws Exception {
            ServiceUserConditionRequest request = createRequest();
            ServiceUserConditionNotFoundException serviceUserConditionNotFoundException = new ServiceUserConditionNotFoundException();

            Mockito.doThrow(serviceUserConditionNotFoundException)
                   .when(serviceUserConditionService)
                   .modify(request);

            mockMvc.perform(
                        patch("/api/service-user-condition/modify/{cond_code}", cond_code)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNotFound()
                    );
        }

        @Test
        @DisplayName("유효하지 않는 값으로 수정할 때, 400 BAD REQUEST를 반환한다")
        void 유효하지_않는_값으로_수정_실패() throws Exception {
            ServiceUserConditionRequest request = createRequest();
            request.setCond_code("21#@222");

            mockMvc.perform(
                        patch("/api/service-user-condition/modify/{cond_code}", cond_code)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest()
                    );
        }
    }

    @Nested
    @DisplayName("DELETE /api/service-user-condition/remove/{cond_code}")
    class Describe_deleteServiceUserCondition {

        @Test
        @DisplayName("존재하는 cond_code로 삭제할 때, 해당 회원 약관 항목을 삭제한다")
        void 존재하는_cond_code로_삭제_성공() throws Exception {
            Mockito.doNothing()
                   .when(serviceUserConditionService)
                   .remove(cond_code);

            mockMvc.perform(
                        delete("/api/service-user-condition/remove/{cond_code}", cond_code)
                    )
                    .andExpect(status().isNoContent()
                    );
        }

    }

    private ServiceUserConditionRequest createRequest() {
        return ServiceUserConditionRequest.builder()
                                          .cond_code(cond_code)
                                          .name("테스트용 회원 약관 항목")
                                          .short_exp("테스트용 약관 항목")
                                          .long_exp("약관 항목 설명")
                                          .chk_use("Y")
                                          .law1("법률1")
                                          .law2("법률2")
                                          .law3("법률3")
                                          .build();
    }

    private ServiceUserConditionResponse createResponse() {
        return ServiceUserConditionResponse.builder()
                                           .cond_code(cond_code)
                                           .name("테스트용 회원 약관 항목")
                                           .short_exp("테스트용 약관 항목")
                                           .long_exp("약관 항목 설명")
                                           .chk_use("Y")
                                           .law1("법률1")
                                           .law2("법률2")
                                           .law3("법률3")
                                           .build();
    }
}