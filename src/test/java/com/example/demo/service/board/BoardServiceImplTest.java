package com.example.demo.service.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardDetailResponse;
import com.example.demo.dto.board.BoardImgResponse;
import com.example.demo.dto.board.BoardMainDto;
import com.example.demo.dto.board.BoardMainResponse;
import com.example.demo.dto.board.BoardUpdateRequest;
import com.example.demo.dto.comment.CommentDetailResponse;
import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.service.board.impl.BoardServiceImpl;
import com.example.demo.service.code.CommonCodeService;
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
import com.example.demo.service.comment.CommentService;
import com.example.demo.service.reply.ReplyService;
import com.example.demo.utils.CustomFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private BoardCategoryDaoImpl boardCategoryDao;

    @Mock
    private ReplyService replyService;

    @Mock
    private BoardCategoryService boardCategoryService;

    @Mock
    private BoardStatusService boardStatusService;

    @Mock
    private CommentService commentService;

    @Mock
    private CommonCodeService commonCodeService;

    @Mock
    private BoardImgService boardImgService;

    @Mock
    private BoardChangeHistoryService boardChangeHistoryService;

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
    class sut_count_test {
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
    class sut_crete_test {

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
    class sut_read_test {

        /**
         * PageResponse<T>
         *
         * - totalCnt; 전체 게시글 수
         * - page; 현재 페이지
         * - pageSize; 페이지 사이즈
         * - totalPage; 전체 페이지 수
         * - List<T> responses; 조회된 게시글 리스트
         *
         */
        @Test
        @DisplayName("사용자가 메인 페이지에 보여줄 게시글을 조회한다.")
        void it_correctly_work_when_user_read_some_boards_on_main_page() {
            Integer page = 1, pageSize = 10; // 1페이지, 10개씩
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("pageSize", pageSize);

            List<BoardMainDto> expected = new ArrayList<>();
            for (int i=0; i<pageSize; i++) {
                expected.add(BoardMainDto.builder()
                                         .bno(i+1)
                                         .cate_name("학습")
                                         .writer("여늘")
                                         .title("딥러닝 전망 및 학습방법")
                                         .view_cnt(10 * i + 1)
                                         .reco_cnt(5 * i + 1)
                                         .thumb("test.jpeg")
                                         .comment_cnt(3 * i + 1)
                                         .build()
                );
            }

            when(boardDao.count()).thenReturn(20);
            when(boardDao.selectForMain(map)).thenReturn(expected);

            PageResponse actual = sut.readForMain(page, pageSize);
            assertEquals(pageSize, actual.getResponses().size());
            for (Object o : actual.getResponses()) {
                assertTrue(o instanceof BoardMainResponse);
                BoardMainResponse response = (BoardMainResponse) o;
                System.out.println(response);

            }
        }

        @Test
        @DisplayName("사용자가 카테고리를 통해 메인 페이지에 해당 카테고리 관련된 게시글을 조회한다.")
        void it_correctly_work_when_user_read_some_boards_on_main_page_by_category() {
            String cate_code = "BC010201";
            Integer page = 1, pageSize = 10; // 1페이지, 10개씩

            Map<String, Object> map = new HashMap<>();
            map.put("cate_code", cate_code);
            map.put("page", page);
            map.put("pageSize", pageSize);

            List<BoardMainDto> expected = new ArrayList<>();
            for (int i=0; i<pageSize; i++) {
                expected.add(BoardMainDto.builder()
                        .bno(i+1)
                        .cate_name("학습")
                        .writer("여늘")
                        .title("딥러닝 전망 및 학습방법")
                        .view_cnt(10 * i + 1)
                        .reco_cnt(5 * i + 1)
                        .thumb("test.jpeg")
                        .comment_cnt(3 * i + 1)
                        .build()
                );
            }


            when(boardCategoryDao.existsByCateCode(cate_code)).thenReturn(true);
            when(boardDao.countByCategory(cate_code)).thenReturn(20);
            when(boardDao.selectForMainByCategory(map)).thenReturn(expected);

            PageResponse actual = sut.readByCategoryForMain(cate_code, page, pageSize);

            assertEquals(pageSize, actual.getResponses().size());
            for (Object o : actual.getResponses()) {
                assertTrue(o instanceof BoardMainResponse);
                BoardMainResponse response = (BoardMainResponse) o;
                System.out.println(response);
            }
        }

        @Test
        @DisplayName("사용자가 검색 키워드를 통해 메인 페이지에 해당 키워드 관련된 게시글을 조회한다.")
        void it_correctly_work_when_user_read_some_boards_on_main_page_by_search_keyword() {
            SearchCondition sc = new SearchCondition();
            sc.setSearchOption("TT");
            sc.setSearchKeyword("딥러닝");
            sc.setSortOption("1");
            sc.setPage(1);
            sc.setPageSize(10);

            List<BoardMainDto> expected = new ArrayList<>();
            for (int i=0; i<sc.getPageSize(); i++) {
                expected.add(BoardMainDto.builder()
                        .bno(i+1)
                        .cate_name("학습")
                        .writer("여늘")
                        .title("딥러닝 전망 및 학습방법")
                        .view_cnt(10 * i + 1)
                        .reco_cnt(5 * i + 1)
                        .thumb("test.jpeg")
                        .comment_cnt(3 * i + 1)
                        .build()
                );
            }

            when(boardDao.countBySearchCondition(sc)).thenReturn(20);
            when(boardDao.selectForMainBySearchCondition(sc)).thenReturn(expected);

            PageResponse actual = sut.readBySearchConditionForMain(sc);
            assertEquals(sc.getPageSize(), actual.getResponses().size());
            for (Object o : actual.getResponses()) {
                assertTrue(o instanceof BoardMainResponse);
                BoardMainResponse response = (BoardMainResponse) o;
                System.out.println(response);
            }

        }

        @Test
        @DisplayName("사용자가 상세 조회한다.(상세 페이지 접속)")
        void it_correctly_work_when_user_read_some_board_detail() {
            Integer bno = 1;
            BoardStatusResponse currStatus = BoardStatusResponse.builder()
                                                                .seq(1)
                                                                .bno(bno)
                                                                .stat_code("3001")
                                                                .comt("게시글 생성")
                                                                .appl_begin(APPL_BEGIN)
                                                                .appl_end(APPL_END)
                                                                .build();

            BoardDto found = BoardDto.builder()
                                     .bno(bno)
                                     .cate_code("BC010201")
                                     .user_seq(1)
                                     .writer("여늘")
                                     .title("딥러닝 전망 및 학습방법")
                                     .cont("안알랴줌 ㅋ")
                                     .comt("...")
                                     .reg_user_seq(REG_USER_SEQ)
                                     .up_user_seq(REG_USER_SEQ)
                                     .reg_date(REG_DATE)
                                     .up_date(REG_DATE)
                                     .build();


            BoardCategoryResponse foundBoardCategory = BoardCategoryResponse.builder()
                                                                          .cate_code("BC010201")
                                                                          .top_cate("BC010200")
                                                                          .name("인공지능")
                                                                          .comt("딥러닝에 대한 내용을 공유합니다.")
                                                                          .ord(1)
                                                                          .chk_use("Y")
                                                                          .level(2)
                                                                          .build();
            List<BoardImgResponse> foundBoardImages = new ArrayList<>();
            List<CommentDetailResponse> foundComments = commentService.readByBno(bno);

            when(boardDao.existsByBno(bno)).thenReturn(true);
            when(boardStatusService.readByBnoAtPresent(bno)).thenReturn(currStatus);
            when(boardDao.select(bno)).thenReturn(found);
            when(boardCategoryService.readByCateCode(found.getCate_code())).thenReturn(foundBoardCategory);
            when(boardImgService.readByBno(bno)).thenReturn(foundBoardImages);
            when(commentService.readByBno(bno)).thenReturn(foundComments);

            BoardDetailResponse actual = sut.readDetailByBno(bno);

            assertNotNull(actual);
        }

        @Test
        @DisplayName("사용자가 프로필 페이지에서 자신이 작성한 게시글을 조회한다.")
        void it_correctly_work_when_user_read_some_boards_on_profile_page() {
            // 추후에 회원 파트 개발되면 처리 예정
        }
    }


    @Nested
    @DisplayName("게시글 수정 관련 테스트")
    class sut_modify_test {

        @Test
        @DisplayName("사용자가 기존에 등록한 게시글의 내용을 수정한다.")
        void it_correctly_work_when_user_modify_some_board() {
            BoardUpdateRequest request = BoardUpdateRequest.builder()
                    .bno(1)
                    .cate_code("BC010201")
                    .user_seq(1)
                    .writer("여늘")
                    .title("딥러닝 전망 및 학습방법")
                    .cont("안알랴줌 ㅋ")
                    .comt("...")
                    .build();

            List<MultipartFile> files = createMultipartFiles();

            BoardDto boardDto = BoardDto.builder()
                    .bno(request.getBno())
                    .cate_code(request.getCate_code())
                    .user_seq(request.getUser_seq())
                    .writer(request.getWriter())
                    .title(request.getTitle())
                    .cont(request.getCont())
                    .comt(request.getComt())
                    .up_user_seq(REG_USER_SEQ)
                    .up_date(REG_DATE)
                    .build();


            when(formatter.getCurrentDateFormat()).thenReturn(UP_DATE);
            when(formatter.getManagerSeq()).thenReturn(UP_USER_SEQ);

            when(boardDao.existsByBnoForUpdate(request.getBno())).thenReturn(true);
            when(boardDao.update(boardDto)).thenReturn(1);
            when(boardImgService.readByBno(request.getBno())).thenReturn(new ArrayList<>());
            for (int i=0; i<files.size(); i++) {
                doNothing().when(boardImgService).saveBoardImage(any(), eq(files.get(i)));
            }
            doNothing().when(boardStatusService).renewState(any());

            assertDoesNotThrow(() -> sut.modify(request, files));
        }

    }

    @Nested
    @DisplayName("게시글 삭제 관련 테스트")
    class sut_remove_test {

        @Test
        @DisplayName("사용자가 자신이 등록한 게시글을 삭제한다.")
        void it_correctly_work_when_user_remove_some_board() {

            Integer bno = 1;
            when(boardDao.delete(bno)).thenReturn(1);
            doNothing().when(boardStatusService).removeByBno(bno);
            doNothing().when(boardChangeHistoryService).removeByBno(bno);
            doNothing().when(commentService).removeByBno(bno);

            assertDoesNotThrow(() -> sut.remove(bno));
        }



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