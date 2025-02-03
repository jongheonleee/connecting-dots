package com.example.demo.service.report.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.dto.report.ReportProcessDetailsDto;
import com.example.demo.dto.report.ReportProcessDetailsRequest;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportProcessAlreadyExistsException;
import com.example.demo.global.error.exception.business.report.ReportProcessNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.code.impl.CommonCodeDaoImpl;
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
class ReportProcessDetailsServiceImplTest {

    @InjectMocks
    private ReportProcessDetailsServiceImpl sut;

    @Mock
    private ReportProcessDetailsDaoImpl reportProcessDetailsDaoImpl;

    @Mock
    private CommonCodeDaoImpl commonCodeDaoImpl;

    @Mock
    private ReportDaoImpl reportDaoImpl;

    @Mock
    private CustomFormatter formatter;

    private static final String APPL_BEGIN = "2025/02/03 00:00:00";
    private static final String APPL_END = "9999/12/31 23:59:59";

    private static final String PROS_CODE = "CD123456";

    private static final Integer MANAGER_SEQ = 1;
    private static final String REG_DATE = "2025/02/03 00:00:00";



    @Nested
    @DisplayName("리포트 처리 내역 등록 관련 테스트 - 2가지 기능 지원")
    class sut_create_renew_test {
        // 1. 리포트 처리 내역 등록(초기 등록)
        @Test
        @DisplayName("사용자가 리포트 처리 내역을 새롭게 등록하려고 했지만, 해당 리포트가 존재하지 않아서 예외가 발생한다.")
        void it_throws_report_not_found_exception_when_user_create_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();

            // when
            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(false);

            // then
            assertThrows(ReportNotFoundException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("리포트 처리 내역을 등록하는 과정에서 전달받은 코드가 존재하지 않는 경우 예외가 발생한다.")
        void it_throws_report_not_found_code_exception_when_user_create_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();

            // when
            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(false);

            // then
            assertThrows(CodeNotFoundException.class, () -> sut.create(request));
        }

        // 해당 경우는 항상 갱신해서 등록되게 처리해야함
        @Test
        @DisplayName("사용자가 리포트 처리 내역을 새롭게 등록하려고 했지만, 기존에 등록된 리포트 처리 내역이 존재하여 예외가 발생한다.")
        void it_throws_report_change_history_exists_exception_when_user_create_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();

            // when
            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(true);
            when(reportProcessDetailsDaoImpl.existsByRno(request.getRno())).thenReturn(true);

            // then
            assertThrows(ReportProcessAlreadyExistsException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("리포트 처리 내역을 등록하는 과정에서 DBMS에 적용되지 않아 예외가 발생한다.")
        void it_throws_not_apply_on_dbms_exception_when_user_create_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();
            ReportProcessDetailsDto dto = createDto(request);

            // when
            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(true);
            when(reportProcessDetailsDaoImpl.existsByRno(request.getRno())).thenReturn(false);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END);
            when(reportProcessDetailsDaoImpl.insert(dto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.create(request));
        }


        @Test
        @DisplayName("사용자가 리포트 처리 내역을 새롭게 등록한다.")
        void it_correctly_work_when_user_create_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();
            ReportProcessDetailsDto dto = createDto(request);

            // when
            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(true);
            when(reportProcessDetailsDaoImpl.existsByRno(request.getRno())).thenReturn(false);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END);
            when(reportProcessDetailsDaoImpl.insert(dto)).thenReturn(1);

            // then
            ReportProcessDetailsResponse actual = sut.create(request);
            assertNotNull(actual);
            assertEquals(request.getRno(), actual.getRno());
            assertEquals(request.getPros_code(), actual.getPros_code());
            assertEquals(request.getChk_use(), actual.getChk_use());

        }


        // 2. 리포트 처리 내역 갱신 & 등록(기존 처리 내역 갱신 후 새로운 처리 내역 등록)
        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하려고 했지만, 해당 리포트가 존재하지 않는 경우 예외가 발생한다.")
        void it_throws_report_not_found_exception_when_user_renew_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();

            // when
            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(false);

            // then
            assertThrows(ReportNotFoundException.class, () -> sut.renew(request));
        }

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하려고 했지만, 전달한 코드가 존재하지 않는 경우 예외가 발생한다.")
        void it_throws_code_not_found_exception_when_user_renew_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();

            // when
            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(false);

            // then
            assertThrows(CodeNotFoundException.class, () -> sut.renew(request));
        }

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하려고 했지만, 기존에 등록된 리포트 처리 내역이 존재하지 않는 경우 예외가 발생한다.")
        void it_throws_report_process_not_found_exception_when_user_renew_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();

            // when
            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(true);
            when(reportProcessDetailsDaoImpl.existsByRno(request.getRno())).thenReturn(false);

            // then
            assertThrows(ReportProcessNotFoundException.class, () -> sut.renew(request));
        }

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하는 과정에서 기존에 등록된 리포트의 종료 시간 업데이트가 정상적으로 처리되지 않은 경우 예외가 발생한다.")
        void it_throws_not_apply_on_dbms_exception_when_user_renew_report_process_details() {
            // given
            ReportProcessDetailsRequest request = createRequest();
            ReportProcessDetailsDto dto = createDto(request);

            // when
            when(formatter.minusDateFormat(1)).thenReturn(APPL_BEGIN);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(true);
            when(reportProcessDetailsDaoImpl.existsByRno(request.getRno())).thenReturn(true);


            when(reportProcessDetailsDaoImpl.selectLatestByRno(request.getRno())).thenReturn(dto);

            dto.updateApplEnd(APPL_END, REG_DATE, MANAGER_SEQ);
            when(reportProcessDetailsDaoImpl.update(dto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.renew(request));
        }

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 갱신하는 과정에서 새로운 리포트 등록이 정상적으로 처리되지 않은 경우 예외가 발생한다.")
        void it_throws_not_apply_on_dbms_exception_when_user_renew_report_process_details_by_not_created() {
            ReportProcessDetailsRequest request = createRequest();
            ReportProcessDetailsDto updatedDto = createDto(request);
            ReportProcessDetailsDto createdDto = createDto(request);

            // when
            when(formatter.minusDateFormat(1)).thenReturn(APPL_BEGIN);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(true);
            when(reportProcessDetailsDaoImpl.existsByRno(request.getRno())).thenReturn(true);


            when(reportProcessDetailsDaoImpl.selectLatestByRno(request.getRno())).thenReturn(updatedDto);

            updatedDto.updateApplEnd(APPL_END, REG_DATE, MANAGER_SEQ);
            when(reportProcessDetailsDaoImpl.update(updatedDto)).thenReturn(1);

            when(reportProcessDetailsDaoImpl.insert(createdDto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> sut.renew(request));

        }

        @Test
        @DisplayName("사용자가 리포트 처리 내역을 성공적으로 갱신한다.")
        void it_correctly_work_when_user_renew_report_process_details() {
            ReportProcessDetailsRequest request = createRequest();
            ReportProcessDetailsDto updatedDto = createDto(request);
            ReportProcessDetailsDto createdDto = createDto(request);

            // when
            when(formatter.minusDateFormat(1)).thenReturn(APPL_BEGIN);
            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);
            when(commonCodeDaoImpl.existsByCode(request.getPros_code())).thenReturn(true);
            when(reportProcessDetailsDaoImpl.existsByRno(request.getRno())).thenReturn(true);


            when(reportProcessDetailsDaoImpl.selectLatestByRno(request.getRno())).thenReturn(updatedDto);

            updatedDto.updateApplEnd(APPL_END, REG_DATE, MANAGER_SEQ);
            when(reportProcessDetailsDaoImpl.update(updatedDto)).thenReturn(1);

            when(reportProcessDetailsDaoImpl.insert(createdDto)).thenReturn(1);

            // then
            ReportProcessDetailsResponse actual = sut.renew(request);

            assertNotNull(actual);
            assertEquals(request.getRno(), actual.getRno());
            assertEquals(request.getPros_code(), actual.getPros_code());
            assertEquals(request.getChk_use(), actual.getChk_use());

        }

    }

    @Nested
    @DisplayName("리포트 처리 내역 조회 관련 테스트 - 3가지 기능 지원")
    class sut_read_test {
        // 1. 특정 처리 내역만 조회

        // 2. 특정 리포트와 관련된 모든 처리 내역 조회

        // 3. 모든 처리 내역 조회
    }

    @Nested
    @DisplayName("리포트 처리 내역 수정 관련 테스트")
    class sut_update_test {

    }

    @Nested
    @DisplayName("리포트 처리 내역 삭제 관련 테스트 - 3가지 기능 지원")
    class sut_delete_test {
        // 1. 특정 처리 내역만 삭제

        // 2. 특정 리포트와 관련된 모든 처리 내역 삭제

        // 3. 모든 처리 내역 삭제
    }

    private ReportProcessDetailsRequest createRequest() {
        return ReportProcessDetailsRequest.builder()
                .seq(1)
                .rno(1)
                .pros_code(PROS_CODE)
                .chk_use("Y")
                .build();
    }

    private ReportProcessDetailsDto createDto(ReportProcessDetailsRequest request) {
        return request.toDto(APPL_BEGIN, MANAGER_SEQ, APPL_BEGIN, APPL_END);
    }
}