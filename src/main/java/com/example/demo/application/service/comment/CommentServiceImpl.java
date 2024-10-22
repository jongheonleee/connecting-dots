package com.example.demo.application.service.comment;


import com.example.demo.application.exception.comment.CommentFormInvalidException;
import com.example.demo.application.exception.comment.CommentNotFoundException;
import com.example.demo.application.exception.global.InternalServerError;
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

    public int count() {
        return commentDao.count();
    }

    public int count(Integer bno) {
        // 게시글 기반으로 카운팅 처리
        return commentDao.count(bno);
    }

    public void create(CommentRequestDto dto) {
        // 댓글 등록 처리
        try {
            int rowCnt = commentDao.insert(dto.createCommentDto());
            if (rowCnt != 1) {
                throw new InternalServerError();
            }
        } catch (DataIntegrityViolationException e) {
            throw new CommentFormInvalidException();
        }
    }

    public List<CommentResponseDto> findByBno(Integer bno) {
        // 게시글과 관련된 댓글 조회
        return commentDao.selectByBno(bno);
    }

    public CommentResponseDto findByCno(Integer cno) {
        // 댓글 번호로 특정 댓글 조회
        var foundComment = commentDao.selectByCno(cno);
        if (foundComment == null) {
            throw new CommentNotFoundException();
        }

        return foundComment;
    }

    public List<CommentResponseDto> findAll() {
        // 모든 댓글 조회
        return commentDao.selectAll();
    }

    public void update(CommentResponseDto dto) {
        // 댓글 수정 처리
        int rowCnt = 0;

        try {
            rowCnt = commentDao.update(dto);
            if (rowCnt != 1) {
                throw new InternalServerError();
            }

        } catch (DataIntegrityViolationException e) {
            throw new CommentFormInvalidException();
        }
    }

    public void increaseLikeCnt(Integer cno) {
        // 좋아요 수 증가
        commentDao.increaseLikeCnt(cno);
    }

    public void increaseDislikeCnt(Integer cno) {
        // 싫어요 수 증가
        commentDao.increaseDislikeCnt(cno);
    }

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
            throw new InternalServerError();
        }
    }


    public void removeByCno(Integer cno) {
        // 댓글 번호로 특정 댓글 삭제
        commentDao.deleteByCno(cno);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        // 모든 댓글 삭제
        int expectedRowCnt = commentDao.count();
        int rowCnt = commentDao.deleteAll();

        if (expectedRowCnt != rowCnt) {
            throw new InternalServerError();
        }
    }
}
