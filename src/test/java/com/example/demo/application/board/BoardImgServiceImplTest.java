package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.board.BoardImgRequest;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class BoardImgServiceImplTest {

    @InjectMocks
    private BoardImgServiceImpl boardImgService;

    @Mock
    private BoardImgDaoImpl boardImgDao;

    @Mock
    private FileServiceImpl fileService;

    @Mock
    private CustomFormatter customFormatter;


    @BeforeEach
    void setUp() {
        assertNotNull(boardImgService);
        assertNotNull(boardImgDao);
        assertNotNull(fileService);
        assertNotNull(customFormatter);
    }

    @Nested
    @DisplayName("이미지 등록 처리 테스트")
    class Describe_saveBoardImage {

        @DisplayName("이미지 등록 처리 성공 테스트")
        @Test
        void 이미지_등록_처리_성공() {
            // given
            // when
            // then
        }

        @DisplayName("이미지 등록 처리 실패시 예외 발생 테스트")
        @Test
        void 이미지_등록시_DBMS_반영안됨() {
            // given
            // when
            // then
        }
    }

    @Nested
    @DisplayName("이미지 수정 처리 테스트")
    class Describe_modifyBoardImg {

        @DisplayName("이미지 수정 처리 성공 테스트")
        @Test
        void 이미지_수정_처리_성공() {
            // given
            // when
            // then
        }

        @DisplayName("이미지 수정 처리 실패 테스트")
        @Test
        void 이미지_수정시_DBMS_반영안됨() {
            // given
            // when
            // then
        }
    }

    @Nested
    @DisplayName("이미지 삭제 처리 테스트")
    class Describe_removeBoardImg {

        @DisplayName("이미지 삭제 처리 성공 테스트")
        @Test
        void 이미지_삭제_처리_성공() {
            // given
            // when
            // then
        }

        @DisplayName("이미지 삭제 처리 실패 테스트")
        @Test
        void 이미지_삭제시_DBMS_반영안됨() {
            // given
            // when
            // then
        }
    }

    private BoardImgRequest createRequest() {
        return null;
    }

    private MultipartFile createMultipartFile() {
        return null;
    }
}