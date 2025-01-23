package com.example.demo.repository.mybatis.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.BoardCategory;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardImgDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardImgDaoImplTest {


    @Autowired
    private BoardImgDaoImpl boardImgDao;

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BoardCategoryDaoImpl boardCategoryDao;

    private BoardDto boardDto;
    private BoardCategoryDto boardCategoryDto;

    private final Integer REG_USER_SEQ = 1;
    private final Integer UP_USER_SEQ = 1;
    private final String REG_DATE = "2025-01-16";
    private final String UP_DATE = "2025-01-16";
    private final String CATE_CODE = "BC101001";
    private final Integer TEST_USER_SEQ = 1;

    @BeforeEach
    void setUp() {
        assertNotNull(boardImgDao);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryDao);

        boardImgDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, boardImgDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());

        // 임시 카테고리 생성 및 등록
        createCategoryDto();
        assertEquals(1, boardCategoryDao.insert(boardCategoryDto));

        // 임시 게시글 생성
        createBoardDto();
        assertEquals(1, boardDao.insert(boardDto));
    }

    @AfterEach
    void clean() {
        boardImgDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, boardImgDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());
    }

    @Nested
    @DisplayName("카운팅 및 존재 여부 확인 처리 테스트")
    class CountAndExistsTest {

        @DisplayName("이미지 카운팅 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 이미지_카운팅_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
            }

            assertEquals(cnt, boardImgDao.count());
        }

        @DisplayName("존재하는 ino에 해당하는 이미지 존재 여부 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 존재하는_ino_이미지_존재_여부_테스트(int cnt) {
            List<BoardImgDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
                dummy.add(boardImgDto);
            }

            for (BoardImgDto boardImgDto : dummy) {
                assertTrue(boardImgDao.existsByIno(boardImgDto.getIno()));
            }

        }

        @Test
        @DisplayName("존재하지 않는 ino에 해당하는 이미지 존재 여부 테스트")
        void 존재하지_않는_ino_이미지_존재_여부_테스트() {
            Integer ino = 99999;
            assertFalse(boardImgDao.existsByIno(ino));
        }

        @DisplayName("존재하는 ino에 해당하는 이미지 존재 여부 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 존재하는_ino_이미지_존재_여부_테스트_2(int cnt) {
            List<BoardImgDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
                dummy.add(boardImgDto);
            }

            for (BoardImgDto boardImgDto : dummy) {
                assertTrue(boardImgDao.existsByInoForUpdate(boardImgDto.getIno()));
            }

        }

        @Test
        @DisplayName("존재하지 않는 ino에 해당하는 이미지 존재 여부 테스트")
        void 존재하지_않는_ino_이미지_존재_여부_테스트_2() {
            Integer ino = 99999;
            assertFalse(boardImgDao.existsByInoForUpdate(ino));
        }
    }

    @Nested
    @DisplayName("이미지 조회 처리 테스트")
    class SelectTest {

        @DisplayName("존재하는 ino로 이미지 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 이미지_조회_테스트(int cnt) {
            List<BoardImgDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
                dummy.add(boardImgDto);
            }

            assertEquals(cnt, boardImgDao.count());

            for (BoardImgDto boardImgDto : dummy) {
                BoardImgDto selected = boardImgDao.selectByIno(boardImgDto.getIno());
                assertEquals(boardImgDto.getName(), selected.getName());
                assertEquals(boardImgDto.getComt(), selected.getComt());
            }

        }

        @DisplayName("존재하지 않는 ino로 이미지 조회 테스트")
        @Test
        void 존재하지_않는_ino로_이미지_조회_테스트() {
            Integer ino = 99999;
            assertNull(boardImgDao.selectByIno(ino));
        }

        @DisplayName("게시글 번호에 해당하는 이미지 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_번호에_해당하는_이미지_조회_테스트(int cnt) {
            List<BoardImgDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
                dummy.add(boardImgDto);
            }

            assertEquals(cnt, boardImgDao.count());

            List<BoardImgDto> selected = boardImgDao.selectByBno(boardDto.getBno());
            assertEquals(cnt, selected.size());
        }

        @DisplayName("존재하지 않는 게시글 번호로 이미지 조회 테스트")
        @Test
        void 존재하지_않는_게시글_번호로_이미지_조회_테스트() {
            Integer bno = 99999;
            List<BoardImgDto> selected = boardImgDao.selectByBno(bno);
            assertEquals(0, selected.size());
        }
    }

    @Nested
    @DisplayName("이미지 등록 처리 테스트")
    class InsertTest {

        @DisplayName("이미지 등록 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 이미지_등록_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
            }

            assertEquals(cnt, boardImgDao.count());
        }

        @DisplayName("여러개 이미지 한번에 등록 처리 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 여러개_이미지_한번에_등록_처리_테스트(int cnt) {
            List<BoardImgDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                dummy.add(boardImgDto);
            }

            assertEquals(cnt, boardImgDao.insertAll(dummy));
            assertEquals(cnt, boardImgDao.count());
        }
    }

    @Nested
    @DisplayName("이미지 수정 처리 테스트")
    class UpdateTest {

        @DisplayName("이미지 수정 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 이미지_수정_테스트(int cnt) {
            List<BoardImgDto> dummy = new ArrayList<>();

            for (int i = 0; i < cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
                dummy.add(boardImgDto);
            }

            assertEquals(cnt, boardImgDao.count());

            for (BoardImgDto boardImgDto : dummy) {
                boardImgDto.setName("수정된 이미지 이름");
                boardImgDto.setComt("수정된 이미지 설명");
                assertEquals(1, boardImgDao.update(boardImgDto));

                BoardImgDto updated = boardImgDao.selectByIno(boardImgDto.getIno());
                assertEquals(boardImgDto.getName(), updated.getName());
                assertEquals(boardImgDto.getComt(), updated.getComt());

            }

            assertEquals(cnt, boardImgDao.count());
        }
    }

    @Nested
    @DisplayName("이미지 삭제 처리 테스트")
    class DeleteTest {

        @DisplayName("이미지 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 이미지_삭제_테스트(int cnt) {
            List<BoardImgDto> dummy = new ArrayList<>();

            for (int i = 0; i < cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
                dummy.add(boardImgDto);
            }

            assertEquals(cnt, boardImgDao.count());

            for (BoardImgDto boardImgDto : dummy) {
                assertEquals(1, boardImgDao.deleteByIno(boardImgDto.getIno()));
            }

            assertEquals(0, boardImgDao.count());
        }

        @DisplayName("게시글 번호에 해당하는 이미지 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 게시글_번호에_해당하는_이미지_삭제_테스트(int cnt) {
            List<BoardImgDto> dummy = new ArrayList<>();

            for (int i = 0; i < cnt; i++) {
                BoardImgDto boardImgDto = craeteBoardImgDto(i);
                assertEquals(1, boardImgDao.insert(boardImgDto));
                dummy.add(boardImgDto);
            }

            assertEquals(cnt, boardImgDao.count());

            assertEquals(cnt, boardImgDao.deleteByBno(boardDto.getBno()));
            assertEquals(0, boardImgDao.count());
        }
    }


    private BoardImgDto craeteBoardImgDto(int i) {
        return BoardImgDto.builder()
                        .bno(boardDto.getBno())
                        .name("테스트용 이미지" + i)
                        .chk_thumb("Y")
                        .img("test" + i + ".jpg")
                        .comt("테스트용 이미지입니다.")
                        .reg_date(REG_DATE)
                        .reg_user_seq(REG_USER_SEQ)
                        .up_date(UP_DATE)
                        .up_user_seq(UP_USER_SEQ)
                        .build();
    }

    private void createCategoryDto() {
        boardCategoryDto = BoardCategoryDto.builder()
                                            .cate_code("BC101001")
                                            .top_cate("BC101000")
                                            .name("테스트용 카테고리")
                                            .comt("테스트용입니다.")
                                            .ord(1)
                                            .chk_use("Y")
                                            .level(1)
                                            .reg_user_seq(REG_USER_SEQ)
                                            .reg_date(REG_DATE)
                                            .up_user_seq(UP_USER_SEQ)
                                            .up_date(UP_DATE)
                                            .build();
    }

    private void createBoardDto() {
        boardDto = BoardDto.builder()
                            .cate_code(CATE_CODE)
                            .user_seq(TEST_USER_SEQ)
                            .writer("홍길동")
                            .title("테스트용 제목")
                            .cont("테스트용 내용")
                            .reg_user_seq(REG_USER_SEQ)
                            .reg_date(REG_DATE)
                            .up_user_seq(UP_USER_SEQ)
                            .up_date(UP_DATE)
                            .build();
    }
}