package com.example.demo.service.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardStatusResponse;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 현재 문제점
 * - 1. 테스트 실행 시간이 매우 김
 * - 2. 자바 리플랙션을 통해서 MAX_RETRY, RETRY_DELAY 필드를 변경했지만, 적용되지 않음
 * - 3. 아마 스프링 컨테이너에서 빈으로 등록된 클래스의 필드는 리플랙션을 통해 변경이 불가능한 것으로 추정
 *
 */

@SpringBootTest
class BoardServiceCreateRetryTest {

    private final Integer MAX_RETRY_FOR_TEST = 10; // 추후에 3으로 변경
    private final Integer RETRY_DELAY_FOR_TEST = 5_000; // 추후에 1_000으로 변경


    @Autowired
    private BoardService sut;

    @MockBean
    private BoardDaoImpl boardDao;


    @MockBean
    private BoardImgService boardImgService;

    @MockBean
    private BoardStatusService boardStatusService;

    @MockBean
    private BoardChangeHistoryService boardChangeHistoryService;

    @MockBean
    private CustomFormatter formatter;

    @MockBean
    private BoardCategoryDaoImpl boardCategoryDaoImpl;


//    @BeforeEach
//    @DisplayName("MAX_RETRY, RETRY_DELAY 필드 수정 - 테스트 환경에서는 더 작은 값으로 구성")
//    void setUp() throws Exception {
//        // 추후에 리플랙션 api 활용할 계획
//        // 테스트 환경에서는 더 작은 값으로 세팅하여 테스트 진행 시간 단축시키기
//        // Unsafe 인스턴스 가져오기
//        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
//        unsafeField.setAccessible(true);
//        Unsafe unsafe = (Unsafe) unsafeField.get(null);
//
//        // MAX_RETRY 변경
//        Field maxRetryField = BoardServiceImpl.class.getDeclaredField("MAX_RETRY");
//        long maxRetryFieldOffset = unsafe.staticFieldOffset(maxRetryField);
//        unsafe.putInt(BoardServiceImpl.class, maxRetryFieldOffset, MAX_RETRY_FOR_TEST);
//
//        // RETRY_DELAY 변경
//        Field retryDelayField = BoardServiceImpl.class.getDeclaredField("RETRY_DELAY");
//        long retryDelayFieldOffset = unsafe.staticFieldOffset(retryDelayField);
//        unsafe.putInt(BoardServiceImpl.class, retryDelayFieldOffset, RETRY_DELAY_FOR_TEST);
//
//    }

    @Nested
    @DisplayName("재시도 복구 처리 적용되는 경우")
    class RetryTest {
        @Test
        @DisplayName("게시글 등록 과정에서 게시글 등록이 DBMS 적용이 되지 않았을 때, 10번 재시도함")
        void 게시글_등록_실패시_재시도_처리() {
            when(formatter.getCurrentDateFormat()).thenReturn("2025-01-15 23:59:59");
            when(formatter.getManagerSeq()).thenReturn(1);

            BoardRequest request = createBoardRequest();
            List<MultipartFile> files = createMultipartFiles();

            when(boardDao.insert(any(BoardDto.class))).thenReturn(0);

            assertThrows(ExhaustedRetryException.class, () -> sut.create(request, files));
            verify(boardDao, Mockito.times(MAX_RETRY_FOR_TEST)).insert(any(BoardDto.class));

        }

        @Test
        @DisplayName("게시글 등록 과정에서 이미지 등록이 DBMS 적용이 되지 않았을 때, 10번 재시도함")
        void 이미지_등록_실패시_재시도_처리() {
            when(formatter.getCurrentDateFormat()).thenReturn("2025-01-15 23:59:59");
            when(formatter.getManagerSeq()).thenReturn(1);

            BoardRequest request = createBoardRequest();
            List<MultipartFile> files = createMultipartFiles();

            // 게시글 정상 등록
            when(boardDao.insert(any(BoardDto.class))).thenReturn(1);
            doThrow(NotApplyOnDbmsException.class).when(boardImgService).saveBoardImage(any(), any());

            assertThrows(ExhaustedRetryException.class, () -> sut.create(request, files));
            verify(boardImgService, Mockito.times(MAX_RETRY_FOR_TEST)).saveBoardImage(any(), any());

        }

        @Test
        @DisplayName("게시글 등록 과정에서 게시글 상태 등록이 DBMS 적용이 되지 않았을 때, 10번 재시도함")
        void 게시글_상태_등록_실패시_재시도_처리() {
            when(formatter.getCurrentDateFormat()).thenReturn("2025-01-15 23:59:59");
            when(formatter.getManagerSeq()).thenReturn(1);

            BoardRequest request = createBoardRequest();
            List<MultipartFile> files = createMultipartFiles();

            // 게시글 정상 등록
            when(boardDao.insert(any(BoardDto.class))).thenReturn(1);
            doNothing().when(boardImgService).saveBoardImage(any(), any());
            doThrow(NotApplyOnDbmsException.class).when(boardStatusService).create(any());

            assertThrows(ExhaustedRetryException.class, () -> sut.create(request, files));
            verify(boardStatusService, Mockito.times(MAX_RETRY_FOR_TEST)).create(any());
        }

        @Test
        @DisplayName("게시글 등록 과정에서 게시글 변경 이력 등록이 DBMS 적용이 되지 않았을 때, 10번 재시도함")
        void 게시글_변경_이력_등록_실패시_재시도_처리() {
            when(formatter.getCurrentDateFormat()).thenReturn("2025-01-15 23:59:59");
            when(formatter.getManagerSeq()).thenReturn(1);

            BoardRequest request = createBoardRequest();
            List<MultipartFile> files = createMultipartFiles();

            when(boardDao.insert(any(BoardDto.class))).thenReturn(1);
            doNothing().when(boardImgService).saveBoardImage(any(), any());
            when(boardStatusService.create(any())).thenReturn(new BoardStatusResponse());  // 구체적인 객체 반환
            doThrow(NotApplyOnDbmsException.class).when(boardChangeHistoryService).createInit(any(), any());

            assertThrows(ExhaustedRetryException.class, () -> sut.create(request, files));
            verify(boardChangeHistoryService, Mockito.times(MAX_RETRY_FOR_TEST)).createInit(any(), any());
        }
    }

    @Nested
    @DisplayName("재시도 복구 처리가 되지 않는 경우")
    class NotRetryTest {
        /**
         *  BusinessException.class, InvalidValueException.class, DataIntegrityViolationException.class,
         *  SQLSyntaxErrorException.class, InvalidDataAccessApiUsageException.class, InvalidDataAccessResourceUsageException.class,
         *  EmptyResultDataAccessException.class, TypeMismatchDataAccessException.class
         *
         *  -> 위의 에러에 대해선 재시도 복구 처리 적용 안됨
         */

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

    private List<MultipartFile> createMultipartFiles() {
        return List.of(
                new MockMultipartFile("test1.jpeg", "test2.jpeg", "image/jpeg", "test3.jpeg".getBytes()),
                new MockMultipartFile("test2.jpeg", "test2.jpeg", "image/jpeg", "test2.jpeg".getBytes()),
                new MockMultipartFile("test3.jpeg", "test3.jpeg", "image/jpeg", "test3.jpeg".getBytes())
        );
    }

}