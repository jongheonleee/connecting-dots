//package com.example.demo.repository.mybatis.unit;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.example.demo.dto.ord_board.BoardFormDto;
//import com.example.demo.dto.ord_comment.CommentRequestDto;
//import com.example.demo.dto.ord_comment.CommentResponseDto;
//import com.example.demo.repository.mybatis.board.BoardDaoImpl;
//import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataIntegrityViolationException;
//
//@SpringBootTest
//class CommentDaoImplTest {
//
//    @Autowired
//    private CommentDaoImpl target;
//
//    @Autowired
//    private BoardDaoImpl boardDao;
//
//    private BoardFormDto boardFormDto = new BoardFormDto();
//    private List<CommentRequestDto> requestDummy = new ArrayList<>();
//    private List<CommentResponseDto> responseDummy = new ArrayList<>();
//
//    @BeforeEach
//    void setUp() {
//        assertNotNull(target);
//        assertNotNull(boardDao);
//
//        requestDummy.clear();
//        responseDummy.clear();
//        target.deleteAll();
//
//        createBoardFormDto();
//        assertTrue(1 == boardDao.insert(boardFormDto));
//    }
//
//
//    /**
//     *
//     * 0. 카운팅
//     * - 0. 예외 - x
//     * - 1. 실패 - x
//     * - 2. 성공
//     *  - 2-0. 여러건 등록하고 전체 카운팅
//     *
//     * 1. 등록
//     * - 0. 예외
//     *  - 0-0. 게시글 번호가 없는 경우 -> DataIntegrityViolationException
//     *  - 0-1. 작성자가 없는 경우 -> DataIntegrityViolationException
//     *  - 0-2. 내용이 없는 경우 -> DataIntegrityViolationException
//     *  - 0-3. 내용이 500자 초과인 경우 -> DataIntegrityViolationException
//     *
//     * - 1. 실패 - x
//     *
//     * - 2. 성공
//     *  - 2-0. 특정 게시글에 댓글 여러개 등록하기
//     *
//     * 2. 특정 게시글과 과련된 댓글 조회
//     * - 0. 예외 - x
//     *
//     * - 1. 실패
//     *  - 1-0. 게시글 번호가 없는 경우 -> 조회 x
//     *
//     * - 2. 성공
//     *  - 2-0. 특정 게시글에 댓글 여러개 등록하고 전체 조회해보기
//     *
//     * 3. 특정 댓글 조회
//     * - 0. 예외 - x
//     *
//     * - 1. 실패
//     *  - 1-0. 댓글 번호가 없는 경우 -> 조회 x
//     *
//     * - 2. 성공
//     *  - 2-0. 특정 게시글에 댓글 여러개 등록하고 각각 댓글 조회해보기
//     *  - 2-1. 특정 게시글에 댓글 여러개 등록하고 랜덤으로 댓글 조회해보기
//     *
//     * 4. 전체 조회
//     * - 0. 예외 - x
//     * - 1. 실패 - x
//     * - 2. 성공
//     * - 2-0. 특정 게시글에 댓글 여러개 등록하고 전체 조회해보기
//     *
//     * 5. 댓글 수정
//     * - 0. 예외
//     *  - 0-0. 댓글에 필수값이 누락된 경우 -> DataIntegrityViolationException
//     *  - 0-1. 댓글 필드 데이터 제약 조건을 위반한 경우 -> DataIntegrityViolationException
//     *
//     * - 1. 실패
//     *  - 1-0. 댓글 번호가 없는 경우 -> 실패
//     *
//     * - 2. 성공
//     *  - 2-0. 여러개의 댓글을 일일이 수정해보고 테스트
//     *  - 2-1. 여러개의 댓글 중에 특정 댓글만 수정해보고 테스트
//     *
//     * 6. 좋아요 증가
//     * - 0. 예외 - x
//     * - 1. 실패
//     *  - 1-0. 댓글 번호가 없는 경우
//     *
//     * - 2. 성공
//     *  - 2-0. 특정 댓글에 좋아요를 여러번 눌러본 경우
//     *  - 2-1. 여러개의 댓글에 좋아요를 일일이 눌러본 경우
//     *
//     * 7. 싫어요 증가
//     * - 0. 예외 - x
//     * - 1. 실패
//     *  - 1-0. 댓글 번호가 없는 경우
//     *
//     * - 2. 성공
//     *  - 2-0. 특정 댓글에 싫어요를 여러번 눌러본 경우
//     *  - 2-1. 여러개의 댓글에 싫어요를 일일이 눌러본 경우
//     *
//     * 8. 특정 게시글과 관련된 댓글 전체 삭제
//     * - 0. 예외 - x
//     * - 1. 실패
//     *  - 1-0. 게시글 번호가 없는 경우
//     *
//     * - 2. 성공
//     *  - 2-0. 특정 게시글에 댓글 여러개 등록하고 전체 삭제
//     *
//     * 9. 특정 댓글 삭제
//     * - 0. 예외 - x
//     * - 1. 실패
//     *  - 1-0. 댓글 번호가 없는 경우
//     * - 2. 성공
//     *  - 2-0. 여러개의 댓글을 일일이 삭제해보고 테스트
//     *  - 2-1. 여러개의 댓글 중에 특정 댓글만 삭제해보고 테스트
//     *
//     * 10. 전체 삭제
//     * - 0. 예외 - x
//     * - 1. 실패 - x
//     * - 2. 성공
//     *  - 2-0. 여러개의 댓글을 전체 삭제해보고 테스트
//     *
//     */
//
//    @DisplayName("2-2-0. 여러건 등록하고 전체 카운팅")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test(int cnt) {
//        /**
//         * cnt 개수 만큼 픽스처에 dto를 생성함
//         * 생성된 dto를 일일이 insert함. 총 cnt 개수 만큼 insert됨
//         * count()를 호출했을 때 cnt가 리턴되는지 확인
//         */
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//        assertTrue(cnt == target.count());
//    }
//
//    @DisplayName("1-0-0. 게시글 번호가 없는 경우 -> DataIntegrityViolationException")
//    @Test
//    public void test1() {
//        var commentDto = createCommentDto(0, 0);
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.insert(commentDto)
//        );
//    }
//
//    @DisplayName("1-0-1. 작성자가 없는 경우 -> DataIntegrityViolationException")
//    @Test
//    public void test2() {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 0);
//        commentDto.setWriter(null);
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.insert(commentDto)
//        );
//    }
//
//    @DisplayName("1-0-2. 내용이 없는 경우 -> DataIntegrityViolationException")
//    @Test
//    public void test3() {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 0);
//        commentDto.setComment(null);
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.insert(commentDto)
//        );
//    }
//
//
//    @DisplayName("1-0-3. 내용이 500자 초과인 경우 -> DataIntegrityViolationException")
//    @Test
//    public void test5() {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 0);
//        commentDto.setComment("a".repeat(501));
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.insert(commentDto)
//        );
//    }
//
//    @DisplayName("2-0-0. 게시글에 댓글 여러개 등록하기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test6(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        createResponseDtoOnResponseDummy(boardFormDto.getBno(), cnt);
//
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundComments = target.selectByBno(boardFormDto.getBno());
//        assertTrue(cnt == foundComments.size());
//
//        sort(responseDummy, foundComments);
//        for (int i=0; i<cnt; i++) {
//            assertTrue(isSameCommentDto(responseDummy.get(i), foundComments.get(i)));
//        }
//    }
//
//    @DisplayName("2-1-0. 게시글 번호가 없는 경우 -> 조회 x")
//    @Test
//    public void test7() {
//        List<CommentResponseDto> foundComments = target.selectByBno(0);
//        assertTrue(foundComments.isEmpty());
//    }
//
//    @DisplayName("2-2-0. 특정 게시글에 댓글 여러개 등록하고 전체 조회해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test8(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        createResponseDtoOnResponseDummy(boardFormDto.getBno(), cnt);
//
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundComments = target.selectByBno(boardFormDto.getBno());
//        assertTrue(cnt == foundComments.size());
//
//        sort(responseDummy, foundComments);
//        for (int i=0; i<cnt; i++) {
//            assertTrue(isSameCommentDto(responseDummy.get(i), foundComments.get(i)));
//        }
//    }
//
//    @DisplayName("3-1-0. 댓글 번호가 없는 경우 -> 조회 x")
//    @Test
//    public void test9() {
//        CommentResponseDto foundComment = target.selectByCno(0);
//        assertNull(foundComment);
//    }
//
//    @DisplayName("3-2-0. 특정 게시글에 댓글 여러개 등록하고 각각 댓글 조회해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test10(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundComments = target.selectByBno(boardFormDto.getBno());
//        assertTrue(cnt == foundComments.size());
//
//        for (var foundComment : foundComments) {
//            var commentDto = target.selectByCno(foundComment.getCno());
//            assertTrue(isSameCommentDto(foundComment, commentDto));
//        }
//
//    }
//
//
//    @DisplayName("3-2-1. 특정 게시글에 댓글 여러개 등록하고 랜덤으로 댓글 조회해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test11(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        createResponseDtoOnResponseDummy(boardFormDto.getBno(), cnt);
//
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundComments = target.selectByBno(boardFormDto.getBno());
//        assertTrue(cnt == foundComments.size());
//
//        sort(responseDummy, foundComments);
//        int randomIdx = chooseRandom(cnt);
//        assertTrue(isSameCommentDto(responseDummy.get(randomIdx), foundComments.get(randomIdx)));
//    }
//
//    @DisplayName("4-2-0. 특정 게시글에 댓글 여러개 등록하고 전체 조회해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test12(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        createResponseDtoOnResponseDummy(boardFormDto.getBno(), cnt);
//
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundComments = target.selectAll();
//        assertTrue(cnt == foundComments.size());
//
//        sort(responseDummy, foundComments);
//        for (int i=0; i<cnt; i++) {
//            assertTrue(isSameCommentDto(responseDummy.get(i), foundComments.get(i)));
//        }
//    }
//
//    @DisplayName("5-0-0. 댓글에 필수값이 누락된 경우 -> DataIntegrityViolationException")
//    @Test
//    public void test13() {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 1);
//        assertTrue(1 == target.insert(commentDto));
//
//        commentDto.setComment(null);
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.update(commentDto)
//        );
//    }
//
//    @DisplayName("5-0-1. 댓글 필드 데이터 제약 조건을 위반한 경우 -> DataIntegrityViolationException")
//    @Test
//    public void test14() {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 1);
//        assertTrue(1 == target.insert(commentDto));
//
//        commentDto.setComment("a".repeat(501));
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.update(commentDto)
//        );
//    }
//
//    @DisplayName("5-1-0. 댓글 번호가 없는 경우 -> 실패")
//    @Test
//    public void test15() {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 1);
//        assertTrue(1 == target.insert(commentDto));
//
//        commentDto.setCno(-19);
//        assertTrue(0 == target.update(commentDto)
//        );
//    }
//
//    @DisplayName("5-2-0. 여러개의 댓글을 일일이 수정해보고 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test16(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundComments = target.selectAll();
//        for (var foundComment : foundComments) {
//            CommentRequestDto updateDto = createCommentDtoForUpdate(foundComment.getBno(), foundComment.getCno());
//            assertTrue(1 == target.update(updateDto));
//        }
//    }
//
//    @DisplayName("5-2-1. 여러개의 댓글 중에 특정 댓글만 수정해보고 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test17(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundComments = target.selectAll();
//        int randomIdx = chooseRandom(cnt);
//        var selectedDto = foundComments.get(randomIdx);
//
//        CommentRequestDto updateDto = createCommentDtoForUpdate(selectedDto.getBno(), selectedDto.getCno());
//        assertTrue(1 == target.update(updateDto));
//    }
//
//    @DisplayName("6-1-0. 댓글 번호가 없는 경우")
//    @Test
//    public void test18() {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 1);
//        assertTrue(0 == target.increaseLikeCnt(commentDto.getCno()));
//    }
//
//    @DisplayName("6-2-0. 특정 댓글에 좋아요를 여러번 눌러본 경우")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test19(int cnt) {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 1);
//        assertTrue(1 == target.insert(commentDto));
//
//        for (int i=0; i<cnt; i++) {
//            assertTrue(1 == target.increaseLikeCnt(commentDto.getCno()));
//        }
//
//        var foundComment = target.selectByCno(commentDto.getCno());
//        assertTrue(cnt == foundComment.getLike_cnt());
//    }
//
//    @DisplayName("6-2-1. 여러개의 댓글에 좋아요를 일일이 눌러본 경우")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test20(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundCommentResponseDtos = target.selectAll();
//        for (var commentDto : foundCommentResponseDtos) {
//            assertTrue(1 == target.increaseLikeCnt(commentDto.getCno()));
//        }
//
//        foundCommentResponseDtos = target.selectAll();
//        for (var foundCommentDto : foundCommentResponseDtos) {
//            assertTrue(1 == foundCommentDto.getLike_cnt());
//        }
//
//    }
//
//    @DisplayName("7-1-0. 댓글 번호가 없는 경우")
//    @Test
//    public void test21() {
//        assertTrue(0 == target.increaseDislikeCnt(0));
//    }
//
//    @DisplayName("7-2-0. 특정 댓글에 싫어요를 여러번 눌러본 경우")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test22(int cnt) {
//        var commentDto = createCommentDto(boardFormDto.getBno(), 1);
//        assertTrue(1 == target.insert(commentDto));
//
//        for (int i=0; i<cnt; i++) {
//            assertTrue(1 == target.increaseDislikeCnt(commentDto.getCno()));
//        }
//
//        var foundCommentDto = target.selectByCno(commentDto.getCno());
//        assertTrue(cnt == foundCommentDto.getDislike_cnt());
//    }
//
//    @DisplayName("7-2-1. 여러개의 댓글에 싫어요를 일일이 눌러본 경우")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test23(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundCommentResponseDtos = target.selectAll();
//        for (var commentDto : foundCommentResponseDtos) {
//            assertTrue(1 == target.increaseDislikeCnt(commentDto.getCno()));
//        }
//
//        foundCommentResponseDtos = target.selectAll();
//        for (var foundCommentDto : foundCommentResponseDtos) {
//            assertTrue(1 == foundCommentDto.getDislike_cnt());
//        }
//    }
//
//    @DisplayName("8-1-0. 게시글 번호가 없는 경우")
//    @Test
//    public void test24() {
//        assertTrue(0 == target.deleteByBno(-10));
//    }
//
//    @DisplayName("8-2-0. 특정 게시글에 댓글 여러개 등록하고 전체 삭제")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test25(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        assertTrue(cnt == target.deleteByBno(boardFormDto.getBno()));
//    }
//
//    @DisplayName("9-1-0. 댓글 번호가 없는 경우")
//    @Test
//    public void test26() {
//        assertTrue(0 == target.deleteByCno(-10));
//    }
//
//    @DisplayName("9-2-0. 여러개의 댓글을 일일이 삭제해보고 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test27(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        List<CommentResponseDto> foundComments = target.selectAll();
//        for (var foundComment : foundComments) {
//            assertTrue(1 == target.deleteByCno(foundComment.getCno()));
//        }
//
//        assertTrue(0 == target.count());
//    }
//
//    @DisplayName("9-2-1. 여러개의 댓글 중에 특정 댓글만 삭제해보고 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test28(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        int randomIdx = chooseRandom(cnt);
//        var selectedDto = requestDummy.get(randomIdx);
//
//        assertTrue(1 == target.deleteByCno(selectedDto.getCno()));
//        assertTrue(cnt - 1 == target.count());
//        assertNull(target.selectByCno(selectedDto.getCno()));
//    }
//
//    @DisplayName("10-2-0. 여러개의 댓글을 전체 삭제해보고 테스트")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 5, 15, 20})
//    public void test29(int cnt) {
//        createRequestDtoOnRequestDummy(boardFormDto.getBno(), cnt);
//        for (var commentDto : requestDummy) {
//            assertTrue(1 == target.insert(commentDto));
//        }
//
//        assertTrue(cnt == target.deleteAll());
//
//    }
//
//
//
//
//    private CommentRequestDto createCommentDto(int bno, int i) {
//        CommentRequestDto commentRequestDto = new CommentRequestDto();
//        commentRequestDto.setBno(bno);
//        commentRequestDto.setWriter("writer" + i);
//        commentRequestDto.setComment("comment" + i);
//        return commentRequestDto;
//    }
//
//    private CommentRequestDto createCommentDtoForUpdate(int bno, int cno) {
//        CommentRequestDto commentRequestDto = new CommentRequestDto();
//        commentRequestDto.setCno(cno);
//        commentRequestDto.setBno(bno);
//        commentRequestDto.setWriter("new writer");
//        commentRequestDto.setComment("new comment");
//        return commentRequestDto;
//    }
//
//
//
//    private void createRequestDtoOnRequestDummy(int bno, int cnt) {
//        for (int i = 0; i < cnt; i++) {
//            var commentDto = createCommentDto(bno, i);
//            requestDummy.add(commentDto);
//        }
//    }
//
//    private void createResponseDtoOnResponseDummy(int bno, int cnt) {
//        for (int i = 0; i < cnt; i++) {
//            var commentDto = createCommentDto(bno, i);
//            responseDummy.add(commentDto.createCommentDto());
//        }
//    }
//
//
//    private void createBoardFormDto() {
//        boardFormDto = new BoardFormDto();
//        boardFormDto.setCate_code("0101");
//        boardFormDto.setId("id");
//        boardFormDto.setTitle("title");
//        boardFormDto.setWriter("writer");
//        boardFormDto.setView_cnt(0);
//        boardFormDto.setReco_cnt(0);
//        boardFormDto.setNot_reco_cnt(0);
//        boardFormDto.setContent("content");
//        boardFormDto.setComt("comt");
//        boardFormDto.setReg_date("2021-01-01");
//        boardFormDto.setReg_id("reg_id");
//        boardFormDto.setUp_date("2021-01-01");
//        boardFormDto.setUp_id("up_id");
//
//    }
//
//    private boolean isSameCommentDto(CommentResponseDto dto1, CommentResponseDto dto2) {
//        return dto1.getBno().equals(dto2.getBno())
//                && dto1.getWriter().equals(dto2.getWriter())
//                && dto1.getContent().equals(dto2.getContent());
//    }
//
//    private void sort(List<CommentResponseDto> list1, List<CommentResponseDto> list2) {
//        list1.sort((o1, o2) -> o1.getWriter().compareTo(o2.getWriter()));
//        list2.sort((o1, o2) -> o1.getWriter().compareTo(o2.getWriter()));
//    }
//
//    private int chooseRandom(int size) {
//        return (int) (Math.random() * size);
//    }
//}