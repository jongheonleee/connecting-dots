package com.example.demo.repository.mybatis.board;

import com.example.demo.dto.board.BestLikeBoardDto;
import com.example.demo.dto.board.BestLikeBoardUpdateDto;
import com.example.demo.dto.board.BestViewBoardDto;
import com.example.demo.dto.board.BestViewBoardUpdateDto;
import com.example.demo.dto.board.BoardResponseDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BestViewBoardDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.BestViewBoardMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countUsed() {
        return session.selectOne(namespace + "countUsed");
    }

    public void insert(BestViewBoardDto dto) {
        session.insert(namespace + "insert", dto);
    }

    public List<BoardResponseDto> selectForView(Map map) {
        return session.selectList(namespace + "selectForView", map);
    }

    public BestViewBoardDto selectOne(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    public void update(BestViewBoardUpdateDto dto) {
        session.update(namespace + "update", dto);
    }

    public void updateUsed() {
        session.update(namespace + "updateUsed");
    }
}
