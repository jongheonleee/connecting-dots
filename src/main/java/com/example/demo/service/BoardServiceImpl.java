package com.example.demo.service;

import com.example.demo.dao.BoardDaoImpl;
import com.example.demo.dto.BoardFormDto;
import com.example.demo.dto.BoardUpdatedFormDto;
import com.example.demo.exception.BoardFormInvalidException;
import com.example.demo.exception.BoardNotFoundException;
import com.example.demo.exception.InternalServerError;
import com.example.demo.exception.RetryFailedException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardServiceImpl {

    private static final int MAX_RETRY = 10;
    private static final int RETRY_DELAY = 5_000;

    private BoardDaoImpl boardDao;
    private BoardImgServiceImpl boardImgService; // 추후에 개발

    @Autowired
    public BoardServiceImpl(BoardDaoImpl boardDao, BoardImgServiceImpl boardImgService) {
        this.boardDao = boardDao;
        this.boardImgService = boardImgService;
    }

    public int count() {
        return boardDao.count();
    }

    @Retryable(
            value = {RuntimeException.class},
            exclude = {BoardFormInvalidException.class, SQLSyntaxErrorException.class, DuplicateKeyException.class},
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY)
    )
    public void create(BoardFormDto dto) {
        int rowCnt = 0;
        try {
            rowCnt = boardDao.insert(dto);
            if (rowCnt != 1) {
                throw new InternalServerError("DB에 정상적으로 반영되지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
            }
        } catch (DataIntegrityViolationException e) {
            throw new BoardFormInvalidException("입력하신 데이터가 올바르지 않습니다. " + e.getMessage());
        }
    }

    // 재시도 실패시 예외 발생
    @Recover
    public void recover(RuntimeException e) {
        throw new RetryFailedException("게시글 작성에 실패했습니다. 재시도 횟수를 초과했습니다.");
    }


    public BoardFormDto findByBno(Integer bno) {
        var foundBoard = boardDao.select(bno);
        if (foundBoard == null) {
            throw new BoardNotFoundException("해당 " + bno + "를 가진 게시글을 찾을 수 없습니다.");
        }

        return foundBoard;
    }

    public List<BoardFormDto> findAll() {
        return boardDao.selectAll();
    }

    public void modify(BoardUpdatedFormDto dto) {
        int rowCnt = 0;

        try {
            rowCnt = boardDao.update(dto);
            if (rowCnt != 1) {
                throw new InternalServerError("DB에 정상적으로 반영되지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
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

    public void remove(Integer bno) {
        var foundBoard = boardDao.select(bno);
        if (foundBoard == null) {
            throw new BoardNotFoundException("해당 " + bno + "를 가진 게시글을 찾을 수 없습니다.");
        }

        int rowCnt = 0;
        rowCnt = boardDao.delete(bno);

        if (rowCnt != 1) {
            throw new InternalServerError("게시글 삭제에 실패했습니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        int totalCnt = boardDao.count();
        int rowCnt = boardDao.deleteAll();

        if (totalCnt != rowCnt) {
            throw new InternalServerError("게시글 전체 삭제에 실패했습니다.");
        }
    }


}
