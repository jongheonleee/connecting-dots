package com.example.demo.application.board;

import com.example.demo.dto.board.BoardChangeHistoryDto;
import com.example.demo.dto.board.BoardChangeHistoryRequest;
import com.example.demo.dto.board.BoardChangeHistoryResponse;
import com.example.demo.global.error.exception.business.board.BoardChangeHistoryNotFoundException;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardChangeHistoryDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardChangeHistoryServiceImpl {

    private final BoardChangeHistoryDaoImpl boardChangeHistoryDao;
    private final BoardDaoImpl boardDao;
    private final CustomFormatter customFormatter;

    public int count() {
        return boardChangeHistoryDao.count();
    }

    public BoardChangeHistoryResponse readBySeq(Integer seq) {
        boolean exists = boardChangeHistoryDao.existsBySeq(seq);

        if (!exists) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 존재하지 않습니다. seq: {}", seq);
            throw new BoardChangeHistoryNotFoundException();
        }

        var found = boardChangeHistoryDao.selectBySeq(seq);
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

    public List<BoardChangeHistoryResponse> readByBno(Integer bno) {
        boolean exists = boardDao.existsByBno(bno);

        if (!exists) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }

        List<BoardChangeHistoryDto> founds = boardChangeHistoryDao.selectByBno(bno);
        return founds.stream()
            .map(found -> BoardChangeHistoryResponse.builder()
                .seq(found.getSeq())
                .bno(found.getBno())
                .title(found.getTitle())
                .cont(found.getCont())
                .comt(found.getComt())
                .appl_begin(found.getAppl_begin())
                .appl_end(found.getAppl_end())
                .build())
            .toList();
    }

    public List<BoardChangeHistoryResponse> readAll() {
        List<BoardChangeHistoryDto> founds = boardChangeHistoryDao.selectAll();
        return founds.stream()
            .map(found -> BoardChangeHistoryResponse.builder()
                .seq(found.getSeq())
                .bno(found.getBno())
                .title(found.getTitle())
                .cont(found.getCont())
                .comt(found.getComt())
                .appl_begin(found.getAppl_begin())
                .appl_end(found.getAppl_end())
                .build())
            .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public BoardChangeHistoryResponse renewBoardChangeHistory(Integer bno, BoardChangeHistoryRequest request) {
        boolean existsBoard = boardDao.existsByBno(bno);

        if (!existsBoard) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }

        boolean existsBoardChangeHistory = boardChangeHistoryDao.existsByBnoForUpdate(bno);

        if (!existsBoardChangeHistory) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 존재하지 않습니다. bno: {}", bno);
            throw new BoardChangeHistoryNotFoundException();
        }

        var found = boardChangeHistoryDao.selectLatestByBno(bno);
        found.setAppl_end(customFormatter.minusDateFormat(1));
        found.setUp_date(customFormatter.getCurrentDateFormat());
        found.setUp_user_seq(customFormatter.getManagerSeq());

        int rowCnt = boardChangeHistoryDao.update(found);

        if (rowCnt != 1) {
            log.error("[BOARD_CHANGE_HISTORY] 게시글 변경 이력 시간 업데이트 처리에 실패하였습니다. bno: {}", bno);
            throw new NotApplyOnDbmsException();
        }

        var newDto = BoardChangeHistoryDto.builder()
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

        rowCnt = boardChangeHistoryDao.insert(newDto);

        if (rowCnt != 1) {
            log.error("[BOARD_CHANGE_HISTORY] 게시글 변경 이력 시간 업데이트 및 새로운 변경 이력 추가 처리에 실패하였습니다. bno: {}", bno);
            throw new NotApplyOnDbmsException();
        }

        return BoardChangeHistoryResponse.builder()
            .seq(newDto.getSeq())
            .bno(newDto.getBno())
            .title(newDto.getTitle())
            .cont(newDto.getCont())
            .comt(newDto.getComt())
            .appl_begin(newDto.getAppl_begin())
            .appl_end(newDto.getAppl_end())
            .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public BoardChangeHistoryResponse createInit(Integer bno, BoardChangeHistoryRequest request) {
        boolean existsBoard = boardDao.existsByBno(bno);

        if (!existsBoard) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }

        boolean existsBoardChangeHistory = boardChangeHistoryDao.existsByBno(bno);

        if (existsBoardChangeHistory) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 이미 존재합니다. bno: {}", bno);
            throw new BoardChangeHistoryNotFoundException();
        }

        var newDto = BoardChangeHistoryDto.builder()
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

        int rowCnt = boardChangeHistoryDao.insert(newDto);

        if (rowCnt != 1) {
            log.error("[BOARD_CHANGE_HISTORY] 게시글 변경 이력 추가 처리에 실패하였습니다. bno: {}", bno);
            throw new NotApplyOnDbmsException();
        }

        return BoardChangeHistoryResponse.builder()
            .seq(newDto.getSeq())
            .bno(newDto.getBno())
            .title(newDto.getTitle())
            .cont(newDto.getCont())
            .comt(newDto.getComt())
            .appl_begin(newDto.getAppl_begin())
            .appl_end(newDto.getAppl_end())
            .build();
    }

    public void removeBySeq(Integer seq) {
        boolean exists = boardChangeHistoryDao.existsBySeq(seq);

        if (!exists) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 존재하지 않습니다. seq: {}", seq);
            throw new BoardChangeHistoryNotFoundException();
        }

        int rowCnt = boardChangeHistoryDao.deleteBySeq(seq);

        if (rowCnt != 1) {
            log.error("[BOARD_CHANGE_HISTORY] 게시글 변경 이력 삭제 처리에 실패하였습니다. seq: {}", seq);
            throw new NotApplyOnDbmsException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByBno(Integer bno) {
        boolean existsBoard = boardDao.existsByBno(bno);

        if (!existsBoard) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }

        boolean existsBoardChangeHistory = boardChangeHistoryDao.existsByBno(bno);

        if (!existsBoardChangeHistory) {
            log.error("[BOARD_CHANGE_HISTORY] 해당 게시글 변경 이력이 존재하지 않습니다. bno: {}", bno);
            throw new BoardChangeHistoryNotFoundException();
        }

        int totalCnt = boardChangeHistoryDao.countByBno(bno);
        int rowCnt = boardChangeHistoryDao.deleteByBno(bno);

        if (rowCnt != totalCnt) {
            log.error("[BOARD_CHANGE_HISTORY] 게시글 변경 이력 삭제 처리에 실패하였습니다. bno: {}", bno);
            throw new NotApplyOnDbmsException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = boardChangeHistoryDao.count();
        int rowCnt = boardChangeHistoryDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[BOARD_CHANGE_HISTORY] 게시글 변경 이력 전체 삭제 처리에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }
    }
}
