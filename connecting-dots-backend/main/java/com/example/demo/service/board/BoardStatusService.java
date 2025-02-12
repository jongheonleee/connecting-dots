package com.example.demo.service.board;

import com.example.demo.dto.board.BoardStatusRequest;
import com.example.demo.dto.board.BoardStatusResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface BoardStatusService {

    BoardStatusResponse create(BoardStatusRequest request);

    void renewState(BoardStatusRequest request);

    void removeBySeq(Integer seq);

    List<BoardStatusResponse> readByBno(Integer bno);

    BoardStatusResponse readByBnoAtPresent(Integer bno);

    List<BoardStatusResponse> readAll();

    void removeByBno(Integer bno);

    void removeAll();

    BoardStatusResponse readBySeq(Integer seq);
}
