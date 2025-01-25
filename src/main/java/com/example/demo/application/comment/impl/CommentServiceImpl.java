package com.example.demo.application.comment.impl;

import com.example.demo.application.comment.CommentChangeHistoryService;
import com.example.demo.application.comment.CommentService;
import com.example.demo.application.reply.ReplyService;
import com.example.demo.dto.comment.CommentChangeHistoryRequest;
import com.example.demo.dto.comment.CommentDetailResponse;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.dto.comment.CommentRequest;
import com.example.demo.dto.comment.CommentResponse;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.business.comment.CommentNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BoardDaoImpl boardDao;
    private final CommentDaoImpl commentDao;
    private final ReplyService replyService;
    private final CommentChangeHistoryService commentChangeHistoryService;
    private final CustomFormatter formatter;


    @Override
    public CommentResponse create(final CommentRequest request) {
        checkBoardExists(request.getBno());
        var dto = createDto(request);
        checkApplied(1, commentDao.insert(dto));
        return createResponse(dto);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(final CommentRequest request) {
        checkCommentExistsForUpdate(request.getCno());
        var oldDto = commentDao.selectByCno(request.getCno());
        var dto = createDto(request);
        checkApplied(1, commentDao.update(dto));
        var historyRequest = CommentChangeHistoryRequest.builder()
                                                        .cno(request.getCno())
                                                        .bef_cont(oldDto.getCont())
                                                        .aft_cont(request.getCont())
                                                        .appl_time(formatter.getCurrentDateFormat())
                                                        .bno(request.getBno())
                                                        .build();
        commentChangeHistoryService.modify(historyRequest);
    }

    @Override
    public void increaseLikeCnt(final Integer cno) {
        checkCommentExistsForUpdate(cno);
        checkApplied(1, commentDao.increaseLikeCnt(cno));
    }

    @Override
    public void increaseDislikeCnt(final Integer cno) {
        checkCommentExistsForUpdate(cno);
        checkApplied(1, commentDao.increaseDislikeCnt(cno));
    }

    @Override
    public void remove(final Integer cno) {
        checkApplied(1, commentDao.deleteByCno(cno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByBno(final Integer bno) {
        checkApplied(commentDao.countByBno(bno), commentDao.deleteByBno(bno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        checkApplied(commentDao.count(), commentDao.deleteAll());
    }



    @Override
    public CommentResponse readByCno(final Integer cno) {
        checkCommentExists(cno);
        return createResponse(commentDao.selectByCno(cno));
    }

    @Override
    public List<CommentDetailResponse> readByBno(final Integer bno) {
        return null;
    }

    private void checkBoardExists(final Integer bno) {
        boolean exists = boardDao.existsByBno(bno);
        if (!exists) {
            log.error("[COMMENT] 댓글을 등록할 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }
    }

    private void checkCommentExists(final Integer cno) {
        boolean exists = commentDao.existsByCno(cno);
        if (!exists) {
            log.error("[COMMENT] 댓글이 존재하지 않습니다. cno: {}", cno);
            throw new CommentNotFoundException();
        }
    }

    private CommentDto createDto(final CommentRequest request) {
        CommentDto dto = CommentDto.builder()
                .bno(request.getBno())
                .cont(request.getCont())
                .user_seq(request.getUser_seq())
                .writer(request.getWriter())
                .reg_date(formatter.getCurrentDateFormat())
                .up_date(formatter.getCurrentDateFormat())
                .reg_user_seq(formatter.getManagerSeq())
                .up_user_seq(formatter.getManagerSeq())
                .build();
        return dto;
    }


    private CommentResponse createResponse(final CommentDto dto) {
        return CommentResponse.builder()
                .cno(dto.getCno())
                .bno(dto.getBno())
                .cont(dto.getCont())
                .like_cnt(dto.getLike_cnt())
                .dislike_cnt(dto.getDislike_cnt())
                .user_seq(dto.getUser_seq())
                .writer(dto.getWriter())
                .build();
    }

    private void checkApplied(final Integer expected, final Integer actual) {
        if (expected != actual) {
            log.error("[COMMENT] 댓글 처리 작업에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }
    }

    private void checkCommentExistsForUpdate(final Integer cno) {
        boolean exists = commentDao.existsByCnoForUpdate(cno);
        if (!exists) {
            log.error("[COMMENT] 댓글을 수정할 댓글이 존재하지 않습니다. cno: {}", cno);
            throw new CommentNotFoundException();
        }
    }


}
