package com.example.demo.repository.board;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardMainDto;
import java.util.List;
import java.util.Map;

public interface BoardRepository {

    int count();

    // 게시글 제목(TT), 작성자(WR), 카테고리(CT) 기반으로 검색
    int countBySearchCondition(SearchCondition sc);

    int countByCategory(String cate_code);

    boolean existsByBno(Integer bno);

    boolean existsByBnoForUpdate(Integer bno);

    int insert(BoardDto dto);

    BoardDto select(Integer bno);

    List<BoardDto> selectAll();

    List<BoardDto> selectBySearchCondition(SearchCondition sc);

    List<BoardMainDto> selectForMain(Map map);

    List<BoardMainDto> selectForMainByCategory(Map map);

    List<BoardMainDto> selectForMainBySearchCondition(SearchCondition sc);

    int update(BoardDto dto);

    int increaseViewCnt(BoardDto dto);

    int increaseRecoCnt(BoardDto dto);

    int increaseNotRecoCnt(BoardDto dto);

    int delete(Integer bno);

    int deleteAll();
}
