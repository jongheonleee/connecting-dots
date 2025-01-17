package com.example.demo.application.board;

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
public class BoardStatusServiceImpl {

    private final BoardDaoImpl boardDao;
    private final BoardStatusDaoImpl boardStatusDao;
    private final CustomFormatter formatter;


    public BoardStatusResponse create(final BoardStatusRequest request) {
        Code statCode = Code.of(request.getStat_code());

        boolean exists = boardDao.existsByBno(request.getBno());
        if (!exists) {
            log.error("[BOARD_STATUS] 게시글이 존재하지 않습니다. bno: {}", request.getBno());
            throw new BoardNotFoundException();
        }

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

        int rowCnt = boardStatusDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[BOARD_STATUS] 게시글 상태 등록에 실패하였습니다. bno: {}", request.getBno());
            throw new NotApplyOnDbmsException();
        }

        return BoardStatusResponse.builder()
                                  .seq(dto.getSeq())
                                  .bno(dto.getBno())
                                  .stat_code(dto.getStat_code())
                                  .comt(dto.getComt())
                                  .appl_begin(dto.getAppl_begin())
                                  .appl_end(dto.getAppl_end())
                                  .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void renewState(final BoardStatusRequest request) {
        boolean exists = boardStatusDao.existsByBnoForUpdate(request.getBno());

        if (!exists) {
            log.error("[BOARD_STATUS] 게시글 상태가 존재하지 않습니다. bno: {}", request.getBno());
            throw new BoardNotFoundException();
        }

        var currStatDto = boardStatusDao.selectByBnoAtPresent(request.getBno());
        currStatDto.updateApplTime(formatter.minusDateFormat(1), formatter.getCurrentDateFormat(), formatter.getManagerSeq());

        int rowCnt = boardStatusDao.update(currStatDto);
        if (rowCnt != 1) {
            log.error("[BOARD_STATUS] 게시글 상태 수정에 실패하였습니다. bno: {}", request.getBno());
            throw new NotApplyOnDbmsException();
        }

        Code statCode = Code.of(currStatDto.getStat_code());
        var newStatDto = BoardStatusDto.builder()
                                       .bno(request.getBno())
                                       .stat_code(statCode.getCode())
                                       .appl_begin(formatter.getCurrentDateFormat())
                                       .appl_end(formatter.plusDateFormat(request.getDays()))
                                       .reg_date(formatter.getCurrentDateFormat())
                                       .reg_user_seq(formatter.getManagerSeq())
                                       .up_date(formatter.getCurrentDateFormat())
                                       .up_user_seq(formatter.getManagerSeq())
                                       .build();

        rowCnt = boardStatusDao.insert(newStatDto);
        if (rowCnt != 1) {
            log.error("[BOARD_STATUS] 게시글 상태 등록에 실패하였습니다. bno: {}", request.getBno());
            throw new NotApplyOnDbmsException();
        }

    }

    public void removeBySeq(Integer seq) {
        int rowCnt = boardStatusDao.deleteBySeq(seq);

        if (rowCnt != 1) {
            log.error("[BOARD_STATUS] 게시글 상태 삭제에 실패하였습니다. seq: {}", seq);
            throw new NotApplyOnDbmsException();
        }
    }

    public BoardStatusResponse readBySeq(Integer seq) {
        boolean exists = boardStatusDao.existsBySeq(seq);

        if (!exists) {
            log.error("[BOARD_STATUS] 게시글 상태가 존재하지 않습니다. seq: {}", seq);
            throw new BoardStatusNotFoundException();
        }

        BoardStatusDto found = boardStatusDao.selectBySeq(seq);

        return BoardStatusResponse.builder()
                                  .seq(found.getSeq())
                                  .bno(found.getBno())
                                  .stat_code(found.getStat_code())
                                  .comt(found.getComt())
                                  .appl_begin(found.getAppl_begin())
                                  .appl_end(found.getAppl_end())
                                  .build();
    }

    public List<BoardStatusResponse> readByBno(Integer bno) {
        boolean exists = boardDao.existsByBno(bno);

        if (!exists) {
            log.error("[BOARD_STATUS] 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }

        List<BoardStatusDto> found = boardStatusDao.selectByBno(bno);
        return found.stream()
                    .map(dto -> BoardStatusResponse.builder()
                                                   .seq(dto.getSeq())
                                                   .bno(dto.getBno())
                                                   .stat_code(dto.getStat_code())
                                                   .comt(dto.getComt())
                                                   .appl_begin(dto.getAppl_begin())
                                                   .appl_end(dto.getAppl_end())
                                                   .build())
                    .toList();
    }

    public List<BoardStatusResponse> readAll() {
        List<BoardStatusDto> found = boardStatusDao.selectAll();
        return found.stream()
                    .map(dto -> BoardStatusResponse.builder()
                                                   .seq(dto.getSeq())
                                                   .bno(dto.getBno())
                                                   .stat_code(dto.getStat_code())
                                                   .comt(dto.getComt())
                                                   .appl_begin(dto.getAppl_begin())
                                                   .appl_end(dto.getAppl_end())
                                                   .build())
                    .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByBno(Integer bno) {
        boolean exists = boardDao.existsByBnoForUpdate(bno);

        if (!exists) {
            log.error("[BOARD_STATUS] 게시글 상태가 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }

        int totalCnt = boardStatusDao.countByBno(bno);
        int rowCnt = boardStatusDao.deleteByBno(bno);

        if (rowCnt != totalCnt) {
            log.error("[BOARD_STATUS] 게시글 상태 삭제에 실패하였습니다. bno: {}", bno);
            throw new NotApplyOnDbmsException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = boardStatusDao.count();
        int rowCnt = boardStatusDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[BOARD_STATUS] 게시글 상태 전체 삭제에 실패하였습니다.");
            throw new NotApplyOnDbmsException();
        }
    }


}
