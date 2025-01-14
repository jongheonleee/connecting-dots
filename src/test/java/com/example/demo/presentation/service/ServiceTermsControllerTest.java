package com.example.demo.presentation.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.example.demo.application.service.ServiceTermsServiceImpl;
import com.example.demo.dto.service.ServiceTermsRequest;
import com.example.demo.dto.service.ServiceTermsResponse;
import com.example.demo.global.error.GlobalExceptionHandler;
import com.example.demo.global.error.exception.business.service.ServiceTermsAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceTermsNotFoundException;
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
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

class ServiceTermsControllerTest {

    private MockMvc mockMvc;
    private ServiceTermsServiceImpl serviceTermsService;


    private final String poli_stat = "ABC0001";
    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                                                                        .serializers(
                                                                                LocalTimeSerializer.INSTANCE)
                                                                        .featuresToDisable(
                                                                                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                                                        .modules(new JavaTimeModule())
                                                                        .build();

    @BeforeEach
    void setUp() {
        // 목킹 설정
        serviceTermsService = Mockito.mock(ServiceTermsServiceImpl.class);

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setDefaultCharset(StandardCharsets.UTF_8);

        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceTermsController(serviceTermsService))
                                .alwaysDo(print())
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .setMessageConverters(messageConverter)
                                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                                .build();

    }

    @Nested
    @DisplayName("GET /api/service-terms/{poli_stat}")
    class Describe_getServiceTerms {

        @Test
        @DisplayName("존재하는 poli_stat을 조회하면, 해당 서비스 정책을 반환한다.")
        void 존재하는_poli_stat_조회() throws Exception {
            ServiceTermsResponse expected = craeteResponse();

            Mockito.when(serviceTermsService.readByPoliStat(poli_stat))
                   .thenReturn(expected);

            mockMvc.perform(
                        get("/api/service-terms/{poli_stat}", poli_stat)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected))
                    );
        }

        @Test
        @DisplayName("존재하지 않는 poli_stat을 조회하면, 404 NOT FOUND를 반환한다.")
        void 존재하지_않는_poli_stat_조회() throws Exception {
            ServiceTermsNotFoundException serviceTermsNotFoundException = new ServiceTermsNotFoundException();
            Mockito.when(serviceTermsService.readByPoliStat(poli_stat))
                    .thenThrow(serviceTermsNotFoundException);

            mockMvc.perform(
                            get("/api/service-terms/{poli_stat}", poli_stat)
                    )
                    .andExpect(status().isNotFound()
                    );
        }
    }


    @Nested
    @DisplayName("POST /api/service-terms/create")
    class Describe_createServiceTerms {

        @Test
        @DisplayName("서비스 정책을 생성하면, 생성된 서비스 정책을 반환한다.")
        void 서비스_정책_생성() throws Exception {
            ServiceTermsRequest request = createRequest();
            ServiceTermsResponse expected = craeteResponse();

            Mockito.when(serviceTermsService.create(request))
                   .thenReturn(expected);

            mockMvc.perform(
                        post("/api/service-terms/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected))
                    );
        }

        @Test
        @DisplayName("중복된 서비스 정책을 생성하면, 409 CONFLICT를 반환한다.")
        void 중복된_서비스_정책_생성() throws Exception {
            ServiceTermsRequest request = createRequest();
            ServiceTermsAlreadyExistsException serviceTermsAlreadyExistsException = new ServiceTermsAlreadyExistsException();

            Mockito.when(serviceTermsService.create(request))
                    .thenThrow(serviceTermsAlreadyExistsException);

            mockMvc.perform(
                            post("/api/service-terms/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isConflict()
                    );
        }

        @Test
        @DisplayName("유효하지 않은 값들로 서비스 정책을 생성하면, 400 BAD REQUEST를 반환한다.")
        void 유효하지_않은_값들로_서비스_정책_생성() throws Exception {
            ServiceTermsRequest request = createRequest();
            request.setPoli_stat("!@#!44");

            mockMvc.perform(
                            post("/api/service-terms/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest()
                    );
        }
    }


    @Nested
    @DisplayName("PATCH /api/service-terms/modify/{poli_stat}")
    class Describe_modifyServiceTerms {

        @Test
        @DisplayName("존재하는 서비스 정책을 수정하면 성공.")
        void 존재하는_서비스_정책_수정() throws Exception {
            ServiceTermsRequest request = createRequest();

            Mockito.doNothing()
                   .when(serviceTermsService)
                   .modify(request);

            mockMvc.perform(
                        patch("/api/service-terms/modify/{poli_stat}", poli_stat)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNoContent()
                    );
        }

        @Test
        @DisplayName("존재하지 않는 서비스 정책을 수정하면, 404 NOT FOUND를 반환한다.")
        void 존재하지_않는_서비스_정책_수정() throws Exception {
            ServiceTermsRequest request = createRequest();
            ServiceTermsNotFoundException serviceTermsNotFoundException = new ServiceTermsNotFoundException();

            Mockito.doThrow(serviceTermsNotFoundException)
                   .when(serviceTermsService)
                   .modify(request);

            mockMvc.perform(
                        patch("/api/service-terms/modify/{poli_stat}", request)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNotFound()
                    );
        }

        @Test
        @DisplayName("유효하지 않은 값들로 서비스 정책을 수정하면, 400 BAD REQUEST를 반환한다.")
        void 유효하지_않은_값들로_서비스_정책_수정() throws Exception {
            ServiceTermsRequest request = createRequest();
            request.setPoli_stat("!@#!44");

            mockMvc.perform(
                            patch("/api/service-terms/modify/{poli_stat}", request)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest()
                    );
        }
    }


    @Nested
    @DisplayName("DELETE /api/service-terms/remove/{poli_stat}")
    class Describe_removeServiceTerms {

        @Test
        @DisplayName("존재하는 서비스 정책을 삭제하면, 204 NO CONTENT를 반환한다.")
        void 존재하는_서비스_정책_삭제() throws Exception {
            Mockito.doNothing()
                     .when(serviceTermsService)
                     .remove(poli_stat);

            mockMvc.perform(
                        delete("/api/service-terms/remove/{poli_stat}", poli_stat)
                    )
                    .andExpect(status().isNoContent()
            );

        }

        @Test
        @DisplayName("존재하지 않는 서비스 정책을 삭제하면, 404 NOT FOUND를 반환한다.")
        void 존재하지_않는_서비스_정책_삭제() {

        }
    }

    private ServiceTermsRequest createRequest() {
        return ServiceTermsRequest.builder()
                                    .poli_stat(poli_stat)
                                    .name("테스트용")
                                    .rule_stat1("AB0001")
                                    .op1("AND")
                                    .rule_stat2("AB0002")
                                    .op2("AND")
                                    .rule_stat3("AB0003")
                                    .comt("비고란")
                                    .chk_use("Y")
                                    .code("0000")
                                    .build();
    }

    private ServiceTermsResponse craeteResponse() {
        return ServiceTermsResponse.builder()
                                    .poli_stat(poli_stat)
                                    .name("테스트용")
                                    .rule_stat1("AB0001")
                                    .op1("AND")
                                    .rule_stat2("AB0002")
                                    .op2("AND")
                                    .rule_stat3("AB0003")
                                    .comt("비고란")
                                    .chk_use("Y")
                                    .code("0000")
                                    .build();
    }
}