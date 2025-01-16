package com.example.demo.application.board;

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


    public void saveBoardImage(BoardImgRequest request, MultipartFile boardImgFile) {
        String imageFileName = uploadImageFile(boardImgFile);
        String imageUrl = createImageUrl(imageFileName);
        var dto = createDto(request, imageFileName, imageUrl);
        checkApplied(1, boardImgDao.insert(dto));
    }


    public void modifyBoardImg(Integer ino, MultipartFile boardImgFile) {
        try {
            if (!boardImgFile.isEmpty()) {
                var exists = boardImgDao.existsByInoForUpdate(ino);
                if (exists) {
                    log.error("[BOARD_IMAGE] 이미지 정보가 존재하지 않습니다. ino: {}", ino);
                    throw new BoardImageNotFoundException();
                }

                BoardImgDto foundBoardImage = boardImgDao.selectByIno(ino);
                if (!StringUtils.isEmpty(foundBoardImage.getName())) {
                    fileService.deleteFile(boardImgLocation + "/" + foundBoardImage.getName());
                }

                String oriBoardImgName = boardImgFile.getOriginalFilename();
                String imgName = fileService.uploadFile(boardImgLocation, oriBoardImgName, boardImgFile.getBytes());
                String imgUrl = "/images/board/"  + imgName;
                foundBoardImage.updateBoardImg(imgName, imgUrl);
                checkApplied(1, boardImgDao.update(foundBoardImage));
            }

        } catch (Exception e) {
            throw new InternalServerException(FILE_UPLOAD_ERROR);
        }
    }

    public void removeBoardImg(Integer ino) {
        var exists = boardImgDao.existsByInoForUpdate(ino);
        if (exists) {
            log.error("[BOARD_IMAGE] 이미지 정보가 존재하지 않습니다. ino: {}", ino);
            throw new BoardImageNotFoundException();
        }

        BoardImgDto foundBoardImage = boardImgDao.selectByIno(ino);
        if (!StringUtils.isEmpty(foundBoardImage.getName())) {
            fileService.deleteFile(boardImgLocation + "/" + foundBoardImage.getName());
        }

        checkApplied(1, boardImgDao.deleteByIno(ino));
    }

    private String uploadImageFile(MultipartFile boardImgFile) {
        boolean checkValidFileName = isValidImageFileName(boardImgFile.getOriginalFilename());
        if (!checkValidFileName) {
            log.error("[BOARD_IMAGE] 잘못된 파일 이름 형식입니다. {}", boardImgFile.getOriginalFilename());
            throw new InvalidBoardImageException();
        }

        try {
            String imageFileName = fileService.uploadFile(boardImgLocation, boardImgFile.getOriginalFilename(), boardImgFile.getBytes());
            return imageFileName;
        } catch (Exception e) {
            log.error("[BOARD_IMAGE] 파일 업로드 중 오류가 발생했습니다. {}", e.getMessage());
            throw new InternalServerException(FILE_UPLOAD_ERROR);
        }
    }

    private String createImageUrl(String imageFileName) {
        return boardImgUrlLocation + imageFileName;
    }

    private boolean isValidImageFileName(String fileName) {
        return StringUtils.isEmpty(fileName) != false;
    }


    private void checkApplied(Integer expected, Integer rowCnt) {
        if (!expected.equals(rowCnt)) {
            throw new NotApplyOnDbmsException();
        }
    }

//    private void checkExists(Integer ino) {
//        var exists = boardImgDao.existsByInoForUpdate(ino);
//        if (!exists) {
//            log.error("[BOARD_IMAGE] 이미지 정보가 존재하지 않습니다. ino: {}", ino);
//            throw new BoardImageNotFoundException();
//        }
//    }
//
//    private void checkExistsImageFile(MultipartFile boardImgFile) {
//        boolean isNotExists = boardImgFile.isEmpty();
//
//        if (isNotExists) {
//            log.error("[BOARD_IMAGE] 이미지 파일이 존재하지 않습니다.");
//            throw new NotExistsBoardImageFileException();
//        }
//    }

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
