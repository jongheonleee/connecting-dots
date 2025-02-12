package com.example.demo.repository.board.impl;

import com.example.demo.dto.board.BoardStatusDto;
import com.example.demo.repository.board.BoardStatusRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardStatusDaoImpl implements BoardStatusRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.board.BoardStatusMapper.";


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
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }


    @Override
    public int insert(BoardStatusDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public List<BoardStatusDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public List<BoardStatusDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
    }

    @Override
    public BoardStatusDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    @Override
    public BoardStatusDto selectByBnoAtPresent(Integer bno) {
        return session.selectOne(namespace + "selectByBnoAtPresent", bno);
    }

    @Override
    public int update(BoardStatusDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int deleteBySeq(Integer seq) {
        return session.delete(namespace + "deleteBySeq", seq);
    }

    @Override
    public int deleteByBno(Integer bno) {
        return session.delete(namespace + "deleteByBno", bno);
    }
}
