package com.example.demo.repository.mybatis.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.repository.board.BoardCategoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardCategoryDaoImplTest {

    private final Integer MAX_LEVEL = 3;
    private final Integer REG_USER_SEQ = 1;
    private final Integer UP_USER_SEQ = 1;
    private final String REG_DATE = "2025-01-14";
    private final String UP_DATE = "2025-01-14";

    @Autowired
    private BoardCategoryRepository boardCategoryDao;

    @BeforeEach
    void setUp() {
        assertNotNull(boardCategoryDao);

        for (int i=MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

       assertEquals(0, boardCategoryDao.count());
    }

    @AfterEach
    void clean() {
        for (int i=MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        assertEquals(0, boardCategoryDao.count());
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
        }

        assertEquals(cnt, boardCategoryDao.count());
    }

    @DisplayName("cate_code로 존재 여부 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void cate_code로_존재_여부_확인_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (BoardCategoryDto boardCategoryDto : dummy) {
            boolean exists = boardCategoryDao.existsByCateCode(boardCategoryDto.getCate_code());
            assertTrue(exists);
        }
    }

    @DisplayName("cate_code로 존재 여부 확인 테스트2")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void cate_code로_존재_여부_확인_테스트2(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (BoardCategoryDto boardCategoryDto : dummy) {
            boolean exists = boardCategoryDao.existsByCateCodeForUpdate(boardCategoryDto.getCate_code());
            assertTrue(exists);
        }
    }

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_테스트(int cnt) {
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
        }
    }


    @DisplayName("cate_code로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void cate_code로_조회_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (BoardCategoryDto expected : dummy) {
            BoardCategoryDto actual = boardCategoryDao.selectByCateCode(expected.getCate_code());

            assertNotNull(actual);
            assertEquals(expected.getCate_code(), actual.getCate_code());
            assertEquals(expected.getTop_cate(), actual.getTop_cate());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getComt(), actual.getComt());
            assertEquals(expected.getOrd(), actual.getOrd());
            assertEquals(expected.getChk_use(), actual.getChk_use());
            assertEquals(expected.getLevel(), actual.getLevel());
        }
    }

    @DisplayName("전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_조회_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        List<BoardCategoryDto> actuals = boardCategoryDao.selectAll();
        assertEquals(cnt, actuals.size());

        dummy.sort((o1, o2) -> o1.getCate_code().compareTo(o2.getCate_code()));
        actuals.sort((o1, o2) -> o1.getCate_code().compareTo(o2.getCate_code()));

        for (int i=0; i<cnt; i++) {
            BoardCategoryDto expected = dummy.get(i);
            BoardCategoryDto actual = actuals.get(i);

            assertEquals(expected.getCate_code(), actual.getCate_code());
            assertEquals(expected.getTop_cate(), actual.getTop_cate());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getComt(), actual.getComt());
            assertEquals(expected.getOrd(), actual.getOrd());
            assertEquals(expected.getChk_use(), actual.getChk_use());
            assertEquals(expected.getLevel(), actual.getLevel());
        }
    }

    @DisplayName("top_cate로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void top_cate로_조회_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=0; i<cnt; i++) {
            BoardCategoryDto expected = dummy.get(i);
            List<BoardCategoryDto> actuals = boardCategoryDao.selectByTopCate(expected.getTop_cate());
            assertEquals(cnt, actuals.size());
        }
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=0; i<cnt; i++) {
            BoardCategoryDto expected = dummy.get(i);
            expected.setName("수정된 이름" + i);
            expected.setComt("수정된 설명" + i);
            expected.setOrd(i + 100);
            expected.setChk_use("N");

            assertEquals(1, boardCategoryDao.update(expected));

            BoardCategoryDto actual = boardCategoryDao.selectByCateCode(expected.getCate_code());
            assertEquals(expected.getCate_code(), actual.getCate_code());
            assertEquals(expected.getTop_cate(), actual.getTop_cate());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getComt(), actual.getComt());
            assertEquals(expected.getOrd(), actual.getOrd());
            assertEquals(expected.getChk_use(), actual.getChk_use());
            assertEquals(expected.getLevel(), actual.getLevel());
        }
    }

    @DisplayName("chk_use Y로 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void chk_use_Y로_수정_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=0; i<cnt; i++) {
            BoardCategoryDto expected = dummy.get(i);
            assertEquals(1, boardCategoryDao.updateChkUseY(expected.getCate_code()));

            BoardCategoryDto actual = boardCategoryDao.selectByCateCode(expected.getCate_code());
            assertEquals("Y", actual.getChk_use());
        }
    }

    @DisplayName("chk_use N로 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void chk_use_N로_수정_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=0; i<cnt; i++) {
            BoardCategoryDto expected = dummy.get(i);
            assertEquals(1, boardCategoryDao.updateChkUseN(expected.getCate_code()));

            BoardCategoryDto actual = boardCategoryDao.selectByCateCode(expected.getCate_code());
            assertEquals("N", actual.getChk_use());
        }
    }

    @DisplayName("cate_code로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void cate_code로_삭제_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=0; i<cnt; i++) {
            BoardCategoryDto expected = dummy.get(i);
            assertEquals(1, boardCategoryDao.deleteByCateCode(expected.getCate_code()));
            assertNull(boardCategoryDao.selectByCateCode(expected.getCate_code()));
        }
    }

    @DisplayName("level로 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void level로_삭제_테스트(int cnt) {
        List<BoardCategoryDto> dummy = new ArrayList<>();
        for (int i=0; i<cnt; i++) {
            BoardCategoryDto dto = createDto(i);
            assertEquals(1, boardCategoryDao.insert(dto));
            dummy.add(dto);
        }

        for (int i=MAX_LEVEL; i>=0; i--) {
            int totalCntForLevel = boardCategoryDao.countByLevel(i);
            assertEquals(totalCntForLevel, boardCategoryDao.deleteByLevel(i));
        }

        assertEquals(0, boardCategoryDao.count());
    }



    private BoardCategoryDto createDto(int i) {
        int z = i % 4; // 0 ~ 3
        return BoardCategoryDto.builder()
                                .cate_code("BC10100" + i)
                                .top_cate("BC101000")
                                .name("테스트용" + i)
                                .comt("테스트" + i)
                                .ord(i + 1)
                                .chk_use("Y")
                                .level(z)
                                .reg_user_seq(REG_USER_SEQ)
                                .reg_date(REG_DATE)
                                .up_user_seq(UP_USER_SEQ)
                                .up_date(UP_DATE)
                                .build();
    }
}