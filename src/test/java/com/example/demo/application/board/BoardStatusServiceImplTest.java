package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.application.code.CommonCodeService;
import com.example.demo.dto.board.BoardStatusDto;
import com.example.demo.dto.board.BoardStatusRequest;
import com.example.demo.dto.board.BoardStatusResponse;
import com.example.demo.dto.code.CodeResponse;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.business.board.BoardStatusNotFoundException;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.board.BoardStatusDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardStatusServiceImplTest {

    @InjectMocks
    private BoardStatusService boardStatusService;

    @Mock
    private BoardStatusDaoImpl boardStatusDao;

    @Mock
    private CustomFormatter formatter;

    @Mock
    private CommonCodeService commonCodeService;

    @Mock
    private BoardDaoImpl boardDao;

    private final String reg_date = "2025-01-17";
    private final Integer reg_user_seq = 1;
    private final String up_date = "2025-01-17";
    private final Integer up_user_seq = 1;

    private final Integer days = 30;

    private final String appl_begin = "2025-01-17 00:00:00";
    private final String apple_update_date = "2025-01-17 00:00:00";
    private final String appl_end = "2025-02-17 00:00:00";



    @BeforeEach
    void setUp() {
        assertNotNull(boardStatusService);
        assertNotNull(boardStatusDao);
        assertNotNull(formatter);
        assertNotNull(commonCodeService);
        assertNotNull(boardDao);

    }

    @Nested
    @DisplayName("게시판 상태 등록 처리 테스트")
    class Describe_register {

        @DisplayName("게시판 상태 등록 처리 성공 테스트")
        @ParameterizedTest
        @CsvSource({"3001, 1, 30", "3002, 1, 30", "3003, 1, 30", "3004, 1, 30", "3005, 1, 30"})
        void It_success_register(String statCode, Integer bno, Integer days) {
            BoardStatusRequest request = createRequest(statCode, bno, days);

            // Given
            when(formatter.getManagerSeq()).thenReturn(reg_user_seq);
            when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
            when(formatter.plusDateFormat(request.getDays())).thenReturn(appl_end);

            when(boardDao.existsByBno(request.getBno())).thenReturn(true);

            BoardStatusDto dto = createBoardStatusDto(request);
            when(boardStatusDao.insert(dto)).thenReturn(1);

            BoardStatusResponse expected = createResponse(dto);
            // When

            BoardStatusResponse actual = boardStatusService.create(request);

            // Then
            assertEquals(expected.getBno(), actual.getBno());
            assertEquals(expected.getStat_code(), actual.getStat_code());
            assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
            assertEquals(expected.getAppl_end(), actual.getAppl_end());
        }

        @DisplayName("존재하지 않는 상태 코드 전달로 인한 게시판 상태 등록 처리 실패 테스트")
        @ParameterizedTest
        @CsvSource({"1, 1, 30", "2, 1, 30", "3, 1, 30", "4, 1, 30", "5, 1, 30"})
        void It_fail_register(String wrongStatCode, Integer bno, Integer days) {
            BoardStatusRequest request = createRequest(wrongStatCode, bno, days);
            assertThrows(CodeNotFoundException.class, () -> boardStatusService.create(request));
        }

        @DisplayName("존재하지 않는 bno 전달로 인한 게시판 상태 등록 처리 실패 테스트")
        @ParameterizedTest
        @CsvSource({"3001, 1, 30", "3002, 1, 30", "3003, 1, 30", "3004, 1, 30", "3005, 1, 30"})
        void It_fail_register_bno(String statCode, Integer wrongBno, Integer days) {
            BoardStatusRequest request = createRequest(statCode, wrongBno, days);
            when(boardDao.existsByBno(request.getBno())).thenReturn(false);
            assertThrows(BoardNotFoundException.class, () -> boardStatusService.create(request));
        }

        @DisplayName("DBMS 적용 불가로 인한 게시판 상태 등록 처리 실패 테스트")
        @ParameterizedTest
        @CsvSource({"3001, 1, 30", "3002, 1, 30", "3003, 1, 30", "3004, 1, 30", "3005, 1, 30"})
        void It_fail_register_dbms(String statCode, Integer bno, Integer days) {
            BoardStatusRequest request = createRequest(statCode, bno, days);
            when(formatter.getManagerSeq()).thenReturn(reg_user_seq);
            when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
            when(formatter.plusDateFormat(request.getDays())).thenReturn(appl_end);

            when(boardDao.existsByBno(request.getBno())).thenReturn(true);

            BoardStatusDto dto = createBoardStatusDto(request);
            when(boardStatusDao.insert(dto)).thenReturn(0);

            assertThrows(NotApplyOnDbmsException.class, () -> boardStatusService.create(request));
        }

    }

    @Nested
    @DisplayName("게시판 상태 수정 처리 테스트")
    class Describe_modify {

        @DisplayName("게시판 상태 수정 처리 성공 테스트")
        @ParameterizedTest
        @CsvSource({"3001, 1, 30", "3002, 1, 30", "3003, 1, 30", "3004, 1, 30", "3005, 1, 30"})
        void It_success_modify(String statCode, Integer bno, Integer days) {
            BoardStatusRequest request = createRequest(statCode, bno, days);
            BoardStatusDto currDto = createBoardStatusDto(request);
            BoardStatusDto newDto = createBoardStatusDto(request);

            // Given
            when(formatter.minusDateFormat(1)).thenReturn(apple_update_date);
            when(formatter.plusDateFormat(days)).thenReturn(appl_end);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            when(boardStatusDao.existsByBnoForUpdate(request.getBno())).thenReturn(true);
            when(boardStatusDao.selectByBnoAtPresent(request.getBno())).thenReturn(currDto);

            currDto.setAppl_end(apple_update_date);
            currDto.setUp_date(up_date);
            currDto.setUp_user_seq(up_user_seq);

            when(boardStatusDao.update(currDto)).thenReturn(1);
            when(boardStatusDao.insert(newDto)).thenReturn(1);

            // When
            // Then
            assertDoesNotThrow(() -> boardStatusService.renewState(request));
        }

        @DisplayName("게시판 상태 수정 처리 실패 테스트 - 존재하지 않는 bno")
        @ParameterizedTest
        @CsvSource({"3001, 1, 30", "3002, 1, 30", "3003, 1, 30", "3004, 1, 30", "3005, 1, 30"})
        void It_fail_modify(String statCode, Integer bno, Integer days) {
            // Given
            BoardStatusRequest request = createRequest(statCode, bno, days);
            when(boardStatusDao.existsByBnoForUpdate(request.getBno())).thenReturn(false);
            // When
            // Then
            assertThrows(BoardStatusNotFoundException.class, () -> boardStatusService.renewState(request));
        }

        @DisplayName("게시판 상태 수정 처리 실패 테스트 - DBMS 적용 불가")
        @ParameterizedTest
        @CsvSource({"3001, 1, 30", "3002, 1, 30", "3003, 1, 30", "3004, 1, 30", "3005, 1, 30"})
        void It_fail_modify_dbms(String statCode, Integer bno, Integer days) {
            // Given
            BoardStatusRequest request = createRequest(statCode, bno, days);
            BoardStatusDto currDto = createBoardStatusDto(request);
            BoardStatusDto newDto = createBoardStatusDto(request);

            when(formatter.minusDateFormat(1)).thenReturn(apple_update_date);
            when(formatter.plusDateFormat(days)).thenReturn(appl_end);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            when(boardStatusDao.existsByBnoForUpdate(request.getBno())).thenReturn(true);
            when(boardStatusDao.selectByBnoAtPresent(request.getBno())).thenReturn(currDto);

            currDto.setAppl_end(apple_update_date);
            currDto.setUp_date(up_date);
            currDto.setUp_user_seq(up_user_seq);

            when(boardStatusDao.update(currDto)).thenReturn(1);
            when(boardStatusDao.insert(newDto)).thenReturn(0);

            // When
            // Then
            assertThrows(NotApplyOnDbmsException.class, () -> boardStatusService.renewState(request));
        }

        @DisplayName("게시판 상태 수정 처리 실패 테스트 - DBMS 적용 실패")
        @ParameterizedTest
        @CsvSource({"3001, 1, 30", "3002, 1, 30", "3003, 1, 30", "3004, 1, 30", "3005, 1, 30"})
        void It_fail_modify_stat_code(String wrongStatCode, Integer bno, Integer days) {
            // Given
            BoardStatusRequest request = createRequest(wrongStatCode, bno, days);
            BoardStatusDto currDto = createBoardStatusDto(request);

            when(formatter.minusDateFormat(1)).thenReturn(appl_end);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            when(boardStatusDao.existsByBnoForUpdate(request.getBno())).thenReturn(true);
            when(boardStatusDao.selectByBnoAtPresent(request.getBno())).thenReturn(currDto);
            when(boardStatusDao.update(currDto)).thenReturn(0);

            // When
            // Then
            assertThrows(NotApplyOnDbmsException.class, () -> boardStatusService.renewState(request));
        }

        @DisplayName("게시판 상태 수정 처리 실패 테스트 - 존재하지 않는 게시글 상태 코드")
        @ParameterizedTest
        @CsvSource({"1, 1, 30", "2, 1, 30", "3, 1, 30", "4, 1, 30", "5, 1, 30"})
        void It_fail_modify_bno(String statCode, Integer wrongBno, Integer days) {
            // Given
            BoardStatusRequest request = createRequest(statCode, wrongBno, days);
            BoardStatusDto currDto = createBoardStatusDto(request);


            when(formatter.minusDateFormat(1)).thenReturn(appl_end);
            when(formatter.getCurrentDateFormat()).thenReturn(up_date);
            when(formatter.getManagerSeq()).thenReturn(up_user_seq);

            when(boardStatusDao.existsByBnoForUpdate(request.getBno())).thenReturn(true);
            when(boardStatusDao.selectByBnoAtPresent(request.getBno())).thenReturn(currDto);
            when(boardStatusDao.update(currDto)).thenReturn(1);

            // When
            // Then
            assertThrows(CodeNotFoundException.class, () -> boardStatusService.renewState(request));
        }

    }

    @Nested
    @DisplayName("게시판 상태 삭제 처리 테스트")
    class Describe_delete {

        @Test
        @DisplayName("게시판 상태 단건 삭제 처리 성공 테스트")
        void It_success_delete() {
            // Given
            Integer seq = 1;
            // When
            when(boardStatusDao.deleteBySeq(seq)).thenReturn(1);
            // Then
            assertDoesNotThrow(() -> boardStatusService.removeBySeq(seq));
        }

        @Test
        @DisplayName("게시판 상태 단건 삭제 처리 실패 테스트")
        void It_fail_delete() {
            // Given
            Integer seq = 1;
            // When
            when(boardStatusDao.deleteBySeq(seq)).thenReturn(0);
            // Then
            assertThrows(NotApplyOnDbmsException.class, () -> boardStatusService.removeBySeq(seq));
        }

        @Test
        @DisplayName("게시판 상태 bno로 전체 삭제 처리 성공 테스트")
        void It_success_delete_bno() {
            // Given
            Integer bno = 1;
            // When
            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardStatusDao.countByBno(bno)).thenReturn(1);
            when(boardStatusDao.deleteByBno(bno)).thenReturn(1);
            // Then
            assertDoesNotThrow(() -> boardStatusService.removeByBno(bno));
        }

        @Test
        @DisplayName("게시판 상태 bno로 전체 삭제 처리 실패 테스트")
        void It_fail_delete_bno() {
            // Given
            Integer bno = 1;
            // When
            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardStatusDao.countByBno(bno)).thenReturn(1);
            when(boardStatusDao.deleteByBno(bno)).thenReturn(0);
            // Then
            assertThrows(NotApplyOnDbmsException.class, () -> boardStatusService.removeByBno(bno));
        }

        @Test
        @DisplayName("게시판 상태 전체 삭제 처리 성공 테스트")
        void It_success_delete_all() {
            // Given
            // When
            when(boardStatusDao.count()).thenReturn(1);
            when(boardStatusDao.deleteAll()).thenReturn(1);
            // Then
            assertDoesNotThrow(() -> boardStatusService.removeAll());
        }

        @Test
        @DisplayName("게시판 상태 전체 삭제 처리 실패 테스트")
        void It_fail_delete_all() {
            // Given
            // When
            when(boardStatusDao.count()).thenReturn(1);
            when(boardStatusDao.deleteAll()).thenReturn(0);
            // Then
            assertThrows(NotApplyOnDbmsException.class, () -> boardStatusService.removeAll());
        }

    }

    @Nested
    @DisplayName("게시판 상태 조회 처리 테스트")
    class Describe_find {

        @Test
        @DisplayName("게시판 상태 단건 조회 처리 성공 테스트")
        void It_success_find() {
            Integer seq = 1;
            BoardStatusRequest request = createRequest("3001", 1, 30);
            BoardStatusDto dto = createBoardStatusDto(request);

            when(boardStatusDao.existsBySeq(seq)).thenReturn(true);
            when(boardStatusDao.selectBySeq(seq)).thenReturn(dto);

            BoardStatusResponse response = boardStatusService.readBySeq(seq);

            assertNotNull(response);

            assertEquals(dto.getSeq(), response.getSeq());
            assertEquals(dto.getBno(), response.getBno());
            assertEquals(dto.getStat_code(), response.getStat_code());
            assertEquals(dto.getComt(), response.getComt());
            assertEquals(dto.getAppl_begin(), response.getAppl_begin());
            assertEquals(dto.getAppl_end(), response.getAppl_end());
        }

        @Test
        @DisplayName("게시판 상태 단건 조회 처리 실패 테스트")
        void It_fail_find() {
            // Given
            Integer seq = 1;

            // When
            when(boardStatusDao.existsBySeq(seq)).thenReturn(false);

            // Then
            assertThrows(BoardStatusNotFoundException.class,
                    () -> boardStatusService.readBySeq(seq));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 29})
        @DisplayName("게시판 관련 상태 전체 조회 처리 성공 테스트")
        void It_success_find_all(int cnt) {
            // Given
            List<BoardStatusDto> dummy = new ArrayList<>();
            Integer bno = 1;

            for (int i=0; i<cnt; i++) {
                BoardStatusRequest request = createRequest("3001", bno, 30);
                BoardStatusDto dto = createBoardStatusDto(request);
                dummy.add(dto);
            }

            // When
            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardStatusDao.selectByBno(bno)).thenReturn(dummy);

            // Then
            List<BoardStatusResponse> result = boardStatusService.readByBno(bno);

            assertEquals(cnt, result.size());

            for (int i=0; i<cnt; i++) {
                BoardStatusDto expected = dummy.get(i);
                BoardStatusResponse actual = result.get(i);

                assertEquals(expected.getSeq(), actual.getSeq());
                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getStat_code(), actual.getStat_code());
                assertEquals(expected.getComt(), actual.getComt());
                assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
                assertEquals(expected.getAppl_end(), actual.getAppl_end());
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 29})
        @DisplayName("전체 게시판 상태 조회 처리 테스트")
        void It_fail_find_all(int cnt) {
            // Given
            List<BoardStatusDto> dummy = new ArrayList<>();
            Integer bno = 1;

            for (int i=0; i<cnt; i++) {
                BoardStatusRequest request = createRequest("3001", bno, 30);
                BoardStatusDto dto = createBoardStatusDto(request);
                dummy.add(dto);
            }

            // When
            when(boardStatusDao.selectAll()).thenReturn(dummy);

            // Then
            List<BoardStatusResponse> result = boardStatusService.readAll();

            assertEquals(cnt, result.size());

            for (int i=0; i<cnt; i++) {
                BoardStatusDto expected = dummy.get(i);
                BoardStatusResponse actual = result.get(i);

                assertEquals(expected.getSeq(), actual.getSeq());
                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getStat_code(), actual.getStat_code());
                assertEquals(expected.getComt(), actual.getComt());
                assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
                assertEquals(expected.getAppl_end(), actual.getAppl_end());
            }
        }
    }

    private BoardStatusRequest createRequest(String statCode, Integer bno, Integer days) {
        return BoardStatusRequest.builder()
                                 .stat_code(statCode)
                                 .bno(bno)
                                 .days(days)
                                 .build();
    }

    private CodeResponse createCodeResponse(String code) {
        return CodeResponse.builder()
                           .seq(1)
                           .level(1)
                           .code(code)
                           .name("테스트용 코드")
                           .chk_use("Y")
                           .top_code("3000")
                           .build();
    }

    private BoardStatusDto createBoardStatusDto(BoardStatusRequest request) {
        return BoardStatusDto.builder()
                             .bno(request.getBno())
                             .stat_code(request.getStat_code())
                             .comt(request.getComt())
                             .appl_begin(reg_date)
                             .appl_end(appl_end)
                             .reg_date(reg_date)
                             .reg_user_seq(reg_user_seq)
                             .up_date(up_date)
                             .up_user_seq(up_user_seq)
                             .build();
    }

    private BoardStatusResponse createResponse(BoardStatusDto dto) {
        return BoardStatusResponse.builder()
                                  .seq(dto.getSeq())
                                  .bno(dto.getBno())
                                  .stat_code(dto.getStat_code())
                                  .comt(dto.getComt())
                                  .appl_begin(dto.getAppl_begin())
                                  .appl_end(dto.getAppl_end())
                                  .build();
    }

}