package com.example.demo.presentation.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.application.service.ServiceUserConditionsServiceImpl;
import com.example.demo.dto.service.ServiceUserConditionsRequest;
import com.example.demo.dto.service.ServiceUserConditionsResponse;
import com.example.demo.global.error.GlobalExceptionHandler;
import com.example.demo.global.error.exception.business.service.ServiceUserConditionsAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceUserConditionsNotFoundException;
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

class ServiceUserConditionsControllerTest {
    private MockMvc mockMvc;
    private ServiceUserConditionsServiceImpl serviceUserConditionsService;


    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                                                                        .serializers(
                                                                                LocalTimeSerializer.INSTANCE)
                                                                        .featuresToDisable(
                                                                                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                                                        .modules(new JavaTimeModule())
                                                                        .build();
    private final String conds_code = "ABC0001";
    private final String cond_code1 = "AB0001";
    private final String cond_code2 = "AB0002";
    private final String cond_code3 = "AB0003";
    private final String cond_code4 = "AB0004";

    @BeforeEach
    void setUp() {
        // 목킹 설정
        serviceUserConditionsService = Mockito.mock(ServiceUserConditionsServiceImpl.class);

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setDefaultCharset(StandardCharsets.UTF_8);

        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceUserConditionsController(serviceUserConditionsService))
                                .alwaysDo(print())
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .setMessageConverters(messageConverter)
                                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                                .build();

    }

    @Nested
    @DisplayName("GET /api/service-user-conditions/{conds_code}")
    class Describe_getServiceUserConditions {

        @Test
        @DisplayName("존재하는 conds_code로 조회할 때, 해당 회원 약관 정책을 반환")
        void 존재하는_conds_code_조회시_회원_약관_정책_반환() throws Exception {
            ServiceUserConditionsResponse expected = createResponse();

            Mockito.when(serviceUserConditionsService.readByCondsCode(conds_code))
                   .thenReturn(expected);

            mockMvc.perform(
                        get("/api/service-user-conditions/{conds_code}", conds_code)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));


        }

        @Test
        @DisplayName("존재하지 않는 conds_code로 조회할 때, 404 NOT FOUND 반환")
        void 존재하지_않는_conds_code_조회시_404_NOT_FOUND_반환() throws Exception {
            ServiceUserConditionsNotFoundException serviceUserConditionsNotFoundException = new ServiceUserConditionsNotFoundException();

            Mockito.when(serviceUserConditionsService.readByCondsCode(conds_code))
                   .thenThrow(serviceUserConditionsNotFoundException);

            mockMvc.perform(
                        get("/api/service-user-conditions/{conds_code}", conds_code)
                    )
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/service-user-conditions/create")
    class Describe_createServiceUserConditions {

        @Test
        @DisplayName("회원 약관 정책을 생성하고, 해당 회원 약관 정책을 반환")
        void 회원_약관_정책_생성_및_반환() throws Exception {
            ServiceUserConditionsRequest request = createRequest();
            ServiceUserConditionsResponse expected = createResponse();

            Mockito.when(serviceUserConditionsService.create(request))
                   .thenReturn(expected);

            mockMvc.perform(
                        post("/api/service-user-conditions/create")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        }

        @Test
        @DisplayName("이미 존재하는 conds_code로 생성할 때, 409 CONFLICT 반환")
        void 이미_존재하는_conds_code_생성시_409_CONFLICT_반환() throws Exception {
            ServiceUserConditionsRequest request = createRequest();
            ServiceUserConditionsAlreadyExistsException serviceUserConditionsAlreadyExistsException = new ServiceUserConditionsAlreadyExistsException();

            Mockito.when(serviceUserConditionsService.create(request))
                   .thenThrow(serviceUserConditionsAlreadyExistsException);

            mockMvc.perform(
                        post("/api/service-user-conditions/create")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("유효하지 않는 값으로 생성할 때, 400 BAD_REQUEST 반환")
        void 유효하지_않는_값으로_생성시_400_BAD_REQUEST_반환() throws Exception {
            ServiceUserConditionsRequest request = createRequest();
            request.setConds_code("!@#23");

            mockMvc.perform(
                        post("/api/service-user-conditions/create")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PATCH /api/service-user-conditions/modify/{conds_code}")
    class Describe_updateServiceUserConditions {

        @Test
        @DisplayName("회원 약관 정책을 수정하고, 해당 회원 약관 정책을 반환")
        void 회원_약관_정책_수정_및_반환() throws Exception {
            ServiceUserConditionsRequest request = createRequest();

            Mockito.doNothing()
                   .when(serviceUserConditionsService)
                   .modify(request);


            mockMvc.perform(
                        patch("/api/service-user-conditions/modify/{conds_code}", conds_code)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNoContent()
            );


        }

        @Test
        @DisplayName("존재하지 않는 conds_code로 수정할 때, 404 NOT FOUND 반환")
        void 존재하지_않는_conds_code_수정시_404_NOT_FOUND_반환() throws Exception {
            ServiceUserConditionsRequest request = createRequest();
            ServiceUserConditionsNotFoundException serviceUserConditionsNotFoundException = new ServiceUserConditionsNotFoundException();

            Mockito.doThrow(serviceUserConditionsNotFoundException)
                   .when(serviceUserConditionsService)
                   .modify(request);

            mockMvc.perform(
                        patch("/api/service-user-conditions/modify/{conds_code}", conds_code)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("유효하지 않는 값으로 수정할 때, 400 BAD_REQUEST 반환")
        void 유효하지_않는_값으로_수정시_400_BAD_REQUEST_반환() throws Exception {
            ServiceUserConditionsRequest request = createRequest();
            request.setConds_code("!@#23");

            mockMvc.perform(
                        patch("/api/service-user-conditions/modify/{conds_code}", conds_code)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /api/service-user-conditions/remove/{conds_code}")
    class Describe_deleteServiceUserConditions {

        @Test
        @DisplayName("회원 약관 정책을 삭제하고, 204 NO CONTENT 반환")
        void 회원_약관_정책_삭제_및_204_NO_CONTENT_반환() throws Exception {
            Mockito.doNothing()
                   .when(serviceUserConditionsService)
                   .removeByCondsCode(conds_code);

            mockMvc.perform(
                        delete("/api/service-user-conditions/remove/{conds_code}", conds_code)
                    )
                    .andExpect(status().isNoContent());
        }
    }

    private ServiceUserConditionsRequest createRequest() {
        return ServiceUserConditionsRequest.builder()
                                           .conds_code(conds_code)
                                           .name("테스트용 약관 정책명")
                                           .cond_code1(cond_code1)
                                           .chk_cond_code1("Y")
                                           .cond_code2(cond_code2)
                                           .chk_cond_code2("N")
                                           .cond_code3(cond_code3)
                                           .chk_cond_code3("Y")
                                           .cond_code4(cond_code4)
                                           .chk_cond_code4("N")
                                           .chk_use("Y")
                                           .build();
    }

    private ServiceUserConditionsResponse createResponse() {
        return ServiceUserConditionsResponse.builder()
                                            .conds_code(conds_code)
                                            .name("테스트용 약관 정책명")
                                            .cond_code1(cond_code1)
                                            .chk_cond_code1("Y")
                                            .cond_code2(cond_code2)
                                            .chk_cond_code2("N")
                                            .cond_code3(cond_code3)
                                            .chk_cond_code3("Y")
                                            .cond_code4(cond_code4)
                                            .chk_cond_code4("N")
                                            .chk_use("Y")
                                            .build();
    }

}