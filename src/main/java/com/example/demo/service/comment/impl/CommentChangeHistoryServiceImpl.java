package com.example.demo.service.comment.impl;

import com.example.demo.service.comment.CommentChangeHistoryService;
import com.example.demo.dto.comment.CommentChangeHistoryDto;
import com.example.demo.dto.comment.CommentChangeHistoryRequest;
import com.example.demo.dto.comment.CommentChangeHistoryResponse;
import com.example.demo.global.error.exception.business.comment.CommentChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.comment.CommentChangeHistoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentChangeHistoryServiceImpl implements CommentChangeHistoryService {

    private final CommentChangeHistoryDaoImpl commentChangeHistoryDao;
    private final CustomFormatter formatter;

    @Override
    public CommentChangeHistoryResponse create(final CommentChangeHistoryRequest request) {
        CommentChangeHistoryDto dto = createDto(request);
        checkApplied(1, commentChangeHistoryDao.insert(dto));
        return createResponse(dto);
    }


    @Override
    public CommentChangeHistoryResponse readBySeq(final Integer seq) {
        CommentChangeHistoryDto dto = commentChangeHistoryDao.select(seq);
        checkEmpty(seq, dto);
        return createResponse(dto);
    }


    @Override
    public List<CommentChangeHistoryResponse> readByCno(final Integer cno) {
        List<CommentChangeHistoryDto> found = commentChangeHistoryDao.selectByCno(cno);
        return found.stream()
                    .map(this::createResponse)
                    .toList();
    }

    @Override
    public void modify(final CommentChangeHistoryRequest request) {
        checkExistsForUpdate(request);
        var dto = createDto(request);
        checkApplied(1, commentChangeHistoryDao.update(dto));
    }


    @Override
    public void remove(final Integer seq) {
        checkApplied(1, commentChangeHistoryDao.delete(seq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByCno(final Integer cno) {
        int totalCnt = commentChangeHistoryDao.countByCno(cno);
        checkApplied(totalCnt, commentChangeHistoryDao.deleteByCno(cno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = commentChangeHistoryDao.count();
        checkApplied(totalCnt, commentChangeHistoryDao.deleteAll());
    }

    private void checkExistsForUpdate(CommentChangeHistoryRequest request) {
        boolean exists = commentChangeHistoryDao.existsByCnoForUpdate(request.getCno());
        if (!exists) {
            log.error("[COMMENT CHANGE HISTORY] 댓글을 수정할 댓글이 존재하지 않습니다. cno: {}", request.getCno());
            throw new CommentChangeHistoryNotFoundException();
        }
    }

    private void checkApplied(final Integer expected, final Integer actual) {
        if (expected != actual) {
            log.error("[COMMENT CHANGE HISTORY] 댓글 변경 이력 등록에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }
    }

    private CommentChangeHistoryDto createDto(
            CommentChangeHistoryRequest request) {
        CommentChangeHistoryDto dto = CommentChangeHistoryDto.builder()
                .bef_cont(request.getBef_cont())
                .aft_cont(request.getAft_cont())
                .appl_time(request.getAppl_time())
                .cno(request.getCno())
                .bno(request.getBno())
                .reg_date(formatter.getCurrentDateFormat())
                .reg_user_seq(formatter.getManagerSeq())
                .up_user_seq(formatter.getManagerSeq())
                .up_date(formatter.getCurrentDateFormat())
                .build();
        return dto;
    }

    private CommentChangeHistoryResponse createResponse(CommentChangeHistoryDto dto) {
        return CommentChangeHistoryResponse.builder()
                .seq(dto.getSeq())
                .bef_cont(dto.getBef_cont())
                .aft_cont(dto.getAft_cont())
                .appl_time(dto.getAppl_time())
                .cno(dto.getCno())
                .bno(dto.getBno())
                .build();
    }

    private void checkEmpty(Integer seq, CommentChangeHistoryDto dto) {
        if (dto == null) {
            log.error("[COMMENT CHANGE HISTORY] 댓글 변경 이력이 존재하지 않습니다. seq: {}", seq);
            throw new CommentChangeHistoryNotFoundException();
        }
    }
}
