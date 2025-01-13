//package com.example.demo.application.board;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//
//import com.example.demo.dto.board.BestCommentBoardDto;
//import com.example.demo.dto.board.BestCommentBoardUpdateDto;
//import com.example.demo.dto.board.BoardFormDto;
//import com.example.demo.dto.board.BoardResponseDto;
//import com.example.demo.repository.mybatis.board.BestCommentBoardDaoImpl;
//import com.example.demo.repository.mybatis.board.BoardDaoImpl;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@Slf4j
//@ExtendWith(MockitoExtension.class)
//class BestCommentBoardServiceImplTest {
//
//    @InjectMocks
//    private BestCommentBoardServiceImpl bestCommentBoardService;
//
//    @Mock
//    private BestCommentBoardDaoImpl bestCommentBoardDao;
//
//    @Mock
//    private BoardDaoImpl boardDao;
//
//
//    @BeforeEach
//    void setUp() {
//        assertNotNull(bestCommentBoardService);
//        assertNotNull(bestCommentBoardDao);
//        assertNotNull(boardDao);
//    }
//
//    @DisplayName("카운팅 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {5, 10, 15, 20, 30})
//    void 카운팅_테스트(int cnt) {
//        when(bestCommentBoardDao.count()).thenReturn(cnt);
//        assertEquals(cnt, bestCommentBoardService.count());
//    }
//
//    @DisplayName("사용된 카운팅 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {5, 10, 15, 20, 30})
//    void 사용된_카운팅_테스트(int cnt) {
//        when(bestCommentBoardDao.countUsed()).thenReturn(cnt);
//        assertEquals(cnt, bestCommentBoardService.countUsed());
//    }
//
//    @DisplayName("베스트 댓글 게시판 저장 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {5, 10, 15, 20, 30})
//    void 베스트_댓글_게시판_저장_테스트(int cnt) {
//        List<BoardFormDto> topByComment = createTopByComment(cnt);
//        when(boardDao.selectTopByComment(cnt)).thenReturn(topByComment);
//        when(bestCommentBoardDao.insert(any(BestCommentBoardDto.class))).thenReturn(1);
//        assertDoesNotThrow(() -> bestCommentBoardService.saveForBestCommentBoards(cnt));
//    }
//
//    @DisplayName("베스트 댓글 게시판 단건 저장 테스트")
//    @Test
//    void 베스트_댓글_게시판_단건_저장_테스트() {
//        BoardFormDto boardFormDto = createBoardFormDto(1);
//        BestCommentBoardDto bestCommentBoardDto = new BestCommentBoardDto();
//        when(bestCommentBoardDao.insert(any(BestCommentBoardDto.class))).thenReturn(1);
//        assertDoesNotThrow(() -> bestCommentBoardService.save(bestCommentBoardDto));
//    }
//
//    @DisplayName("베스트 댓글 게시판 단건 조회 테스트")
//    @Test
//    void 베스트_댓글_게시판_단건_조회_테스트() {
//        BestCommentBoardDto bestCommentBoardDto = new BestCommentBoardDto();
//        when(bestCommentBoardDao.select(1)).thenReturn(bestCommentBoardDto);
//        assertEquals(bestCommentBoardDto, bestCommentBoardService.read(1));
//    }
//
//    @DisplayName("베스트 댓글 게시판 모두 조회 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {5, 10, 15, 20, 30})
//    void 베스트_댓글_게시판_모두_조회_테스트(int cnt) {
//        List<BestCommentBoardDto> bestCommentBoardDtos = createBestCommentBoardDtos(cnt);
//        when(bestCommentBoardDao.selectAll()).thenReturn(bestCommentBoardDtos);
//        assertEquals(bestCommentBoardDtos, bestCommentBoardService.readAll());
//    }
//
//    @DisplayName("베스트 댓글 게시판 뷰 전용 조회 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {10})
//    void 베스트_댓글_게시판_뷰_전용_조회_테스트(int cnt) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("offset", 0);
//        map.put("pageSize", 10);
//
//        List<BoardResponseDto> boardResponses = createBoardResponses(cnt);
//        when(bestCommentBoardDao.selectForView(map)).thenReturn(boardResponses);
//        assertEquals(boardResponses, bestCommentBoardService.readForView(map));
//    }
//
//    @DisplayName("베스트 댓글 게시판 수정 테스트")
//    @Test
//    void 베스트_댓글_게시판_수정_테스트() {
//        BestCommentBoardUpdateDto bestCommentBoardUpdateDto = createBestCommentBoardUpdateDto(1);
//        when(bestCommentBoardDao.update(bestCommentBoardUpdateDto)).thenReturn(1);
//        assertDoesNotThrow(() -> bestCommentBoardService.modify(bestCommentBoardUpdateDto));
//    }
//
//    @DisplayName("베스트 댓글 게시판 Y -> N 업데이트 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 10, 15, 20})
//    void 베스트_댓글_게시판_Y_N_업데이트_테스트(int cnt) {
//        when(bestCommentBoardDao.countForChangeUsed()).thenReturn(cnt);
//        when(bestCommentBoardDao.updateUsed("admin1234")).thenReturn(cnt);
//        assertDoesNotThrow(() -> bestCommentBoardService.modifyUsed("admin1234"));
//    }
//
//    @DisplayName("베스트 댓글 게시판 삭제 테스트")
//    @Test
//    void 베스트_댓글_게시판_삭제_테스트() {
//        when(bestCommentBoardDao.delete(1)).thenReturn(1);
//        assertDoesNotThrow(() -> bestCommentBoardService.remove(1));
//    }
//
//    @DisplayName("베스트 댓글 게시판 모두 삭제 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 10, 15, 20})
//    void 베스트_댓글_게시판_모두_삭제_테스트(int cnt) {
//        when(bestCommentBoardDao.count()).thenReturn(cnt);
//        when(bestCommentBoardDao.deleteAll()).thenReturn(cnt);
//        assertDoesNotThrow(() -> bestCommentBoardService.removeAll());
//    }
//
//    private List<BoardFormDto> createTopByComment(int cnt) {
//        ArrayList<BoardFormDto> list = new ArrayList<>();
//
//        for (int i=0; i<cnt; i++) {
//            list.add(createBoardFormDto(i));
//        }
//
//        return list;
//    }
//
//    private BoardFormDto createBoardFormDto(int i) {
//        var boardFormDto = new BoardFormDto();
//
//        boardFormDto.setBno(i);
//        boardFormDto.setTitle("제목" + i);
//        boardFormDto.setContent("내용" + i);
//        boardFormDto.setWriter("홍만동" + i);
//        boardFormDto.setReg_date("2025-01-05 00:00:00");
//        boardFormDto.setUp_date("2025-01-05 00:00:00");
//        boardFormDto.setComt("...");
//
//        return boardFormDto;
//    }
//
//    private List<BoardResponseDto> createBoardResponses(int cnt) {
//        ArrayList<BoardResponseDto> list = new ArrayList<>();
//
//        for (int i=0; i<cnt; i++) {
//            list.add(createBoardResponseDto(i));
//        }
//
//        return list;
//    }
//
//    private BoardResponseDto createBoardResponseDto(int i) {
//        var boardResponseDto = new BoardResponseDto();
//
//        boardResponseDto.setBno(i);
//        boardResponseDto.setTitle("제목" + i);
//        boardResponseDto.setWriter("홍만동" + i);
//        boardResponseDto.setReg_date("2025-01-05 00:00:00");
//
//        return boardResponseDto;
//    }
//
//    private List<BestCommentBoardUpdateDto> createBestCommentBoardUpdateDtos(int cnt) {
//        ArrayList<BestCommentBoardUpdateDto> list = new ArrayList<>();
//
//        for (int i=0; i<cnt; i++) {
//            list.add(createBestCommentBoardUpdateDto(i));
//        }
//
//        return list;
//    }
//
//    private BestCommentBoardUpdateDto createBestCommentBoardUpdateDto(int i) {
//        var bestCommentBoardUpdateDto = new BestCommentBoardUpdateDto();
//
//        bestCommentBoardUpdateDto.setSeq(i);
//        bestCommentBoardUpdateDto.setUsed("N");
//
//        return bestCommentBoardUpdateDto;
//    }
//
//    private BestCommentBoardDto createBestCommentBoardDto(int i) {
//        var bestCommentBoardDto = new BestCommentBoardDto();
//
//        bestCommentBoardDto.setBno(i);
//        bestCommentBoardDto.setAppl_begin("2025-01-05 00:00:00");
//        bestCommentBoardDto.setAppl_end("2025-02-05 00:00:00");
//        bestCommentBoardDto.setReg_id("admin1234");
//        bestCommentBoardDto.setReg_date("2025-01-05 00:00:00");
//        bestCommentBoardDto.setUp_id("admin1234");
//        bestCommentBoardDto.setUp_date("2025-01-05 00:00:00");
//        bestCommentBoardDto.setUsed("Y");
//
//        return bestCommentBoardDto;
//    }
//
//    private List<BestCommentBoardDto> createBestCommentBoardDtos(int cnt) {
//        List<BestCommentBoardDto> list = new ArrayList<>();
//
//        for (int i=0; i<cnt; i++) {
//            list.add(createBestCommentBoardDto(i));
//        }
//
//        return list;
//    }
//
//}