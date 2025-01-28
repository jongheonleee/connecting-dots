package com.example.demo.repository.board;

import com.example.demo.dto.board.BoardStatusDto;
import java.util.List;

public interface BoardStatusRepository {

    int count();

    int countByBno(Integer bno);

    boolean existsBySeq(Integer seq);

    boolean existsBySeqForUpdate(Integer seq);

    boolean existsByBno(Integer bno);

    boolean existsByBnoForUpdate(Integer bno);

    int deleteAll();

    int insert(BoardStatusDto dto);

    List<BoardStatusDto> selectAll();

    List<BoardStatusDto> selectByBno(Integer bno);

    BoardStatusDto selectBySeq(Integer seq);

    BoardStatusDto selectByBnoAtPresent(Integer bno);

    int update(BoardStatusDto dto);

    int deleteBySeq(Integer seq);

    int deleteByBno(Integer bno);
}
