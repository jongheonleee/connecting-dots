package com.example.demo.service.report.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.demo.dto.report.ReportChangeHistoryResponse;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.dto.report.ReportRequest;
import com.example.demo.dto.report.ReportResponse;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportAlreadyProcessedException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.report.impl.ReportCategoryDaoImpl;
import com.example.demo.repository.report.impl.ReportDaoImpl;
import com.example.demo.repository.report.impl.ReportProcessDetailsDaoImpl;
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
    private ReportProcessDetailsDaoImpl reportProcessDetailsDaoImpl;

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
    @DisplayName("리포트 조회 관련 테스트 - 총 3가지 기능 지원")
    class sut_read_test {

        // 1. 관리자 페이지에서 모든 리포트 조회함
        // 추후에 개발 진행

        // 2. 사용자가 자신이 작성한 모든 리포트 조회함
        // 추후에 개발 진행

        // 3. 특정 리포트를 상세하게 조회함
        @Test
        @DisplayName("사용자가 특정 리포트를 상세 조회하려고 할 때, 해당 리포트가 존재하지 않는 경우 예외가 발생한다.")
        void it_throws_exception_when_specific_report_does_not_exist_when_user_tries_to_view_specific_report() {
            // given
            Integer rno = 1;

            // when
            when(reportDaoImpl.selectByRno(rno)).thenReturn(null);

            // then
            assertThrows(ReportNotFoundException.class, () -> sut.readReportDetailsByRno(rno));
        }

        @Test
        @DisplayName("사용자가 특정 리포트를 상세 조회하려고 할 때, 처리 내역이 존재하지 않는 경우 예외가 발생한다.")
        void it_throws_exception_when_processing_history_does_not_exist_when_user_tries_to_view_specific_report() {
            // given
            Integer rno = 1;
            ReportDto dto = ReportDto.builder().build();

            // when
            when(reportDaoImpl.selectByRno(rno)).thenReturn(dto);
            doThrow(RuntimeException.class).when(reportProcessDetailsServiceImpl).readByRnoAtPresent(rno);

            // then
            assertThrows(RuntimeException.class, () -> sut.readReportDetailsByRno(rno));
        }

        @Test
        @DisplayName("사용자가 특정 리포트를 성공적으로 조회한다")
        void it_correctly_work_when_user_views_specific_report() {
            // given
            Integer rno = 1;
            ReportDto dto = ReportDto.builder().build();
            ReportProcessDetailsResponse processDetailsResponse = ReportProcessDetailsResponse.builder().build();

            // when
            when(reportDaoImpl.selectByRno(rno)).thenReturn(dto);
            when(reportProcessDetailsServiceImpl.readByRnoAtPresent(rno)).thenReturn(processDetailsResponse);

            // then
            assertDoesNotThrow(() -> sut.readReportDetailsByRno(rno));
        }

    }


    @Nested
    @DisplayName("리포트 수정 관련 테스트 - 총 2가지 기능 지원")
    class sut_modify_test {

        // 1. 사용자가 리포트 내용 자체를 수정한다.
        @Test
        @DisplayName("사용자가 리포트를 수정하려고 할 때, 해당 리포트가 존재하지 않는 경우 예외가 발생한다.")
        void it_throws_exception_when_report_does_not_exist_when_user_tries_to_modify_report() {
            // given
            ReportRequest request = createRequest();

            // when
            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(false);

            // then
            assertThrows(ReportNotFoundException.class, () -> sut.modify(request));
        }

        // 추후에 개발할 예정
        @Test
        @DisplayName("사용자가 리포트를 수정하려고 할 때, 자신의 리포트가 아닌 것을 수정하려고 하면 예외가 발생한다.")
        void it_throws_exception_when_user_tries_to_modify_report_that_is_not_his_own() {
            // given
            // when
            // then
        }

        @Test
        @DisplayName("사용자가 리포트를 수정하려고 할 때, 리포트 처리 내역이 이미 진행되어서 수정할 수 없는 경우 예외가 발생한다.")
        void it_throws_exception_when_user_tries_to_modify_report_that_has_already_been_processed() {
            // given
            ReportRequest request = createRequest();

            // when
            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);
            when(reportProcessDetailsServiceImpl.canChangeReport(request.getRepo_seq())).thenReturn(false);

            // then
            assertThrows(ReportAlreadyProcessedException.class, () -> sut.modify(request));
        }

        @Test
        @DisplayName("사용자가 리포트를 수정하는 과정에서 RDDMS에 반영되지 않아서 예외가 발생한다.")
        void it_throws_exception_when_report_is_not_reflected_in_rddms_when_user_modifies_report() {
            // given
            ReportRequest request = createRequest();
            ReportDto dto = createDto(request);

            // when
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(LAST_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);
            when(reportProcessDetailsServiceImpl.canChangeReport(request.getRepo_seq())).thenReturn(true);
            when(reportDaoImpl.update(dto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modify(request));

        }

        @Test
        @DisplayName("사용자가 리포트를 수정하는 과정에서 리포트 변경 이력을 갱신하는 과정에서 예외가 발생하여 실패한다.")
        void it_fails_when_exception_occurs_while_updating_report_change_history_in_the_process_of_modifying_report() {
            // given
            ReportRequest request = createRequest();
            ReportDto dto = createDto(request);

            // when
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(LAST_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);
            when(reportProcessDetailsServiceImpl.canChangeReport(request.getRepo_seq())).thenReturn(true);
            when(reportDaoImpl.update(dto)).thenReturn(1);
            doThrow(RuntimeException.class).when(reportChangeHistoryServiceImpl).renew(any());

            // then
            assertThrows(RuntimeException.class, () -> sut.modify(request));

        }

        @Test
        @DisplayName("사용자가 성공적으로 리포트를 수정한다.")
        void it_correctly_work_when_user_modifies_report() {
            // given
            ReportRequest request = createRequest();
            ReportDto dto = createDto(request);

            // when
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(LAST_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);
            when(reportProcessDetailsServiceImpl.canChangeReport(request.getRepo_seq())).thenReturn(true);
            when(reportDaoImpl.update(dto)).thenReturn(1);
            when(reportChangeHistoryServiceImpl.renew(any())).thenReturn(ReportChangeHistoryResponse.builder().build());

            // then
            assertDoesNotThrow(() -> sut.modify(request));

        }

    }

    @Nested
    @DisplayName("리포트 삭제 관련 테스트 - 3가지 기능 지원")
    class sut_remove_test {

        // 1. 단건 삭제

        // 추후에 추가 구현[]
        @Test
        @DisplayName("사용자가 특정 리포트를 삭제하려고 했지만, 자신의 리포트 글이 아니기에 예외가 발생한다.")
        void it_throws_exception_when_user_tries_to_delete_report_that_is_not_his_own() {
            // given
            // when
            // then
        }

        @Test
        @DisplayName("사용자가 특정 리포트를 삭제하려고 했지만, 이미 리포트가 처리가 되어 삭제할 수 없는 경우 예외가 발생한다.")
        void it_throws_exception_when_user_tries_to_delete_report_that_has_already_been_processed() {
            // given
            Integer rno = 1;
            ReportDto dto = ReportDto.builder().build();

            // when
            when(reportDaoImpl.selectByRno(rno)).thenReturn(dto);
            // 신고자의 시퀀스와 현재 회원의 시퀀스를 비교함 -> 이 과정에서 true 반환
            when(reportProcessDetailsServiceImpl.canChangeReport(rno)).thenReturn(false);


            // then
            assertThrows(ReportAlreadyProcessedException.class, () -> sut.removeByRno(rno));
        }

        @Test
        @DisplayName("사용자가 특정 리포트를 삭제할 때, 변경 이력을 삭제하는 과정에서 예외가 발생하여 실패한다.")
        void it_fails_when_exception_occurs_in_the_process_of_deleting_change_history() {
            // given
            Integer rno = 1;
            ReportDto dto = ReportDto.builder().build();

            // when
            when(reportDaoImpl.selectByRno(rno)).thenReturn(dto);
            // 신고자의 시퀀스와 현재 회원의 시퀀스를 비교함 -> 이 과정에서 true 반환
            when(reportProcessDetailsServiceImpl.canChangeReport(rno)).thenReturn(true);
            doThrow(RuntimeException.class).when(reportChangeHistoryServiceImpl).removeByRno(rno);

            // then
            assertThrows(RuntimeException.class, () -> sut.removeByRno(rno));
        }

        @Test
        @DisplayName("사용자가 특정 리포트를 삭제할 때, 해당 리포트 관련 처리 내역을 삭제하는 도중에 예외가 발생하면 실패한다.")
        void it_fails_when_exception_occurs_while_deleting_report_related_processing_history() {
            // given
            Integer rno = 1;
            ReportDto dto = ReportDto.builder().build();

            // when
            when(reportDaoImpl.selectByRno(rno)).thenReturn(dto);
            // 신고자의 시퀀스와 현재 회원의 시퀀스를 비교함 -> 이 과정에서 true 반환
            when(reportProcessDetailsServiceImpl.canChangeReport(rno)).thenReturn(true);
            doNothing().when(reportChangeHistoryServiceImpl).removeByRno(rno);
            doThrow(RuntimeException.class).when(reportProcessDetailsServiceImpl).removeByRno(rno);

            // then
            assertThrows(RuntimeException.class, () -> sut.removeByRno(rno));
        }

        @Test
        @DisplayName("사용자가 특정 리포트를 삭제하는 과정에서 해당 리포트를 삭제할 때 정상적으로 RDDMS에 반영되지 않아서 예외가 발생한다.")
        void it_throws_exception_when_report_is_not_reflected_in_rddms_when_user_deletes_specific_report() {
            // given
            Integer rno = 1;
            ReportDto dto = ReportDto.builder().build();

            // when
            when(reportDaoImpl.selectByRno(rno)).thenReturn(dto);
            // 신고자의 시퀀스와 현재 회원의 시퀀스를 비교함 -> 이 과정에서 true 반환
            when(reportProcessDetailsServiceImpl.canChangeReport(rno)).thenReturn(true);
            doNothing().when(reportChangeHistoryServiceImpl).removeByRno(rno);
            doNothing().when(reportProcessDetailsServiceImpl).removeByRno(rno);
            when(reportDaoImpl.delete(rno)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.removeByRno(rno));
        }

        @Test
        @DisplayName("사용자가 특정 리포트를 성공적으로 삭제한다.")
        void it_correctly_work_when_user_delete_report() {
            // given
            Integer rno = 1;
            ReportDto dto = ReportDto.builder().build();

            // when
            when(reportDaoImpl.selectByRno(rno)).thenReturn(dto);
            // 신고자의 시퀀스와 현재 회원의 시퀀스를 비교함 -> 이 과정에서 true 반환
            when(reportProcessDetailsServiceImpl.canChangeReport(rno)).thenReturn(true);
            doNothing().when(reportChangeHistoryServiceImpl).removeByRno(rno);
            doNothing().when(reportProcessDetailsServiceImpl).removeByRno(rno);
            when(reportDaoImpl.delete(rno)).thenReturn(1);

            // then
            assertDoesNotThrow(() -> sut.removeByRno(rno));
        }



        // 2. 사용자가 탈퇴할 때 해당 리포트 내용 모두 삭제 처리 - 추가 구현 필요
        @Test
        @DisplayName("사용자가 탈퇴하여 그와 관련된 모든 리포트를 삭제할 때, 해당 리포트를 삭제하는 과정에서 예외가 발생하면 실패한다.")
        void it_fail_when_exception_occurs_while_deleting_report_when_user_withdraws_and_deletes_all_related_reports() {
            // given
            // when
            // then
        }

        @Test
        @DisplayName("사용자가 탈퇴하여 그와 관련된 모든 리포트를 삭제할 때, 카운팅 된 리포트 개수와 삭제된 리포트 개수가 일치하지 않아서 예외가 발생한다.")
        void it_throws_exception_when_the_counted_report_number_and_the_deleted_report_number_do_not_match_when_the_user_withdraws_and_deletes_all_related_reports() {
            // given
            // when
            // then
        }

        @Test
        @DisplayName("사용자가 탈퇴하여 그와 관련된 모든 리포트를 성공적으로 삭제한다.")
        void it_correctly_work_when_user_withdraws_and_deletes_all_related_reports() {
            // given
            // when
            // then
        }

        // 3. 모든 리포트를 삭제한다.

        // 추후에 개발예정
        @Test
        @DisplayName("사용자가 모든 리포트를 삭제할 때, 사용자가 관리자가 아닌 경우 예외가 발생한다.")
        void it_throws_exception_when_user_is_not_administrator_when_user_deletes_all_reports() {
            // given
            // when
            // then
        }

        @Test
        @DisplayName("사용자가 모든 리포트를 삭제할 때, 변경 이력을 삭제하는 과정에서 예외가 발생하여 실패한다.")
        void it_fails_when_exception_occurs_in_the_process_of_deleting_change_history_when_user_deletes_all_reports() {
            // given
            // when
            doThrow(RuntimeException.class).when(reportChangeHistoryServiceImpl).removeAll();

            // then
            assertThrows(RuntimeException.class, () -> sut.removeAll());
        }

        @Test
        @DisplayName("사용자가 모든 리포트를 삭제할 때, 해당 리포트 관련 처리 내역을 삭제하는 도중에 예외가 발생하면 실패한다.")
        void it_fails_when_exception_occurs_while_deleting_report_related_processing_history_when_user_deletes_all_reports() {
            // given
            // when
            doNothing().when(reportChangeHistoryServiceImpl).removeAll();
            doThrow(RuntimeException.class).when(reportProcessDetailsServiceImpl).removeAll();

            // then
            assertThrows(RuntimeException.class, () -> sut.removeAll());
        }

        @Test
        @DisplayName("사용자가 모든 리포트를 삭제할 때, 해당 리포트를 삭제할 때 정상적으로 RDDMS에 반영되지 않아서 예외가 발생한다.")
        void it_throws_exception_when_report_is_not_reflected_in_rddms_when_user_deletes_all_reports() {
            // given
            // when
            doNothing().when(reportChangeHistoryServiceImpl).removeAll();
            doNothing().when(reportProcessDetailsServiceImpl).removeAll();
            when(reportDaoImpl.count()).thenReturn(10);
            when(reportDaoImpl.deleteAll()).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.removeAll());
        }

        @Test
        @DisplayName("사용자가 모든 리포트를 성공적으로 삭제한다.")
        void it_correctly_work_when_user_deletes_all_reports() {
            // given
            // when
            doNothing().when(reportChangeHistoryServiceImpl).removeAll();
            doNothing().when(reportProcessDetailsServiceImpl).removeAll();
            when(reportDaoImpl.count()).thenReturn(10);
            when(reportDaoImpl.deleteAll()).thenReturn(10);

            // then
            assertDoesNotThrow(() -> sut.removeAll());
        }

    }


    private ReportRequest createRequest() {
        return ReportRequest.builder()
                            .rno(1)
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
                        .rno(request.getRno())
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