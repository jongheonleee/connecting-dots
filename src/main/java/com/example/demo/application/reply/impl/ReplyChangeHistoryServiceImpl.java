package com.example.demo.application.reply.impl;

import com.example.demo.application.reply.ReplyChangeHistoryService;
import com.example.demo.dto.reply.ReplyChangeHistoryDto;
import com.example.demo.dto.reply.ReplyChangeHistoryRequest;
import com.example.demo.dto.reply.ReplyChangeHistoryResponse;
import com.example.demo.global.error.exception.business.reply.ReplyChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.reply.ReplyChangeHistoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyChangeHistoryServiceImpl implements ReplyChangeHistoryService {

    private final ReplyChangeHistoryDaoImpl replyChangeHistoryDao;
    private final CustomFormatter formatter;

    @Override
    public ReplyChangeHistoryResponse create(final ReplyChangeHistoryRequest request) {
        var dto = createDto(request);
        checkApplied(1, replyChangeHistoryDao.insert(dto));
        return createResponse(dto);
    }


    @Override
    public ReplyChangeHistoryResponse readBySeq(final Integer seq) {
        var found = replyChangeHistoryDao.selectBySeq(seq);
        if (found == null) {
            log.error("대댓글 변경 이력이 존재하지 않습니다. seq: {}", seq);
            throw new ReplyChangeHistoryNotFoundException();
        }
        return createResponse(found);
    }

    @Override
    public List<ReplyChangeHistoryResponse> readByRcno(final Integer rcno) {
        var found = replyChangeHistoryDao.selectByRcno(rcno);
        return found.stream()
                    .map(this::createResponse)
                    .toList();
    }

    @Override
    public List<ReplyChangeHistoryResponse> readAll() {
        var found = replyChangeHistoryDao.selectAll();
        return found.stream()
                    .map(this::createResponse)
                    .toList();
    }

    @Override
    public void modify(final ReplyChangeHistoryRequest request) {
        checkExistsForUpdate(request);
        var dto = createDto(request);
        checkApplied(1, replyChangeHistoryDao.update(dto));
    }


    @Override
    public void remove(final Integer seq) {
        checkApplied(1, replyChangeHistoryDao.deleteBySeq(seq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRcno(final Integer rcno) {
        checkApplied(replyChangeHistoryDao.countByRcno(rcno), replyChangeHistoryDao.deleteByRcno(rcno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        checkApplied(replyChangeHistoryDao.count(), replyChangeHistoryDao.deleteAll());
    }


    private void checkApplied(Integer expected, Integer actual) {
        if (expected != actual) {
            log.error("[REPLY CHANGE HISTORY] 적용된 행의 수가 일치하지 않습니다. expected: {}, actual: {}", expected, actual);
            throw new NotApplyOnDbmsException();
        }
    }

    private ReplyChangeHistoryDto createDto(ReplyChangeHistoryRequest request) {
        ReplyChangeHistoryDto dto = ReplyChangeHistoryDto.builder()
                .rcno(request.getRcno())
                .bno(request.getBno())
                .cno(request.getCno())
                .bef_cont(request.getBef_cont())
                .aft_cont(request.getAft_cont())
                .appl_time(formatter.getCurrentDateFormat())
                .comt(request.getComt())
                .build();
        return dto;
    }

    private ReplyChangeHistoryResponse createResponse(final ReplyChangeHistoryDto dto) {
        ReplyChangeHistoryResponse response = ReplyChangeHistoryResponse.builder()
                .rcno(dto.getRcno())
                .bno(dto.getBno())
                .cno(dto.getCno())
                .bef_cont(dto.getBef_cont())
                .aft_cont(dto.getAft_cont())
                .appl_time(dto.getAppl_time())
                .comt(dto.getComt())
                .build();
        return response;
    }

    private void checkExistsForUpdate(ReplyChangeHistoryRequest request) {
        boolean exists = replyChangeHistoryDao.existsBySeqForUpdate(request.getRcno());
        if (!exists) {
            log.error("대댓글 변경 이력이 존재하지 않습니다. rcno: {}", request.getRcno());
            throw new ReplyChangeHistoryNotFoundException();
        }
    }


}
