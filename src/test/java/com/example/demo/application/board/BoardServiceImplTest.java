package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.application.board.impl.BoardCategoryServiceImpl;
import com.example.demo.application.board.impl.BoardChangeHistoryServiceImpl;
import com.example.demo.application.board.impl.BoardImgServiceImpl;
import com.example.demo.application.board.impl.BoardServiceImpl;
import com.example.demo.application.board.impl.BoardStatusServiceImpl;
import com.example.demo.application.code.CommonCodeService;
import com.example.demo.dto.board.BoardCategoryResponse;
import com.example.demo.dto.board.BoardChangeHistoryRequest;
import com.example.demo.dto.board.BoardChangeHistoryResponse;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardImgRequest;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardStatusRequest;
import com.example.demo.dto.board.BoardStatusResponse;
import com.example.demo.dto.code.CodeResponse;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.utils.CustomFormatter;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 추가해야 하는 것들이 있으므로 그거 먼저 구현하고 TDD 진행
 * - BoardImgDaoImpl boardImgDao; -> 게시판 생성, 조회시 이미지 처리
 * - BoardStatusDaoImpl boardStatusDao; -> 게시판 상태 변경시 적용
 * - BoardChangeHistoryDaoImpl boardChangeHistoryDao; -> 게시판 변경 이력 기록
 */
@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @InjectMocks
    private BoardServiceImpl sut;

    @Mock
    private BoardDaoImpl boardDao;

    @Mock
    private BoardCategoryServiceImpl boardCategoryService;

    @Mock
    private BoardStatusServiceImpl boardStatusService;

    @Mock
    private CommonCodeService commonCodeService;

    @Mock
    private BoardImgServiceImpl boardImgService;

    @Mock
    private BoardChangeHistoryServiceImpl boardChangeHistoryService;

    @Mock
    private CustomFormatter formatter;

    private final String REG_DATE = "2025-01-17";
    private final Integer REG_USER_SEQ = 1;
    private final String UP_DATE = "2025-01-17";
    private final Integer UP_USER_SEQ = 1;


    private final String APPL_BEGIN = "2025-01-17 00:00:00";
    private final String APPL_END = "9999-12-31 23:59:59";
    private final String END_APPL = "2025-01-17 00:00:00";


    @BeforeEach
    void setUp() {
        assertNotNull(sut);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryService);
        assertNotNull(formatter);
    }

    @Nested
    @DisplayName("게시글 카운팅 처리 테스트")
    class CountTest {
        @Test
        @DisplayName("게시글이 없을 때")
        void countWhenNoBoard() {
            // given
            int expected = 0;
            // when
            when(boardDao.count()).thenReturn(expected);
            int actual = sut.count();
            // then
            assertEquals(expected, actual);
        }

        @DisplayName("게시글이 n개 등록되어 있을 때")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void countWhenNBoard(int n) {
            // given
            int expected = n;
            // when
            when(boardDao.count()).thenReturn(expected);
            int actual = sut.count();
            // then
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("게시글 생성 관련 테스트")
    class CreateTest {

        @Test
        @DisplayName("게시글 생성 성공 테스트")
        void createBoard() {
            // given
            // 게시글 리퀘스트 객체, 이미지 파일 생성
            // - 게시글 내용, 이미지, 게시글 카테고리, 게시글 상태
            BoardRequest request = createBoardRequest();
            List<MultipartFile> files = createMultipartFiles();

            when(formatter.getCurrentDateFormat()).thenReturn(REG_DATE);
            when(formatter.getManagerSeq()).thenReturn(REG_USER_SEQ);

            when(boardDao.insert(any())).thenReturn(1);

            for (int i=0; i<files.size(); i++) {
                doNothing().when(boardImgService).saveBoardImage(any(), eq(files.get(i)));
            }


            BoardStatusRequest boardStatusRequest = createBoardStatusRequestRequest();
            BoardStatusResponse boardStatusResponse = createBoardStatusRequestResponse(boardStatusRequest);
            when(boardStatusService.create(any())).thenReturn(boardStatusResponse);

            BoardChangeHistoryRequest boardChangeHistoryRequest = createBoardChangeHistoryRequest();
            BoardChangeHistoryResponse boardChangeHistoryResponse = createBoardChangeHistoryResponse(boardChangeHistoryRequest);
            when(boardChangeHistoryService.createInit(any(), any())).thenReturn(boardChangeHistoryResponse);

            // when
            // 게시글 생성 서비스 호출
            // then
            // 게시글 생성 결과 확인
            assertDoesNotThrow(() -> sut.create(request, files));
        }

    }

    @Nested
    @DisplayName("게시글 조회 관련 테스트")
    class ReadTest {

        @Test
        @DisplayName("게시글 상세 조회 테스트")
        void readBoard() {
            // given
            // 게시글 번호
            int bno = 1;
            // when
            // 게시글 상세 조회 서비스 호출
            // then
            // 게시글 상세 조회 결과 확인
        }

        @Test
        @DisplayName("게시글 검색 조회 테스트")
        void readBoardBySearch() {
            // given
            // 검색 조건
            // when
            // 게시글 검색 조회 서비스 호출
            // then
            // 게시글 검색 조회 결과 확인
        }

    }


    @Nested
    @DisplayName("게시글 수정 관련 테스트")
    class ModifyTest {


    }

    @Nested
    @DisplayName("게시글 삭제 관련 테스트")
    class DeleteTest {


    }

    private BoardRequest createBoardRequest() {
        return BoardRequest.builder()
                            .cate_code("BC010201")
                            .user_seq(1)
                            .writer("여늘")
                            .title("딥러닝 전망 및 학습방법")
                            .cont("안알랴줌 ㅋ")
                            .comt("...")
                            .build();
    }

    private BoardDto createBoardDto(BoardRequest request) {
        return BoardDto.builder()
                       .cate_code(request.getCate_code())
                       .user_seq(request.getUser_seq())
                       .writer(request.getWriter())
                       .title(request.getTitle())
                       .cont(request.getCont())
                       .comt(request.getComt())
                       .reg_user_seq(REG_USER_SEQ)
                       .up_user_seq(REG_USER_SEQ)
                       .reg_date(REG_DATE)
                       .up_date(REG_DATE)
                       .build();
    }

    private List<MultipartFile> createMultipartFiles() {
        return List.of(
                new MockMultipartFile("test1.jpeg", "test2.jpeg", "image/jpeg", "test3.jpeg".getBytes()),
                new MockMultipartFile("test2.jpeg", "test2.jpeg", "image/jpeg", "test2.jpeg".getBytes()),
                new MockMultipartFile("test3.jpeg", "test3.jpeg", "image/jpeg", "test3.jpeg".getBytes())
        );
    }

    private BoardCategoryResponse createBoardCategoryResponse() {
        return BoardCategoryResponse.builder()
                                    .cate_code("BC010201")
                                    .top_cate("BC010200")
                                    .name("인공지능")
                                    .comt("딥러닝에 대한 내용을 공유합니다.")
                                    .ord(1)
                                    .chk_use("Y")
                                    .level(2)
                                    .build();
    }

    private CodeResponse createCodeResponse() {
        return CodeResponse.builder()
                           .level(2)
                           .code("3001")
                           .name("생성완료")
                           .top_code("3000")
                           .build();
    }

    private BoardImgRequest createBoardImageRequest(int bno, int i, MultipartFile file) {
        return BoardImgRequest.builder()
                            .bno(bno)
                            .name(file.getName())
                            .chk_thumb(i == 0 ? "Y" : "N")
                            .build();
    }


    private BoardImgRequest createImageRequest() {
        return BoardImgRequest.builder()
                .bno(1)
                .name("test.jpeg")
                .img("test.jpeg")
                .chk_thumb("Y")
                .comt("...")
                .build();
    }

    private BoardChangeHistoryResponse createBoardChangeHistoryResponse(BoardChangeHistoryRequest request) {
        return BoardChangeHistoryResponse.builder()
                                         .seq(1)
                                         .bno(request.getBno())
                                         .title(request.getTitle())
                                         .cont(request.getCont())
                                         .comt(request.getComt())
                                         .build();
    }


    private BoardStatusResponse createBoardStatusRequestResponse(BoardStatusRequest request) {
        return BoardStatusResponse.builder()
                                  .seq(1)
                                  .bno(request.getBno())
                                  .stat_code("3001")
                                  .comt("게시글 생성")
                                  .appl_begin(APPL_BEGIN)
                                  .appl_end(APPL_END)
                                  .build();
    }



    private BoardImgRequest createBoardImgRequest() {
        return BoardImgRequest.builder()
                .bno(1)
                .name("test.jpeg")
                .img("test.jpeg")
                .chk_thumb("Y")
                .comt("...")
                .build();
    }

    private MultipartFile createMultipartFile() {
        return new MockMultipartFile("test.jpeg", "test.jpeg", "image/jpeg", "test.jpeg".getBytes());
    }


    private BoardStatusRequest createBoardStatusRequestRequest() {
        return BoardStatusRequest.builder()
                                 .stat_code("3001")
                                 .bno(1)
                                 .days(1)
                                 .build();
    }

    private BoardChangeHistoryRequest createBoardChangeHistoryRequest() {
        return BoardChangeHistoryRequest.builder()
                .bno(1)
                .title("게시글 제목")
                .cont("게시글 내용")
                .comt("게시글 댓글")
                .build();
    }



}