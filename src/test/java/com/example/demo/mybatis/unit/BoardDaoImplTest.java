package com.example.demo.mybatis.unit;

import static org.junit.jupiter.api.Assertions.*;


import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardUpdatedFormDto;
import com.example.demo.dto.category.CategoryDto;
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
class BoardDaoImplTest {

    @Autowired
    private BoardDaoImpl target;

    @Autowired
    private com.example.demo.repository.mybatis.board.BoardImgDaoImpl BoardImgDaoImpl;

    private List<BoardFormDto> fixture = new ArrayList<>();
    private List<BoardFormDto> updatedFixture = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        fixture.clear();
        updatedFixture.clear();
        assertNotNull(target);
        BoardImgDaoImpl.deleteAll();
        target.deleteAll();
    }

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
     * - 2. 성공
     *  - 2-0. 여러건 등록하기
     *
     * 2. 조회
     * - 0. 예외 - x
     * - 1. 실패 - x
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 각각 조회하고 내용 비교
     *  - 2-1. 여러건 등록하고 랜덤으로 한게 조회해서 내용 비교
     *
     * 2. 전체 조회
     * - 0. 예외 - x
     * - 1. 실패 - x
     * - 2. 성공
     *  - 2-1. 여러건 등록하고 전체 조회하고 내용 비교
     *
     * 3. 수정
     * - 0. 예외
     *  - 0-0. 데이터 제약 조건 위배
     *  - 0-1. 필수 데이터 누락
     *  - 0-2. 데이터 자체가 없는 경우
     *
     * - 1. 실패
     *  - 1-0. 존재하지 않는 게시글 수정
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 각각 수정하고 내용 비교
     *  - 2-1. 여러건 등록하고 랜덤으로 한개 수정해서 내용 비교
     *
     *
     * 3. 조회수 증가
     * - 0. 예외 - x
     * - 1. 실패
     *  - 1-0. 존재하지 않는 게시글 조회수 증가
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 각각 조회수 증가하고 내용 비교
     *  - 2-1. 여러건 등록하고 랜덤으로 한개 조회수 증가해서 내용 비교
     *
     * 3. 추천수 증가
     * - 0. 예외 - x
     * - 1. 실패
     *  - 1-0. 존재하지 않는 게시글 추천수 증가
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 각각 추천수 증가하고 내용 비교
     *  - 2-1. 여러건 등록하고 랜덤으로 한개 추천수 증가해서 내용 비교
     *
     * 3. 비추천수 증가
     * - 0. 예외 - x
     * - 1. 실패
     *  - 1-0. 존재하지 않는 게시글 비추천수 증가
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 각각 비추천수 증가하고 내용 비교
     *  - 2-1. 여러건 등록하고 랜덤으로 한개 비추천수 증가해서 내용 비교
     *
     * 4. 삭제
     * - 0. 예외 - x
     * - 1. 실패
     *  - 1-0. 존재하지 않는 게시글 삭제
     *
     * - 2. 성공
     *  - 2-0. 여러건 등록하고 각각 삭제하고 내용 비교
     *  - 2-1. 여러건 등록하고 랜덤으로 한개 삭제해서 내용 비교
     *
     * 4. 전체 삭제
     * - 0. 예외 - x
     * - 1. 실패 - x
     * - 2. 성공
     * - 2-0. 여러건 등록하고 전체 삭제하고 카운팅
     *
     */

    @DisplayName("0-2-0. 여러건 등록하고 전체 카운팅")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test0(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
            System.out.println("boardFormDto = " + boardFormDto);
        }

        assertEquals(cnt, target.count());
    }

    @DisplayName("1-0-0. 데이터 제약 조건 위배")
    @Test
    public void test1() {
        var wrongDto = createBoardFormDto(1);
        // varchar(2000) 넘어감
        wrongDto.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus iaculis enim nec velit ullamcorper, eget ornare urna venenatis. Integer vehicula nisl ut erat dictum, nec aliquam odio cursus. Fusce nec erat vel sem mollis consequat. Suspendisse potenti. Morbi feugiat nunc non risus sodales, nec iaculis urna malesuada. Maecenas condimentum et ipsum et vehicula. Integer eget luctus nulla. Aenean sollicitudin ultricies sem ut ornare. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Pellentesque venenatis, magna at cursus aliquam, nunc tortor tincidunt justo, eget tempor enim libero in arcu. Aenean fringilla sem vitae lorem auctor pharetra. In hac habitasse platea dictumst. Integer vitae nulla id odio eleifend mollis ut et libero. Nam scelerisque, velit id malesuada laoreet, eros sem tincidunt mauris, non condimentum nisl sapien sed magna. Fusce finibus tortor sed augue elementum, vel pharetra magna mollis.\n"
                + "\n"
                + "Quisque elementum tellus nulla, vitae consequat metus ultrices non. Etiam at justo sit amet ex tincidunt ultrices. Fusce facilisis tortor et interdum vehicula. Sed eget fermentum mauris. Integer consectetur ex quis sapien aliquam ornare. Fusce tincidunt nulla nec risus venenatis, sed suscipit ligula pharetra. Nam in risus vestibulum, malesuada felis at, convallis dolor. Integer ac nisi turpis. Cras vulputate ex quis ante malesuada, quis auctor dolor ullamcorper. Integer egestas id nisl sit amet viverra. Pellentesque egestas pulvinar augue, et tincidunt est dapibus ac.\n"
                + "\n"
                + "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Morbi pharetra arcu in purus aliquet, vel suscipit felis suscipit. Duis convallis venenatis tortor, non laoreet ligula fringilla non. Cras laoreet, mauris non faucibus tincidunt, urna orci pharetra arcu, vel blandit quam arcu vel libero. Proin fermentum justo ex, sit amet ultricies odio ultricies a. Curabitur ac magna in dolor fermentum euismod ac et orci. Vestibulum tempus gravida risus in sollicitudin. Phasellus blandit mi non semper scelerisque. Integer id magna sed purus dictum faucibus non nec metus. Nam venenatis pharetra est, ac egestas orci varius at. Curabitur congue, ipsum sit amet luctus fringilla, dolor ex gravida nisl, vel efficitur ipsum erat eget felis.\n"
                + "\n"
                + "Integer id nulla convallis, suscipit orci non, volutpat dui. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Integer eu facilisis libero. Aenean posuere sagittis convallis. Aliquam erat volutpat. Aliquam tempor, ex sit amet efficitur consequat, nunc mi vestibulum nisl, non facilisis arcu ex eget tortor. Nullam at finibus lectus. Phasellus eget dui tempor, vehicula justo et, pulvinar risus. Mauris volutpat tincidunt velit. Sed vestibulum odio eget lacus bibendum, in rhoncus dolor venenatis. Cras nec orci elit. Duis nec viverra magna. Praesent accumsan lectus quis orci tincidunt facilisis. Nullam in ex nibh. Ut consectetur, velit et lacinia tristique, elit dui suscipit ligula, id iaculis elit arcu vel libero. Pellentesque varius tellus non turpis bibendum varius.\n"
                + "\n"
                + "In hac habitasse platea dictumst. Suspendisse potenti. Aliquam pulvinar, ex in aliquam luctus, arcu elit pellentesque nisi, sit amet pulvinar velit erat et ex. Donec id varius odio. Fusce auctor scelerisque lorem, et eleifend sapien vulputate a. Mauris dapibus, turpis in suscipit tristique, nulla ex convallis sapien, et ultricies est lacus ac augue. Curabitur egestas augue ut dolor tincidunt, vel accumsan urna suscipit. Phasellus dictum augue ligula, et bibendum ligula interdum et. Morbi ac nunc nec lectus luctus elementum ut non ligula. Phasellus dapibus venenatis lacus, id cursus erat consequat eu. Nulla facilisi.\n");

        assertThrows(DataIntegrityViolationException.class, () -> target.insert(wrongDto));
    }

    @DisplayName("1-0-1. 필수 데이터 누락")
    @Test
    public void test2() {
        var wrongDto1 = createBoardFormDto(1);
        wrongDto1.setCate_code(null);
        assertThrows(DataIntegrityViolationException.class, () -> target.insert(wrongDto1));

        var wrongDto2 = createBoardFormDto(1);
        wrongDto2.setId(null);
        assertThrows(DataIntegrityViolationException.class, () -> target.insert(wrongDto2));

        var wrongDto3 = createBoardFormDto(1);
        wrongDto3.setTitle(null);
        assertThrows(DataIntegrityViolationException.class, () -> target.insert(wrongDto3));

        var wrongDto4 = createBoardFormDto(1);
        wrongDto4.setWriter(null);
        assertThrows(DataIntegrityViolationException.class, () -> target.insert(wrongDto4));
    }

    @DisplayName("1-0-2. 데이터 자체가 없는 경우")
    @Test
    public void test3() {
        assertThrows(DataIntegrityViolationException.class, () -> target.insert(null));
    }

    @DisplayName("1-2-0. 여러건 등록하기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test4(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }

        List<BoardFormDto> actual = target.selectAll();
        assertTrue(isSameDummy(fixture, actual));
    }


    @DisplayName("2-2-1. 여러건 등록하고 랜덤으로 한개 조회해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test6(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }

        List<BoardFormDto> actual = target.selectAll();
        sortDummy(actual, fixture);

        int randomIdx = chooseRandom(cnt);
        var expected = fixture.get(randomIdx);
        var actualOne = actual.get(randomIdx);
        assertTrue(isSameBoardFormDto(expected, actualOne));
    }

    @DisplayName("2-2-1. 여러건 등록하고 전체 조회하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test7(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }

        List<BoardFormDto> actual = target.selectAll();
        assertTrue(isSameDummy(fixture, actual));
    }

    @DisplayName("3-0-0. 데이터 제약 조건 위배")
    @Test
    public void test8() {
        var dto = createBoardFormDto(1);
        assertTrue(1 == target.insert(dto));
        var foundBoard = target.selectAll().get(0);
        int foundBoardBno = foundBoard.getBno();
        var boardUpdatedFormDto = createBoardUpdatedFormDto(foundBoardBno);
        // varchar(2000) 넘어감
        boardUpdatedFormDto.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus iaculis enim nec velit ullamcorper, eget ornare urna venenatis. Integer vehicula nisl ut erat dictum, nec aliquam odio cursus. Fusce nec erat vel sem mollis consequat. Suspendisse potenti. Morbi feugiat nunc non risus sodales, nec iaculis urna malesuada. Maecenas condimentum et ipsum et vehicula. Integer eget luctus nulla. Aenean sollicitudin ultricies sem ut ornare. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Pellentesque venenatis, magna at cursus aliquam, nunc tortor tincidunt justo, eget tempor enim libero in arcu. Aenean fringilla sem vitae lorem auctor pharetra. In hac habitasse platea dictumst. Integer vitae nulla id odio eleifend mollis ut et libero. Nam scelerisque, velit id malesuada laoreet, eros sem tincidunt mauris, non condimentum nisl sapien sed magna. Fusce finibus tortor sed augue elementum, vel pharetra magna mollis.\n"
                + "\n"
                + "Quisque elementum tellus nulla, vitae consequat metus ultrices non. Etiam at justo sit amet ex tincidunt ultrices. Fusce facilisis tortor et interdum vehicula. Sed eget fermentum mauris. Integer consectetur ex quis sapien aliquam ornare. Fusce tincidunt nulla nec risus venenatis, sed suscipit ligula pharetra. Nam in risus vestibulum, malesuada felis at, convallis dolor. Integer ac nisi turpis. Cras vulputate ex quis ante malesuada, quis auctor dolor ullamcorper. Integer egestas id nisl sit amet viverra. Pellentesque egestas pulvinar augue, et tincidunt est dapibus ac.\n"
                + "\n"
                + "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Morbi pharetra arcu in purus aliquet, vel suscipit felis suscipit. Duis convallis venenatis tortor, non laoreet ligula fringilla non. Cras laoreet, mauris non faucibus tincidunt, urna orci pharetra arcu, vel blandit quam arcu vel libero. Proin fermentum justo ex, sit amet ultricies odio ultricies a. Curabitur ac magna in dolor fermentum euismod ac et orci. Vestibulum tempus gravida risus in sollicitudin. Phasellus blandit mi non semper scelerisque. Integer id magna sed purus dictum faucibus non nec metus. Nam venenatis pharetra est, ac egestas orci varius at. Curabitur congue, ipsum sit amet luctus fringilla, dolor ex gravida nisl, vel efficitur ipsum erat eget felis.\n"
                + "\n"
                + "Integer id nulla convallis, suscipit orci non, volutpat dui. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Integer eu facilisis libero. Aenean posuere sagittis convallis. Aliquam erat volutpat. Aliquam tempor, ex sit amet efficitur consequat, nunc mi vestibulum nisl, non facilisis arcu ex eget tortor. Nullam at finibus lectus. Phasellus eget dui tempor, vehicula justo et, pulvinar risus. Mauris volutpat tincidunt velit. Sed vestibulum odio eget lacus bibendum, in rhoncus dolor venenatis. Cras nec orci elit. Duis nec viverra magna. Praesent accumsan lectus quis orci tincidunt facilisis. Nullam in ex nibh. Ut consectetur, velit et lacinia tristique, elit dui suscipit ligula, id iaculis elit arcu vel libero. Pellentesque varius tellus non turpis bibendum varius.\n"
                + "\n"
                + "In hac habitasse platea dictumst. Suspendisse potenti. Aliquam pulvinar, ex in aliquam luctus, arcu elit pellentesque nisi, sit amet pulvinar velit erat et ex. Donec id varius odio. Fusce auctor scelerisque lorem, et eleifend sapien vulputate a. Mauris dapibus, turpis in suscipit tristique, nulla ex convallis sapien, et ultricies est lacus ac augue. Curabitur egestas augue ut dolor tincidunt, vel accumsan urna suscipit. Phasellus dictum augue ligula, et bibendum ligula interdum et. Morbi ac nunc nec lectus luctus elementum ut non ligula. Phasellus dapibus venenatis lacus, id cursus erat consequat eu. Nulla facilisi.\n");
        assertThrows(DataIntegrityViolationException.class, () -> target.update(boardUpdatedFormDto));
    }

    @DisplayName("3-0-1. 필수 데이터 누락")
    @Test
    public void test9() {
        var dto = createBoardFormDto(1);
        assertTrue(1 == target.insert(dto));
        var foundBoard = target.selectAll().get(0);
        int foundBoardBno = foundBoard.getBno();
        var wrongDto = createBoardUpdatedFormDto(foundBoardBno);
        // varchar(2000) 넘어감

        var wrongDto1 = createBoardUpdatedFormDto(foundBoardBno);
        wrongDto1.setContent(null);
        assertThrows(DataIntegrityViolationException.class, () -> target.update(wrongDto1));

        var wrongDto2 = createBoardUpdatedFormDto(foundBoardBno);
        wrongDto2.setTitle(null);
        assertThrows(DataIntegrityViolationException.class, () -> target.update(wrongDto2));
    }

    @DisplayName("3-0-2. 데이터 자체가 없는 경우")
    @Test
    public void test10() {
        assertTrue(0 == target.update(null));
   }

    @DisplayName("3-1-0. 존재하지 않는 게시글 수정")
    @Test
    public void test11() {
        var wrongDto = createBoardUpdatedFormDto(0);
        assertTrue(0 == target.update(wrongDto));
    }

    @DisplayName("3-2-0. 여러건 등록하고 각각 수정하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test12(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }

        List<BoardFormDto> actual = target.selectAll();

        for (var boardFormDto : actual) {
            var boardUpdatedFormDto = createBoardUpdatedFormDto(boardFormDto.getBno());
            updatedFixture.add(boardUpdatedFormDto);
            assertTrue(1 == target.update(boardUpdatedFormDto));
        }
    }

    @DisplayName("3-2-1. 여러건 등록하고 랜덤으로 한개 수정해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test13(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }

        List<BoardFormDto> actual = target.selectAll();
        int randomIdx = chooseRandom(cnt);
        var boardFormDto = actual.get(randomIdx);

        var boardUpdatedFormDto = createBoardUpdatedFormDto(boardFormDto.getBno());
        assertTrue(1 == target.update(boardUpdatedFormDto));

    }

    @DisplayName("3-1-0. 존재하지 않는 게시글 조회수 증가")
    @Test
    public void test14() {
        assertTrue(0 == target.increaseRecoCnt(0));
    }

    @DisplayName("3-2-0. 여러건 등록하고 각각 조회수 증가하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test15(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }
        List<BoardFormDto> actual = target.selectAll();

        for (var boardFormDto : actual) {
            assertTrue(1 == target.increaseViewCnt(boardFormDto.getBno()));
        }

        List<BoardFormDto> actual2 = target.selectAll();
        for (var boardFormDto : actual2) {
            assertTrue(boardFormDto.getView_cnt() == 1);
        }
    }

    @DisplayName("3-2-1. 여러건 등록하고 랜덤으로 한개 조회수 증가해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test16(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }
        List<BoardFormDto> actual = target.selectAll();
        int randomIdx = chooseRandom(cnt);
        var boardFormDto = actual.get(randomIdx);
        assertTrue(1 == target.increaseViewCnt(boardFormDto.getBno()));

        actual = target.selectAll();
        for (int i=0; i<cnt; i++) {
            if (i == randomIdx) {
                assertTrue(actual.get(i).getView_cnt() == 1);
            } else {
                assertTrue(actual.get(i).getView_cnt() == 0);
            }
        }
    }

    @DisplayName("3-1-0. 존재하지 않는 게시글 추천수 증가")
    @Test
    public void test17() {
        assertTrue(0 == target.increaseRecoCnt(0));
    }

    @DisplayName("3-2-0. 여러건 등록하고 각각 추천수 증가하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test18(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }
        List<BoardFormDto> actual = target.selectAll();

        for (var boardFormDto : actual) {
            assertTrue(1 == target.increaseRecoCnt(boardFormDto.getBno()));
        }

        actual = target.selectAll();
        for (var boardFormDto : actual) {
            assertTrue(boardFormDto.getReco_cnt() == 1);
        }

    }

    @DisplayName("3-2-1. 여러건 등록하고 랜덤으로 한개 추천수 증가해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test19(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }
        List<BoardFormDto> actual = target.selectAll();
        int randomIdx = chooseRandom(cnt);
        var boardFormDto = actual.get(randomIdx);
        assertTrue(1 == target.increaseRecoCnt(boardFormDto.getBno()));

        actual = target.selectAll();
        for (int i=0; i<cnt; i++) {
            if (i == randomIdx) {
                assertTrue(actual.get(i).getReco_cnt() == 1);
            } else {
                assertTrue(actual.get(i).getReco_cnt() == 0);
            }
        }
    }

    @DisplayName("3-1-0. 존재하지 않는 게시글 비추천수 증가")
    @Test
    public void test20() {
        assertTrue(0 == target.increaseNotRecoCnt(0));
    }

    @DisplayName("3-2-0. 여러건 등록하고 각각 비추천수 증가하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test21(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }
        List<BoardFormDto> actual = target.selectAll();

        for (var boardFormDto : actual) {
            assertTrue(1 == target.increaseNotRecoCnt(boardFormDto.getBno()));
        }

        actual = target.selectAll();
        for (var boardFormDto : actual) {
            assertTrue(boardFormDto.getNot_reco_cnt() == 1);
        }
    }

    @DisplayName("3-2-1. 여러건 등록하고 랜덤으로 한개 비추천수 증가해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test22(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }
        List<BoardFormDto> actual = target.selectAll();
        int randomIdx = chooseRandom(cnt);
        var boardFormDto = actual.get(randomIdx);
        assertTrue(1 == target.increaseNotRecoCnt(boardFormDto.getBno()));

        actual = target.selectAll();
        for (int i=0; i<cnt; i++) {
            if (i == randomIdx) {
                assertTrue(actual.get(i).getNot_reco_cnt() == 1);
            } else {
                assertTrue(actual.get(i).getNot_reco_cnt() == 0);
            }
        }
    }

    @DisplayName("4-1-0. 존재하지 않는 게시글 삭제")
    @Test
    public void test23() {
        assertTrue(0 == target.delete(0));
    }

    @DisplayName("4-2-0. 여러건 등록하고 각각 삭제하고 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test24(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }

        List<BoardFormDto> actual = target.selectAll();
        for (var boardFormDto : actual) {
            assertTrue(1 == target.delete(boardFormDto.getBno()));
        }

        assertTrue(0 == target.count());
    }

    @DisplayName("4-2-1. 여러건 등록하고 랜덤으로 한개 삭제해서 내용 비교")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test25(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }

        List<BoardFormDto> actual = target.selectAll();
        int randomIdx = chooseRandom(cnt);

        var boardFormDto = actual.get(randomIdx);
        assertTrue(1 == target.delete(boardFormDto.getBno()));
        assertNull(target.select(boardFormDto.getBno()));
    }

    @DisplayName("4-2-0. 여러건 등록하고 전체 삭제하고 카운팅")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test26(int cnt) {
        createFixture(cnt);
        for (var boardFormDto : fixture) {
            assertTrue(1 == target.insert(boardFormDto));
        }

        assertTrue(cnt == target.deleteAll());
        assertTrue(0 == target.count());
    }


    private void createFixture(int cnt) {
        fixture.clear();
        for (int i = 0; i < cnt; i++) {
            var dto = createBoardFormDto(i);
            fixture.add(dto);
        }
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

        return dto;
    }

    private BoardFormDto createBoardUpdatedFormDto(int i) {
        var dto = new BoardFormDto();

        dto.setBno(i);
        dto.setTitle("title" + i);
        dto.setContent("content" + i);
        dto.setComt("comt" + i);
        dto.setUp_date("2021-01-01");
        dto.setUp_id("up_id" + i);

        return dto;
    }

    private int chooseRandom(int cnt) {
        return (int) (Math.random() * cnt);
    }

    private boolean isSameBoardFormDto(BoardFormDto dto1, BoardFormDto dto2) {
        return dto1.getId().equals(dto2.getId()) &&
                dto1.getTitle().equals(dto2.getTitle()) &&
                dto1.getWriter().equals(dto2.getWriter()) &&
                dto1.getView_cnt().equals(dto2.getView_cnt()) &&
                dto1.getReco_cnt().equals(dto2.getReco_cnt()) &&
                dto1.getNot_reco_cnt().equals(dto2.getNot_reco_cnt()) &&
                dto1.getContent().equals(dto2.getContent()) &&
                dto1.getComt().equals(dto2.getComt()) &&
                dto1.getReg_id().equals(dto2.getReg_id()) &&
                dto1.getUp_id().equals(dto2.getUp_id());
    }

    private boolean isSameDummy(List<BoardFormDto> list1, List<BoardFormDto> list2) {
        sortDummy(list1, list2);

        int size = list1.size();
        if (size != list2.size()) {
            return false;
        }

        for (int i=0; i<size; i++) {
            if (!isSameBoardFormDto(list1.get(i), list2.get(i))) {
                return false;
            }
        }

        return true;

    }

    private void sortDummy(List<BoardFormDto> list1, List<BoardFormDto> list2) {
        list1.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
        list2.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
    }

    private CategoryDto createCategoryDto(String cate_code) {
        var dto = new CategoryDto();
        dto.setCate_code(cate_code);
        dto.setName("cate_name");
        dto.setReg_date("2021-01-01");
        dto.setReg_id("reg_id");
        dto.setUp_date("2021-01-01");
        dto.setUp_id("up_id");
        return dto;
    }

}