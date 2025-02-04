package com.example.demo.service.report.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.demo.dto.report.ReportChangeHistoryResponse;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.dto.report.ReportRequest;
import com.example.demo.dto.report.ReportResponse;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.report.impl.ReportCategoryDaoImpl;
import com.example.demo.repository.report.impl.ReportDaoImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl sut;

    // 추후에 추가해야함
//    @Mock
//    private UserService userService;

    @Mock
    private ReportDaoImpl reportDaoImpl;

    @Mock
    private ReportCategoryServiceImpl reportCategoryServiceImpl;

    @Mock
    private ReportCategoryDaoImpl reportCategoryDaoImpl;

    @Mock
    private ReportChangeHistoryServiceImpl reportChangeHistoryServiceImpl;

    @Mock
    private ReportProcessDetailsServiceImpl reportProcessDetailsServiceImpl;

    @Mock
    private CustomFormatter formatter;

    private static final String PROS_CODE = "CD123456";
    private static final Integer MANAGER_SEQ = 1;
    private static final String REG_DATE = "2025/02/03 00:00:00";
    private static final String LAST_DATE = "9999/12/31 23:59:59";


    @Nested
    @DisplayName("리포트 생성 관련 테스트")
    class sut_create_test {

        /**
         * 검증
         * - 1. 전달 받은 카테고리가 존재하며 사용가능한지 확인
         * - 2. 전달 받은 신고자와 신고 대상자가 회원인지 확인
         *
         * 등록
         * - 1. 리포트 등록
         * - 2. 초기 변경 이력 등록
         * - 3. 초기 진행 내역 등록
         *
         * 반환
         * - 1. 등록된 dto -> response로 변환하여 반환
         */

        // 추후에 추가 구현해야함
        @Test
        @DisplayName("사용자가 리포트를 생성할 때, 신고자가 존재하지 않는 회원인 경우 예외가 발생한다.")
        void it_throws_exception_when_reporter_does_not_exist() {
            // given
            // when
            // then
        }

        // 추후에 추가 구현해야함
        @Test
        @DisplayName("사용자가 리포트를 생성할 때, 신고 대상자가 존재하지 않는 회원인 경우 예외가 발생한다.")
        void it_throws_exception_when_reportee_does_not_exist() {
            // given
            // when
            // then
        }

        @Test
        @DisplayName("사용자가 리포트를 생성할 때, 등록하려고 하는 리포트 카테고리가 존재하지 않는 경우 예외가 발생한다.")
        void it_throws_exception_when_report_category_does_not_exist() {
            // given
            ReportRequest request = createRequest();

            // when
            when(reportCategoryDaoImpl.existsByCateCode(request.getCate_code())).thenReturn(false);

            // then
            assertThrows(CodeNotFoundException.class, () -> sut.create(request));
       }

        @Test
        @DisplayName("사용자가 리포트를 생성할 때, 리포트 등록 과정이 정상적으로 DBMS에 반영되지 않는 경우 예외가 발생한다.")
        void it_throws_exception_when_report_registration_process_is_not_reflected_in_dbms() {
            // given
            ReportRequest request = createRequest();
            ReportDto dto = createDto(request);

            // when
            when(reportCategoryDaoImpl.existsByCateCode(request.getCate_code())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(LAST_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(reportDaoImpl.insert(dto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("사용자가 리포트를 생성할 때, 초기 변경 이력을 등록하는 과정에서 예외가 발생하여 실패한다.")
        void it_fails_when_exception_occurs_in_the_process_of_registering_initial_change_history() {
            // given
            ReportRequest request = createRequest();
            ReportDto dto = createDto(request);

            // when
            when(reportCategoryDaoImpl.existsByCateCode(request.getCate_code())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(LAST_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(reportDaoImpl.insert(dto)).thenReturn(1);
            when(reportChangeHistoryServiceImpl.create(any())).thenThrow(RuntimeException.class);

            // then
            assertThrows(RuntimeException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("사용자가 리포트를 생성할 때, 초기 진행 내역을 등록하는 과정에서 예외가 발생하여 실패한다.")
        void it_fails_when_exception_occurs_in_the_process_of_registering_initial_progress_history() {
            // given
            ReportRequest request = createRequest();
            ReportDto dto = createDto(request);

            // when
            when(reportCategoryDaoImpl.existsByCateCode(request.getCate_code())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(LAST_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(reportDaoImpl.insert(dto)).thenReturn(1);
            when(reportChangeHistoryServiceImpl.create(any())).thenReturn(ReportChangeHistoryResponse.builder().build());
            when(reportProcessDetailsServiceImpl.create(any())).thenThrow(RuntimeException.class);

            // then
            assertThrows(RuntimeException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("사용자가 리포트를 성공적으로 생성한다.")
        void it_correctly_work_when_user_create_report() {
            // given
            ReportRequest request = createRequest();
            ReportDto dto = createDto(request);

            // when
            when(reportCategoryDaoImpl.existsByCateCode(request.getCate_code())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(LAST_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(reportDaoImpl.insert(dto)).thenReturn(1);
            when(reportChangeHistoryServiceImpl.create(any())).thenReturn(ReportChangeHistoryResponse.builder().build());
            when(reportProcessDetailsServiceImpl.create(any())).thenReturn(ReportProcessDetailsResponse.builder().build());

            // then
            ReportResponse actual = sut.create(request);

            assertNotNull(actual);
            assertEquals(dto.getRno(), actual.getRno());
            assertEquals(dto.getCate_code(), actual.getCate_code());
            assertEquals(dto.getTitle(), actual.getTitle());
            assertEquals(dto.getCont(), actual.getCont());
            assertEquals(dto.getChk_change(), actual.getChk_change());
            assertEquals(dto.getComt(), actual.getComt());
            assertEquals(dto.getRepo_seq(), actual.getRepo_seq());
            assertEquals(dto.getResp_seq(), actual.getResp_seq());
            assertEquals(dto.getBoar(), actual.getBoar());
            assertEquals(dto.getCmnt(), actual.getCmnt());
            assertEquals(dto.getRepl(), actual.getRepl());
            assertEquals(dto.getType(), actual.getType());


        }



    }

    @Nested
    @DisplayName("리포트 조회 관련 테스트")
    class sut_read_test {

    }


    @Nested
    @DisplayName("리포트 수정 관련 테스트")
    class sut_modify_test {

    }

    @Nested
    @DisplayName("리포트 삭제 관련 테스트")
    class sut_remove_test {

    }


    private ReportRequest createRequest() {
        return ReportRequest.builder()
                            .cate_code("AA123456")
                            .title("신고합니다.")
                            .cont("신고합니다.")
                            .chk_change("N")
                            .comt("신고합니다.")
                            .repo_seq(1)
                            .resp_seq(2)
                            .boar(1)
                            .cmnt(1)
                            .repl(1)
                            .type(0)
                            .build();
    }


    private ReportDto createDto(ReportRequest request) {
        return ReportDto.builder()
                        .cate_code(request.getCate_code())
                        .title(request.getTitle())
                        .cont(request.getCont())
                        .chk_change(request.getChk_change())
                        .comt(request.getComt())
                        .repo_seq(request.getRepo_seq())
                        .resp_seq(request.getResp_seq())
                        .boar(request.getBoar())
                        .cmnt(request.getCmnt())
                        .repl(request.getRepl())
                        .type(request.getType())
                        .reg_date(REG_DATE)
                        .reg_user_seq(MANAGER_SEQ)
                        .up_date(REG_DATE)
                        .up_user_seq(MANAGER_SEQ)
                        .build();
    }


}