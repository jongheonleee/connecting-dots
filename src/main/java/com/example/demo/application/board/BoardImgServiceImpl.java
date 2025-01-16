package com.example.demo.application.board;

import com.example.demo.dto.board.BoardImgRequest;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
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

//    public void createBoardImg(BoardImgRequest request, MultipartFile boardImgFile) {
//        String oriBoardImgName = boardImgFile.getOriginalFilename();
//        String imgName = "";
//        String imgUrl = "";
//
//        try {
//            if (!StringUtils.isEmpty(oriBoardImgName)) {
//                imgName = fileService.uploadFile(boardImgLocation, oriBoardImgName, boardImgFile.getBytes());
//                imgUrl = "/images/board/" + imgName;
//            }
//
//            dto.updateBoardImg(imgName, imgUrl);
//            int rowCnt = boardImgDao.insert(dto);
//            if (rowCnt != 1) {
//                throw new NotApplyOnDbmsException();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new InternalServiceException();
//        }
//    }
//
//
//    public void modifyBoardImg(Integer ino, MultipartFile boardImgFile) {
//        try {
//            if (!boardImgFile.isEmpty()) {
//                var foundBoardImg = boardImgDao.existsByIno(ino);
////                if (foundBoardImg == null) {
//////                    throw new BoardImgNotFoundException();
////                }
//
//                if (!StringUtils.isEmpty(foundBoardImg.getName())) {
//                    fileService.deleteFile(boardImgLocation + "/" + foundBoardImg.getName());
//                }
//
//                String oriBoardImgName = boardImgFile.getOriginalFilename();
//                String imgName = fileService.uploadFile(boardImgLocation, oriBoardImgName, boardImgFile.getBytes());
//                String imgUrl = "/images/board/"  + imgName;
//                foundBoardImg.updateBoardImg(imgName, imgUrl);
//
//                int rowCnt = boardImgDao.update(foundBoardImg);
//                if (rowCnt != 1) {
//                    throw new NotApplyOnDbmsException();
//                }
//            }
//
//        } catch (Exception e) {
//            throw new InternalServiceException();
//        }
//    }

}
