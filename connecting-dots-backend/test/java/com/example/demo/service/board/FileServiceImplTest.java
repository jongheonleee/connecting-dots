package com.example.demo.service.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.service.board.impl.FileServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("학습 테스트, 어떤식으로 동작하는지 파악하기")
class FileServiceImplTest {

    private FileService fileService = new FileServiceImpl();

    @Test
    @DisplayName("파일 업로드 처리 테스트")
    void uploadFile() {
        String uploadPath = "/Users/qwefghnm1212/desktop/connecting-dots/board";
        String originalFileName = "test.jpeg";
        byte[] fileData = "test.jpeg".getBytes();
        assertDoesNotThrow(() -> fileService.uploadFile(uploadPath, originalFileName, fileData));
    }

    // 밑에 테스트는 해당 객체 메서드의 접근 제한자가 private으로 되어있어서 테스트가 불가능함.
    // 어떤식으로 동작하는지 알고 싶었을 때만 public으로 변경해서 테스트를 진행함
//
//    @Test
//    @DisplayName("UUID를 이용한 저장 파일명 생성 테스트")
//    void createSavedFileName() {
//        String originalFileName = "test.jpeg";
//        String savedFileName = fileService.createSavedFileName(originalFileName);
//        System.out.println("savedFileName = " + savedFileName);
//    }
//
//    @Test
//    @DisplayName("파일 확장자 추출 테스트")
//    void getExtension() {
//        String originalFileName = "test.jpeg";
//        String extension = fileService.getExtension(originalFileName);
//        assertEquals(".jpeg", extension);
//    }
//
//    @Test
//    @DisplayName("파일 업로드 URL 생성 테스트")
//    void createFileUploadUrl() {
//        /**
//         * boardImgLocation=/Users/qwefghnm1212/desktop/connecting-dots/board
//         * boardImgUrlLocation=/images/board/
//         */
//
//        String uploadPath = "/Users/qwefghnm1212/desktop/connecting-dots/board";
//        String savedFileName = "test.jpeg";
//
//        String fileUploadUrl = fileService.createFileUploadUrl(uploadPath, savedFileName);
//        assertEquals("/Users/qwefghnm1212/desktop/connecting-dots/board/test.jpeg", fileUploadUrl);
//    }
//
//    @Test
//    @DisplayName("파일 쓰기 테스트")
//    void write() {
//        byte[] fileData = "test.jpeg".getBytes();
//        String filePath = "/Users/qwefghnm1212/desktop/connecting-dots/board/test.jpeg";
//        assertDoesNotThrow(() -> fileService.write(fileData, filePath));
//    }
//
//    @Test
//    @DisplayName("파일 삭제 테스트")
//    void deleteFile() {
//        byte[] fileData = "test.jpeg".getBytes();
//        String filePath = "/Users/qwefghnm1212/desktop/connecting-dots/board/test.jpeg";
//        assertDoesNotThrow(() -> fileService.write(fileData, filePath));
//        assertDoesNotThrow(() -> fileService.deleteFile(filePath));
//
//    }
}