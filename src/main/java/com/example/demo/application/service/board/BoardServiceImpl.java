package com.example.demo.application.service.board;

import com.example.demo.dto.SearchCondition;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.dto.board.BoardDetailDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardImgFormDto;
import com.example.demo.dto.board.BoardUpdatedFormDto;
import com.example.demo.application.exception.board.BoardFormInvalidException;
import com.example.demo.application.exception.board.BoardNotFoundException;
import com.example.demo.application.exception.global.InternalServerError;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BoardDaoImpl boardDao;
    private BoardImgServiceImpl boardImgService; // 추후에 개발

    @Autowired
    public BoardServiceImpl(BoardDaoImpl boardDao, BoardImgServiceImpl boardImgService,
            BoardImgDaoImpl boardImgDaoImpl) {
        this.boardDao = boardDao;
        this.boardImgService = boardImgService;
        this.boardImgDaoImpl = boardImgDaoImpl;
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
                throw new InternalServerError("DB에 정상적으로 반영되지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
            }

            // 이미지 등록
            for (int i=0; i<boardImgFiles.size(); i++) {
                var boardImgDto = new BoardImgFormDto();
                boardImgDto.setBno(dto.getBno());
                boardImgService.createBoardImg(boardImgDto, boardImgFiles.get(i));
            }

        } catch (DataIntegrityViolationException e) {
            throw new BoardFormInvalidException("입력하신 데이터가 올바르지 않습니다. " + e.getMessage());
        }
    }

//    // 재시도 실패시 예외 발생
//    @Recover
//    public void recover(RuntimeException e) {
//        throw new RetryFailedException("게시글 작성에 실패했습니다. 재시도 횟수를 초과했습니다.");
//    }


    public BoardFormDto findByBno(Integer bno) {
        var foundBoard = boardDao.select(bno);
        if (foundBoard == null) {
            throw new BoardNotFoundException("해당 " + bno + "를 가진 게시글을 찾을 수 없습니다.");
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
            throw new BoardNotFoundException("해당 " + bno + "를 가진 게시글을 찾을 수 없습니다.");
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
                throw new InternalServerError("DB에 정상적으로 반영되지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
            }

            // 기존에 저장된 이미지 전체 삭제
            boardImgDaoImpl.deleteByBno(dto.getBno());

            // 이미지 재등록
            for (int i=0; i<boardImgFiles.size(); i++) {
                var boardImgDto = new BoardImgFormDto();
                boardImgDto.setBno(dto.getBno());
                boardImgService.createBoardImg(boardImgDto, boardImgFiles.get(i));
            }

        } catch (DataIntegrityViolationException e) {
            throw new BoardFormInvalidException("입력하신 데이터가 올바르지 않습니다. " + e.getMessage());
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
            throw new BoardNotFoundException("해당 " + bno + "를 가진 게시글을 찾을 수 없습니다.");
        }

        // 관련 이미지 삭제
        for (var foundBoardImg : foundBoardImgs) {
            rowCnt += boardImgDaoImpl.deleteByIno(foundBoardImg.getIno());
        }

        // 게시글 삭제
        rowCnt += boardDao.delete(bno);

        if (rowCnt != expectedRowCnt) {
            throw new InternalServerError("게시글 삭제에 실패했습니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        int expectedRowCnt = boardDao.count() + boardImgDaoImpl.count();
        int rowCnt = 0;

        rowCnt += boardImgDaoImpl.deleteAll();
        rowCnt += boardDao.deleteAll();

        if (expectedRowCnt != rowCnt) {
            throw new InternalServerError("게시글 전체 삭제에 실패했습니다.");
        }
    }


}
