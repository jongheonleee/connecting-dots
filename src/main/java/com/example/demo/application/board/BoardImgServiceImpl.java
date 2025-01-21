package com.example.demo.application.board;

import static com.example.demo.global.error.exception.ErrorCode.*;
import static com.example.demo.global.error.exception.ErrorCode.FILE_UPLOAD_ERROR;

import com.example.demo.dto.board.BoardImgDto;
import com.example.demo.dto.board.BoardImgRequest;
import com.example.demo.global.error.exception.business.board.BoardImageNotFoundException;
import com.example.demo.global.error.exception.business.board.InvalidBoardImageException;
import com.example.demo.global.error.exception.technology.InternalServerException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import com.example.demo.utils.CustomFormatter;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardImgServiceImpl {

    @Value("${boardImgLocation}")
    private String boardImgLocation;

    @Value("${boardImgUrlLocation}")
    private String boardImgUrlLocation;


    private final BoardImgDaoImpl boardImgDao;
    private final FileServiceImpl fileService;
    private final CustomFormatter customFormatter;


    public void saveBoardImage(final BoardImgRequest request, final MultipartFile boardImgFile) {
        checkValidImageFile(boardImgFile);
        String imageFileName = uploadFile(boardImgFile);
        String imageUrl = createImageUrl(imageFileName);
        var dto = createDto(request, imageFileName, imageUrl);
        checkApplied(1, boardImgDao.insert(dto));
    }


    public void modifyBoardImg(final Integer ino, final MultipartFile boardImgFile) {
        checkValidImageFile(boardImgFile);
        checkExistsByIno(ino);

        var found = removeFileByIno(ino);
        String newImageFileName = uploadFile(boardImgFile);
        String newImageUrl = createImageUrl(newImageFileName);
        found.updateBoardImg(newImageFileName, newImageUrl);

        checkApplied(1, boardImgDao.update(found));
    }

    public void removeBoardImg(final Integer ino) {
        checkExistsByIno(ino);
        removeFileByIno(ino);
        checkApplied(1, boardImgDao.deleteByIno(ino));
    }


    private String uploadFile(final MultipartFile boardImgFile) {
        String oriBoardImgName = boardImgFile.getOriginalFilename();
        String imgName = "";

        try {
            imgName = fileService.uploadFile(boardImgLocation, oriBoardImgName, boardImgFile.getBytes());
            return imgName;
        } catch (Exception e) {
            log.error("[BOARD_IMAGE] 파일 업로드 중 오류가 발생했습니다. {}", e.getMessage());
            throw new InternalServerException(FILE_UPLOAD_ERROR);
        }
    }

    private BoardImgDto removeFileByIno(final Integer ino) {
        var foundBoardImage = boardImgDao.selectByIno(ino);
        if (!StringUtils.isEmpty(foundBoardImage.getName())) {
            fileService.deleteFile(boardImgLocation + "/" + foundBoardImage.getName());
        }
        return foundBoardImage;
    }

    private void checkExistsByIno(final Integer ino) {
        var exists = boardImgDao.existsByInoForUpdate(ino);
        if (!exists) {
            log.error("[BOARD_IMAGE] 이미지 정보가 존재하지 않습니다. ino: {}", ino);
            throw new BoardImageNotFoundException();
        }
    }

    private void checkValidImageFile(final MultipartFile boardImgFile) {
        checkExistsFile(boardImgFile);
        checkValidFileName(boardImgFile);
    }

    private void checkValidFileName(final MultipartFile boardImgFile) {
        boolean isEmptyFileName = StringUtils.isEmpty(boardImgFile.getOriginalFilename());
        if (isEmptyFileName) {
            log.error("[BOARD_IMAGE] 잘못된 파일 이름 형식입니다. {}", boardImgFile.getOriginalFilename());
            throw new InvalidBoardImageException(BOARD_INVALID_IMAGE_FILE_NAME);
        }
    }

    private void checkExistsFile(final MultipartFile boardImgFile) {
        if (boardImgFile == null || boardImgFile.isEmpty()) {
            log.error("[BOARD_IMAGE] 이미지 파일이 존재하지 않습니다.");
            throw new InvalidBoardImageException(BOARD_IMAGE_FILE_NOT_EXIST);
        }
    }


    private String createImageUrl(final String imageFileName) {
        log.info("[BOARD_IMAGE] boardImgUrlLocation + imageFileName: {}", boardImgUrlLocation + imageFileName);
        return boardImgUrlLocation + imageFileName;
    }


    private void checkApplied(final Integer expected, final Integer rowCnt) {
        if (!expected.equals(rowCnt)) {
            throw new NotApplyOnDbmsException();
        }
    }


    private BoardImgDto createDto(final BoardImgRequest request, final String imageFileName, final String imageUrl) {
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
