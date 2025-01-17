package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.demo.dto.board.BoardImgDto;
import com.example.demo.dto.board.BoardImgRequest;
import com.example.demo.global.error.exception.business.board.BoardImageNotFoundException;
import com.example.demo.global.error.exception.business.board.InvalidBoardImageException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class BoardImgSrviceImplTest {

    @InjectMocks
    private BoardImgServiceImpl boardImgService;

    @Mock
    private BoardImgDaoImpl boardImgDao;

    @Mock
    private FileServiceImpl fileService;

    @Mock
    private CustomFormatter customFormatter;

    private String boardImgLocation = "/test/location";
    private String boardImgUrlLocation = "http://test-url.com/";


    @BeforeEach
    void setUp() throws Exception {
        assertNotNull(boardImgService);
        assertNotNull(boardImgDao);
        assertNotNull(fileService);
        assertNotNull(customFormatter);

        // Reflection으로 @Value 필드 설정
        Field boardImgLocationField = BoardImgServiceImpl.class.getDeclaredField("boardImgLocation");
        boardImgLocationField.setAccessible(true);
        boardImgLocationField.set(boardImgService, boardImgLocation);

        Field boardImgUrlLocationField = BoardImgServiceImpl.class.getDeclaredField("boardImgUrlLocation");
        boardImgUrlLocationField.setAccessible(true);
        boardImgUrlLocationField.set(boardImgService, boardImgUrlLocation);
    }

    @Nested
    @DisplayName("이미지 등록 처리 테스트")
    class Describe_saveBoardImage {

        @DisplayName("이미지 등록 처리 성공 테스트")
        @Test
        void 이미지_등록_처리_성공() throws Exception {
            // given
            BoardImgRequest request = createRequest();
            MultipartFile boardImgFile = createMultipartFile();

            String imageFileName = "test.jpeg";
            when(fileService.uploadFile(boardImgLocation, boardImgFile.getOriginalFilename(), boardImgFile.getBytes())).thenReturn(imageFileName);
            when(boardImgDao.insert(any())).thenReturn(1);

            assertDoesNotThrow(() -> boardImgService.saveBoardImage(request, boardImgFile));
        }

        @DisplayName("이미지 등록 처리 성공 테스트, 이미지 파일이 없는 경우")
        @Test
        void 이미지_등록_처리_성공_이미지_파일이_없는_경우() throws Exception {
            // given
            BoardImgRequest request = createRequest();
            MultipartFile boardImgFile = null;

            // when

            // then
            assertThrows(InvalidBoardImageException.class, () -> boardImgService.saveBoardImage(request, boardImgFile));
        }

        @DisplayName("이미지 파일 이름이 존재하지 않는 경우, 예외 발생")
        @Test
        void 이미지_등록_처리_성공_이미지_파일_이름이_존재하지_않는_경우() throws Exception {
            // given
            BoardImgRequest request = createRequest();
            MultipartFile boardImgFile = createWrongMultipartFile();

            // then
            assertThrows(InvalidBoardImageException.class, () -> boardImgService.saveBoardImage(request, boardImgFile));
        }

        @DisplayName("이미지 등록 처리 실패시 예외 발생 테스트, DBMS 반영 안됨")
        @Test
        void 이미지_등록시_DBMS_반영안됨() throws Exception {
            // given
            BoardImgRequest request = createRequest();
            MultipartFile boardImgFile = createMultipartFile();

            // when
            when(fileService.uploadFile(boardImgLocation, boardImgFile.getOriginalFilename(), boardImgFile.getBytes())).thenReturn("test.jpeg");
            when(boardImgDao.insert(any())).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> boardImgService.saveBoardImage(request, boardImgFile));
        }
    }

    @Nested
    @DisplayName("이미지 수정 처리 테스트")
    class Describe_modifyBoardImg {

        @DisplayName("이미지 수정 처리 성공 테스트")
        @Test
        void 이미지_수정_처리_성공() throws Exception {
            // given
            Integer ino = 1;
            String imageFileName = "test.jpeg";
            BoardImgRequest request = createRequest();
            MultipartFile boardImgFile = createMultipartFile();
            BoardImgDto dto = createDto(request, imageFileName, boardImgUrlLocation);

            // when
            when(boardImgDao.existsByInoForUpdate(ino)).thenReturn(true);
            when(boardImgDao.selectByIno(ino)).thenReturn(dto);
            doNothing().when(fileService).deleteFile(boardImgLocation + "/" + dto.getName());
            when(fileService.uploadFile(boardImgLocation, boardImgFile.getOriginalFilename(), boardImgFile.getBytes())).thenReturn(imageFileName);
            when(boardImgDao.update(dto)).thenReturn(1);

            // then
            assertDoesNotThrow(() -> boardImgService.modifyBoardImg(ino, boardImgFile));
        }

        @DisplayName("이미지 수정 처리 실패 테스트, ino가 존재하지 않는 경우")
        @Test
        void 이미지_수정시_ino_조회_실패() {
            // given
            Integer ino = 1;
            MultipartFile boardImgFile = createMultipartFile();

            // when
            when(boardImgDao.existsByInoForUpdate(ino)).thenReturn(false);

            // then
            assertThrows(BoardImageNotFoundException.class, () -> boardImgService.modifyBoardImg(ino, boardImgFile));
        }

        @DisplayName("이미지 수정 처리 실패 테스트, 이미지 파일이 없는 경우")
        @Test
        void 이미지_수정시_이미지_파일_없음() {
            // given
            Integer ino = 1;
            MultipartFile boardImgFile = null;

            // then
            assertThrows(InvalidBoardImageException.class, () -> boardImgService.modifyBoardImg(ino, boardImgFile));
        }


        @DisplayName("이미지 수정 처리 실패 테스트, 이미지 파일 이름이 존재하지 않는 경우")
        @Test
        void 이미지_수정시_이미지_파일_이름_없음() {
            // given
            Integer ino = 1;
            MultipartFile boardImgFile = createWrongMultipartFile();

            // then
            assertThrows(InvalidBoardImageException.class, () -> boardImgService.modifyBoardImg(ino, boardImgFile));
        }

        @DisplayName("이미지 수정 처리 실패 테스트, DBMS 반영 안됨")
        @Test
        void 이미지_수정시_DBMS_반영안됨() throws Exception {
            // given
            Integer ino = 1;
            String imageFileName = "test.jpeg";
            BoardImgRequest request = createRequest();
            MultipartFile boardImgFile = createMultipartFile();
            BoardImgDto dto = createDto(request, imageFileName, boardImgUrlLocation);

            // when
            when(boardImgDao.existsByInoForUpdate(ino)).thenReturn(true);
            when(boardImgDao.selectByIno(ino)).thenReturn(dto);
            doNothing().when(fileService).deleteFile(boardImgLocation + "/" + dto.getName());
            when(fileService.uploadFile(boardImgLocation, boardImgFile.getOriginalFilename(), boardImgFile.getBytes())).thenReturn(imageFileName);
            when(boardImgDao.update(dto)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> boardImgService.modifyBoardImg(ino, boardImgFile));
        }
    }

    @Nested
    @DisplayName("이미지 삭제 처리 테스트")
    class Describe_removeBoardImg {

        @DisplayName("이미지 삭제 처리 성공 테스트")
        @Test
        void 이미지_삭제_처리_성공() throws Exception {
            // given
            Integer ino = 1;
            BoardImgDto dto = createDto(createRequest(), "test.jpeg", boardImgUrlLocation);

            // when
            when(boardImgDao.existsByInoForUpdate(ino)).thenReturn(true);
            when(boardImgDao.selectByIno(ino)).thenReturn(dto);
            doNothing().when(fileService).deleteFile(boardImgLocation + "/" + dto.getName());
            when(boardImgDao.deleteByIno(ino)).thenReturn(1);

            // then
            assertDoesNotThrow(() -> boardImgService.removeBoardImg(ino));
        }

        @DisplayName("이미지 삭제 처리 실패 테스트, ino가 존재하지 않는 경우")
        @Test
        void 이미지_삭제시_ino_조회_실패() {
            // given
            Integer ino = 1;

            // when
            when(boardImgDao.existsByInoForUpdate(ino)).thenReturn(false);

            // then
            assertThrows(BoardImageNotFoundException.class, () -> boardImgService.removeBoardImg(ino));
        }

        @DisplayName("이미지 삭제 처리 실패 테스트")
        @Test
        void 이미지_삭제시_DBMS_반영안됨() {
            // given
            Integer ino = 1;
            BoardImgDto dto = createDto(createRequest(), "test.jpeg", boardImgUrlLocation);

            // when
            when(boardImgDao.existsByInoForUpdate(ino)).thenReturn(true);
            when(boardImgDao.selectByIno(ino)).thenReturn(dto);
            doNothing().when(fileService).deleteFile(boardImgLocation + "/" + dto.getName());
            when(boardImgDao.deleteByIno(ino)).thenReturn(0);

            // then
            assertThrows(NotApplyOnDbmsException.class, () -> boardImgService.removeBoardImg(ino));
        }
    }

    private BoardImgRequest createRequest() {
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

    private MultipartFile createWrongMultipartFile() {
        return new MockMultipartFile("test.jpeg", null, "image/jpeg", "test.jpeg".getBytes());
    }

    private BoardImgDto createDto(BoardImgRequest request, String imageFileName, String imageUrl) {
        return BoardImgDto.builder()
                        .bno(request.getBno())
                        .name(imageFileName)
                        .img(imageUrl)
                        .comt(request.getComt())
                        .reg_user_seq(customFormatter.getManagerSeq())
                        .up_user_seq(customFormatter.getManagerSeq())
                        .reg_date(customFormatter.getCurrentDateFormat())
                        .up_date(customFormatter.getCurrentDateFormat())
                        .chk_thumb(request.getChk_thumb())
                        .build();
    }
}