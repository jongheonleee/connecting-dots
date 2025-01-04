package com.example.demo.repository.mybatis.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.PageHandler;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.dto.comment.CommentRequestDto;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import com.example.demo.repository.mybatis.category.CategoryDaoImpl;
import com.example.demo.dto.board.BoardDetailDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardImgFormDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class BoardDaoIntegrationTest {

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardImgDaoImpl boardImgDao;

    @Autowired
    private CategoryDaoImpl categoryDao;

    @Autowired
    private CommentDaoImpl commentDao;
    @Autowired
    private BoardDaoImpl boardDaoImpl;

    @BeforeEach
    void setUp() {
        assertNotNull(boardDao);
        assertNotNull(boardImgDao);
        assertNotNull(categoryDao);
        assertNotNull(commentDao);

        commentDao.deleteAll();
        boardImgDao.deleteAll();
        categoryDao.deleteAll();
        boardDao.deleteAll();
    }

    // 카테고리 기반 게시글 조회 기능
    @DisplayName("카테고리 기반 게시글 조회 기능 ")
    @Test
    public void test1() {
        // 게시글 등록
            // 카테고리 등록
            // 게시글 등록
            // 이미지 등록

        var categoryDto = createCategoryDto("0101");
        var boardFormDto = createBoardFormDto(1, categoryDto.getCate_code());
        assertTrue(1 == boardDao.insert(boardFormDto));

        var boardImgFormDto = createBoardImgFormDto(boardFormDto.getBno());
        assertTrue(1 == boardImgDao.insert(boardImgFormDto));

        // 게시글 카테고리로 조회
        List<BoardFormDto> foundBoardDtos = boardDao.selectAllByCategory(categoryDto.getCate_code());
        for (BoardFormDto foundBoardDto : foundBoardDtos) {
            System.out.println("foundBoardDto = " + foundBoardDto);
        }

    }

    // 게시글 상세 조회 테스트
    @DisplayName("게시글 상세 조회 기능 ")
    @Test
    public void test2() {
        // 게시글 등록
            // 카테고리 등록
            // 게시글 등록
            // 이미지 등록
        var categoryDto = createCategoryDto("0101");
        assertTrue(1 == categoryDao.insert(categoryDto));

        var boardFormDto = createBoardFormDto(1, categoryDto.getCate_code());
        assertTrue(1 == boardDao.insert(boardFormDto));

        var boardImgFormDto = createBoardImgFormDto(boardFormDto.getBno());
        assertTrue(1 == boardImgDao.insert(boardImgFormDto));

        // 게시글 상세 조회
        BoardDetailDto foundBoardDetial = boardDao.selectDetailByBno(boardFormDto.getBno());
        System.out.println("foundBoardDetial = " + foundBoardDetial);
    }

    /**
     * v2 조회 테스트
     *
     * - v2-0. 게시글 여러건 단순 조회
     * - v2-1. 게시글 여러건 카테고리 기반 조회
     * - v2-2. 게시글 여러건 검색조건 기반 조회
     *
     */

    @DisplayName("v2-0. 게시글 여러건 단순 조회 ")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test3(int cnt) {
        // given
        // - 0. 카테고리 등록
        CategoryDto categoryDto = createCategoryDto("0101");
        assertTrue(1 == categoryDao.insert(categoryDto));

        // - 1. 게시글 이미지와 동시에 cnt개 등록, 이때 섬네일 표시해주기
        // - 2. 각 댓글 5개 등록
        for (int i=0; i<cnt; i++) {
            BoardFormDto boardFormDto = createBoardFormDto(i, categoryDto.getCate_code());
            assertTrue(1 == boardDao.insert(boardFormDto));

            // 이미지 등록 처리
            for (int j=0; j<5; j++) {
                if (j == 0) {
                    BoardImgFormDto boardImgFormDto = createBoardImgThumbnailForm(boardFormDto.getBno());
                    assertTrue(1 == boardImgDao.insert(boardImgFormDto));
                } else {
                    BoardImgFormDto boardImgFormDto = createBoardImgFormDto(boardFormDto.getBno());
                    assertTrue(1 == boardImgDao.insert(boardImgFormDto));
                }
            }

            // 댓글 등록 처리
            for (int j=0; j<5; j++) {
                CommentRequestDto commentRequestDto = createCommentFormDto(boardFormDto.getBno());
                assertTrue(1 == commentDao.insert(commentRequestDto));
            }
        }


        // when
        // - 필요한 매개변수 저장 - offset, pageSize
        Map<String, Object> map = new HashMap<>();
        map.put("offset", 1);
        map.put("pageSize", 10);

        // - dao의 selectV2() 호출
        List<BoardResponseDto> boardResponseDtos = boardDao.selectV2(map);

        // then
        // - 결과 비교 : bno, 작성자, 카테고리 코드, 작성일, 조회수, 추천수, 섬네일, 댓글수
        for (BoardResponseDto boardResponseDto : boardResponseDtos) {
            System.out.println("boardResponseDto = " + boardResponseDto);
        }
    }

    @DisplayName("v2-1. 게시글 여러건 카테고리 기반 조회 ")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test4(int cnt) {
        // given
        // - 0. 카테고리 등록
        CategoryDto categoryDto = createCategoryDto("0101");
        assertTrue(1 == categoryDao.insert(categoryDto));

        // - 1. 게시글 이미지와 동시에 cnt개 등록, 이때 섬네일 표시해주기
        // - 2. 각 댓글 5개 등록
        for (int i=0; i<cnt; i++) {
            BoardFormDto boardFormDto = createBoardFormDto(i, categoryDto.getCate_code());
            assertTrue(1 == boardDao.insert(boardFormDto));

            // 이미지 등록 처리
            for (int j=0; j<5; j++) {
                if (j == 0) {
                    BoardImgFormDto boardImgFormDto = createBoardImgThumbnailForm(boardFormDto.getBno());
                    assertTrue(1 == boardImgDao.insert(boardImgFormDto));
                } else {
                    BoardImgFormDto boardImgFormDto = createBoardImgFormDto(boardFormDto.getBno());
                    assertTrue(1 == boardImgDao.insert(boardImgFormDto));
                }
            }

            // 댓글 등록 처리
            for (int j=0; j<5; j++) {
                CommentRequestDto commentRequestDto = createCommentFormDto(boardFormDto.getBno());
                assertTrue(1 == commentDao.insert(commentRequestDto));
            }
        }

        // when
        // - 필요한 매개변수 저장 - cate_code, offset, pageSize
        Map<String, Object> map = new HashMap<>();
        map.put("cate_code", categoryDto.getCate_code());
        map.put("offset", 1);
        map.put("pageSize", 10);

        // then
        // - 결과 비교 : bno, 작성자, 카테고리 코드, 작성일, 조회수, 추천수, 섬네일, 댓글수
        // - dao의 selectV2() 호출
        List<BoardResponseDto> boardResponseDtos = boardDao.selectV2ByCategory(map);

        // then
        // - 결과 비교 : bno, 작성자, 카테고리 코드, 작성일, 조회수, 추천수, 섬네일, 댓글수
        for (BoardResponseDto boardResponseDto : boardResponseDtos) {
            System.out.println("boardResponseDto = " + boardResponseDto);
        }
    }

    @DisplayName("v2-2. 게시글 여러건 검색조건 기반 조회 ")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test5(int cnt) {
        // given
        // - 0. 카테고리 등록
        CategoryDto categoryDto = createCategoryDto("0101");
        assertTrue(1 == categoryDao.insert(categoryDto));

        // - 1. 게시글 이미지와 동시에 cnt개 등록, 이때 섬네일 표시해주기
        // - 2. 각 댓글 5개 등록
        for (int i=0; i<cnt; i++) {
            BoardFormDto boardFormDto = createBoardFormDto(i, categoryDto.getCate_code());
            assertTrue(1 == boardDao.insert(boardFormDto));

            // 이미지 등록 처리
            for (int j=0; j<5; j++) {
                if (j == 0) {
                    BoardImgFormDto boardImgFormDto = createBoardImgThumbnailForm(boardFormDto.getBno());
                    assertTrue(1 == boardImgDao.insert(boardImgFormDto));
                } else {
                    BoardImgFormDto boardImgFormDto = createBoardImgFormDto(boardFormDto.getBno());
                    assertTrue(1 == boardImgDao.insert(boardImgFormDto));
                }
            }

            // 댓글 등록 처리
            for (int j=0; j<5; j++) {
                CommentRequestDto commentRequestDto = createCommentFormDto(boardFormDto.getBno());
                assertTrue(1 == commentDao.insert(commentRequestDto));
            }
        }

        // when
        // - 필요한 매개변수 저장 - searchCondition, offset, pageSize
        SearchCondition sc = new SearchCondition(1, 10, "T", "title", "1");

        // then
        // - 결과 비교 : bno, 작성자, 카테고리 코드, 작성일, 조회수, 추천수, 섬네일, 댓글수
        List<BoardResponseDto> boardResponseDtos = boardDaoImpl.selectV2BySearchCondition(sc);
        for (BoardResponseDto boardResponseDto : boardResponseDtos) {
            System.out.println("boardResponseDto = " + boardResponseDto);
        }
    }

    @DisplayName("베스트 댓글수 게시글 추출 쿼리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30})
    void 베스트_댓글수_게시글_추출_테스트(int cnt) {
        // cnt 개수만큼 게시글 등록
        for (int i=0; i<cnt; i++) {
            BoardFormDto boardFormDto = createBoardFormDto(i, "0101");
            assertTrue(1 == boardDao.insert(boardFormDto));

            // 각 게시글에 자신의 수 * 3 만큼 댓글 등록
            for (int j=0; j<i*3; j++) {
                CommentRequestDto commentRequestDto = createCommentFormDto(boardFormDto.getBno());
                assertTrue(1 == commentDao.insert(commentRequestDto));
            }
        }

        // 베스트 댓글수 5개 추출
        List<BoardFormDto> bestCommentBoardDtos = boardDao.selectTopByComment(5);
        assertEquals(5, bestCommentBoardDtos.size());

        // 결과 확인
        bestCommentBoardDtos.stream().forEach(b -> log.info("bestCommentBoardDto: {}", b));
    }


    @DisplayName("베스트 조회수 게시글 추출 쿼리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30})
    void 베스트_조회수_게시글_추출_테스트(int cnt) {
        // cnt 개수만큼 게시글 등록
        // 현재 게시글 등록될 때 조회수는 10의 배수로 증가
        for (int i=0; i<cnt; i++) {
            BoardFormDto boardFormDto = createBoardFormDto(i, "0101");
            assertEquals(1, boardDao.insert(boardFormDto));

            for (int j=0; j<i; j++) {
                assertEquals(1, boardDao.increaseViewCnt(boardFormDto.getBno()));
            }
        }

        // 베스트 조회수 5개 추출
        List<BoardFormDto> bestViewBoardDtos = boardDao.selectTopByView(5);
        assertEquals(5, bestViewBoardDtos.size());

        // 결과 확인
        bestViewBoardDtos.stream().forEach(b -> log.info("bestViewBoardDto: {}", b));
    }


    @DisplayName("베스트 추천수 게시글 추출 쿼리 테스트")
    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30})
    void 베스트_추천수_게시글_추출_테스트(int cnt) {
        // cnt 개수만큼 게시글 등록
        // 현재 게시글 등록될 때 추천수는 10의 배수로 증가
        for (int i=0; i<cnt; i++) {
            BoardFormDto boardFormDto = createBoardFormDto(i, "0101");
            assertTrue(1 == boardDao.insert(boardFormDto));

            for (int j=0; j<i; j++) {
                assertEquals(1, boardDao.increaseRecoCnt(boardFormDto.getBno()));
            }
        }

        // 베스트 추천수 5개 추출
        List<BoardFormDto> bestRecoBoardDtos = boardDao.selectTopByReco(5);
        assertEquals(5, bestRecoBoardDtos.size());

        // 결과 확인
        bestRecoBoardDtos.stream().forEach(b -> log.info("bestRecoBoardDto: {}", b));
    }

    private BoardFormDto createBoardFormDto(int i, String cate_code) {
        var dto = new BoardFormDto();

        dto.setCate_code(cate_code);
        dto.setId("id" + i);
        dto.setTitle("title" + i);
        dto.setWriter("writer" + i);
        dto.setView_cnt(i*10);
        dto.setReco_cnt(i*10);
        dto.setNot_reco_cnt(i*10);
        dto.setContent("content" + i);
        dto.setComt("comt" + i);
        dto.setReg_date("2021-01-01");
        dto.setReg_id("reg_id" + i);
        dto.setUp_date("2021-01-01");
        dto.setUp_id("up_id" + i);

        return dto;
    }

    private BoardImgFormDto createBoardImgFormDto(int bno) {
        var dto = new BoardImgFormDto();

        dto.setBno(bno);
        dto.setName("img_name");
        dto.setImg("img_path");
        dto.setComt("img_name");
        dto.setReg_date("2021-01-01");
        dto.setReg_id("reg_id");
        dto.setUp_date("2021-01-01");
        dto.setUp_id("up_id");
        dto.setThumb("N");

        return dto;
    }

    private BoardImgFormDto createBoardImgThumbnailForm(int bno) {
        var dto = new BoardImgFormDto();

        dto.setBno(bno);
        dto.setName("img_name");
        dto.setImg("img_path");
        dto.setComt("img_name");
        dto.setReg_date("2021-01-01");
        dto.setReg_id("reg_id");
        dto.setUp_date("2021-01-01");
        dto.setUp_id("up_id");
        dto.setThumb("Y");

        return dto;
    }

    private CategoryDto createCategoryDto(String cate_code) {
        var dto = new CategoryDto();

        dto.setCate_code(cate_code);
        dto.setTop_cate("top_cate");
        dto.setName("name");
        dto.setComt("comt");
        dto.setReg_date("reg_date");
        dto.setReg_id("reg_id");
        dto.setUp_date("up_date");
        dto.setUp_id("up_id");

        return dto;
    }

    private CommentRequestDto createCommentFormDto(int bno) {
        var dto = new CommentRequestDto();

        dto.setBno(bno);
        dto.setComment("dwadawdaw");
        dto.setWriter("reg_id");

        return dto;
    }


}