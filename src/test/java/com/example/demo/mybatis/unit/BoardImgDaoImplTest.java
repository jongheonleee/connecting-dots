package com.example.demo.mybatis.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardImgFormDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
class BoardImgDaoImplTest {

    private List<BoardImgFormDto> fixture = new ArrayList<>();
    private List<BoardImgFormDto> updatedFixture = new ArrayList<>();

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardImgDaoImpl target;

    /**
     *
     * 0. 카운팅
     * - 0. 예외 - x
     * - 1. 실패 - x
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 전체 카운팅
     *
     * 1. 등록
     * - 0. 예외
     *  - 0-0. 데이터 제약 조건 위배
     *  - 0-1. 필수 데이터 누락
     *  - 0-2. 데이터 자체가 없는 경우
     *
     * - 1. 실패 - x
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하기
     *
     * 2. 게시글 번호로 조회
     * - 0. 예외 - x
     *
     * - 1. 실패
     *  - 1-0. 없는 게시글 번호로 조회
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 게시글 번호로 조회
     *
     * 2. 이미지 번호로 조회
     * - 0. 예외 - x
     *
     * - 1. 실패
     *  - 1-0. 없는 이미지 번호로 조회
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 이미지 번호로 조회
     *
     * 2. 전체 조회
     * - 0. 예외 - x
     *
     * - 1. 실패 - x
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 전체 조회
     *
     * 3. 수정
     * - 0. 예외
     *  - 0-0. 데이터 제약 조건 위배
     *  - 0-1. 필수 데이터 누락
     *  - 0-2. 데이터 자체가 없는 경우
     *
     * - 1. 실패
     *  - 1-0. 없는 이미지 번호로 수정
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 각각 수정하고 내용 비교
     *  - 2-1. 여러건 등록하고 랜덤으로 한개 수정해서 내용 비교
     *
     * 4. 게시글 번호로 삭제
     * - 0. 예외 - x
     * - 1. 실패
     *  - 1-0. 없는 게시글 번호로 삭제
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 게시글 번호로 삭제
     *  - 2-1. 여러건 등록하고 랜덤으로 한개 삭제해서 내용 비교
     *
     * 4. 이미지 번호로 삭제
     * - 0. 예외 - x
     *
     * - 1. 실패
     *  - 1-0. 없는 이미지 번호로 삭제
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 이미지 번호로 삭제
     *
     * 4. 전체 삭제
     * - 0. 예외 - x
     *
     * - 1. 실패 - x
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 전체 삭제
     *
     */


    @BeforeEach
    public void setUp() {
        fixture.clear();
        updatedFixture.clear();
        assertNotNull(boardDao);
        assertNotNull(target);
        target.deleteAll();
        boardDao.deleteAll();
    }

    @DisplayName("0-2-0. 여러건 등록하고 전체 카운팅")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test0(int cnt) {
        var boardDto = createBoardFormDto(1);
        createFixture(boardDto.getBno(), cnt);
        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }


        assertTrue(cnt == target.count());
    }

    @DisplayName("1-0-0. 데이터 제약 조건 위배")
    @Test
    public void test1() {
        var boardDto = createBoardFormDto(1);
        var boardImgDto = createBoardImgFormDto(boardDto.getBno());
        // varchar(200) 넘어가는 길이
        boardImgDto.setImg("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"\n");
        assertThrows(DataIntegrityViolationException.class, () -> target.insert(boardImgDto));
    }

    @DisplayName("1-0-1. 필수 데이터 누락")
    @Test
    public void test2() {
        var boardDto = createBoardFormDto(1);
        var boardImgDto = createBoardImgFormDto(boardDto.getBno());
        boardImgDto.setImg(null);
        assertThrows(DataIntegrityViolationException.class, () -> target.insert(boardImgDto));
    }

    @DisplayName("1-0-2. 데이터 자체가 없는 경우")
    @Test
    public void test3() {
        assertThrows(DataIntegrityViolationException.class, () -> target.insert(null));
    }

    @DisplayName("2-0-0. 여러건 등록하기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test4(int cnt) {
        var boardFormDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardFormDto.getBno(), cnt);
        }

        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }
    }

    @DisplayName("2-2-0. 여러건 등록하고 각각 조회하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test5(int cnt) {
        var boardFormDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardFormDto.getBno(), cnt);
        }

        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }

        var foundBoardImgDtos = target.selectAllByBno(boardFormDto.getBno());
        sort(fixture, foundBoardImgDtos);

        for (int i=0; i<cnt; i++) {
            assertTrue(isSameBoardImgFormDto(fixture.get(i), foundBoardImgDtos.get(i)));
        }

    }

    @DisplayName("2-2-1. 여러건 등록하고 랜덤으로 한개 조회해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test6(int cnt) {
        var boardFormDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardFormDto.getBno(), cnt);
        }

        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }

        var foundBoardImgDtos = target.selectAllByBno(boardFormDto.getBno());
        sort(fixture, foundBoardImgDtos);

        int randomIdx = chooseRandom(cnt);
        assertTrue(isSameBoardImgFormDto(fixture.get(randomIdx), foundBoardImgDtos.get(randomIdx)));
    }

    @DisplayName("2-2-1. 여러건 등록하고 전체 조회하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test7(int cnt) {
        var boardFormDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardFormDto.getBno(), cnt);
        }

        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }

        var foundBoardImgDtos = target.selectAllByBno(boardFormDto.getBno());
        sort(fixture, foundBoardImgDtos);

        for (int i=0; i<cnt; i++) {
            assertTrue(isSameBoardImgFormDto(fixture.get(i), foundBoardImgDtos.get(i)));
        }
    }

    @DisplayName("3-0-0. 데이터 제약 조건 위배")
    @Test
    public void test8() {
        var boardDto = createBoardFormDto(1);
        var boardImgDto = createBoardImgFormDto(boardDto.getBno());
        assertTrue(1 == target.insert(boardImgDto));

        var foundBoardImgDto = target.selectAllByBno(boardDto.getBno()).get(0);
        foundBoardImgDto.setImg("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"\n");
        assertThrows(DataIntegrityViolationException.class, () -> target.update(foundBoardImgDto));
    }

    @DisplayName("3-0-1. 필수 데이터 누락")
    @Test
    public void test9() {
        var boardDto = createBoardFormDto(1);
        var boardImgDto = createBoardImgFormDto(boardDto.getBno());
        assertTrue(1 == target.insert(boardImgDto));

        var foundBoardImgDto = target.selectAllByBno(boardDto.getBno()).get(0);
        foundBoardImgDto.setImg(null);
        assertThrows(DataIntegrityViolationException.class, () -> target.update(foundBoardImgDto));

    }

    @DisplayName("3-0-2. 데이터 자체가 없는 경우")
    @Test
    public void test10() {
        var boardDto = createBoardFormDto(1);
        var boardImgDto = createBoardImgFormDto(boardDto.getBno());
        assertTrue(1 == target.insert(boardImgDto));
        assertTrue(0 == target.update(null));


    }

    @DisplayName("3-1-0. 없는 이미지 번호로 수정")
    @Test
    public void test11() {
        var boardDto = createBoardFormDto(1);
        var boardImgDto = createBoardImgFormDto(boardDto.getBno());
        assertTrue(1 == target.insert(boardImgDto));

        var foundBoardImgDto = target.selectAllByBno(boardDto.getBno()).get(0);
        foundBoardImgDto.setIno(-10);
        assertTrue(0 == target.update(foundBoardImgDto));

    }

    @DisplayName("3-2-0. 여러건 등록하고 각각 수정하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test12(int cnt) {
        var boardDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardDto.getBno(), cnt);
        }
        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }
        var foundBoardImgDtos = target.selectAllByBno(boardDto.getBno());

        for (var foundBoardImgDto : foundBoardImgDtos) {
            var updatedBoardImgDto = createBoardImgUpdatedFormDto(foundBoardImgDto.getBno(), foundBoardImgDto.getIno());
            assertTrue(1 == target.update(updatedBoardImgDto));
            updatedFixture.add(updatedBoardImgDto);
        }

        foundBoardImgDtos = target.selectAllByBno(boardDto.getBno());
        sort(updatedFixture, foundBoardImgDtos);

        for (int i=0; i<cnt; i++) {
            assertTrue(isSameBoardImgFormDto(updatedFixture.get(i), foundBoardImgDtos.get(i)));
        }

    }

    @DisplayName("3-2-1. 여러건 등록하고 랜덤으로 한개 수정해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test13(int cnt) {
        var boardDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardDto.getBno(), cnt);
        }
        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }

        var foundBoardImgDtos = target.selectAllByBno(boardDto.getBno());

        for (var foundBoardImgDto : foundBoardImgDtos) {
            var updatedBoardImgDto = createBoardImgUpdatedFormDto(foundBoardImgDto.getBno(), foundBoardImgDto.getIno());
            assertTrue(1 == target.update(updatedBoardImgDto));
            updatedFixture.add(updatedBoardImgDto);
        }

        foundBoardImgDtos = target.selectAllByBno(boardDto.getBno());
        sort(updatedFixture, foundBoardImgDtos);
        int randomIdx = chooseRandom(cnt);

        assertTrue(isSameBoardImgFormDto(updatedFixture.get(randomIdx), foundBoardImgDtos.get(randomIdx)));
    }

    @DisplayName("4-1-0. 없는 게시글 번호로 삭제")
    @Test
    public void test14() {
        assertTrue(0 == target.deleteByBno(-10));
    }

    @DisplayName("4-2-0. 여러건 등록하고 게시글 번호로 삭제")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test15(int cnt) {
        var boardDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardDto.getBno(), cnt);
        }

        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }

        assertTrue(cnt == target.deleteByBno(boardDto.getBno()));
    }

    @DisplayName("4-2-1. 여러건 등록하고 랜덤으로 한개 삭제해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test16(int cnt) {
        var boardDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardDto.getBno(), cnt);
        }
        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }
        List<BoardImgFormDto> foundBoardImgDtos = target.selectAllByBno(boardDto.getBno());
        int randomIdx = chooseRandom(cnt);
        assertTrue(1 == target.deleteByIno(foundBoardImgDtos.get(randomIdx).getIno()));

    }

    @DisplayName("4-1-0. 없는 이미지 번호로 삭제")
    @Test
    public void test17() {
        assertTrue(0 == target.deleteByIno(-10));
    }

    @DisplayName("4-2-0. 여러건 등록하고 이미지 번호로 삭제")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test18(int cnt) {
        var boardDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardDto.getBno(), cnt);
        }
        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }
        List<BoardImgFormDto> foundBoardImgDtos = target.selectAllByBno(boardDto.getBno());
        for (var boardImgDto : foundBoardImgDtos) {
            assertTrue(1 == target.deleteByIno(boardImgDto.getIno()));
        }
    }

    @DisplayName("4-2-0. 여러건 등록하고 전체 삭제")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test19(int cnt) {
        var boardDto = createBoardFormDto(1);
        for (int i=0; i<cnt; i++) {
            createFixture(boardDto.getBno(), cnt);
        }

        for (var boardImgDto : fixture) {
            assertTrue(1 == target.insert(boardImgDto));
        }

        assertTrue(cnt == target.deleteAll());
    }






    private BoardFormDto createBoardFormDto(int i) {
        var dto = new BoardFormDto();

        dto.setCate_code("0101");
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

        assertTrue(1 == boardDao.insert(dto));

        return boardDao.selectAll().get(0);
    }

    private void createFixture(int bno, int cnt) {
        fixture.clear();
        for (int i=0; i<cnt; i++) {
            var boardImgFormDto = createBoardImgFormDto(bno);
            fixture.add(boardImgFormDto);
        }
    }


    private BoardImgFormDto createBoardImgFormDto(int bno) {
        var dto = new BoardImgFormDto();

        dto.setBno(bno);
        dto.setIno(1);
        dto.setImg("img_path");
        dto.setComt("img_name");
        dto.setReg_date("2021-01-01");
        dto.setReg_id("reg_id");
        dto.setUp_date("2021-01-01");
        dto.setUp_id("up_id");

        return dto;
    }

    private BoardImgFormDto createBoardImgUpdatedFormDto(int bno, int ino) {
        var dto = new BoardImgFormDto();

        dto.setBno(bno);
        dto.setIno(ino);
        dto.setImg("new_img_path");
        dto.setComt("new_img_name");
        dto.setUp_id("new_up_id");

        return dto;
    }

    private int chooseRandom(int cnt) {
        return (int) (Math.random() * cnt);
    }

    private void sort(List<BoardImgFormDto> list1, List<BoardImgFormDto> list2) {
        list1.sort((o1, o2) -> o1.getImg().compareTo(o2.getImg()));
        list2.sort((o1, o2) -> o1.getImg().compareTo(o2.getImg()));
    }

    private boolean isSameBoardImgFormDto(BoardImgFormDto dto1, BoardImgFormDto dto2) {
        System.out.println("dto1 = " + dto1);
        System.out.println("dto2 = " + dto2);

        return dto1.getBno().equals(dto2.getBno()) &&
                dto1.getComt().equals(dto2.getComt()) &&
                dto1.getImg().equals(dto2.getImg());
    }



}