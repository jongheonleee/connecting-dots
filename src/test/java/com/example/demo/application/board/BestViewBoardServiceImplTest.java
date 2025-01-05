package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.board.BestViewBoardDto;
import com.example.demo.dto.board.BestViewBoardUpdateDto;
import com.example.demo.dto.board.BestViewBoardUpdateDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.repository.mybatis.board.BestViewBoardDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BestViewBoardServiceImplTest {

    @InjectMocks
    private BestViewBoardServiceImpl bestViewBoardService;

    @Mock
    private BestViewBoardDaoImpl bestViewBoardDao;

    @Mock
    private BoardDaoImpl boardDao;

    @BeforeEach
    void setUp() {
        assertNotNull(bestViewBoardService);
        assertNotNull(bestViewBoardDao);
        assertNotNull(boardDao);
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 30})
    void 카운팅_테스트(int cnt) {
        when(bestViewBoardDao.count()).thenReturn(cnt);
        assertEquals(cnt, bestViewBoardService.count());
    }

    @DisplayName("사용된 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 30})
    void 사용된_카운팅_테스트(int cnt) {
        when(bestViewBoardDao.countUsed()).thenReturn(cnt);
        assertEquals(cnt, bestViewBoardService.countUsed());
    }

    @DisplayName("베스트 댓글 게시판 저장 테스트")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 30})
    void 베스트_댓글_게시판_저장_테스트(int cnt) {
        List<BoardFormDto> topByView = createTopByView(cnt);
        when(boardDao.selectTopByView(cnt)).thenReturn(topByView);
        when(bestViewBoardDao.insert(any(BestViewBoardDto.class))).thenReturn(1);
        assertDoesNotThrow(() -> bestViewBoardService.saveForBestViewBoards(cnt));
    }

    @DisplayName("베스트 댓글 게시판 단건 저장 테스트")
    @Test
    void 베스트_댓글_게시판_단건_저장_테스트() {
        BoardFormDto boardFormDto = createBoardFormDto(1);
        BestViewBoardDto bestViewBoardDto = new BestViewBoardDto();
        when(bestViewBoardDao.insert(any(BestViewBoardDto.class))).thenReturn(1);
        assertDoesNotThrow(() -> bestViewBoardService.save(bestViewBoardDto));
    }

    @DisplayName("베스트 댓글 게시판 단건 조회 테스트")
    @Test
    void 베스트_댓글_게시판_단건_조회_테스트() {
        BestViewBoardDto bestViewBoardDto = new BestViewBoardDto();
        when(bestViewBoardDao.select(1)).thenReturn(bestViewBoardDto);
        assertEquals(bestViewBoardDto, bestViewBoardService.read(1));
    }

    @DisplayName("베스트 댓글 게시판 모두 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 30})
    void 베스트_댓글_게시판_모두_조회_테스트(int cnt) {
        List<BestViewBoardDto> bestViewBoardDtos = createBestViewBoardDtos(cnt);
        when(bestViewBoardDao.selectAll()).thenReturn(bestViewBoardDtos);
        assertEquals(bestViewBoardDtos, bestViewBoardService.readAll());
    }

    @DisplayName("베스트 댓글 게시판 뷰 전용 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {10})
    void 베스트_댓글_게시판_뷰_전용_조회_테스트(int cnt) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", 0);
        map.put("pageSize", 10);

        List<BoardResponseDto> boardResponses = createBoardResponses(cnt);
        when(bestViewBoardDao.selectForView(map)).thenReturn(boardResponses);
        assertEquals(boardResponses, bestViewBoardService.readForView(map));
    }

    @DisplayName("베스트 댓글 게시판 수정 테스트")
    @Test
    void 베스트_댓글_게시판_수정_테스트() {
        BestViewBoardUpdateDto bestViewBoardUpdateDto = createBestViewBoardUpdateDto(1);
        when(bestViewBoardDao.update(bestViewBoardUpdateDto)).thenReturn(1);
        assertDoesNotThrow(() -> bestViewBoardService.modify(bestViewBoardUpdateDto));
    }

    @DisplayName("베스트 댓글 게시판 Y -> N 업데이트 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 베스트_댓글_게시판_Y_N_업데이트_테스트(int cnt) {
        when(bestViewBoardDao.countForChangeUsed()).thenReturn(cnt);
        when(bestViewBoardDao.updateUsed("admin1234")).thenReturn(cnt);
        assertDoesNotThrow(() -> bestViewBoardService.modifyUsed("admin1234"));
    }

    @DisplayName("베스트 댓글 게시판 삭제 테스트")
    @Test
    void 베스트_댓글_게시판_삭제_테스트() {
        when(bestViewBoardDao.delete(1)).thenReturn(1);
        assertDoesNotThrow(() -> bestViewBoardService.remove(1));
    }

    @DisplayName("베스트 댓글 게시판 모두 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 베스트_댓글_게시판_모두_삭제_테스트(int cnt) {
        when(bestViewBoardDao.count()).thenReturn(cnt);
        when(bestViewBoardDao.deleteAll()).thenReturn(cnt);
        assertDoesNotThrow(() -> bestViewBoardService.removeAll());
    }

    private List<BoardFormDto> createTopByView(int cnt) {
        ArrayList<BoardFormDto> list = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            list.add(createBoardFormDto(i));
        }

        return list;
    }

    private BoardFormDto createBoardFormDto(int i) {
        var boardFormDto = new BoardFormDto();

        boardFormDto.setBno(i);
        boardFormDto.setTitle("제목" + i);
        boardFormDto.setContent("내용" + i);
        boardFormDto.setWriter("홍만동" + i);
        boardFormDto.setReg_date("2025-01-05 00:00:00");
        boardFormDto.setUp_date("2025-01-05 00:00:00");
        boardFormDto.setComt("...");

        return boardFormDto;
    }

    private List<BoardResponseDto> createBoardResponses(int cnt) {
        ArrayList<BoardResponseDto> list = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            list.add(createBoardResponseDto(i));
        }

        return list;
    }

    private BoardResponseDto createBoardResponseDto(int i) {
        var boardResponseDto = new BoardResponseDto();

        boardResponseDto.setBno(i);
        boardResponseDto.setTitle("제목" + i);
        boardResponseDto.setWriter("홍만동" + i);
        boardResponseDto.setReg_date("2025-01-05 00:00:00");

        return boardResponseDto;
    }

    private List<BestViewBoardUpdateDto> createBestViewBoardUpdateDtos(int cnt) {
        ArrayList<BestViewBoardUpdateDto> list = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            list.add(createBestViewBoardUpdateDto(i));
        }

        return list;
    }

    private BestViewBoardUpdateDto createBestViewBoardUpdateDto(int i) {
        var bestViewBoardUpdateDto = new BestViewBoardUpdateDto();

        bestViewBoardUpdateDto.setSeq(i);
        bestViewBoardUpdateDto.setUsed("N");

        return bestViewBoardUpdateDto;
    }

    private BestViewBoardDto createBestViewBoardDto(int i) {
        var bestViewBoardDto = new BestViewBoardDto();

        bestViewBoardDto.setBno(i);
        bestViewBoardDto.setAppl_begin("2025-01-05 00:00:00");
        bestViewBoardDto.setAppl_end("2025-02-05 00:00:00");
        bestViewBoardDto.setReg_id("admin1234");
        bestViewBoardDto.setReg_date("2025-01-05 00:00:00");
        bestViewBoardDto.setUp_id("admin1234");
        bestViewBoardDto.setUp_date("2025-01-05 00:00:00");
        bestViewBoardDto.setUsed("Y");

        return bestViewBoardDto;
    }

    private List<BestViewBoardDto> createBestViewBoardDtos(int cnt) {
        List<BestViewBoardDto> list = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            list.add(createBestViewBoardDto(i));
        }

        return list;
    }
}