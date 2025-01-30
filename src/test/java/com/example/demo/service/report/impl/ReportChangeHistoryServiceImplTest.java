package com.example.demo.service.report.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.report.ReportChangeHistoryDto;
import com.example.demo.dto.report.ReportChangeHistoryRequest;
import com.example.demo.global.error.exception.business.report.ReportChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.business.report.ReportNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.report.impl.ReportChangeHistoryDaoImpl;
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
class ReportChangeHistoryServiceImplTest {

    @InjectMocks
    private ReportChangeHistoryServiceImpl sut;

    @Mock
    private ReportChangeHistoryDaoImpl reportChangeHistoryDaoImpl;

    @Mock
    private ReportDaoImpl reportDaoImpl;

    @Mock
    private CustomFormatter formatter;

    private final String CURRENT_DATE_FORMAT = "2025/01/30 12:00:00";
    private final String APPL_BEGIN_TIME = "2025/01/30 12:00:00";
    private final String APPL_END_TIME = "9999/12/31 23:59:59";
    private final Integer MANAGER_SEQ = 1;


    @Nested
    @DisplayName("사용자가 리포트 변경 이력을 생성한다 - 총 2가지 방식 지원")
    class sut_create_test {

        // 1. 사용자가 변경이력을 생성함
        @Test
        @DisplayName("사용자가 최초로 변경 이력을 생성할 때, 해당 리포트가 존재하지 않으면 예외가 발생한다.")
        void it_throws_exception_when_report_not_exists() {
            var request = createRequest();

            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END_TIME);

            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(false);

            assertThrows(ReportNotFoundException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("사용자가 최초로 변경 이력을 생성할 때, DBMS에 정상 반영이 되지 않는 경우 예외가 발생한다.")
        void it_throws_exception_when_not_apply_on_dbms() {
            var request = createRequest();

            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END_TIME);

            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);

            when(reportChangeHistoryDaoImpl.insert(any())).thenReturn(0);

            assertThrows(NotApplyOnDbmsException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("사용자가 최초로 변경 이력을 생성할 때, 정상적으로 생성된다.")
        void it_creates_report_change_history() {
            var request = createRequest();

            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END_TIME);

            when(reportDaoImpl.existsByRno(request.getRno())).thenReturn(true);

            when(reportChangeHistoryDaoImpl.insert(any())).thenReturn(1);

            var actual = sut.create(request);

            assertNotNull(actual);
            assertEquals(request.getRno(), actual.getRno());
            assertEquals(request.getTitle(), actual.getTitle());
            assertEquals(request.getCont(), actual.getCont());

        }

        // 2. 사용자가 변경이력을 갱신함
        @Test
        @DisplayName("사용자가 변경 이력을 갱신할 때, 해당 리포트가 존재하지 않으면 예외가 발생한다.")
        void it_throws_exception_when_report_not_exists2() {
            var request = createRequest();

            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END_TIME);

            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(false);

            assertThrows(ReportNotFoundException.class, () -> sut.renew(request));
        }

        @Test
        @DisplayName("사용자가 변경 이력을 갱신할 때, 기존에 등록된 변경이력이 없으면 예외가 발생한다.")
        void it_throws_exception_when_report_change_history_not_exists() {
            var request = createRequest();

            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END_TIME);

            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);
            when(reportChangeHistoryDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(false);

            assertThrows(ReportChangeHistoryNotFoundException.class, () -> sut.renew(request));
        }

        @Test
        @DisplayName("사용자가 변경 이력을 갱신할 때, DBMS에 정상 반영이 되지 않는 경우 예외가 발생한다. - 기존 변경 이력 업데이트 처리 x")
        void it_throws_exception_when_not_apply_on_dbms2() {
            var request = createRequest();
            var dto = createDto();

            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END_TIME);
            when(formatter.minusDateFormat(1)).thenReturn(CURRENT_DATE_FORMAT);

            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);
            when(reportChangeHistoryDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);

            when(reportChangeHistoryDaoImpl.selectLatestByRno(request.getRno())).thenReturn(dto);
            when(reportChangeHistoryDaoImpl.update(dto)).thenReturn(0);

            assertThrows(NotApplyOnDbmsException.class, () -> sut.renew(request));
        }

        @Test
        @DisplayName("사용자가 변경 이력을 갱신할 때, DBMS에 정상 반영이 되지 않는 경우 예외가 발생한다. - 새로운 변경 이력 생성 처리 x")
        void it_throws_exception_when_not_apply_on_dbms3() {
            var request = createRequest();
            var dto = createDto();

            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END_TIME);
            when(formatter.minusDateFormat(1)).thenReturn(CURRENT_DATE_FORMAT);

            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);
            when(reportChangeHistoryDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);

            when(reportChangeHistoryDaoImpl.selectLatestByRno(request.getRno())).thenReturn(dto);
            when(reportChangeHistoryDaoImpl.update(dto)).thenReturn(1);
            when(reportChangeHistoryDaoImpl.insert(any())).thenReturn(0);

            assertThrows(NotApplyOnDbmsException.class, () -> sut.renew(request));
        }

        @Test
        @DisplayName("사용자가 변경 이력을 갱신할 때, 정상적으로 갱신된다.")
        void it_renews_report_change_history() {
            var request = createRequest();
            var dto = createDto();

            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getLastDateFormat()).thenReturn(APPL_END_TIME);
            when(formatter.minusDateFormat(1)).thenReturn(CURRENT_DATE_FORMAT);

            when(reportDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);
            when(reportChangeHistoryDaoImpl.existsByRnoForUpdate(request.getRno())).thenReturn(true);

            when(reportChangeHistoryDaoImpl.selectLatestByRno(request.getRno())).thenReturn(dto);
            when(reportChangeHistoryDaoImpl.update(dto)).thenReturn(1);
            when(reportChangeHistoryDaoImpl.insert(any())).thenReturn(1);

            var actual = sut.renew(request);

            assertNotNull(actual);
            assertEquals(request.getRno(), actual.getRno());
            assertEquals(request.getTitle(), actual.getTitle());
            assertEquals(request.getCont(), actual.getCont());
        }

    }

    @Nested
    @DisplayName("사용자가 리포트 변경 이력을 조회한다 - 총 3가지 방식 지원")
    class sut_read_test {

    }

    @Nested
    @DisplayName("사용자가 리포트 변경 이력을 수정한다")
    class sut_modify_test {

    }

    @Nested
    @DisplayName("사용자가 리포트 변경 이력을 삭제한다 - 총 3가지 방식 지원")
    class sut_remove_test {

    }

    private ReportChangeHistoryRequest createRequest() {
        return ReportChangeHistoryRequest.builder()
                                            .rno(1)
                                            .title("테스트용 리포트 변경 이력 입니다.")
                                            .cont("테스트용 리포트 변경 이력 내용 입니다.")
                                            .build();
    }

    private ReportChangeHistoryDto createDto() {
        return ReportChangeHistoryDto.builder()
                                        .seq(1)
                                        .rno(1)
                                        .title("테스트용 리포트 변경 이력 입니다.")
                                        .cont("테스트용 리포트 변경 이력 내용 입니다.")
                                        .appl_begin(APPL_BEGIN_TIME)
                                        .appl_end(APPL_END_TIME)
                                        .reg_user_seq(MANAGER_SEQ)
                                        .up_user_seq(MANAGER_SEQ)
                                        .reg_date(CURRENT_DATE_FORMAT)
                                        .up_date(CURRENT_DATE_FORMAT)
                                        .build();
    }
}