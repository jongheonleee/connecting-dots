package com.example.demo.service;

import com.example.demo.dao.BoardImgDaoImpl;
import com.example.demo.dto.BoardImgFormDto;
import com.example.demo.exception.BoardImgNotFoundException;
import com.example.demo.exception.InternalServerError;
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
                throw new InternalServerError("DB에 정상적으로 반영되지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError("게시글 이미지를 생성하는 도중 오류가 발생했습니다.");
        }
    }

    public void modifyBoardImg(Integer ino, MultipartFile boardImgFile) {
        try {
            if (!boardImgFile.isEmpty()) {
                var foundBoardImg = boardImgDao.selectByIno(ino);
                if (foundBoardImg == null) {
                    throw new BoardImgNotFoundException("게시글 이미지를 찾을 수 없습니다.");
                }

                if (!StringUtils.isEmpty(foundBoardImg.getName())) {
                    fileService.deleteFile(boardImgLocation + "/" + foundBoardImg.getName());
                }

                String oriBoardImgName = boardImgFile.getOriginalFilename();
                String imgName = fileService.uploadFile(boardImgLocation, oriBoardImgName,
                        boardImgFile.getBytes());
                String imgUrl = "/images/board/" + imgName;
                foundBoardImg.updateBoardImg(imgName, imgUrl);

                int rowCnt = boardImgDao.update(foundBoardImg);
                if (rowCnt != 1) {
                    throw new InternalServerError(
                            "DB에 정상적으로 반영되지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError("게시글 이미지를 수정하는 도중 오류가 발생했습니다.");
        }
    }

}
