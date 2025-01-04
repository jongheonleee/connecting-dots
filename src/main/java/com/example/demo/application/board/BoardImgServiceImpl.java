package com.example.demo.application.board;

import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import com.example.demo.dto.board.BoardImgFormDto;
import com.example.demo.exception.board.BoardImgNotFoundException;
import com.example.demo.exception.global.InternalServerError;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardImgServiceImpl {

    @Value("${boardImgLocation}")
    private String boardImgLocation;

    private BoardImgDaoImpl boardImgDao;

    private FileServiceImpl fileService;

    @Autowired
    public BoardImgServiceImpl(BoardImgDaoImpl boardImgDao, FileServiceImpl fileService) {
        this.boardImgDao = boardImgDao;
        this.fileService = fileService;
    }


    public void createBoardImg(BoardImgFormDto dto, MultipartFile boardImgFile) {
        String oriBoardImgName = boardImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        try {
            if (!StringUtils.isEmpty(oriBoardImgName)) {
                imgName = fileService.uploadFile(boardImgLocation, oriBoardImgName,
                        boardImgFile.getBytes());
                imgUrl = "/images/board/" + imgName;
            }

            dto.updateBoardImg(imgName, imgUrl);
            int rowCnt = boardImgDao.insert(dto);
            if (rowCnt != 1) {
                throw new InternalServerError();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError();
        }
    }


    public void modifyBoardImg(Integer ino, MultipartFile boardImgFile) {
        try {
            if (!boardImgFile.isEmpty()) {
                var foundBoardImg = boardImgDao.selectByIno(ino);
                if (foundBoardImg == null) {
                    throw new BoardImgNotFoundException();
                }

                if (!StringUtils.isEmpty(foundBoardImg.getName())) {
                    fileService.deleteFile(boardImgLocation + "/" + foundBoardImg.getName());
                }

                String oriBoardImgName = boardImgFile.getOriginalFilename();
                String imgName = fileService.uploadFile(boardImgLocation, oriBoardImgName, boardImgFile.getBytes());
                String imgUrl = "/images/board/"  + imgName;
                foundBoardImg.updateBoardImg(imgName, imgUrl);

                int rowCnt = boardImgDao.update(foundBoardImg);
                if (rowCnt != 1) {
                    throw new InternalServerError();
                }
            }

        } catch (Exception e) {
            throw new InternalServerError();
        }
    }

}
