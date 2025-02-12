package com.example.demo.repository.board.impl;

import com.example.demo.dto.board.BoardChangeHistoryDto;
import com.example.demo.repository.board.BoardChangeHistoryRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardChangeHistoryDaoImpl implements BoardChangeHistoryRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.board.BoardChangeHistoryMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByBno(Integer bno) {
        return session.selectOne(namespace + "countByBno", bno);
    }

    @Override
    public boolean existsBySeq(Integer seq) {
        return session.selectOne(namespace + "existsBySeq", seq);
    }

    @Override
    public boolean existsBySeqForUpdate(Integer seq) {
        return session.selectOne(namespace + "existsBySeqForUpdate", seq);
    }

    @Override
    public boolean existsByBno(Integer bno) {
        return session.selectOne(namespace + "existsByBno", bno);
    }

    @Override
    public boolean existsByBnoForUpdate(Integer bno) {
        return session.selectOne(namespace + "existsByBnoForUpdate", bno);
    }

    @Override
    public int insert(BoardChangeHistoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(BoardChangeHistoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public BoardChangeHistoryDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    @Override
    public List<BoardChangeHistoryDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
    }

    @Override
    public List<BoardChangeHistoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public BoardChangeHistoryDto selectLatestByBno(Integer bno) {
        return session.selectOne(namespace + "selectLatestByBno", bno);
    }

    @Override
    public int deleteBySeq(Integer seq) {
        return session.delete(namespace + "deleteBySeq", seq);
    }

    @Override
    public int deleteByBno(Integer bno) {
        return session.delete(namespace + "deleteByBno", bno);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
}
