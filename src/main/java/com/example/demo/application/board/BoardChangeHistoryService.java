package com.example.demo.application.board;

import com.example.demo.dto.board.BoardChangeHistoryRequest;
import com.example.demo.dto.board.BoardChangeHistoryResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface BoardChangeHistoryService {

    int count();

    BoardChangeHistoryResponse readBySeq(Integer seq);

    List<BoardChangeHistoryResponse> readByBno(Integer bno);

    List<BoardChangeHistoryResponse> readAll();

    @Transactional(rollbackFor = Exception.class)
    BoardChangeHistoryResponse renewBoardChangeHistory(Integer bno,
            BoardChangeHistoryRequest request);

    @Transactional(rollbackFor = Exception.class)
    BoardChangeHistoryResponse createInit(Integer bno, BoardChangeHistoryRequest request);

    void removeBySeq(Integer seq);

    @Transactional(rollbackFor = Exception.class)
    void removeByBno(Integer bno);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
