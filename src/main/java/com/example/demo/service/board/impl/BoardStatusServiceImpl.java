package com.example.demo.service.board.impl;

import com.example.demo.service.board.BoardStatusService;
import com.example.demo.domain.Code;
import com.example.demo.dto.board.BoardStatusDto;
import com.example.demo.dto.board.BoardStatusRequest;
import com.example.demo.dto.board.BoardStatusResponse;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.business.board.BoardStatusNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.board.BoardStatusDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardStatusServiceImpl implements BoardStatusService {

    private final BoardDaoImpl boardDao;
    private final BoardStatusDaoImpl boardStatusDao;
    private final CustomFormatter formatter;


    @Override
    public BoardStatusResponse create(final BoardStatusRequest request) {
        var statCode = Code.of(request.getStat_code());
        checkBoardExists(request.getBno());
        var dto = createDto(request, statCode);
        checkApplied(1, boardStatusDao.insert(dto));
        return createResponse(dto);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void renewState(final BoardStatusRequest request) {
        checkBoardStatusExists(request);

        var currStatDto = boardStatusDao.selectByBnoAtPresent(request.getBno());
        currStatDto.updateApplTime(formatter.minusDateFormat(1), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        checkApplied(1, boardStatusDao.update(currStatDto));

        var statCode = Code.of(currStatDto.getStat_code());
        var newStatDto = createDto(request, statCode);
        checkApplied(1, boardStatusDao.insert(newStatDto));

    }


    @Override
    public void removeBySeq(final Integer seq) {
        int rowCnt = boardStatusDao.deleteBySeq(seq);
        checkApplied(1, rowCnt);
    }


    @Override
    public List<BoardStatusResponse> readByBno(final Integer bno) {
        checkBoardExists(bno);
        List<BoardStatusDto> found = boardStatusDao.selectByBno(bno);
        return found.stream()
                    .map(this::createResponse)
                    .toList();
    }

    @Override
    public BoardStatusResponse readByBnoAtPresent(final Integer bno) {
        checkBoardExists(bno);
        var found = boardStatusDao.selectByBnoAtPresent(bno);
        return createResponse(found);
    }

    @Override
    public List<BoardStatusResponse> readAll() {
        List<BoardStatusDto> found = boardStatusDao.selectAll();
        return found.stream()
                    .map(this::createResponse)
                    .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByBno(final Integer bno) {
        checkBoardExists(bno);
        int totalCnt = boardStatusDao.countByBno(bno);
        int rowCnt = boardStatusDao.deleteByBno(bno);
        checkApplied(totalCnt, rowCnt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = boardStatusDao.count();
        int rowCnt = boardStatusDao.deleteAll();
        checkApplied(totalCnt, rowCnt);
    }

    @Override
    public BoardStatusResponse readBySeq(final Integer seq) {
        boolean exists = boardStatusDao.existsBySeq(seq);

        if (!exists) {
            log.error("[BOARD_STATUS] 게시글 상태가 존재하지 않습니다. seq: {}", seq);
            throw new BoardStatusNotFoundException();
        }

        var found = boardStatusDao.selectBySeq(seq);
        return createResponse(found);
    }



    private void checkBoardExists(Integer bno) {
        boolean exists = boardDao.existsByBno(bno);
        if (!exists) {
            log.error("[BOARD_STATUS] 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }
    }

    private void checkBoardStatusExists(BoardStatusRequest request) {
        boolean exists = boardStatusDao.existsByBnoForUpdate(request.getBno());

        if (!exists) {
            log.error("[BOARD_STATUS] 게시글 상태가 존재하지 않습니다. bno: {}", request.getBno());
            throw new BoardStatusNotFoundException();
        }
    }

    private BoardStatusDto createDto(BoardStatusRequest request, Code statCode) {
        var dto = BoardStatusDto.builder()
                .bno(request.getBno())
                .stat_code(statCode.getCode())
                .appl_begin(formatter.getCurrentDateFormat())
                .appl_end(formatter.plusDateFormat(request.getDays()))
                .reg_date(formatter.getCurrentDateFormat())
                .reg_user_seq(formatter.getManagerSeq())
                .up_date(formatter.getCurrentDateFormat())
                .up_user_seq(formatter.getManagerSeq())
                .build();
        return dto;
    }

    private void checkApplied(Integer expected, Integer actual) {
        if (!expected.equals(actual)) {
            log.error("[BOARD_STATUS] 게시글 상태 적용에 실패하였습니다. expected: {}, actual: {}", expected, actual);
            throw new NotApplyOnDbmsException();
        }
    }


    private BoardStatusResponse createResponse(BoardStatusDto dto) {
        return BoardStatusResponse.builder()
                .seq(dto.getSeq())
                .bno(dto.getBno())
                .stat_code(dto.getStat_code())
                .comt(dto.getComt())
                .appl_begin(dto.getAppl_begin())
                .appl_end(dto.getAppl_end())
                .build();
    }
}
