package com.example.demo.repository.mybatis.board;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.board.BestCommentBoardDto;
import com.example.demo.dto.board.BestCommentBoardUpdateDto;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardImgFormDto;
import com.example.demo.dto.comment.CommentRequestDto;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class BestCommentBoardDaoImplTest {

    @Autowired
    private BoardImgDaoImpl boardImgDao;

    @Autowired
    private CommentDaoImpl commentDao;

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BestCommentBoardDaoImpl target;

    @BeforeEach
    void setUp() {
        assertNotNull(target);
        assertNotNull(boardDao);
        assertNotNull(boardImgDao);
        assertNotNull(commentDao);

        target.deleteAll();
        boardImgDao.deleteAll();
        commentDao.deleteAll();
        boardDao.deleteAll();
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            var boardDto = createBoardDto(i, "0101");
            assertEquals(1, boardDao.insert(boardDto));

            var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
            assertEquals(1, target.insert(bestCommentBoardDto));
        }

        int totalCnt = target.count();
        assertEquals(cnt, totalCnt);
    }

    @DisplayName("Y 카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void Y_카운팅_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            var boardDto = createBoardDto(i, "0101");
            assertEquals(1, boardDao.insert(boardDto));

            var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
            if (i == 0) {
                bestCommentBoardDto.setUsed("N");
            }
            assertEquals(1, target.insert(bestCommentBoardDto));
        }

        int totalCnt = target.countUsed();
        assertEquals(cnt-1, totalCnt);
    }

    @DisplayName("모두 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 모두_조회_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            var boardDto = createBoardDto(i, "0101");
            assertEquals(1, boardDao.insert(boardDto));

            var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
            assertEquals(1, target.insert(bestCommentBoardDto));
        }

        var list = target.selectAll();
        assertEquals(cnt, list.size());
        list.stream().forEach(dto -> log.info("dto: {}", dto));
    }

    @DisplayName("단건 조회 테스트")
    @Test
    void 단건_조회_테스트() {
        var boardDto = createBoardDto(1, "0101");
        assertEquals(1, boardDao.insert(boardDto));

        var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
        assertEquals(1, target.insert(bestCommentBoardDto));

        Integer seq = bestCommentBoardDto.getSeq();
        var foundDto = target.select(seq);
        assertNotNull(foundDto);
        log.info("foundDto: {}", foundDto);
    }

    @DisplayName("페이지 뷰 전용 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 페이지_뷰_전용_조회_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            var boardDto = createBoardDto(i, "0101");
            assertEquals(1, boardDao.insert(boardDto));

            var boardImgDto = createBoardImgFormDto(boardDto.getBno());
            assertEquals(1, boardImgDao.insert(boardImgDto));

            var commentDto = createCommentRequestDto(boardDto.getBno());
            assertEquals(1, commentDao.insert(commentDto));

            var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
            assertEquals(1, target.insert(bestCommentBoardDto));
        }

        int totalCnt = target.count();
        assertEquals(cnt, totalCnt);

        var map = new HashMap<String, Object>();
        map.put("offset", 0);
        map.put("pageSize", 10);

        var list = target.selectForView(map);
        assertEquals((cnt > 10 ? 10 : cnt), list.size());

        list.stream().forEach(dto -> log.info("dto: {}", dto));
    }

    @DisplayName("필드 수정 처리 테스트")
    @Test
    void 필드_수정_처리_테스트() {
        var boardDto = createBoardDto(1, "0101");
        assertEquals(1, boardDao.insert(boardDto));

        var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
        assertEquals(1, target.insert(bestCommentBoardDto));

        var bestCommentBoardUpdateDto = createBestCommentBoardUpdateDto(bestCommentBoardDto.getSeq());
        assertEquals(1, target.update(bestCommentBoardUpdateDto));

        var foundDto = target.select(bestCommentBoardDto.getSeq());
        assertNotNull(foundDto);
        log.info("foundDto: {}", foundDto);
    }

    @DisplayName("사용 여부 수정 처리 테스트")
    @Test
    void 사용_여부_수정_처리_테스트() {
        var boardDto = createBoardDto(1, "0101");
        assertEquals(1, boardDao.insert(boardDto));

        var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
        bestCommentBoardDto.setAppl_begin("2024-08-01");
        bestCommentBoardDto.setAppl_end("2024-09-01");
        assertEquals(1, target.insert(bestCommentBoardDto));

        var bestCommentBoardUpdateDto = createBestCommentBoardUpdateDto(bestCommentBoardDto.getSeq());
        assertEquals(1, target.updateUsed("admin1234"));

        var foundDto = target.select(bestCommentBoardDto.getSeq());
        assertNotNull(foundDto);
        assertEquals("N", foundDto.getUsed());

    }

    @DisplayName("삭제 처리 테스트")
    @Test
    void 삭제_처리_테스트() {
        var boardDto = createBoardDto(1, "0101");
        assertEquals(1, boardDao.insert(boardDto));

        var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
        assertEquals(1, target.insert(bestCommentBoardDto));

        Integer seq = bestCommentBoardDto.getSeq();
        assertEquals(1, target.delete(seq));
        assertNull(target.select(seq));

    }

    @DisplayName("모두 삭제 처리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 모두_삭제_처리_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            var boardDto = createBoardDto(i, "0101");
            assertEquals(1, boardDao.insert(boardDto));

            var bestCommentBoardDto = createBestCommentBoardDto(boardDto.getBno());
            assertEquals(1, target.insert(bestCommentBoardDto));
        }

        assertEquals(cnt, target.deleteAll());
        assertEquals(0, target.count());

    }

    private BestCommentBoardDto createBestCommentBoardDto(Integer bno) {
        var dto = new BestCommentBoardDto();
        dto.setBno(bno);
        dto.setAppl_begin("2025-01-01");
        dto.setAppl_end("2025-02-01");
        dto.setComt("베스트 댓글 수 게시글");
        dto.setReg_date("2025-01-01");
        dto.setReg_id("admin1234");
        dto.setUp_date("2025-01-01");
        dto.setUp_id("up_id");
        dto.setUsed("Y");
        return dto;
    }

    private BestCommentBoardUpdateDto createBestCommentBoardUpdateDto(Integer seq) {
        var dto = new BestCommentBoardUpdateDto();

        dto.setSeq(seq);
        dto.setAppl_begin("2025-01-01");
        dto.setAppl_end("2025-02-01");
        dto.setComt("업데이트 처리된 베스트 댓글 수 게시글");
        dto.setReg_date("2025-01-01");
        dto.setReg_id("admin1234");
        dto.setUp_date("2025-01-04");
        dto.setUp_id("홍길동");
        dto.setUsed("N");

        return dto;
    }

    private BoardFormDto createBoardDto(Integer i, String cate_code) {
        var dto = new BoardFormDto();

        dto.setCate_code(cate_code);
        dto.setId("id" + i);
        dto.setTitle("title" + i);
        dto.setWriter("writer" + i);
        dto.setView_cnt(0);
        dto.setReco_cnt(0);
        dto.setNot_reco_cnt(0);
        dto.setContent("content" + i);
        dto.setComt("comt" + i);
        dto.setReg_date("2021-01-01");
        dto.setReg_id("reg_id" + i);
        dto.setUp_date("2021-01-01");
        dto.setUp_id("up_id" + i);

        return dto;
    }

    private BoardImgFormDto createBoardImgFormDto(Integer bno) {
        var dto = new BoardImgFormDto();

        dto.setBno(bno);
        dto.setName("게시글 썸네일");
        dto.setImg("img");
        dto.setComt("...");
        dto.setReg_date("2025-01-01");
        dto.setReg_id("admin1234");
        dto.setUp_date("2025-01-01");
        dto.setUp_id("admin1234");
        dto.setThumb("Y");

        return dto;
    }

    private CommentRequestDto createCommentRequestDto(Integer bno) {
        var dto = new CommentRequestDto();

        dto.setBno(bno);
        dto.setComment("좋은 글입니다. 굿굿!!");
        dto.setWriter("홍길동");

        return dto;
    }

}