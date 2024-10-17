package com.example.demo.dao.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dao.BoardDaoImpl;
import com.example.demo.dao.BoardImgDaoImpl;
import com.example.demo.dao.CategoryDaoImpl;
import com.example.demo.dto.BoardDetailDto;
import com.example.demo.dto.BoardFormDto;
import com.example.demo.dto.BoardImgFormDto;
import com.example.demo.dto.CategoryDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardDaoIntegrationTest {

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardImgDaoImpl boardImgDao;

    @Autowired
    private CategoryDaoImpl categoryDao;

    @BeforeEach
    void setUp() {
        assertNotNull(boardDao);
        assertNotNull(boardImgDao);
        assertNotNull(categoryDao);
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
        assertTrue(1 == categoryDao.insert(categoryDto));

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
        BoardDetailDto foundBoardDetial = boardDao.selectByBno(boardFormDto.getBno());
        System.out.println("foundBoardDetial = " + foundBoardDetial);
    }

    private BoardFormDto createBoardFormDto(int i, String cate_code) {
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


}