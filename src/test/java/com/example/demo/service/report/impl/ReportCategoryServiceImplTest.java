package com.example.demo.service.report.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.report.ReportCategoryDto;
import com.example.demo.dto.report.ReportCategoryRequest;
import com.example.demo.dto.report.ReportCategoryResponse;
import com.example.demo.global.error.exception.business.report.ReportCategoryAlreadyExistsException;
import com.example.demo.global.error.exception.business.report.ReportCategoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.report.impl.ReportCategoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportCategoryServiceImplTest {

    @InjectMocks
    private ReportCategoryServiceImpl sut;

    @Mock
    private ReportCategoryDaoImpl reportCategoryDaoImpl;

    @Mock
    private CustomFormatter formatter;

    private final String CURRENT_DATE_FORMAT = "2025/01/30 12:00:00";
    private final Integer MANAGER_SEQ = 1;


    @Nested
    @DisplayName("사용자가 카테고리를 생성한다.")
    class sut_create_test {

        @Test
        @DisplayName("사용자가 전달한 카테고리 코드번호가 중복되는 경우 예외가 발생한다.")
        void it_throws_exception_when_duplicated_cate_code() {
            var request = createCategoryRequest();

            when(reportCategoryDaoImpl.existsByCateCode(any())).thenReturn(true);
            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            assertThrows(ReportCategoryAlreadyExistsException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("사용자가 카테고리를 생성하는 과정에서 DBMS에 정상 반영이 안되면 예외가 발생한다.")
        void it_throws_exception_when_failed_to_create() {
            var request = createCategoryRequest();

            when(reportCategoryDaoImpl.existsByCateCode(any())).thenReturn(false);
            when(reportCategoryDaoImpl.insert(any())).thenReturn(0);
            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            assertThrows(NotApplyOnDbmsException.class, () -> sut.create(request));
        }

        @Test
        @DisplayName("사용자가 카테고리를 성공적으로 생성했다면 카테고리 리스폰스가 반환되야 한다.")
        void it_correctly_return_response_when_successfully_create() {
            var request = createCategoryRequest();

            when(reportCategoryDaoImpl.existsByCateCode(any())).thenReturn(false);
            when(reportCategoryDaoImpl.insert(any())).thenReturn(1);
            when(formatter.getCurrentDateFormat()).thenReturn(CURRENT_DATE_FORMAT);
            when(formatter.getManagerSeq()).thenReturn(MANAGER_SEQ);

            ReportCategoryResponse actual = sut.create(request);
            assertNotNull(actual);
        }

    }

    @Nested
    @DisplayName("사용자가 카테고리를 조회한다 - 총 3가지 방식 지원함")
    class sut_read_test {

        // 1. 카테고리 코드번호로 조회
        @Test
        @DisplayName("사용자가 전달한 cate_code에 대한 카테고리가 존재하지 않을 경우 예외가 발생한다.")
        void it_throws_exception_when_not_found_cate_code() {
            String cateCode = "RC010101";
            when(reportCategoryDaoImpl.selectByCateCode(cateCode)).thenReturn(null);
            assertThrows(ReportCategoryNotFoundException.class, () -> sut.readByCateCode(cateCode));
        }

        @Test
        @DisplayName("사용자가 전달한 cate_code에 대한 카테고리를 성공적으로 조회하여 응답한다.")
        void it_correctly_return_response_when_successfully_read_by_cate_code() {
            String cateCode = "RC010101";
            var dto = ReportCategoryDto.builder()
                                       .cate_code(cateCode)
                                       .top_cate("RC010100")
                                       .name("테스트용 카테고리")
                                       .ord(1)
                                       .chk_use("Y")
                                       .level(1)
                                       .build();

            when(reportCategoryDaoImpl.selectByCateCode(cateCode)).thenReturn(dto);

            var actual = sut.readByCateCode(cateCode);

            assertNotNull(actual);
            assertEquals(cateCode, actual.getCate_code());
            assertEquals(dto.getTop_cate(), actual.getTop_cate());
            assertEquals(dto.getName(), actual.getName());
            assertEquals(dto.getOrd(), actual.getOrd());
            assertEquals(dto.getChk_use(), actual.getChk_use());
            assertEquals(dto.getLevel(), actual.getLevel());

        }

        // 2. 상위 카테고리 코드번호로 여러개 조회
        @Test
        @DisplayName("사용자가 전달한 top_cate에 대해 관련된 여러개의 카테고리를 성공적으로 응답한다.")
        void it_correctly_work_when_user_read_using_top_cate() {
            List<ReportCategoryDto> expected = new ArrayList<>();
            for (int i=0; i<5; i++) {
                var request = createCategoryRequest();
                expected.add(request.toDto(CURRENT_DATE_FORMAT, MANAGER_SEQ));
            }

            String topCate = "RC010100";
            when(reportCategoryDaoImpl.selectByTopCate(topCate)).thenReturn(expected);

            List<ReportCategoryResponse> actual = sut.readByTopCate(topCate);
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());

            for (int i=0; i<expected.size(); i++) {
                assertEquals(expected.get(i).getCate_code(), actual.get(i).getCate_code());
                assertEquals(expected.get(i).getTop_cate(), actual.get(i).getTop_cate());
                assertEquals(expected.get(i).getName(), actual.get(i).getName());
                assertEquals(expected.get(i).getOrd(), actual.get(i).getOrd());
                assertEquals(expected.get(i).getChk_use(), actual.get(i).getChk_use());
                assertEquals(expected.get(i).getLevel(), actual.get(i).getLevel());
            }
        }

        @Test
        @DisplayName("사용자가 모든 카테고리를 조회할 때 성공적으로 응답한다.")
        void it_correctly_work_when_user_read_all_categories() {
            List<ReportCategoryDto> expected = new ArrayList<>();
            for (int i=0; i<5; i++) {
                var request = createCategoryRequest();
                expected.add(request.toDto(CURRENT_DATE_FORMAT, MANAGER_SEQ));
            }

            when(reportCategoryDaoImpl.selectAll()).thenReturn(expected);

            List<ReportCategoryResponse> actual = sut.readAll();
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());

            for (int i=0; i<expected.size(); i++) {
                assertEquals(expected.get(i).getCate_code(), actual.get(i).getCate_code());
                assertEquals(expected.get(i).getTop_cate(), actual.get(i).getTop_cate());
                assertEquals(expected.get(i).getName(), actual.get(i).getName());
                assertEquals(expected.get(i).getOrd(), actual.get(i).getOrd());
                assertEquals(expected.get(i).getChk_use(), actual.get(i).getChk_use());
                assertEquals(expected.get(i).getLevel(), actual.get(i).getLevel());
            }
        }

    }

    @Nested
    @DisplayName("사용자가 카테고리를 수정한다. - 총 3가지 방식 지원함")
    class sut_modify_test {

        // 1. 사용자가 카테고리 내용 전반을 수정함
        @Test
        @DisplayName("사용자가 전달한 카테고리 정보에서 cate_code가 존재하지 않을 경우 예외가 발생한다.")
        void it_throws_exception_when_not_found_cate_code() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(false);
            assertThrows(ReportCategoryNotFoundException.class, () -> sut.modify(request));
        }

        @Test
        @DisplayName("사용자가 전달한 카테고리 정보를 수정하는 과정에서 DBMS에 정상 반영이 안되면 예외가 발생한다.")
        void it_throws_exception_when_failed_to_modify() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(true);
            when(reportCategoryDaoImpl.update(any())).thenReturn(0);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modify(request));
        }

        @Test
        @DisplayName("사용자가 전달한 카테고리 정보를 성공적으로 수정했다면 아무것도 반환하지 않는다.")
        void it_correctly_work_when_successfully_modify() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(true);
            when(reportCategoryDaoImpl.update(any())).thenReturn(1);
            assertDoesNotThrow(() -> sut.modify(request));
        }

        // 2. 사용자가 카테고리의 사용여부를 수정함 - Y
        @Test
        @DisplayName("사용자가 전달한 카테고리 정보에서 cate_code가 존재하지 않을 경우 예외가 발생한다.")
        void it_throws_exception_when_not_found_cate_code_for_enable() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(false);
            assertThrows(ReportCategoryNotFoundException.class, () -> sut.modifyChkUseY(request.getCate_code()));
        }

        @Test
        @DisplayName("사용자가 전달한 카테고리 정보를 수정하는 과정에서 DBMS에 정상 반영이 안되면 예외가 발생한다.")
        void it_throws_exception_when_failed_to_modify_chk_use_y() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(true);
            when(reportCategoryDaoImpl.updateChkUseY(request.getCate_code())).thenReturn(0);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modifyChkUseY(request.getCate_code()));
        }

        @Test
        @DisplayName("사용자가 전달한 카테고리 정보를 성공적으로 수정했다면 아무것도 반환하지 않는다.")
        void it_correctly_work_when_successfully_modify_chk_use_y() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(true);
            when(reportCategoryDaoImpl.updateChkUseY(request.getCate_code())).thenReturn(1);
            assertDoesNotThrow(() -> sut.modifyChkUseY(request.getCate_code()));
        }

        // 3. 사용자가 카테고리의 사용여부를 수정함 - N
        @Test
        @DisplayName("사용자가 전달한 카테고리 정보에서 cate_code가 존재하지 않을 경우 예외가 발생한다.")
        void it_throws_exception_when_not_found_cate_code_for_disable() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(false);
            assertThrows(ReportCategoryNotFoundException.class, () -> sut.modifyChkUseN(request.getCate_code()));
        }

        @Test
        @DisplayName("사용자가 전달한 카테고리 정보를 수정하는 과정에서 DBMS에 정상 반영이 안되면 예외가 발생한다.")
        void it_throws_exception_when_failed_to_modify_chk_use_n() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(true);
            when(reportCategoryDaoImpl.updateChkUseN(request.getCate_code())).thenReturn(0);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.modifyChkUseN(request.getCate_code()));
        }

        @Test
        @DisplayName("사용자가 전달한 카테고리 정보를 성공적으로 수정했다면 아무것도 반환하지 않는다.")
        void it_correctly_work_when_successfully_modify_chk_use_n() {
            var request = createCategoryRequest();
            when(reportCategoryDaoImpl.existsByCateCodeForUpdate(request.getCate_code())).thenReturn(true);
            when(reportCategoryDaoImpl.updateChkUseN(request.getCate_code())).thenReturn(1);
            assertDoesNotThrow(() -> sut.modifyChkUseN(request.getCate_code()));
        }
    }

    @Nested
    @DisplayName("사용자가 카테고리를 삭제한다. - 총 2가지 방식 지원함")
    class sut_remove_test {

        // 1. 카테고리 코드번호로 삭제
        @Test
        @DisplayName("사용자가 전달한 cate_code에 대한 카테고리를 삭제하는 과정에서 DBMS에 정상 반영이 안되면 예외가 발생한다.")
        void it_throws_exception_when_failed_to_remove() {
            String cateCode = "RC010101";
            when(reportCategoryDaoImpl.deleteByCateCode(cateCode)).thenReturn(0);
            assertThrows(NotApplyOnDbmsException.class, () -> sut.remove(cateCode));
        }

        @Test
        @DisplayName("사용자가 전달한 cate_code에 대한 카테고리를 성공적으로 삭제했다면 아무것도 반환하지 않는다.")
        void it_correctly_work_when_successfully_remove_by_cate_code() {
            String cateCode = "RC010101";
            when(reportCategoryDaoImpl.deleteByCateCode(cateCode)).thenReturn(1);
            assertDoesNotThrow(() -> sut.remove(cateCode));
        }

        // 2. 모든 카테고리 삭제
        @Test
        @DisplayName("사용자가 모든 카테고리를 삭제하는 과정에서 DBMS에 정상 반영이 안되면 예외가 발생한다.")
        void it_throws_exception_when_failed_to_remove_all() {
            when(reportCategoryDaoImpl.count()).thenReturn(10);
            when(reportCategoryDaoImpl.deleteByLevel(3)).thenReturn(0);
            when(reportCategoryDaoImpl.deleteByLevel(2)).thenReturn(0);
            when(reportCategoryDaoImpl.deleteByLevel(1)).thenReturn(0);

            assertThrows(NotApplyOnDbmsException.class, () -> sut.removeAll());
        }

        @Test
        @DisplayName("사용자가 모든 카테고리를 성공적으로 삭제했다면 아무것도 반환하지 않는다.")
        void it_correctly_work_when_successfully_remove_all() {
            when(reportCategoryDaoImpl.count()).thenReturn(10);
            when(reportCategoryDaoImpl.deleteByLevel(3)).thenReturn(3);
            when(reportCategoryDaoImpl.deleteByLevel(2)).thenReturn(3);
            when(reportCategoryDaoImpl.deleteByLevel(1)).thenReturn(4);

            assertDoesNotThrow(() -> sut.removeAll());
        }
    }

    private ReportCategoryRequest createCategoryRequest() {
        return ReportCategoryRequest.builder()
                .cate_code("RC010101")
                .top_cate("RC010100")
                .name("테스트용 카테고리")
                .ord(1)
                .chk_use("Y")
                .level(1)
                .build();
    }
}