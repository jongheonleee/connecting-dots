package com.example.demo.application.board;

import com.example.demo.dto.SearchCondition;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.dto.board.BoardDetailDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardImgFormDto;
import com.example.demo.global.error.exception.business.board.BoardFormInvalidException;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.technology.InternalServerException;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardServiceImpl {

    private static final int MAX_RETRY = 10;
    private static final int RETRY_DELAY = 5_000;

    private final BoardImgDaoImpl boardImgDaoImpl;
    private final BoardDaoImpl boardDao;
    private final BoardImgServiceImpl boardImgService;
    private final FileServiceImpl fileService;

    public BoardServiceImpl(BoardDaoImpl boardDao, BoardImgServiceImpl boardImgService,
            BoardImgDaoImpl boardImgDaoImpl, FileServiceImpl fileService) {
        this.boardDao = boardDao;
        this.boardImgService = boardImgService;
        this.boardImgDaoImpl = boardImgDaoImpl;
        this.fileService = fileService;
    }

    public int count() {
        return boardDao.count();
    }

    public int count(SearchCondition sc) {
        return boardDao.count(sc);
    }

    public int count(String cateCode) {
        return boardDao.count(cateCode);
    }

    @Retryable(
            value = {RuntimeException.class},
            exclude = {BoardFormInvalidException.class, SQLSyntaxErrorException.class, DuplicateKeyException.class},
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY)
    )
    @Transactional(rollbackFor = Exception.class)
    public void create(BoardFormDto dto, List<MultipartFile> boardImgFiles) {
        int rowCnt = 0;
        try {
            // 게시글 등록
            rowCnt = boardDao.insert(dto);
            if (rowCnt != 1) {
                throw new InternalServerException(null);
            }

            // 이미지 등록
            for (int i=0; i<boardImgFiles.size(); i++) {
                var boardImgDto = new BoardImgFormDto();
                boardImgDto.setBno(dto.getBno());
                boardImgService.createBoardImg(boardImgDto, boardImgFiles.get(i));
            }

        } catch (DataIntegrityViolationException e) {
            throw new BoardFormInvalidException();
        }
    }




    public BoardFormDto findByBno(Integer bno) {
        var foundBoard = boardDao.select(bno);
        if (foundBoard == null) {
            throw new BoardNotFoundException();
        }

        return foundBoard;
    }

    public List<BoardFormDto> findByCategory(String cate_code) {
        List<BoardFormDto> boardFormDtos = boardDao.selectAllByCategory(cate_code);
        return boardFormDtos;
    }

    public BoardDetailDto findDetailByBno(Integer bno) {
        var foundDetailBoard = boardDao.selectDetailByBno(bno);
        if (foundDetailBoard == null) {
            throw new BoardNotFoundException();
        }

        boardDao.increaseViewCnt(bno);
        return foundDetailBoard;
    }

    public List<BoardFormDto> findBySearchCondition(SearchCondition sc) {
        return boardDao.selectBySearchCondition(sc);
    }

    public List<BoardFormDto> findAll() {
        return boardDao.selectAll();
    }

    @Retryable(
            value = {RuntimeException.class},
            exclude = {BoardFormInvalidException.class, SQLSyntaxErrorException.class, DuplicateKeyException.class},
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY)
    )
    @Transactional(rollbackFor = Exception.class)
    public void modify(BoardFormDto dto, List<MultipartFile> boardImgFiles) {
        int rowCnt = 0;

        try {
            rowCnt = boardDao.update(dto);
            if (rowCnt != 1) {
                throw new InternalServerException(null);
            }

            // 기존에 저장된 이미지 전체 삭제 - DB, 파일시스템
            boardImgDaoImpl.deleteByBno(dto.getBno());
            List<BoardImgFormDto> foundBoardImgs = boardImgDaoImpl.selectAllByBno(dto.getBno());
            for (var foundBoardImg : foundBoardImgs) {
                fileService.deleteFile(foundBoardImg.getImg());
            }

            // 이미지 재등록
            for (int i=0; i<boardImgFiles.size(); i++) {
                var boardImgDto = new BoardImgFormDto();
                boardImgDto.setBno(dto.getBno());
                boardImgService.createBoardImg(boardImgDto, boardImgFiles.get(i));
            }

        } catch (Exception e) {
            throw new BoardFormInvalidException();
        }
    }

    public void increaseViewCnt(Integer bno) {
        int rowCnt = boardDao.increaseViewCnt(bno);
    }

    public void increaseRecoCnt(Integer bno) {
        int rowCnt = boardDao.increaseRecoCnt(bno);
    }

    public void increaseNotRecoCnt(Integer bno) {
        int rowCnt = boardDao.increaseNotRecoCnt(bno);
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(Integer bno) {
        var foundBoard = boardDao.select(bno); // 1
        var foundBoardImgs = boardImgDaoImpl.selectAllByBno(bno); // size

        int rowCnt = 0;
        int expectedRowCnt = 1 + foundBoardImgs.size();

        if (foundBoard == null) {
            throw new BoardNotFoundException();
        }

        // 관련 이미지 삭제
        for (var foundBoardImg : foundBoardImgs) {
            rowCnt += boardImgDaoImpl.deleteByIno(foundBoardImg.getIno());
        }

        // 게시글 삭제
        rowCnt += boardDao.delete(bno);

        if (rowCnt != expectedRowCnt) {
            throw new InternalServerException(null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        int expectedRowCnt = boardDao.count() + boardImgDaoImpl.count();
        int rowCnt = 0;

        rowCnt += boardImgDaoImpl.deleteAll();
        rowCnt += boardDao.deleteAll();

        if (expectedRowCnt != rowCnt) {
            throw new InternalServerException(null);
        }
    }


}
