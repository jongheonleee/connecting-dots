# Connecting Dots ✨

### 📢 프로젝트 과정에서의 주요 학습 포인트 
> - 이 프로젝트를 통해 학습한 부분은 크게 5가지가 있습니다.
> - 1. TDD 개발을 통해 목적지향적으로 코딩을 할 수 있다. 
> - 2. RDB 대규모 설계시 테스트 작성의 복잡도 증가하게 되며 이를 적절히 대처해야한다.
> - 3. 서비스 정책을 RDB에 적절히 쪼개서 테이블로 정의하면 동적으로 생성할 수 있다.
> - 4. 단위, 통합, 학습 테스트 작성
> - 5. 성능 개선을 위한 서브쿼리 해싱, 인라인 뷰(Inline View), 인덱싱  
<br>

### JUnit5·Mockito 기반 TDD 개발, 46,700 라인 작성 및 2,120개 테스트 수행 
<img width="943" alt="KakaoTalk_Photo_2025-05-24-18-35-29" src="https://github.com/user-attachments/assets/b0fe738c-7128-4dbb-aa1a-66a277af8e6c" />
<img width="1267" alt="KakaoTalk_Photo_2025-05-24-18-35-42" src="https://github.com/user-attachments/assets/1ad334bd-9454-4837-b40c-b8bb129ba532" />

> - 이 과정을 통해서 배운 것은 TDD 사이클에 익숙해지는 것과 효율적인 개발 방법입니다.
> - TDD는 사이클은 크게 기능 구현 단계와 리팩토링 단계로 분류할 수 있습니다.
>   - 1. 테스트 코드 추가
>   - 2. 테스트 실패 확인
>   - 3. 기능 구현
>   - 4. 테스트 성공 확인
>   - 5. 리팩토링
> - 즉, 1~4번까지는 기능 구현 단계이고 5번부터는 리팩토링 단계입니다.
> - 제가 TDD를 활용하기 전에는 기능 구현을 빨리해야 할 시점에 코드 스타일이나 구조를 개선 시키려는 실수를 한적이 많습니다.
> - 하지만, TDD를 통해서 현재 시점에 나한테 정말 중요한 작업은 무엇인지 생각할 수 있게 되었고 그에 따라 효율적이며 목적지향적으로 코딩을 할 수 있었습니다.

### 단위·통합·학습 테스트 활용하여 포괄적으로 테스트 코드 다루기 
> - 테스트 코드를 단순하게 기능이 정상 작동하는지 검증하기 위한 용도로 사용하지 않았습니다.
> - 내가 사용하고자 하는 기술을 실제로 적용해보고 테스트 해봄으로써 더 깊이 있는 학습을 진행할 수 있게 "학습 테스트" 방식을 사용했습니다.
> - 또한, 특정 기능에 대해서 빠르게 피드백을 받을 수 있는 "단위 테스트"를 적극 활용 했습니다.
> - 하지만, 단위 테스트로만 애플리케이션을 테스트할 경우 해당 애플리케이션의 신뢰성이 떨어진다고 생각합니다.
> - 그 이유는, OOP 특성상 기능 단위로 오브젝트를 정의하고 각 오브젝트끼리 소통하며 구동되기 때문에 전체 시스템을 운영할 때 정상 작동하는지도 테스트해야합니다.
> - 이를 위해서 "통합 테스트" 방식을 적용했습니다.

#### 1. 단위 테스트 
```Java
@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @InjectMocks
    private BoardServiceImpl sut;

    @Mock
    private BoardDaoImpl boardDaoImpl;

    @Mock
    private BoardCategoryDaoImpl boardCategoryDaoImpl;

    @Mock
    private ReplyServiceImpl replyServiceImpl;

    @Mock
    private BoardCategoryServiceImpl boardCategoryServiceImpl;

    @Mock
    private BoardStatusService boardStatusServiceImpl;

    @Mock
    private CommentServiceImpl commentServiceImpl;

    @Mock
    private CommonCodeServiceImpl commonCodeServiceImpl;

    @Mock
    private BoardImgServiceImpl boardImgServiceImpl;

    @Mock
    private BoardChangeHistoryServiceImpl boardChangeHistoryServiceImpl;

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
        assertNotNull(boardDaoImpl);
        assertNotNull(boardCategoryServiceImpl);
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
            when(boardDaoImpl.count()).thenReturn(expected);
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
            when(boardDaoImpl.count()).thenReturn(expected);
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

            when(boardDaoImpl.insert(any())).thenReturn(1);

            for (int i=0; i<files.size(); i++) {
                doNothing().when(boardImgServiceImpl).saveBoardImage(any(), eq(files.get(i)));
            }


            BoardStatusRequest boardStatusRequest = createBoardStatusRequestRequest();
            BoardStatusResponse boardStatusResponse = createBoardStatusRequestResponse(boardStatusRequest);
            when(boardStatusServiceImpl.create(any())).thenReturn(boardStatusResponse);

            BoardChangeHistoryRequest boardChangeHistoryRequest = createBoardChangeHistoryRequest();
            BoardChangeHistoryResponse boardChangeHistoryResponse = createBoardChangeHistoryResponse(boardChangeHistoryRequest);
            when(boardChangeHistoryServiceImpl.createInit(any(), any())).thenReturn(boardChangeHistoryResponse);

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

            when(boardDaoImpl.count()).thenReturn(20);
            when(boardDaoImpl.selectForMain(map)).thenReturn(expected);

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


            when(boardCategoryDaoImpl.existsByCateCode(cate_code)).thenReturn(true);
            when(boardDaoImpl.countByCategory(cate_code)).thenReturn(20);
            when(boardDaoImpl.selectForMainByCategory(map)).thenReturn(expected);

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

            when(boardDaoImpl.countBySearchCondition(sc)).thenReturn(20);
            when(boardDaoImpl.selectForMainBySearchCondition(sc)).thenReturn(expected);

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
            List<CommentDetailResponse> foundComments = commentServiceImpl.readByBno(bno);

            when(boardDaoImpl.existsByBno(bno)).thenReturn(true);
            when(boardStatusServiceImpl.readByBnoAtPresent(bno)).thenReturn(currStatus);
            when(boardDaoImpl.select(bno)).thenReturn(found);
            when(boardCategoryServiceImpl.readByCateCode(found.getCate_code())).thenReturn(foundBoardCategory);
            when(boardImgServiceImpl.readByBno(bno)).thenReturn(foundBoardImages);
            when(commentServiceImpl.readByBno(bno)).thenReturn(foundComments);

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

            when(boardDaoImpl.existsByBnoForUpdate(request.getBno())).thenReturn(true);
            when(boardDaoImpl.update(boardDto)).thenReturn(1);
            when(boardImgServiceImpl.readByBno(request.getBno())).thenReturn(new ArrayList<>());
            for (int i=0; i<files.size(); i++) {
                doNothing().when(boardImgServiceImpl).saveBoardImage(any(), eq(files.get(i)));
            }
            doNothing().when(boardStatusServiceImpl).renewState(any());

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
            when(boardDaoImpl.delete(bno)).thenReturn(1);
            doNothing().when(boardStatusServiceImpl).removeByBno(bno);
            doNothing().when(boardChangeHistoryServiceImpl).removeByBno(bno);
            doNothing().when(commentServiceImpl).removeByBno(bno);

            assertDoesNotThrow(() -> sut.remove(bno));
        }



    }

}
```

#### 2. 통합 테스트 
```Java
@WithMockUser
@WebMvcTest(BoardCategoryController.class)
class BoardCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardCategoryServiceImpl boardCategoryServiceImpl;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(boardCategoryServiceImpl);
    }

    @Nested
    @DisplayName("GET /api/board-category/{cate_code}")
    class GetBoardCategory {

        @Test
        @DisplayName("존재하는 코드로 상세 조회 성공")
        void 존재하는_코드로_상세_조회_성공() throws Exception {
            given(boardCategoryServiceImpl.readByCateCode(any()))
                    .willReturn(createResponse(1));

            mockMvc.perform(get("/api/board-category/AB010101"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.cate_code").value("AB010101"),
                            jsonPath("$.top_cate").value("AB010100"),
                            jsonPath("$.name").value("테스트용 카테고리1"),
                            jsonPath("$.comt").value("..."),
                            jsonPath("$.ord").value(2),
                            jsonPath("$.chk_use").value("Y"),
                            jsonPath("$.level").value(2)
                 );
        }

        @Test
        @DisplayName("존재하지 않는 코드로 상세 조회 실패")
        void 존재하지_않는_코드로_상세_조회_실패() throws Exception {
            given(boardCategoryServiceImpl.readByCateCode(any()))
                    .willThrow(new BoardCategoryNotFoundException());

            mockMvc.perform(get("/api/board-category/AB010101"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/board-category/top/{top_cate}")
    class GetBoardCategoryList {

        @Test
        @DisplayName("존재하는 코드로 리스트 조회 성공")
        void 존재하는_코드로_리스트_조회_성공() throws Exception {
            given(boardCategoryServiceImpl.readByTopCate(any()))
                    .willReturn(List.of(createResponse(1), createResponse(2)));

            mockMvc.perform(get("/api/board-category/top/AB010100"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$[0].cate_code").value("AB010101"),
                            jsonPath("$[0].top_cate").value("AB010100"),
                            jsonPath("$[0].name").value("테스트용 카테고리1"),
                            jsonPath("$[0].comt").value("..."),
                            jsonPath("$[0].ord").value(2),
                            jsonPath("$[0].chk_use").value("Y"),
                            jsonPath("$[0].level").value(2),
                            jsonPath("$[1].cate_code").value("AB010102"),
                            jsonPath("$[1].top_cate").value("AB010100"),
                            jsonPath("$[1].name").value("테스트용 카테고리2"),
                            jsonPath("$[1].comt").value("..."),
                            jsonPath("$[1].ord").value(3),
                            jsonPath("$[1].chk_use").value("Y"),
                            jsonPath("$[1].level").value(3)
                    );
        }

        @Test
        @DisplayName("존재하지 않는 코드로 리스트 조회 실패")
        void 존재하지_않는_코드로_리스트_조회_실패() throws Exception {
            given(boardCategoryServiceImpl.readByTopCate(any()))
                    .willThrow(new BoardCategoryNotFoundException());

            mockMvc.perform(get("/api/board-category/top/AB010100"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/board-category/all")
    class GetAllBoardCategory {

        @Test
        @DisplayName("모든 카테고리 조회 성공")
        void 모든_카테고리_조회_성공() throws Exception {
            given(boardCategoryServiceImpl.readAll())
                    .willReturn(List.of(createResponse(1), createResponse(2)));

            mockMvc.perform(get("/api/board-category/all"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$[0].cate_code").value("AB010101"),
                            jsonPath("$[0].top_cate").value("AB010100"),
                            jsonPath("$[0].name").value("테스트용 카테고리1"),
                            jsonPath("$[0].comt").value("..."),
                            jsonPath("$[0].ord").value(2),
                            jsonPath("$[0].chk_use").value("Y"),
                            jsonPath("$[0].level").value(2),
                            jsonPath("$[1].cate_code").value("AB010102"),
                            jsonPath("$[1].top_cate").value("AB010100"),
                            jsonPath("$[1].name").value("테스트용 카테고리2"),
                            jsonPath("$[1].comt").value("..."),
                            jsonPath("$[1].ord").value(3),
                            jsonPath("$[1].chk_use").value("Y"),
                            jsonPath("$[1].level").value(3)
                    );
        }

        @Test
        @DisplayName("카테고리가 존재하지 않는 경우, 빈 리스트 반환")
        void 카테고리가_존재하지_않는_경우_빈_리스트_반환() throws Exception {
            given(boardCategoryServiceImpl.readAll())
                    .willReturn(List.of());

            mockMvc.perform(get("/api/board-category/all"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isEmpty()
                    );
        }
    }

    @Nested
    @DisplayName("POST /api/board-category/create")
    class PostBoardCategory {

        @Test
        @DisplayName("카테고리 생성 성공")
        void 카테고리_생성_성공() throws Exception {
            given(boardCategoryServiceImpl.create(any()))
                    .willReturn(createResponse(1));

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(post("/api/board-category/create")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.cate_code").value("AB010101"),
                            jsonPath("$.top_cate").value("AB010100"),
                            jsonPath("$.name").value("테스트용 카테고리1"),
                            jsonPath("$.comt").value("..."),
                            jsonPath("$.ord").value(2),
                            jsonPath("$.chk_use").value("Y"),
                            jsonPath("$.level").value(2)
                    );
        }

        @Test
        @DisplayName("전달받은 코드가 이미 존재하는 코드인 경우, 예외 발생 -> 409 CONFLICT")
        void 전달받은_코드가_이미_존재하는_코드인_경우_예외_발생() throws Exception {
            given(boardCategoryServiceImpl.create(any()))
                    .willThrow(new BoardCategoryAlreadyExistsException());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(post("/api/board-category/create")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("유효하지 않은 값들로 전달받은 경우, 예외 발생 -> 400 BAD REQUEST")
        void 유효하지_않은_값들로_전달받은_경우_예외_발생() throws Exception {
            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "!@33",
                            "top_cate", "2 ",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(post("/api/board-category/create")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("DBMS 반영 실패로 인한 예외 발생 -> 500 INTERNAL SERVER ERROR")
        void DBMS_반영_실패로_인한_예외_발생() throws Exception {
            given(boardCategoryServiceImpl.create(any()))
                    .willThrow(new NotApplyOnDbmsException());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(post("/api/board-category/create")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("PATCH /api/board-category/{cate_code}")
    class PatchBoardCategory {
        @Test
        @DisplayName("카테고리 수정 성공")
        void 카테고리_수정_성공() throws Exception {
            willDoNothing()
                    .given(boardCategoryServiceImpl)
                    .modify(any());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(put("/api/board-category/AB010101")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(boardCategoryServiceImpl).modify(any());
        }

        @Test
        @DisplayName("존재하지 않는 코드로 수정 시, 예외 발생 -> 404 NOT FOUND")
        void 존재하지_않는_코드로_수정_시_예외_발생() throws Exception {
            willThrow(new BoardCategoryNotFoundException())
                    .given(boardCategoryServiceImpl)
                    .modify(any());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(put("/api/board-category/AB010101")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DBMS 반영 실패로 인한 예외 발생 -> 500 INTERNAL SERVER ERROR")
        void DBMS_반영_실패로_인한_예외_발생() throws Exception {
            willThrow(new NotApplyOnDbmsException())
                    .given(boardCategoryServiceImpl)
                    .modify(any());

            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "AB010101",
                            "top_cate", "AB010100",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(put("/api/board-category/AB010101")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("유효하지 않은 값들로 전달받은 경우, 예외 발생 -> 400 BAD REQUEST")
        void 유효하지_않은_값들로_전달받은_경우_예외_발생() throws Exception {
            String body = objectMapper.writeValueAsString(
                    Map.of("cate_code", "!@33",
                            "top_cate", "2 ",
                            "name", "테스트용 카테고리1",
                            "comt", "...",
                            "ord", 2,
                            "chk_use", "Y",
                            "level", 2)
            );

            mockMvc.perform(put("/api/board-category/AB010101")
                            .with(csrf()) // CSRF 토큰 추가
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("PATCH /api/board-category/usey/{cate_code}")
    class PatchBoardCategoryUseY {

        @Test
        @DisplayName("사용 중인 카테고리로 변경 성공")
        void 사용_중인_카테고리로_변경_성공() throws Exception {
            willDoNothing()
                    .given(boardCategoryServiceImpl)
                    .modifyChkUseY(any());

            mockMvc.perform(patch("/api/board-category/usey/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(boardCategoryServiceImpl).modifyChkUseY(any());
        }

        @Test
        @DisplayName("존재하지 않는 코드로 사용 중인 카테고리 변경 시, 예외 발생 -> 404 NOT FOUND")
        void 존재하지_않는_코드로_사용_중인_카테고리_변경_시_예외_발생() throws Exception {
            willThrow(new BoardCategoryNotFoundException())
                    .given(boardCategoryServiceImpl)
                    .modifyChkUseY(any());

            mockMvc.perform(patch("/api/board-category/usey/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DBMS 반영 실패로 인한 예외 발생 -> 500 INTERNAL SERVER ERROR")
        void DBMS_반영_실패로_인한_예외_발생() throws Exception {
            willThrow(new NotApplyOnDbmsException())
                    .given(boardCategoryServiceImpl)
                    .modifyChkUseY(any());

            mockMvc.perform(patch("/api/board-category/usey/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("DELETE /api/board-category")
    class DeleteBoardCategory {

        @Test
        @DisplayName("코드로 카테고리 삭제 성공")
        void 코드로_카테고리_삭제_성공() throws Exception {
            willDoNothing()
                    .given(boardCategoryServiceImpl)
                    .remove(any());

            mockMvc.perform(delete("/api/board-category/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(boardCategoryServiceImpl).remove(any());
        }

        @Test
        @DisplayName("존재하지 않는 코드로 삭제 시, 예외 발생 -> 404 NOT FOUND")
        void 존재하지_않는_코드로_삭제_시_예외_발생() throws Exception {
            willThrow(new BoardCategoryNotFoundException())
                    .given(boardCategoryServiceImpl)
                    .remove(any());

            mockMvc.perform(delete("/api/board-category/AB010101")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("모든 카테고리 삭제 성공")
        void 모든_카테고리_삭제_성공() throws Exception {
            willDoNothing()
                    .given(boardCategoryServiceImpl)
                    .removeAll();

            mockMvc.perform(delete("/api/board-category/all")
                            .with(csrf())) // CSRF 토큰 추가
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(boardCategoryServiceImpl).removeAll();
        }
    }
}
```

#### 3. 학습 테스트 
```Java
@SpringBootTest
class UserDaoImplLearningTest {

    @Autowired
    private UserDaoImpl target;

    @BeforeEach
    public void setUp() {
        assertNotNull(target);
        target.deleteAll();
    }



//    회원 등록
//        -> 발생하는 예외 분석
//            - 1. not null 제약 조건 위배할 경우 - DataIntegrityViolationException
//            - 2. 중복된 아이디로 등록할 경우 - DuplicateKeyException
//            - 3. 데이터 길이 제한 초과할 경우 - DataIntegrityViolationException

    @Test
    @DisplayName("not null 제약 조건 위배할 경우 - DataIntegrityViolationException")
    public void learn1() {
        var dto = createDto();
        dto.setId(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(dto)
        );
    }

    @Test
    @DisplayName("중복된 아이디로 등록할 경우 - DuplicateKeyException")
    public void learn2() {
        var dto = createDto();
        target.insert(dto);
        assertThrows(DuplicateKeyException.class,
                () -> target.insert(dto)
        );
    }

    @Test
    @DisplayName("데이터 길이 제한 초과할 경우  - DataIntegrityViolationException")
    public void learn3() {
        var dto = createDto();
        dto.setName("Over fifty characters are required to store this data without truncation issues.");
        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(dto)
        );
    }

//    회원 조회
//        - 회원 조회시 예외가 발생할까? - 없음(null 반환)
//        - 여러건을 조회하는지 단건을 조회하는지에 따라 달라짐
//        - 단건 조회시 예외가 발생할까? id로 찾을 때 해당 값이 null이면 어떻할까?
//        - 여러건은 예외 발생 안할듯..
    @Test
    @DisplayName("단건 조회시 예외가 발생할까? id로 찾을 때 해당 값이 null이면 어떻게 할까?")
    public void learn4() {
        var dto = createDto();
        assertTrue(1 == target.insert(dto));
        var found = target.selectById(null);
        assertTrue(found == null);
    }

//    회원 삭제
//        -> 없는 아이디로 삭제할 경우 어떻게 될까? 적용된 로우수가 0이됨

    @DisplayName("2-1. 없는 아이디로 삭제하는 경우 삭제 실패")
    @Test
    public void test11() {
        String notExistId = "notExistId";
        int rowCnt = target.deleteById(notExistId);
    }

}
```

### Oracle 를 기점으로 성능 튜닝에 대해서 생각해봤습니다(실제 적용된 SQL은 MySQL) 
> - 쿼리를 튜닝한다고 가정했을 때, 가장 중요한 요소는 아래와 같습니다. 결국에는 I/O 발생을 최소화하여 병목지점을 제거하는 것이 핵심입니다.  
>   - 1. 인덱싱 튜닝 
>   - 2. 조인 튜닝
>   - 3. 소트 튜닝     
>   - 4. DML 튜닝
> - 여기서는 중점적으로 인덱스 튜닝과 조인 튜닝(서브 쿼리 캐싱)에 대해서 고민했습니다.
> - 인덱스를 탐색하는 과정은 크게 2가지로 구분할 수 있습니다. 수직 탐색과 수평 탐색입니다.
> - 수직 탐색과 수평 탐색을 각 각 O(N), O(M) 이라고 가정했을 때, M을 줄이는 것이 핵심이라고 생각합니다.
> - 따라서 적절히 복합 pk를 구성해서 테이블을 정의하고 인덱싱 탐색시 수평 탐색 범위를 최소화하려고 노력했습니다. 
> - 또한, 조인 튜닝을 할 때 전체 테이블을 조인 하지않고 인라인 뷰로 조인 처리하거나 서브 쿼리 캐싱을 활용하여 성능 개선하는 것이 중요하다고 생각합니다.

```sql
-- 1. 허용된 기간 내의 베스트 게시글 조회
SELECT
    b.bno                        AS bno,
    b.title                      AS title,
    b.writer                     AS writer,
    b.cate_code                  AS cate_code,
    b.reg_date                   AS reg_date,
    b.view_cnt                   AS view_cnt,
    b.reco_cnt                   AS reco_cnt,
   ( SELECT i.img
     FROM image AS i
     WHERE i.bno = b.bno
     AND i.thumb = 'Y')          AS thumb,
   ( SELECT COUNT(*)
     FROM comment AS c
     WHERE c.bno = b.bno)        AS comment_cnt
FROM board                       AS b
INNER JOIN (
    SELECT bno, appl_begin, appl_end, used
    FROM best_comment_board
    WHERE used = 'Y')            AS cb
ON        b.bno = cb.bno
WHERE     CURRENT_DATE BETWEEN cb.appl_begin AND cb.appl_end
ORDER BY  b.reg_date DESC, b.view_cnt DESC, comment_cnt DESC
LIMIT     #{offset}, #{pageSize};

-- 2. 게시글 조회
SELECT
      b.bno                    AS bno,
      b.title                  AS title,
      b.writer                 AS writer,
      (
        SELECT name
        FROM board_category AS bc
        WHERE b.cate_code = bc.cate_code
      )                        AS cate_name,
      b.reg_date               AS reg_date,
      b.view_cnt               AS view_cnt,
      b.reco_cnt               AS reco_cnt,
      (
        SELECT img
        FROM image AS i
        WHERE b.bno = i.bno
        AND i.thumb = 'Y'
      )                        AS thumb,
      (
        SELECT count(*)
        FROM comment AS c
        WHERE b.bno = c.bno
      )                        AS comment_cnt
FROM        board AS b
ORDER BY    reg_date DESC, view_cnt DESC, comment_cnt DESC
LIMIT       #{offset}, #{pageSize};

```

<br>



