package com.example.demo.repository.board;

import com.example.demo.dto.board.BoardChangeHistoryDto;
import java.util.List;

public interface BoardChangeHistoryRepository {

    int count();

    int countByBno(Integer bno);

    boolean existsBySeq(Integer seq);

    boolean existsBySeqForUpdate(Integer seq);

    boolean existsByBno(Integer bno);

    boolean existsByBnoForUpdate(Integer bno);

    int insert(BoardChangeHistoryDto dto);

    int update(BoardChangeHistoryDto dto);

    BoardChangeHistoryDto selectBySeq(Integer seq);

    List<BoardChangeHistoryDto> selectByBno(Integer bno);

    List<BoardChangeHistoryDto> selectAll();

    BoardChangeHistoryDto selectLatestByBno(Integer bno);

    int deleteBySeq(Integer seq);

    int deleteByBno(Integer bno);

    int deleteAll();
}
