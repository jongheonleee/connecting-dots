package com.example.demo.service.board.impl;

import com.example.demo.repository.board.BoardChangeHistoryRepository;
import com.example.demo.repository.board.BoardRepository;
import com.example.demo.service.board.BoardChangeHistoryService;
import com.example.demo.dto.board.BoardChangeHistoryDto;
import com.example.demo.dto.board.BoardChangeHistoryRequest;
import com.example.demo.dto.board.BoardChangeHistoryResponse;
import com.example.demo.global.error.exception.business.board.BoardChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardChangeHistoryServiceImpl implements BoardChangeHistoryService {

    private final BoardChangeHistoryRepository boardChangeHistoryDao;
    private final BoardRepository boardDao;
    private final CustomFormatter customFormatter;

    @Override
    public int count() {
        return boardChangeHistoryDao.count();
    }

    @Override
    public BoardChangeHistoryResponse readBySeq(final Integer seq) {
        checkBoardChangeHistoryExists(seq);
        var found = boardChangeHistoryDao.selectBySeq(seq);
        return createResponse(found);
    }

    @Override
    public List<BoardChangeHistoryResponse> readByBno(final Integer bno) {
        checkBoardExists(bno);
        return boardChangeHistoryDao.selectByBno(bno)
                                    .stream()
                                    .map(this::createResponse)
                                    .toList();
    }


    @Override
    public List<BoardChangeHistoryResponse> readAll() {
        return boardChangeHistoryDao.selectAll()
                                    .stream()
                                    .map(this::createResponse)
                                    .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoardChangeHistoryResponse renewBoardChangeHistory(final Integer bno,
            final BoardChangeHistoryRequest request) {
        checkBoardExists(bno);
        checkBoardChangeHistoryExistsByBnoForUpdate(bno);

        var found = boardChangeHistoryDao.selectLatestByBno(bno);
        found.updateApplEnd(customFormatter.minusDateFormat(1), customFormatter.getLastDateFormat(), customFormatter.getManagerSeq());
        var newDto = createDto(bno, request);

        checkApplied(2, boardChangeHistoryDao.update(found) + boardChangeHistoryDao.insert(newDto));
        return createResponse(newDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoardChangeHistoryResponse createInit(final Integer bno,
            final BoardChangeHistoryRequest request) {
        checkBoardExists(bno);
        checkBoardChangeHistoryAlreadyExists(bno);
        var newDto = createDto(bno, request);
        checkApplied(1, boardChangeHistoryDao.insert(newDto));
        return createResponse(newDto);
    }


    @Override
    public void removeBySeq(final Integer seq) {
        checkBoardChangeHistoryExists(seq);
        checkApplied(1, boardChangeHistoryDao.deleteBySeq(seq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByBno(final Integer bno) {
        checkBoardExists(bno);
        checkBoardChangeHistoryExistsByBno(bno);
        checkApplied(boardChangeHistoryDao.countByBno(bno), boardChangeHistoryDao.deleteByBno(bno));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        checkApplied(boardChangeHistoryDao.count(), boardChangeHistoryDao.deleteAll());
    }

    private void checkBoardChangeHistoryExistsByBno(final Integer bno) {
        boolean existsBoardChangeHistory = boardChangeHistoryDao.existsByBno(bno);

        if (!existsBoardChangeHistory) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 존재하지 않습니다. bno: {}", bno);
            throw new BoardChangeHistoryNotFoundException();
        }
    }



    private void checkBoardChangeHistoryAlreadyExists(final Integer bno) {
        boolean existsBoardChangeHistory = boardChangeHistoryDao.existsByBno(bno);

        if (existsBoardChangeHistory) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 이미 존재합니다. bno: {}", bno);
            throw new BoardChangeHistoryNotFoundException();
        }
    }

    private BoardChangeHistoryResponse createResponse(final BoardChangeHistoryDto found) {
        return BoardChangeHistoryResponse.builder()
                .seq(found.getSeq())
                .bno(found.getBno())
                .title(found.getTitle())
                .cont(found.getCont())
                .comt(found.getComt())
                .appl_begin(found.getAppl_begin())
                .appl_end(found.getAppl_end())
                .build();
    }

    private void checkBoardChangeHistoryExists(final Integer seq) {
        boolean exists = boardChangeHistoryDao.existsBySeq(seq);

        if (!exists) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 존재하지 않습니다. seq: {}", seq);
            throw new BoardChangeHistoryNotFoundException();
        }
    }

    private void checkBoardExists(final Integer bno) {
        boolean exists = boardDao.existsByBno(bno);

        if (!exists) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }
    }

    private void checkBoardChangeHistoryExistsByBnoForUpdate(final Integer bno) {
        boolean existsBoardChangeHistory = boardChangeHistoryDao.existsByBnoForUpdate(bno);

        if (!existsBoardChangeHistory) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 존재하지 않습니다. bno: {}", bno);
            throw new BoardChangeHistoryNotFoundException();
        }
    }

    private void checkApplied(final Integer expected, final Integer actual) {
        if (expected != actual) {
            log.error("[BOARD_CHANGE_HISTORY] 게시글 변경 이력 처리에 실패하였습니다. expected: {}, actual: {}", expected, actual);
            throw new NotApplyOnDbmsException();
        }
    }

    private BoardChangeHistoryDto createDto(final Integer bno, final BoardChangeHistoryRequest request) {
        return BoardChangeHistoryDto.builder()
                .bno(bno)
                .title(request.getTitle())
                .cont(request.getCont())
                .comt(request.getComt())
                .appl_begin(customFormatter.getCurrentDateFormat())
                .appl_end(customFormatter.getLastDateFormat())
                .reg_date(customFormatter.getCurrentDateFormat())
                .reg_user_seq(customFormatter.getManagerSeq())
                .up_date(customFormatter.getLastDateFormat())
                .up_user_seq(customFormatter.getManagerSeq())
                .build();
    }
}
