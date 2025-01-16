package com.example.demo.repository.mybatis.board;

import com.example.demo.dto.ord_board.BestCommentBoardDto;
import com.example.demo.dto.ord_board.BestCommentBoardUpdateDto;
import com.example.demo.dto.ord_board.BoardResponseDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BestCommentBoardDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.board.BestCommentBoardMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countUsed() {
        return session.selectOne(namespace + "countUsed");
    }

    public int countForChangeUsed() {
        return session.selectOne(namespace + "countForChangeUsed");
    }

    public int insert(BestCommentBoardDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public List<BoardResponseDto> selectForView(Map map) {
        return session.selectList(namespace + "selectForView", map);
    }

    public BestCommentBoardDto select(Integer seq) {
        return session.selectOne(namespace + "select", seq);
    }

    public List<BestCommentBoardDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int update(BestCommentBoardUpdateDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int updateUsed(String up_id) {
        return session.update(namespace + "updateUsed", up_id);
    }

    public int delete(Integer seq) {
        return session.delete(namespace + "delete", seq);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
