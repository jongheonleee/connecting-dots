package com.example.demo.repository.mybatis.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.BoardCategory;
import com.example.demo.domain.Code;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardStatusDto;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.repository.board.BoardCategoryRepository;
import com.example.demo.repository.board.BoardRepository;
import com.example.demo.repository.board.BoardStatusRepository;
import com.example.demo.repository.code.CommonCodeRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardStatusDaoImplTest {

    @Autowired
    private BoardStatusRepository boardStatusDao;

    @Autowired
    private BoardRepository boardDao;

    @Autowired
    private BoardCategoryRepository boardCategoryDao;

    @Autowired
    private CommonCodeRepository commonCodeDao;

    private BoardDto boardDto;
    private BoardCategoryDto boardCategoryDto;
    private CodeDto codeDto;

    private final Integer REG_USER_SEQ = 1;
    private final Integer UP_USER_SEQ = 1;
    private final String REG_DATE = "2025-01-16 00:00:00";
    private final String UP_DATE = "2025-01-16 00:00:00";
    private final String CATE_CODE = "BC101001";
    private final Integer TEST_USER_SEQ = 1;
    private final String COMMON_CODE = "4001";
    private final String TOP_COMMON_CODE = "4000";
    private final String APPL_BEGIN = "2025-01-01 00:00:00";
    private final String APPL_END = "2025-02-16 00:00:00";


    @BeforeEach
    void setUp() {
        assertNotNull(commonCodeDao);
        assertNotNull(boardStatusDao);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryDao);


        boardStatusDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        for (int i= Code.MAX_LEVEL; i>=0; i--) {
            commonCodeDao.deleteByLevel(i);
        }

        assertEquals(0, boardStatusDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());
        assertEquals(0, commonCodeDao.count());

        // 임시 카테고리 생성 및 등록
        createCategoryDto();
        assertEquals(1, boardCategoryDao.insert(boardCategoryDto));

        // 게시글 상태 코드 생성 및 등록
        createCommonCode();
        assertEquals(1, commonCodeDao.insert(codeDto));

        // 임시 게시글 생성
        createBoardDto();
        assertEquals(1, boardDao.insert(boardDto));
    }

    @AfterEach
    void clean() {
        boardStatusDao.deleteAll();
        boardDao.deleteAll();
        for (int i= BoardCategory.MAX_LEVEL; i>=0; i--) {
            boardCategoryDao.deleteByLevel(i);
        }

        for (int i= Code.MAX_LEVEL; i>=0; i--) {
            commonCodeDao.deleteByLevel(i);
        }

        assertEquals(0, boardStatusDao.count());
        assertEquals(0, boardDao.count());
        assertEquals(0, boardCategoryDao.count());
    }

    @Nested
    @DisplayName("카운팅 및 존재 여부 테스트")
    class CountAndExistsTest {
        @DisplayName("n개 등록 후 카운팅 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void n개_등록_후_카운팅_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
            }

            assertEquals(cnt, boardStatusDao.count());
        }

        @DisplayName("n개 등록 후 seq로 존재 여부 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void n개_등록_후_존재_여부_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardStatusDto dto : dummy) {
                assertTrue(boardStatusDao.existsBySeq(dto.getSeq()));
            }
        }

        @DisplayName("n개 등록 후 bno로 존재 여부 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void n개_등록_후_bno로_존재_여부_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardStatusDto dto : dummy) {
                assertTrue(boardStatusDao.existsByBno(dto.getBno()));
            }
        }

        @DisplayName("n개 등록한 후 seq로 삭제 후 존재 여부 테스트, 이때 SELECT FOR UPDATE 처리")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void n개_등록한_후_삭제_후_존재_여부_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardStatusDto dto : dummy) {
                assertTrue(boardStatusDao.existsBySeqForUpdate(dto.getSeq()));
            }
        }
    }

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @DisplayName("n개 생성 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void n개_생성_테스트(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
            }

            assertEquals(cnt, boardStatusDao.count());
        }

    }

    @Nested
    @DisplayName("조회 테스트")
    class ReadTest {

        @DisplayName("모두 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void 모두_조회_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            List<BoardStatusDto> result = boardStatusDao.selectAll();
            assertEquals(cnt, result.size());

            dummy.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
            result.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            for (int i=0; i<cnt; i++) {
                BoardStatusDto expected = dummy.get(i);
                BoardStatusDto actual = result.get(i);

                assertEquals(expected.getSeq(), actual.getSeq());
                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getStat_code(), actual.getStat_code());
                assertEquals(expected.getComt(), actual.getComt());
                assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
                assertEquals(expected.getAppl_end(), actual.getAppl_end());
                assertEquals(expected.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(expected.getReg_date(), actual.getReg_date());
                assertEquals(expected.getUp_user_seq(), actual.getUp_user_seq());
                assertEquals(expected.getUp_date(), actual.getUp_date());
            }
        }

        @DisplayName("bno로 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void bno로_조회_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            List<BoardStatusDto> result = boardStatusDao.selectByBno(boardDto.getBno());

            dummy.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));
            result.sort((a, b) -> a.getSeq().compareTo(b.getSeq()));

            for (int i=0; i<cnt; i++) {
                BoardStatusDto expected = dummy.get(i);
                BoardStatusDto actual = result.get(i);

                assertEquals(expected.getSeq(), actual.getSeq());
                assertEquals(expected.getBno(), actual.getBno());
                assertEquals(expected.getStat_code(), actual.getStat_code());
                assertEquals(expected.getComt(), actual.getComt());
                assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
                assertEquals(expected.getAppl_end(), actual.getAppl_end());
                assertEquals(expected.getReg_user_seq(), actual.getReg_user_seq());
                assertEquals(expected.getReg_date(), actual.getReg_date());
                assertEquals(expected.getUp_user_seq(), actual.getUp_user_seq());
                assertEquals(expected.getUp_date(), actual.getUp_date());
            }
        }

        @DisplayName("bno로 현재 시점에 적용되는 게시글 상태 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void bno로_현재_시점에_적용되는_게시글_상태_조회_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);

                if (i != cnt-1) {
                    dto.setAppl_begin("2024-12-12 00:00:00");
                    dto.setAppl_end("2024-12-31 00:00:00");
                } else {
                    dto.setAppl_begin("2025-01-16 00:00:00");
                    dto.setAppl_end("9999-12-31 23:59:59");
                }

                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            BoardStatusDto expected = dummy.get(cnt - 1);
            BoardStatusDto actual = boardStatusDao.selectByBnoAtPresent(boardDto.getBno());

            assertEquals(expected.getBno(), actual.getBno());
            assertEquals(expected.getStat_code(), actual.getStat_code());
            assertEquals(expected.getComt(), actual.getComt());
            assertEquals(expected.getAppl_begin(), actual.getAppl_begin());
            assertEquals(expected.getAppl_end(), actual.getAppl_end());
            assertEquals(expected.getReg_user_seq(), actual.getReg_user_seq());
            assertEquals(expected.getReg_date(), actual.getReg_date());
            assertEquals(expected.getUp_user_seq(), actual.getUp_user_seq());
            assertEquals(expected.getUp_date(), actual.getUp_date());
        }

        @DisplayName("seq로 조회 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void seq로_조회_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            for (BoardStatusDto boardStatusDto : dummy) {
                BoardStatusDto result = boardStatusDao.selectBySeq(boardStatusDto.getSeq());

                assertEquals(boardStatusDto.getSeq(), result.getSeq());
                assertEquals(boardStatusDto.getBno(), result.getBno());
                assertEquals(boardStatusDto.getStat_code(), result.getStat_code());
                assertEquals(boardStatusDto.getComt(), result.getComt());
                assertEquals(boardStatusDto.getAppl_begin(), result.getAppl_begin());
                assertEquals(boardStatusDto.getAppl_end(), result.getAppl_end());
                assertEquals(boardStatusDto.getReg_user_seq(), result.getReg_user_seq());
                assertEquals(boardStatusDto.getReg_date(), result.getReg_date());
                assertEquals(boardStatusDto.getUp_user_seq(), result.getUp_user_seq());
                assertEquals(boardStatusDto.getUp_date(), result.getUp_date());
            }
        }
    }

    @Nested
    @DisplayName("수정 테스트")
    class UpdateTest {
        @DisplayName("n개 수정 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void n개_수정_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = dummy.get(i);
                dto.setStat_code("4002");
                dto.setComt("수정된 게시글 상태");
                dto.setAppl_begin("2025-01-01 00:00:00");
                dto.setAppl_end("2025-01-31 00:00:00");
                dto.setUp_user_seq(UP_USER_SEQ);
                dto.setUp_date(UP_DATE);

                assertEquals(1, boardStatusDao.update(dto));

                BoardStatusDto result = boardStatusDao.selectBySeq(dto.getSeq());

                assertEquals(dto.getSeq(), result.getSeq());
                assertEquals(dto.getBno(), result.getBno());
                assertEquals(dto.getStat_code(), result.getStat_code());
                assertEquals(dto.getComt(), result.getComt());
                assertEquals(dto.getAppl_begin(), result.getAppl_begin());
                assertEquals(dto.getAppl_end(), result.getAppl_end());
                assertEquals(dto.getReg_user_seq(), result.getReg_user_seq());
                assertEquals(dto.getReg_date(), result.getReg_date());
                assertEquals(dto.getUp_user_seq(), result.getUp_user_seq());
                assertEquals(dto.getUp_date(), result.getUp_date());
            }
        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    class DeleteTest {

        @DisplayName("n개 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void n개_삭제_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = dummy.get(i);
                assertEquals(1, boardStatusDao.deleteBySeq(dto.getSeq()));
            }

            assertEquals(0, boardStatusDao.count());
        }

        @DisplayName("bno로 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void bno로_삭제_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            assertEquals(cnt, boardStatusDao.deleteByBno(boardDto.getBno()));
            assertEquals(0, boardStatusDao.count());
        }

        @DisplayName("전체 삭제 테스트")
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20, 30})
        void 전체_삭제_테스트(int cnt) {
            List<BoardStatusDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardStatusDto dto = createBoardStatusDto(i);
                assertEquals(1, boardStatusDao.insert(dto));
                dummy.add(dto);
            }

            assertEquals(cnt, boardStatusDao.deleteAll());
            assertEquals(0, boardStatusDao.count());
        }
    }

    private BoardStatusDto createBoardStatusDto(int i) {
        return BoardStatusDto.builder()
                .bno(boardDto.getBno())
                .stat_code(COMMON_CODE)
                .comt("테스트용 게시글 상태")
                .appl_begin(APPL_BEGIN)
                .appl_end(APPL_END)
                .reg_user_seq(REG_USER_SEQ)
                .reg_date(REG_DATE)
                .up_user_seq(UP_USER_SEQ)
                .up_date(UP_DATE)
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

    private void createCommonCode() {
        codeDto = CodeDto.builder()
                        .level(1)
                        .code(COMMON_CODE)
                        .name("테스트용 게시글 생성")
                        .top_code(TOP_COMMON_CODE)
                        .chk_use("Y")
                        .reg_date(REG_DATE)
                        .reg_user_seq(REG_USER_SEQ)
                        .up_date(UP_DATE)
                        .up_user_seq(UP_USER_SEQ)
                        .build();

    }
}