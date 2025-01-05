package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.board.BestCommentBoardDto;
import com.example.demo.dto.board.BestCommentBoardUpdateDto;
import com.example.demo.dto.board.BestLikeBoardDto;
import com.example.demo.dto.board.BestLikeBoardUpdateDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.repository.mybatis.board.BestLikeBoardDaoImpl;
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
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BestLikeBoardServiceImplTest {

    @InjectMocks
    private BestLikeBoardServiceImpl bestLikeBoardService;

    @Mock
    private BestLikeBoardDaoImpl bestLikeBoardDao;

    @Mock
    private BoardDaoImpl boardDao;

    @BeforeEach
    void setUp() {
        assertNotNull(bestLikeBoardService);
        assertNotNull(bestLikeBoardDao);
        assertNotNull(boardDao);
    }


    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 30})
    void 카운팅_테스트(int cnt) {
        when(bestLikeBoardDao.count()).thenReturn(cnt);
        assertEquals(cnt, bestLikeBoardService.count());
    }

    @DisplayName("사용된 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 30})
    void 사용된_카운팅_테스트(int cnt) {
        when(bestLikeBoardDao.countUsed()).thenReturn(cnt);
        assertEquals(cnt, bestLikeBoardService.countUsed());
    }

    @DisplayName("베스트 좋아요 게시판 저장 테스트")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 30})
    void 베스트_좋아요_게시판_저장_테스트(int cnt) {
        List<BoardFormDto> topByLike = createTopByLike(cnt);
        when(boardDao.selectTopByReco(cnt)).thenReturn(topByLike);
        when(bestLikeBoardDao.insert(any(BestLikeBoardDto.class))).thenReturn(1);
        assertDoesNotThrow(() -> bestLikeBoardService.saveForBestLikeBoards(cnt));
    }

    @DisplayName("베스트 좋아요 게시판 단건 저장 테스트")
    @Test
    void 베스트_좋아요_게시판_단건_저장_테스트() {
        BoardFormDto boardFormDto = createBoardFormDto(1);
        BestLikeBoardDto bestLikeBoardDto = new BestLikeBoardDto();
        when(bestLikeBoardDao.insert(any(BestLikeBoardDto.class))).thenReturn(1);
        assertDoesNotThrow(() -> bestLikeBoardService.save(bestLikeBoardDto));
    }

    @DisplayName("베스트 좋아요 게시판 단건 조회 테스트")
    @Test
    void 베스트_좋아요_게시판_단건_조회_테스트() {
        BestLikeBoardDto bestLikeBoardDto = new BestLikeBoardDto();
        when(bestLikeBoardDao.select(1)).thenReturn(bestLikeBoardDto);
        assertEquals(bestLikeBoardDto, bestLikeBoardService.read(1));
    }

    @DisplayName("베스트 좋아요 게시판 모두 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 30})
    void 베스트_좋아요_게시판_모두_조회_테스트(int cnt) {
        List<BestLikeBoardDto> bestLikeBoardDtos = createBestLikeBoardDtos(cnt);
        when(bestLikeBoardDao.selectAll()).thenReturn(bestLikeBoardDtos);
        assertEquals(bestLikeBoardDtos, bestLikeBoardService.readAll());
    }

    @DisplayName("베스트 좋아요 게시판 뷰 전용 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {10})
    void 베스트_좋아요_게시판_뷰_전용_조회_테스트(int cnt) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", 0);
        map.put("pageSize", 10);

        List<BoardResponseDto> boardResponses = createBoardResponses(cnt);
        when(bestLikeBoardDao.selectForView(map)).thenReturn(boardResponses);
        assertEquals(boardResponses, bestLikeBoardService.readForView(map));
    }

    @DisplayName("베스트 좋아요 게시판 수정 테스트")
    @Test
    void 베스트_좋아요_게시판_수정_테스트() {
        BestLikeBoardUpdateDto bestLikeBoardUpdateDto = createBestLikeBoardUpdateDto(1);
        when(bestLikeBoardDao.update(bestLikeBoardUpdateDto)).thenReturn(1);
        assertDoesNotThrow(() -> bestLikeBoardService.modify(bestLikeBoardUpdateDto));
    }

    @DisplayName("베스트 좋아요 게시판 Y -> N 업데이트 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 베스트_좋아요_게시판_Y_N_업데이트_테스트(int cnt) {
        when(bestLikeBoardDao.countForChangeUsed()).thenReturn(cnt);
        when(bestLikeBoardDao.updateUsed("admin1234")).thenReturn(cnt);
        assertDoesNotThrow(() -> bestLikeBoardService.modifyUsed("admin1234"));
    }

    @DisplayName("베스트 좋아요 게시판 삭제 테스트")
    @Test
    void 베스트_좋아요_게시판_삭제_테스트() {
        when(bestLikeBoardDao.delete(1)).thenReturn(1);
        assertDoesNotThrow(() -> bestLikeBoardService.remove(1));
    }

    @DisplayName("베스트 좋아요 게시판 모두 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 베스트_좋아요_게시판_모두_삭제_테스트(int cnt) {
        when(bestLikeBoardDao.count()).thenReturn(cnt);
        when(bestLikeBoardDao.deleteAll()).thenReturn(cnt);
        assertDoesNotThrow(() -> bestLikeBoardService.removeAll());
    }

    private List<BoardFormDto> createTopByLike(int cnt) {
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

    private List<BestLikeBoardUpdateDto> createBestLikeBoardUpdateDtos(int cnt) {
        ArrayList<BestLikeBoardUpdateDto> list = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            list.add(createBestLikeBoardUpdateDto(i));
        }

        return list;
    }

    private BestLikeBoardUpdateDto createBestLikeBoardUpdateDto(int i) {
        var bestLikeBoardUpdateDto = new BestLikeBoardUpdateDto();

        bestLikeBoardUpdateDto.setSeq(i);
        bestLikeBoardUpdateDto.setUsed("N");

        return bestLikeBoardUpdateDto;
    }

    private BestLikeBoardDto createBestCommentBoardDto(int i) {
        var bestLikeBoardDto = new BestLikeBoardDto();

        bestLikeBoardDto.setBno(i);
        bestLikeBoardDto.setAppl_begin("2025-01-05 00:00:00");
        bestLikeBoardDto.setAppl_end("2025-02-05 00:00:00");
        bestLikeBoardDto.setReg_id("admin1234");
        bestLikeBoardDto.setReg_date("2025-01-05 00:00:00");
        bestLikeBoardDto.setUp_id("admin1234");
        bestLikeBoardDto.setUp_date("2025-01-05 00:00:00");
        bestLikeBoardDto.setUsed("Y");

        return bestLikeBoardDto;
    }

    private List<BestLikeBoardDto> createBestLikeBoardDtos(int cnt) {
        List<BestLikeBoardDto> list = new ArrayList<>();

        for (int i=0; i<cnt; i++) {
            list.add(createBestCommentBoardDto(i));
        }

        return list;
    }
}