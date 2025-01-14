package com.example.demo.application.comment;


import com.example.demo.global.error.exception.business.comment.CommentFormInvalidException;
import com.example.demo.global.error.exception.business.comment.CommentNotFoundException;
import com.example.demo.global.error.exception.technology.InternalServerError;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.dto.comment.CommentRequestDto;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl {

    private final CommentDaoImpl commentDao;

    public CommentServiceImpl(CommentDaoImpl commentDao) {
        this.commentDao = commentDao;
    }

    // 전체 댓글 수 카운트
    public int count() {
        return commentDao.count();
    }

    // 게시판 관련 댓글 수 카운트
    public int count(Integer bno) {
        // 게시글 기반으로 카운팅 처리
        return commentDao.count(bno);
    }

    // 댓글 등록 처리
    public void create(CommentRequestDto dto) {
        // 댓글 등록 처리
        try {
            int rowCnt = commentDao.insert(dto);
            if (rowCnt != 1) {
                throw new InternalServerError(null);
            }
        } catch (DataIntegrityViolationException e) {
            throw new CommentFormInvalidException();
        }
    }

    // 게시글과 관련된 댓글 조회
    public List<CommentResponseDto> findByBno(Integer bno) {
        // 게시글과 관련된 댓글 조회
        return commentDao.selectByBno(bno);
    }

    // 특정 댓글 조회
    public CommentResponseDto findByCno(Integer cno) {
        // 댓글 번호로 특정 댓글 조회
        var foundComment = commentDao.selectByCno(cno);
        if (foundComment == null) {
            throw new CommentNotFoundException();
        }

        return foundComment;
    }

    // 모든 댓글 조회
    public List<CommentResponseDto> findAll() {
        // 모든 댓글 조회
        return commentDao.selectAll();
    }

    // 댓글 수정 처리
    public void update(CommentRequestDto dto) {
        // 댓글 수정 처리
        int rowCnt = 0;

        try {
            rowCnt = commentDao.update(dto);
            if (rowCnt != 1) {
                throw new InternalServerError(null);
            }

        } catch (DataIntegrityViolationException e) {
            throw new CommentFormInvalidException();
        }
    }

    // 댓글 좋아요 처리
    public void increaseLikeCnt(Integer cno) {
        // 좋아요 수 증가
        commentDao.increaseLikeCnt(cno);
    }

    // 댓글 싫어요 처리
    public void increaseDislikeCnt(Integer cno) {
        // 싫어요 수 증가
        commentDao.increaseDislikeCnt(cno);
    }

    // 게시글과 관련된 모든 댓글 삭제
    @Transactional(rollbackFor = Exception.class)
    public void removeByBno(Integer bno) {
        // 게시글과 관련된 모든 댓글 삭제
        List<CommentResponseDto> foundComments = commentDao.selectByBno(bno);
        int rowCnt = 0;
        int expectedRowCnt = foundComments.size();

        for (var foundComment : foundComments) {
            rowCnt += commentDao.deleteByCno(foundComment.getCno());
        }

        if (rowCnt != expectedRowCnt) {
            throw new InternalServerError(null);
        }
    }


    // 특정 댓글 삭제
    public void removeByCno(Integer cno) {
        // 댓글 번호로 특정 댓글 삭제
        int rowCnt = commentDao.deleteByCno(cno);

        if (rowCnt != 1) {
            throw new InternalServerError(null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        // 모든 댓글 삭제
        int expectedRowCnt = commentDao.count();
        int rowCnt = commentDao.deleteAll();

        if (expectedRowCnt != rowCnt) {
            throw new InternalServerError(null);
        }
    }
}
