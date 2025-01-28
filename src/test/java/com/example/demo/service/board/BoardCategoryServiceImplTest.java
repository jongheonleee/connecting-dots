package com.example.demo.service.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardCategoryRequest;
import com.example.demo.dto.board.BoardCategoryResponse;
import com.example.demo.global.error.exception.business.board.BoardCategoryAlreadyExistsException;
import com.example.demo.global.error.exception.business.board.BoardCategoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.board.BoardCategoryRepository;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardCategoryServiceImplTest {

    @InjectMocks
    private BoardCategoryService boardCategoryService;

    @Mock
    private BoardCategoryRepository boardCategoryDao;

    @Mock
    private CustomFormatter formatter;

    private final String reg_date = "2025-01-15";
    private final Integer reg_user_seq = 1;
    private final String up_date = "2025-01-15";
    private final Integer up_user_seq = 1;


    @BeforeEach
    void setUp() {
        assertNotNull(boardCategoryService);
        assertNotNull(boardCategoryDao);
        assertNotNull(formatter);
    }

    @Nested
    @DisplayName("게시글 카테고리 조회 관련 테스트")
    class Describe_게시글_카테고리_조회_관련_테스트 {

        @DisplayName("카운팅 처리")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카운팅_처리(int cnt) {
            when(boardCategoryDao.count()).thenReturn(cnt);
            assertEquals(cnt, boardCategoryService.count());
        }

        @DisplayName("존재하는 카테고리 코드로 상세 조회 처리")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카테고리_코드로_상세_조회_처리(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                BoardCategoryResponse expected = createResponse(i);
                BoardCategoryDto dto = createDto(request);

                when(boardCategoryDao.existsByCateCode(request.getCate_code())).thenReturn(true);
                when(boardCategoryDao.selectByCateCode(request.getCate_code())).thenReturn(dto);

                BoardCategoryResponse actual = boardCategoryService.readByCateCode(request.getCate_code());

                assertEquals(expected.getCate_code(), actual.getCate_code());
                assertEquals(expected.getTop_cate(), actual.getTop_cate());
                assertEquals(expected.getName(), actual.getName());
                assertEquals(expected.getOrd(), actual.getOrd());
                assertEquals(expected.getChk_use(), actual.getChk_use());
                assertEquals(expected.getLevel(), actual.getLevel());
                assertEquals(expected.getComt(), actual.getComt());
            }
        }

        @DisplayName("존재하지 않는 카테고리 코드로 상세 조회 처리시 예외 발생 -> BoardCategoryNotFoundException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카테고리_코드로_상세_조회_처리시_예외_발생(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                when(boardCategoryDao.existsByCateCode(request.getCate_code())).thenReturn(false);
                assertThrows(BoardCategoryNotFoundException.class, () -> boardCategoryService.readByCateCode(request.getCate_code()));
            }
        }

        @DisplayName("존재하는 상위 카테고리 코드로 그 하위 카테고리 모두 조회 처리")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 상위_카테고리_코드로_그_하위_카테고리_모두_조회_처리(int cnt) {
            String top_cate = "AB010100";
            List<BoardCategoryDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                request.setTop_cate(top_cate);

                BoardCategoryDto dto = createDto(request);
                dummy.add(dto);
            }
            dummy.sort((o1, o2) -> o1.getOrd().compareTo(o2.getOrd()));

            when(boardCategoryDao.existsByCateCode(top_cate)).thenReturn(true);
            when(boardCategoryDao.selectByTopCate(top_cate)).thenReturn(dummy);

            List<BoardCategoryResponse> actual = boardCategoryService.readByTopCate(top_cate);

            assertEquals(cnt, actual.size());

            for (int i=0; i<cnt; i++) {
                BoardCategoryDto expected = dummy.get(i);
                BoardCategoryResponse response = actual.get(i);

                assertEquals(expected.getCate_code(), response.getCate_code());
                assertEquals(expected.getTop_cate(), response.getTop_cate());
                assertEquals(expected.getName(), response.getName());
                assertEquals(expected.getOrd(), response.getOrd());
                assertEquals(expected.getChk_use(), response.getChk_use());
                assertEquals(expected.getLevel(), response.getLevel());
                assertEquals(expected.getComt(), response.getComt());
            }
        }

        @DisplayName("존재하지 않는 상위 카테고리 코드로 그 하위 카테고리 조회시 예외 발생 -> BoardCategoryNotFoundException")
        @Test
        void 상위_카테고리_코드로_그_하위_카테고리_조회시_예외_발생() {
            String top_cate = "AB010100";
            when(boardCategoryDao.existsByCateCode(top_cate)).thenReturn(false);
            assertThrows(BoardCategoryNotFoundException.class, () -> boardCategoryService.readByTopCate(top_cate));
        }

        @DisplayName("모든 카테고리 조회 처리")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 모든_카테고리_조회_처리(int cnt) {
            List<BoardCategoryDto> dummy = new ArrayList<>();

            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                BoardCategoryDto dto = createDto(request);
                dummy.add(dto);
            }

            dummy.sort((o1, o2) -> o1.getOrd().compareTo(o2.getOrd()));

            when(boardCategoryDao.selectAll()).thenReturn(dummy);

            List<BoardCategoryResponse> actual = boardCategoryService.readAll();

            assertEquals(cnt, actual.size());
            for (int i=0; i<cnt; i++) {
                BoardCategoryDto expected = dummy.get(i);
                BoardCategoryResponse response = actual.get(i);

                assertEquals(expected.getCate_code(), response.getCate_code());
                assertEquals(expected.getTop_cate(), response.getTop_cate());
                assertEquals(expected.getName(), response.getName());
                assertEquals(expected.getOrd(), response.getOrd());
                assertEquals(expected.getChk_use(), response.getChk_use());
                assertEquals(expected.getLevel(), response.getLevel());
                assertEquals(expected.getComt(), response.getComt());
            }
        }

    }

    @Nested
    @DisplayName("카테고리 생성 관련 테스트")
    class Describe_카테고리_생성_관련_테스트 {

        @DisplayName("카테고리 생성 처리")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카테고리_생성_처리(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                BoardCategoryResponse expected = createResponse(i);
                BoardCategoryDto dto = createDto(request);

                when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
                when(formatter.getManagerSeq()).thenReturn(reg_user_seq);

                when(boardCategoryDao.existsByCateCode(request.getCate_code())).thenReturn(false);
                when(boardCategoryDao.insert(dto)).thenReturn(1);

                BoardCategoryResponse actual = boardCategoryService.create(request);

                assertEquals(expected.getCate_code(), actual.getCate_code());
                assertEquals(expected.getTop_cate(), actual.getTop_cate());
                assertEquals(expected.getName(), actual.getName());
                assertEquals(expected.getOrd(), actual.getOrd());
                assertEquals(expected.getChk_use(), actual.getChk_use());
                assertEquals(expected.getLevel(), actual.getLevel());
                assertEquals(expected.getComt(), actual.getComt());
            }
        }

        @DisplayName("주어진 카테고리 코드 번호가 이미 등록된 경우 예외 발생 -> BoardCategoryAlreadyExistsException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 주어진_카테고리_코드_번호가_이미_등록된_경우_예외_발생(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                when(boardCategoryDao.existsByCateCode(request.getCate_code())).thenReturn(true);
                assertThrows(BoardCategoryAlreadyExistsException.class, () -> boardCategoryService.create(request));
            }
        }

        @DisplayName("DBMS에 정상적으로 반영되지 않은 경우 예외 발생 -> NotApplyOnDbmsException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void DBMS에_정상적으로_반영되지_않은_경우_예외_발생(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                BoardCategoryDto dto = createDto(request);

                when(formatter.getCurrentDateFormat()).thenReturn(reg_date);
                when(formatter.getManagerSeq()).thenReturn(reg_user_seq);

                when(boardCategoryDao.existsByCateCode(request.getCate_code())).thenReturn(false);
                when(boardCategoryDao.insert(dto)).thenReturn(0);

                assertThrows(NotApplyOnDbmsException.class, () -> boardCategoryService.create(request));
            }
        }
    }

    @Nested
    @DisplayName("카테고리 수정 관련 테스트")
    class Describe_카테고리_수정_관련_테스트 {

        @DisplayName("카테고리 수정 처리")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카테고리_수정_처리(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();

                request.setName("수정된 카테고리" + i);
                request.setChk_use("N");
                request.setComt("수정된 카테고리입니다.");

                BoardCategoryDto dto = createDto(request);

                when(formatter.getCurrentDateFormat()).thenReturn(up_date);
                when(formatter.getManagerSeq()).thenReturn(up_user_seq);

                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(true);
                when(boardCategoryDao.update(dto)).thenReturn(1);

                assertDoesNotThrow(() -> boardCategoryService.modify(request));
            }
        }

        @DisplayName("주어진 카테고리 코드가 존재하지 않는 경우 예외 발생 -> BoardCategoryNotFoundException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 주어진_카테고리_코드_번호가_존재하지_않는_경우_예외_발생(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();

                request.setName("수정된 카테고리" + i);
                request.setChk_use("N");
                request.setComt("수정된 카테고리입니다.");

                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(false);

                assertThrows(BoardCategoryNotFoundException.class,
                        () -> boardCategoryService.modify(request)
                );
            }
        }

        @DisplayName("DBMS에 정상적으로 반영되지 않은 경우 예외 발생 -> NotApplyOnDbmsException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void DBMS에_정상적으로_반영되지_않은_경우_예외_발생(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();

                request.setName("수정된 카테고리" + i);
                request.setChk_use("N");
                request.setComt("수정된 카테고리입니다.");

                BoardCategoryDto dto = createDto(request);

                when(formatter.getCurrentDateFormat()).thenReturn(up_date);
                when(formatter.getManagerSeq()).thenReturn(up_user_seq);

                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(true);
                when(boardCategoryDao.update(dto)).thenReturn(0);

                assertThrows(NotApplyOnDbmsException.class,
                        () -> boardCategoryService.modify(request)
                );
            }
        }

        @DisplayName("존재하는 카테고리 코드로 카테고리 사용 여부 변경 처리 - Y")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카테고리_번호로_카테고리_사용_여부_변경_처리_Y(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();

                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(true);
                when(boardCategoryDao.updateChkUseY(cate_code)).thenReturn(1);

                assertDoesNotThrow(() -> boardCategoryService.modifyChkUseY(cate_code));
            }
        }

        @DisplayName("DBMS에 정상적으로 반영되지 않은 경우 예외 발생 -> NotApplyOnDbmsException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void DBMS에_정상적으로_반영되지_않은_경우_예외_발생_Y(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();

                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(true);
                when(boardCategoryDao.updateChkUseY(cate_code)).thenReturn(0);

                assertThrows(NotApplyOnDbmsException.class,
                        () -> boardCategoryService.modifyChkUseY(cate_code)
                );
            }
        }

        @DisplayName("존재하지 않는 카테고리 번호로 카테고리 사용 여부 변경 처리 - Y -> BoardCategoryNotFoundException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 존재하지_않는_카테고리_코드로_사용_여부_변경시_예외_발생(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();
                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(false);

                assertThrows(BoardCategoryNotFoundException.class,
                        () -> boardCategoryService.modifyChkUseY(cate_code)
                );
            }
        }

        @DisplayName("존재하는 카테고리 코드로 카테고리 사용 여부 변경 처리 - N")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카테고리_번호로_카테고리_사용_여부_변경_처리_N(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();

                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(true);
                when(boardCategoryDao.updateChkUseN(cate_code)).thenReturn(1);

                assertDoesNotThrow(() -> boardCategoryService.modifyChkUseN(cate_code));
            }
        }

        @DisplayName("존재하지 않는 카테고리 번호로 카테고리 사용 여부 변경 처리 - N -> BoardCategoryNotFoundException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 존재하지_않는_카테고리_코드로_사용_여부_변경시_예외_발생_N(int cnt) {
            for (int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();
                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(false);

                assertThrows(BoardCategoryNotFoundException.class,
                        () -> boardCategoryService.modifyChkUseN(cate_code)
                );
            }
        }


    }

    @Nested
    @DisplayName("카테고리 삭제 관련 테스트")
    class Describe_카테고리_삭제_관련_테스트 {

        @DisplayName("카테고리 삭제 처리")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void 카테고리_삭제_처리(int cnt) {
            for(int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();
                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(true);
                when(boardCategoryDao.deleteByCateCode(cate_code)).thenReturn(1);

                assertDoesNotThrow(() -> boardCategoryService.remove(cate_code));
            }
        }

        @DisplayName("DBMS에 정상적으로 반영되지 않은 경우 예외 발생 -> NotApplyOnDbmsException")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void DBMS에_정상적으로_반영되지_않은_경우_예외_발생(int cnt) {
            for(int i=0; i<cnt; i++) {
                BoardCategoryRequest request = createRequest(i);
                String cate_code = request.getCate_code();
                when(boardCategoryDao.existsByCateCodeForUpdate(cate_code)).thenReturn(true);
                when(boardCategoryDao.deleteByCateCode(cate_code)).thenReturn(0);
                assertThrows(NotApplyOnDbmsException.class, () -> boardCategoryService.remove(cate_code));
            }
        }

    }


    private BoardCategoryRequest createRequest(int i) {
        return BoardCategoryRequest.builder()
                                    .cate_code("AB010101" + i)
                                    .top_cate("AB010100" + i)
                                    .name("테스트용 카테고리" + i)
                                    .ord(1 + i)
                                    .chk_use("Y")
                                    .level(1 + i)
                                    .comt("테스트용 카테고리입니다.")
                                    .build();
    }

    private BoardCategoryResponse createResponse(int i) {
        return BoardCategoryResponse.builder()
                                    .cate_code("AB010101" + i)
                                    .top_cate("AB010100" + i)
                                    .name("테스트용 카테고리" + i)
                                    .ord(1 + i)
                                    .chk_use("Y")
                                    .level(1 + i)
                                    .comt("테스트용 카테고리입니다.")
                                    .build();
    }

    private BoardCategoryDto createDto(BoardCategoryRequest request) {
        return BoardCategoryDto.builder()
                                .cate_code(request.getCate_code())
                                .top_cate(request.getTop_cate())
                                .name(request.getName())
                                .ord(request.getOrd())
                                .chk_use(request.getChk_use())
                                .level(request.getLevel())
                                .comt(request.getComt())
                                .reg_user_seq(reg_user_seq)
                                .reg_date(reg_date)
                                .up_user_seq(up_user_seq)
                                .up_date(up_date)
                                .build();
    }

}