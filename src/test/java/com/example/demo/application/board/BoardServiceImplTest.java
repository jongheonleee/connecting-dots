package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 추가해야 하는 것들이 있으므로 그거 먼저 구현하고 TDD 진행
 * - BoardImgDaoImpl boardImgDao; -> 게시판 생성, 조회시 이미지 처리
 * - BoardStatusDaoImpl boardStatusDao; -> 게시판 상태 변경시 적용
 * - BoardChangeHistoryDaoImpl boardChangeHistoryDao; -> 게시판 변경 이력 기록
 */
@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @InjectMocks
    private BoardServiceImpl boardService;

    @Mock
    private BoardDaoImpl boardDao;

    @Mock
    private BoardCategoryServiceImpl boardCategoryService;

    @Mock
    private CustomFormatter formatter;

    @BeforeEach
    void setUp() {
        assertNotNull(boardService);
        assertNotNull(boardDao);
        assertNotNull(boardCategoryService);
        assertNotNull(formatter);
    }

    @Nested
    @DisplayName("게시글 카운팅 처리 테스트")
    class CountTest {


    }

    @Nested
    @DisplayName("게시글 생성 관련 테스트")
    class CreateTest {


    }

    @Nested
    @DisplayName("게시글 조회 관련 테스트")
    class ReadTest {


    }

    @Nested
    @DisplayName("게시글 수정 관련 테스트")
    class ModifyTest {


    }

    @Nested
    @DisplayName("게시글 삭제 관련 테스트")
    class DeleteTest {


    }


}