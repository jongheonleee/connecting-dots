package com.example.demo.controller.service;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.service.service.impl.ServiceRuleUseServiceImpl;
import com.example.demo.dto.service.ServiceRuleUseRequest;
import com.example.demo.dto.service.ServiceRuleUseResponse;
import com.example.demo.global.error.GlobalExceptionHandler;
import com.example.demo.global.error.exception.business.service.ServiceRuleUseAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceRuleUseNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
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

/**
 * 컨트롤러에서 테스트해야할 내용
 * - 1. HTTP Request와 handler mapping 여부
 * - 2. Request Parameter, Body 값 검증
 * - 3. Service Layer 호출 여부
 * - 4. 기대한 HTTP Response 값 반환 여부
 */

// 단위 테스트와 가깝게 테스트 구성
// 스프링 부트 띄우지 말고 단위 테스트 진행
class ServiceRuleUseControllerTest {


    private MockMvc mockMvc;
    private ServiceRuleUseServiceImpl serviceRuleUseService;


    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                                                                         .serializers(LocalTimeSerializer.INSTANCE)
                                                                         .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                                                         .modules(new JavaTimeModule())
                                                                         .build();

    @BeforeEach
    void setUp() {
        // 목킹
        serviceRuleUseService = Mockito.mock(ServiceRuleUseServiceImpl.class);

        // mockMvc 설정
        // MappingJackson2HttpMessageConverter 설정
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper); // ObjectMapper 주입
        messageConverter.setDefaultCharset(StandardCharsets.UTF_8); // UTF-8 인코딩 설정

        // MockMvc 설정
        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceRuleUseController(serviceRuleUseService))
                                 .alwaysDo(print())
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .setMessageConverters(messageConverter) // JSON 메시지 컨버터 추가
                                 .addFilters(new CharacterEncodingFilter("UTF-8", true)) // UTF-8 필터 추가
                                 .build();
    }

    @Nested
    @DisplayName("GET /api/service-rule-use/{rule_stat}")
    class GetServiceRuleUse {

        @Test
        @DisplayName("정상적인 rule_stat이 주어지면 해당 rule_stat에 대한 정보를 반환한다.")
        void 정상적인_rule_stat이_주어지면_해당_rule_stat에_대한_정보를_반환한다() throws Exception {
            String rule_stat = "RS0001";
            ServiceRuleUseResponse expected = createResponse(rule_stat);

            Mockito.when(serviceRuleUseService.readByRuleStat(rule_stat))
                   .thenReturn(expected);


             mockMvc.perform(
                            get("/api/service-rule-use/{rule_stat}", rule_stat).accept(MediaType.APPLICATION_JSON)
                     )
                     .andExpect(status().isOk())
                     .andExpect(content().string(objectMapper.writeValueAsString(expected)));
        }

        @Test
        @DisplayName("존재하지 않는 rule_stat이 주어지면 404 NOT FOUND를 반환한다.")
        void 존재하지_않는_rule_stat이_주어지면_400_NOT_FOUND를_반환한다() throws Exception {
            String rule_stat = "RS0001";
            ServiceRuleUseNotFoundException serviceRuleUseNotFoundException = new ServiceRuleUseNotFoundException();

            Mockito.when(serviceRuleUseService.readByRuleStat(rule_stat))
                   .thenThrow(serviceRuleUseNotFoundException);

            mockMvc.perform(
                            get("/api/service-rule-use/{rule_stat}", rule_stat).accept(MediaType.APPLICATION_JSON)
                     )
                     .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("POST /api/service-rule-use/create")
    class PostServiceRuleUse {

        @Test
        @DisplayName("정상적인 요청 값이 주어지면 해당 정상적으로 생성한다.")
        void 정상적인_요청_값이_주어지면_해당_정상적으로_생성한다() throws Exception {
            String rule_stat = "RS0001";
            ServiceRuleUseRequest request = createRequest(rule_stat); // 요청 객체 생성
            ServiceRuleUseResponse expected = createResponse(rule_stat); // 예상 응답 객체 생성 (오타 수정)

            Mockito.when(serviceRuleUseService.create(request))
                    .thenReturn(expected);

            mockMvc.perform(
                            post("/api/service-rule-use/create") // 컨트롤러와 일치하는 URI
                                    .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                                    .content(objectMapper.writeValueAsString(request)) // 요청 본문 설정
                    )
                    .andExpect(status().isCreated()) // 응답 상태 코드 검증
                    .andExpect(content().json(objectMapper.writeValueAsString(expected))); // 응답 본문 검증
        }

        @Test
        @DisplayName("중복된 rule_stat이 주어지면 409 CONFLICT를 반환한다.")
        void 중복된_rule_stat이_주어지면_409_CONFLICT를_반환한다() throws Exception {
            String rule_stat = "RS0001";
            ServiceRuleUseRequest request = createRequest(rule_stat); // 요청 객체 생성

            Mockito.when(serviceRuleUseService.create(request))
                    .thenThrow(new ServiceRuleUseAlreadyExistsException());

            mockMvc.perform(
                            post("/api/service-rule-use/create") // 컨트롤러와 일치하는 URI
                                    .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                                    .content(objectMapper.writeValueAsString(request)) // 요청 본문 설정
                    )
                    .andExpect(status().isConflict()); // 응답 상태 코드 검증
        }

        @Test
        @DisplayName("DBMS 적용되지 않은 기능이 주어지면 500 INTERNAL_SERVER_ERROR를 반환한다.")
        void DBMS_적용되지_않은_기능이_주어지면_500_INTERNAL_SERVER_ERROR를_반환한다() throws Exception {
            String rule_stat = "RS0001";
            ServiceRuleUseRequest request = createRequest(rule_stat); // 요청 객체 생성

            Mockito.when(serviceRuleUseService.create(request))
                    .thenThrow(new NotApplyOnDbmsException());

            mockMvc.perform(
                            post("/api/service-rule-use/create") // 컨트롤러와 일치하는 URI
                                    .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                                    .content(objectMapper.writeValueAsString(request)) // 요청 본문 설정
                    )
                    .andExpect(status().is5xxServerError()); // 응답 상태 코드 검증
        }

    }

    @Nested
    @DisplayName("PATCH /api/service-rule-use/modify/{rule_stat}")
    class PatchServiceRuleUse {

        @Test
        @DisplayName("정상적인 요청 값이 주어지면 해당 정상적으로 수정한다.")
        void 정상적인_요청_값이_주어지면_해당_정상적으로_수정한다() throws Exception {
            String rule_stat = "RS0001";
            ServiceRuleUseRequest request = createRequest(rule_stat); // 요청 객체 생성

            mockMvc.perform(
                            patch("/api/service-rule-use/modify/{rule_stat}", rule_stat) // 컨트롤러와 일치하는 URI
                                    .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                                    .content(objectMapper.writeValueAsString(request)) // 요청 본문 설정
                    )
                    .andExpect(status().isNoContent()); // 응답 상태 코드 검증
        }

        @Test
        @DisplayName("존재하지 않는 rule_stat이 주어지면 404 NOT FOUND를 반환한다.")
        void 존재하지_않는_rule_stat이_주어지면_404_NOT_FOUND를_반환한다() throws Exception {
            String rule_stat = "RS0001";
            ServiceRuleUseNotFoundException serviceRuleUseNotFoundException = new ServiceRuleUseNotFoundException();

            Mockito.doThrow(serviceRuleUseNotFoundException)
                   .when(serviceRuleUseService)
                   .modify(any(ServiceRuleUseRequest.class));

            mockMvc.perform(
                            patch("/api/service-rule-use/modify/{rule_stat}", rule_stat) // 컨트롤러와 일치하는 URI
                                    .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                                    .content(objectMapper.writeValueAsString(createRequest(rule_stat))) // 요청 본문 설정
                    )
                    .andExpect(status().isNotFound()); // 응답 상태 코드 검증
        }

        @Test
        @DisplayName("DBMS 적용되지 않은 기능이 주어지면 500 INTERNAL_SERVER_ERROR를 반환한다.")
        void DBMS_적용되지_않은_기능이_주어지면_500_INTERNAL_SERVER_ERROR를_반환한다() throws Exception {
            String rule_stat = "RS0001";
            ServiceRuleUseRequest request = createRequest(rule_stat); // 요청 객체 생성

            Mockito.doThrow(new NotApplyOnDbmsException())
                   .when(serviceRuleUseService)
                   .modify(request);

            mockMvc.perform(
                            patch("/api/service-rule-use/modify/{rule_stat}", rule_stat) // 컨트롤러와 일치하는 URI
                                    .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                                    .content(objectMapper.writeValueAsString(request)) // 요청 본문 설정
                    )
                    .andExpect(status().is5xxServerError()); // 응답 상태 코드 검증
        }

    }

    @Nested
    @DisplayName("DELETE /api/service-rule-use/remove/{rule_stat}")
    class DeleteServiceRuleUse {

        @Test
        @DisplayName("정상적인 rule_stat이 주어지면 해당 rule_stat에 대한 정보를 삭제한다.")
        void 정상적인_rule_stat이_주어지면_해당_rule_stat에_대한_정보를_삭제한다() throws Exception {
            String rule_stat = "RS0001";

            // removeByRuleStat 메서드가 호출되면 아무 동작도 하지 않도록 설정
            Mockito.doNothing()
                    .when(serviceRuleUseService)
                    .removeByRuleStat(rule_stat);

            // 삭제 요청을 보내고 상태 코드 204 No Content를 반환하는지 확인
            mockMvc.perform(
                            delete("/api/service-rule-use/remove/{rule_stat}", rule_stat)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNoContent()); // 삭제 후 응답 상태가 204인지 확인
        }

        @Test
        @DisplayName("DBMS 적용되지 않은 기능이 주어지면 500 INTERNAL_SERVER_ERROR를 반환한다.")
        void DBMS_적용되지_않은_기능이_주어지면_500_INTERNAL_SERVER_ERROR를_반환한다() throws Exception {
            String rule_stat = "RS0001";

            Mockito.doThrow(new NotApplyOnDbmsException())
                   .when(serviceRuleUseService)
                   .removeByRuleStat(rule_stat);

            mockMvc.perform(
                            delete("/api/service-rule-use/remove/{rule_stat}", rule_stat)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().is5xxServerError()); // 응답 상태 코드 검증
        }


    }

    private ServiceRuleUseResponse createResponse(String rule_stat) {
        return ServiceRuleUseResponse.builder()
                .rule_stat(rule_stat)
                .chk_use("Y")
                .curr_use_op(1)
                .curr_use_val(1)
                .name("테스트용")
                .tar_name("테스트 칼럼")
                .op1(">=")
                .val1("15")
                .build();
    }

    private ServiceRuleUseRequest createRequest(String rule_stat) {
        return ServiceRuleUseRequest.builder()
                                    .rule_stat(rule_stat)
                                    .chk_use("Y")
                                    .curr_use_op(1)
                                    .curr_use_val(1)
                                    .name("테스트용")
                                    .tar_name("테스트 칼럼")
                                    .op1(">=")
                                    .val1("15")
                                    .code("0001")
                                    .build();
    }
}