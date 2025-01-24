package com.example.demo.application.comment;

import com.example.demo.dto.comment.CommentChangeHistoryDto;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.dto.comment.CommentRequest;
import com.example.demo.dto.comment.CommentResponse;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.business.comment.CommentNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentChangeHistoryDaoImpl;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import com.example.demo.repository.mybatis.reply.ReplyDaoImpl;
import com.example.demo.utils.CustomFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl {

    private final BoardDaoImpl boardDao;
    private final CommentDaoImpl commentDao;
    private final ReplyDaoImpl replyDao;
    private final CommentChangeHistoryServiceImpl commentChangeHistoryService;
    private final CustomFormatter formatter;


    public CommentResponse create(final CommentRequest request) {
        boolean exists = boardDao.existsByBno(request.getBno());
        if (!exists) {
            log.error("[COMMENT] 댓글을 등록할 게시글이 존재하지 않습니다. bno: {}", request.getBno());
            throw new BoardNotFoundException();
        }

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

        int rowCnt = commentDao.insert(dto);
        if (rowCnt != 1) {
            log.error("[COMMENT] 댓글 등록에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }

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

    // 이 부분 다시 작성해야함. 변경 이력 기록하는 부분 추가해야함
    public void modify(final CommentRequest request) {
        boolean exists = commentDao.existsByCnoForUpdate(request.getCno());
        if (!exists) {
            log.error("[COMMENT] 댓글을 수정할 댓글이 존재하지 않습니다. cno: {}", request.getCno());
            throw new CommentNotFoundException();
        }

        int rowCnt = commentDao.update(CommentDto.builder()
                                                  .cno(request.getCno())
                                                  .cont(request.getCont())
                                                  .up_date(formatter.getCurrentDateFormat())
                                                  .up_user_seq(formatter.getManagerSeq())
                                                  .build());

        if (rowCnt != 1) {
            log.error("[COMMENT] 댓글 수정에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }

//        CommentDto commentDto = commentDao.selectByCno(request.getCno());
//        CommentChangeHistoryDto commentChangeHistoryDto = CommentChangeHistoryDto.builder()
//                .bef_cont(commentDto.getCont())
//                .aft_cont(request.getCont())
//                .appl_time(formatter.getCurrentDateFormat())
//                .reg_date(formatter.getCurrentDateFormat())
//                .reg_user_seq(formatter.getManagerSeq())
//                .up_date(formatter.getCurrentDateFormat())
//                .up_user_seq(formatter.getManagerSeq())
//                .cno(request.getCno())
//                .bno(commentDto.getBno())
//                .build();
//        rowCnt = commentChangeHistoryDao.insert(commentChangeHistoryDto);

        // 가장 최근 변경 이력을 조회
        // 종료 시간 현재 시간으로 업데이트
        // 새로운 변경 이력 추가

        if (rowCnt != 1) {
            log.error("[COMMENT] 댓글 수정에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }

    }

    public void increaseLikeCnt(final Integer cno) {
        boolean exists = commentDao.existsByCnoForUpdate(cno);
        if (!exists) {
            log.error("[COMMENT] 좋아요 수를 증가할 댓글이 존재하지 않습니다. cno: {}", cno);
            throw new CommentNotFoundException();
        }

        int rowCnt = commentDao.increaseLikeCnt(cno);
        if (rowCnt != 1) {
            log.error("[COMMENT] 좋아요 수 증가에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }

    }

    public void increaseDislikeCnt(final Integer cno) {
        boolean exists = commentDao.existsByCnoForUpdate(cno);
        if (!exists) {
            log.error("[COMMENT] 싫어요 수를 증가할 댓글이 존재하지 않습니다. cno: {}", cno);
            throw new CommentNotFoundException();
        }

        int rowCnt = commentDao.increaseDislikeCnt(cno);
        if (rowCnt != 1) {
            log.error("[COMMENT] 싫어요 수 증가에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }
    }
}
