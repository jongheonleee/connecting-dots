package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.dto.board.BoardChangeHistoryDto;
import com.example.demo.dto.board.BoardChangeHistoryRequest;
import com.example.demo.dto.board.BoardChangeHistoryResponse;
import com.example.demo.global.error.exception.business.board.BoardChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardChangeHistoryDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardChangeHistoryServiceImplTest {

    @InjectMocks
    private BoardChangeHistoryService boardChangeHistoryService;

    @Mock
    private BoardChangeHistoryDaoImpl boardChangeHistoryDao;

    @Mock
    private BoardDaoImpl boardDao;

    @Mock
    private CustomFormatter customFormatter;

    private final String APPL_BEGIN = "2025-01-17 00:00:00";
    private final String APPL_END = "9999-12-31 23:59:59";
    private final String END_APPL = "2025-01-17 00:00:00";

    @BeforeEach
    void setUp() {
        assertNotNull(boardChangeHistoryService);
        assertNotNull(boardChangeHistoryDao);
        assertNotNull(boardDao);
        assertNotNull(customFormatter);
    }

    @Nested
    @DisplayName("게시글 변경 이력 조회 처리 관련 테스트")
    class GetBoardChangeHistory {

        @Test
        @DisplayName("한 게시글에 특정 변경 이력 하나만 조회 처리 - seq")
        void 한_게시글_특정_변경_이력_하나만_조회_테스트() {
            // given
            Integer seq = 1;
            BoardChangeHistoryDto dto = createBoardChangeHistoryDto(seq);

            // when
            when(boardChangeHistoryDao.existsBySeq(seq)).thenReturn(true);
            when(boardChangeHistoryDao.selectBySeq(seq)).thenReturn(dto);

            // then
            BoardChangeHistoryResponse actual = boardChangeHistoryService.readBySeq(seq);

            assertNotNull(actual);
            assertEquals(dto.getSeq(), actual.getSeq());
            assertEquals(dto.getBno(), actual.getBno());
            assertEquals(dto.getTitle(), actual.getTitle());
            assertEquals(dto.getCont(), actual.getCont());
            assertEquals(dto.getComt(), actual.getComt());
            assertEquals(dto.getAppl_begin(), actual.getAppl_begin());
            assertEquals(dto.getAppl_end(), actual.getAppl_end());
        }

        @Test
        @DisplayName("한 게시글에 특정 변경 이력 하나만 조회 처리 실패 - seq에 대한 게시글 변경 이력 없음")
        void 한_게시글_특정_변경_이력_하나만_조회_실패_테스트() {
            // given
            Integer seq = 1;

            // when
            when(boardChangeHistoryDao.existsBySeq(seq)).thenReturn(false);

            // then
            assertThrows(BoardChangeHistoryNotFoundException.class, () -> boardChangeHistoryService.readBySeq(seq));
        }

        @DisplayName("한 게시글에 모든 변경 이력 조회 처리 - bno")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 한_게시글_모든_변경_이력_조회_테스트(int cnt) {
            // given
            Integer bno = 1;

            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createBoardChangeHistoryDto(i);
                dummy.add(dto);
            }

            // when
            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.selectByBno(bno)).thenReturn(dummy);

            // then
            List<BoardChangeHistoryResponse> result = boardChangeHistoryService.readByBno(bno);

            assertNotNull(result);
            assertEquals(cnt, result.size());

            for (int i=0; i<cnt; i++) {
                var expected = dummy.get(i);
                var actual = result.get(i);

                assertEquals(expected.getSeq(), actual.getSeq());
                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getTitle(), actual.getTitle());
                assertEquals(expected.getCont(), actual.getCont());
                assertEquals(expected.getComt(), actual.getComt());
                assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
                assertEquals(expected.getAppl_end(), actual.getAppl_end());
            }
        }

        @DisplayName("모든 게시글에 대한 변경 이력 조회 처리 - all")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 모든_게시글_변경_이력_조회_테스트(int cnt) {
            // given
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                var dto = createBoardChangeHistoryDto(i);
                dummy.add(dto);
            }

            // when
            when(boardChangeHistoryDao.selectAll()).thenReturn(dummy);

            // then
            List<BoardChangeHistoryResponse> result = boardChangeHistoryService.readAll();

            assertNotNull(result);
            assertEquals(cnt, result.size());

            for (int i=0; i<cnt; i++) {
                var expected = dummy.get(i);
                var actual = result.get(i);

                assertEquals(expected.getSeq(), actual.getSeq());
                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getTitle(), actual.getTitle());
                assertEquals(expected.getCont(), actual.getCont());
                assertEquals(expected.getComt(), actual.getComt());
                assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
                assertEquals(expected.getAppl_end(), actual.getAppl_end());
            }
        }
    }


    @Nested
    @DisplayName("가장 최근 게시글 변경 이력 시간 업데이트 및 새로운 변경 이력 추가 처리 관련 테스트")
    class UpdateBoardChangeHistory {

        @Test
        @DisplayName("게시글 변경 이력 시간 업데이트 및 새로운 변경 이력 추가 처리")
        void 게시글_변경_이력_시간_업데이트_및_새로운_변경_이력_추가_테스트() {
            // given
            Integer bno = 1;
            BoardChangeHistoryDto ordDto = createBoardChangeHistoryDto(1);
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();
            BoardChangeHistoryDto newDto = createBoardChangeHistoryDto(request);

            // when
            when(customFormatter.getCurrentDateFormat()).thenReturn(APPL_BEGIN);
            when(customFormatter.minusDateFormat(1)).thenReturn(END_APPL);
            when(customFormatter.getLastDateFormat()).thenReturn(APPL_END);
            when(customFormatter.getManagerSeq()).thenReturn(1);

            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBnoForUpdate(bno)).thenReturn(true);
            when(boardChangeHistoryDao.selectLatestByBno(1)).thenReturn(ordDto);
            when(boardChangeHistoryDao.update(ordDto)).thenReturn(1);
            when(boardChangeHistoryDao.insert(newDto)).thenReturn(1);

            // then
            BoardChangeHistoryResponse actual = boardChangeHistoryService.renewBoardChangeHistory(bno, request);

            assertNotNull(actual);
            assertEquals(newDto.getSeq(), actual.getSeq());
            assertEquals(newDto.getBno(), actual.getBno());
            assertEquals(newDto.getTitle(), actual.getTitle());
            assertEquals(newDto.getCont(), actual.getCont());
            assertEquals(newDto.getComt(), actual.getComt());
            assertEquals(newDto.getAppl_begin(), actual.getAppl_begin());
            assertEquals(newDto.getAppl_end(), actual.getAppl_end());

        }

        @Test
        @DisplayName("게시글 변경 이력 시간 업데이트 및 새로운 변경 이력 추가 처리 실패 1 - bno에 대한 게시글 없음")
        void 게시글_변경_이력_시간_업데이트_및_새로운_변경_이력_추가_실패_테스트_1() {
            // given
            Integer bno = 1;
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();

            // when
            when(boardDao.existsByBno(bno)).thenReturn(false);

            // then
            assertThrows(BoardNotFoundException.class, () -> boardChangeHistoryService.renewBoardChangeHistory(bno, request));
        }

        @Test
        @DisplayName("게시글 변경 이력 시간 업데이트 및 새로운 변경 이력 추가 처리 실패 2 - 가장 최근 변경 이력이 존재하지 않는 경우")
        void 게시글_변경_이력_시간_업데이트_및_새로운_변경_이력_추가_실패_테스트_2() {
            // given
            Integer bno = 1;
            BoardChangeHistoryDto ordDto = createBoardChangeHistoryDto(1);
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();
            BoardChangeHistoryDto newDto = createBoardChangeHistoryDto(request);

            // when
            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBnoForUpdate(bno)).thenReturn(false);

            // then
            assertThrows(BoardChangeHistoryNotFoundException.class, () -> boardChangeHistoryService.renewBoardChangeHistory(bno, request));

        }

        @Test
        @DisplayName("게시글 변경 이력 시간 업데이트 및 새로운 변경 이력 추가 처리 실패 3 - update() 실패")
        void 게시글_변경_이력_시간_업데이트_및_새로운_변경_이력_추가_실패_테스트_3() {
            // given
            Integer bno = 1;
            BoardChangeHistoryDto ordDto = createBoardChangeHistoryDto(1);
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();

            // when
            when(customFormatter.minusDateFormat(1)).thenReturn(END_APPL);
            when(customFormatter.getManagerSeq()).thenReturn(1);

            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBnoForUpdate(bno)).thenReturn(true);
            when(boardChangeHistoryDao.selectLatestByBno(1)).thenReturn(ordDto);
            when(boardChangeHistoryDao.update(ordDto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> boardChangeHistoryService.renewBoardChangeHistory(bno, request));
        }

        @Test
        @DisplayName("게시글 변경 이력 시간 업데이트 및 새로운 변경 이력 추가 처리 실패 4 - insert() 실패")
        void 게시글_변경_이력_시간_업데이트_및_새로운_변경_이력_추가_실패_테스트_4() {
            // given
            Integer bno = 1;
            BoardChangeHistoryDto ordDto = createBoardChangeHistoryDto(1);
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();
            BoardChangeHistoryDto newDto = createBoardChangeHistoryDto(request);

            // when
            when(customFormatter.getCurrentDateFormat()).thenReturn(APPL_BEGIN);
            when(customFormatter.minusDateFormat(1)).thenReturn(END_APPL);
            when(customFormatter.getLastDateFormat()).thenReturn(APPL_END);
            when(customFormatter.getManagerSeq()).thenReturn(1);

            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBnoForUpdate(bno)).thenReturn(true);
            when(boardChangeHistoryDao.selectLatestByBno(1)).thenReturn(ordDto);
            when(boardChangeHistoryDao.update(ordDto)).thenReturn(1);
            when(boardChangeHistoryDao.insert(newDto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> boardChangeHistoryService.renewBoardChangeHistory(bno, request));
        }

    }

    @Nested
    @DisplayName("게시글 변경 이력 생성 처리 관련 테스트")
    class CreateBoardChangeHistory {

        @Test
        @DisplayName("게시글 변경 이력 생성 처리 성공")
        void 게시글_변경_이력_생성_테스트() {
            // given
            Integer bno = 1;
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();
            BoardChangeHistoryDto newDto = createBoardChangeHistoryDto(request);

            // when
            when(customFormatter.getCurrentDateFormat()).thenReturn(APPL_BEGIN);
            when(customFormatter.getLastDateFormat()).thenReturn(APPL_END);
            when(customFormatter.getManagerSeq()).thenReturn(1);

            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBno(bno)).thenReturn(false);
            when(boardChangeHistoryDao.insert(newDto)).thenReturn(1);

            // then
            BoardChangeHistoryResponse actual = boardChangeHistoryService.createInit(bno, request);

            assertNotNull(actual);
            assertEquals(newDto.getSeq(), actual.getSeq());
            assertEquals(newDto.getBno(), actual.getBno());
            assertEquals(newDto.getTitle(), actual.getTitle());
            assertEquals(newDto.getCont(), actual.getCont());
            assertEquals(newDto.getComt(), actual.getComt());
            assertEquals(newDto.getAppl_begin(), actual.getAppl_begin());
            assertEquals(newDto.getAppl_end(), actual.getAppl_end());
        }

        @Test
        @DisplayName("게시글 변경 이력 생성 처리 실패 1 - bno 관련 게시글 없음")
        void 게시글_변경_이력_생성_실패_테스트_1() {
            // given
            Integer bno = 1;
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();

            // when
            when(boardDao.existsByBno(bno)).thenReturn(false);

            // then
            assertThrows(BoardNotFoundException.class, () -> boardChangeHistoryService.createInit(bno, request));
        }

        @Test
        @DisplayName("게시글 변경 이력 생성 처리 실패 2 - 이미 존재하는 게시글 변경 이력")
        void 게시글_변경_이력_생성_실패_테스트_2() {
            // given
            Integer bno = 1;
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();

            // when
            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBno(bno)).thenReturn(true);

            // then
            assertThrows(BoardChangeHistoryNotFoundException.class, () -> boardChangeHistoryService.createInit(bno, request));
        }

        @Test
        @DisplayName("게시글 변경 이력 생성 처리 실패 3 - insert() 실패")
        void 게시글_변경_이력_생성_실패_테스트_3() {
            // given
            Integer bno = 1;
            BoardChangeHistoryRequest request = createBoardChangeHistoryRequest();
            BoardChangeHistoryDto newDto = createBoardChangeHistoryDto(request);

            // when
            when(customFormatter.getCurrentDateFormat()).thenReturn(APPL_BEGIN);
            when(customFormatter.getLastDateFormat()).thenReturn(APPL_END);
            when(customFormatter.getManagerSeq()).thenReturn(1);

            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBno(bno)).thenReturn(false);
            when(boardChangeHistoryDao.insert(newDto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> boardChangeHistoryService.createInit(bno, request));
        }


    }


    @Nested
    @DisplayName("게시글 변경 이력 삭제 처리 관련 테스트")
    class DeleteBoardChangeHistory {

        @DisplayName("한 게시글에 대한 변경 이력 삭제 처리 - seq")
        @Test
        void 한_게시글_변경_이력_삭제_테스트() {
            // given
            Integer seq = 1;

            // when
            when(boardChangeHistoryDao.existsBySeq(seq)).thenReturn(true);
            when(boardChangeHistoryDao.deleteBySeq(seq)).thenReturn(1);

            // then
            assertDoesNotThrow(() -> boardChangeHistoryService.removeBySeq(seq));
        }

        @DisplayName("한 게시글에 대한 변경 이력 삭제 처리 실패 - seq에 대한 게시글 없음")
        @Test
        void 한_게시글_변경_이력_삭제_실패_테스트_1() {
            // given
            Integer seq = 1;

            // when
            when(boardChangeHistoryDao.existsBySeq(seq)).thenReturn(false);

            // then
            assertThrows(BoardChangeHistoryNotFoundException.class, () -> boardChangeHistoryService.removeBySeq(seq));
        }

        @DisplayName("한 게시글에 대한 변경 이력 삭제 처리 실패 - DBMS 처리 실패")
        @Test
        void 한_게시글_변경_이력_삭제_실패_테스트_2() {
            // given
            Integer seq = 1;

            // when
            when(boardChangeHistoryDao.existsBySeq(seq)).thenReturn(true);
            when(boardChangeHistoryDao.deleteBySeq(seq)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> boardChangeHistoryService.removeBySeq(seq));
        }



        @DisplayName("한 게시글에 대한 변경 이력 삭제 처리 - bno")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 한_게시글_변경_이력_삭제_테스트(int cnt) {
            // given
            Integer bno = 1;

            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createBoardChangeHistoryDto(i);
                dummy.add(dto);
            }

            // when
            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.countByBno(bno)).thenReturn(cnt);
            when(boardChangeHistoryDao.deleteByBno(bno)).thenReturn(cnt);

            // then
            assertDoesNotThrow(() -> boardChangeHistoryService.removeByBno(bno));
        }

        @DisplayName("한 게시글에 대한 변경 이력 삭제 처리 실패 - bno에 대한 게시글 없음")
        @Test
        void 한_게시글_변경_이력_삭제_실패_테스트_3() {
            // given
            Integer bno = 1;

            // when
            when(boardDao.existsByBno(bno)).thenReturn(false);

            // then
            assertThrows(BoardNotFoundException.class, () -> boardChangeHistoryService.removeByBno(bno));
        }

        @DisplayName("한 게시글에 대한 변경 이력 삭제 처리 실패 - DBMS 처리 실패")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 한_게시글_변경_이력_삭제_실패_테스트_4(int cnt) {
            // given
            Integer bno = 1;
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var dto = createBoardChangeHistoryDto(i);
                dummy.add(dto);
            }

            // when
            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.existsByBno(bno)).thenReturn(true);
            when(boardChangeHistoryDao.countByBno(bno)).thenReturn(cnt);
            when(boardChangeHistoryDao.deleteByBno(bno)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> boardChangeHistoryService.removeByBno(bno));
        }

        @DisplayName("모든 게시글에 대한 변경 이력 삭제 처리 - all")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 모든_게시글_변경_이력_삭제_테스트(int cnt) {
            // given
            List<BoardChangeHistoryDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                var dto = createBoardChangeHistoryDto(i);
                dummy.add(dto);
            }

            // when
            when(boardChangeHistoryDao.count()).thenReturn(cnt);
            when(boardChangeHistoryDao.deleteAll()).thenReturn(cnt);

            // then
            assertDoesNotThrow(() -> boardChangeHistoryService.removeAll());

        }

        @DisplayName("한 게시글에 대한 변경 이력 삭제 처리 실패 - bno에 대한 게시글 없음")
        @Test
        void 한_게시글_변경_이력_삭제_실패_테스트() {
            // given
            Integer bno = 1;

            // when
            when(boardDao.existsByBno(bno)).thenReturn(false);

            // then
            assertThrows(BoardNotFoundException.class, () -> boardChangeHistoryService.removeByBno(bno));
        }

    }

    private BoardChangeHistoryDto createBoardChangeHistoryDto(int i) {
        return BoardChangeHistoryDto.builder()
                .bno(1)
                .title("게시글 제목" + i)
                .cont("게시글 내용")
                .comt("게시글 댓글")
                .appl_begin(APPL_BEGIN)
                .appl_end(APPL_END)
                .reg_date(APPL_BEGIN)
                .reg_user_seq(1)
                .up_date(APPL_END)
                .up_user_seq(1)
                .build();
    }

    private BoardChangeHistoryRequest createBoardChangeHistoryRequest() {
        return BoardChangeHistoryRequest.builder()
                .bno(1)
                .title("게시글 제목")
                .cont("게시글 내용")
                .comt("게시글 댓글")
                .build();
    }

    private BoardChangeHistoryDto createBoardChangeHistoryDto(BoardChangeHistoryRequest request) {
        return BoardChangeHistoryDto.builder()
                .bno(request.getBno())
                .title(request.getTitle())
                .cont(request.getCont())
                .comt(request.getComt())
                .appl_begin(APPL_BEGIN)
                .appl_end(APPL_END)
                .reg_date(APPL_BEGIN)
                .reg_user_seq(1)
                .up_date(APPL_END)
                .up_user_seq(1)
                .build();
    }


}