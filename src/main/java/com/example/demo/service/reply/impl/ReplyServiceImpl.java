package com.example.demo.service.reply.impl;

import com.example.demo.service.reply.ReplyChangeHistoryService;
import com.example.demo.service.reply.ReplyService;
import com.example.demo.dto.reply.ReplyChangeHistoryRequest;
import com.example.demo.dto.reply.ReplyDto;
import com.example.demo.dto.reply.ReplyRequest;
import com.example.demo.dto.reply.ReplyResponse;
import com.example.demo.global.error.exception.business.reply.ReplyNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.reply.ReplyDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyDaoImpl replyDao;
    private final ReplyChangeHistoryService replyChangeHistoryService;
    private final CustomFormatter formatter;

    @Override
    public ReplyResponse create(final ReplyRequest request) {
        var dto = createDto(request);
        checkApplied(1, replyDao.insert(dto));
        return createResponse(dto);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(final ReplyRequest request) {
        checkExistsForUpdate(request.getRcno());
        var foundOld = replyDao.select(request.getRcno());
        var historyRequest = ReplyChangeHistoryRequest.builder()
                                                      .rcno(request.getRcno())
                                                      .cno(request.getCno())
                                                      .bno(request.getBno())
                                                      .bef_cont(foundOld.getCont())
                                                      .aft_cont(request.getCont())
                                                      .appl_time(formatter.getCurrentDateFormat())
                                                      .build();
        replyChangeHistoryService.create(historyRequest);
        var dto = createDto(request);
        checkApplied(1, replyDao.update(dto));
    }


    @Override
    public void remove(final Integer rcno) {
        checkApplied(1, replyDao.delete(rcno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByCno(final Integer cno) {
        checkApplied(replyDao.countByCno(cno), replyDao.deleteByCno(cno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByBno(final Integer bno) {
        checkApplied(replyDao.countByBno(bno), replyDao.deleteByBno(bno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        checkApplied(replyDao.count(), replyDao.deleteAll());
    }

    @Override
    public ReplyResponse readByRcno(final Integer rcno) {
        var found = replyDao.select(rcno);

        if (found == null) {
            log.error("대댓글이 존재하지 않습니다. rcno: {}", rcno);
            throw new ReplyNotFoundException();
        }

        return createResponse(found);
    }

    @Override
    public List<ReplyResponse> readByCno(final Integer cno) {
        return replyDao.selectByCno(cno)
                        .stream()
                        .map(this::createResponse)
                        .toList();
    }

    @Override
    public List<ReplyResponse> readAll() {
        return replyDao.selectAll()
                        .stream()
                        .map(this::createResponse)
                        .toList();
    }

    private void checkExistsForUpdate(final Integer rcno) {
        boolean exists = replyDao.existsByRcnoForUpdate(rcno);
        if (!exists) {
            log.error("수정할 대댓글이 존재하지 않습니다. rcno: {}",rcno);
            throw new ReplyNotFoundException();
        }
    }

    private ReplyResponse createResponse(final ReplyDto dto) {
        return ReplyResponse.builder()
                .rcno(dto.getRcno())
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
            log.error("댓글 처리중 오류가 발생하였습니다. expected: {}, actual: {}", expected, actual);
            throw new NotApplyOnDbmsException();
        }
    }

    private ReplyDto createDto(final ReplyRequest request) {
        var dto = ReplyDto.builder()
                .rcno(request.getRcno())
                .cno(request.getCno())
                .bno(request.getBno())
                .cont(request.getCont())
                .writer(request.getWriter())
                .user_seq(request.getUser_seq())
                .reg_date(formatter.getCurrentDateFormat())
                .reg_user_seq(formatter.getManagerSeq())
                .up_date(formatter.getCurrentDateFormat())
                .reg_user_seq(formatter.getManagerSeq())
                .build();
        return dto;
    }


}
