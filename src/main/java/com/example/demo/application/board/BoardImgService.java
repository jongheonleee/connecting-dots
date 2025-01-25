package com.example.demo.application.board;

import com.example.demo.dto.board.BoardImgRequest;
import com.example.demo.dto.board.BoardImgResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface BoardImgService {

    void saveBoardImage(BoardImgRequest request, MultipartFile boardImgFile);

    List<BoardImgResponse> readByBno(Integer bno);

    void modifyBoardImg(Integer ino, MultipartFile boardImgFile);

    void removeByIno(Integer ino);
}
