package com.example.demo.repository.mybatis.board;

import com.example.demo.dto.board.BoardStatusDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardStatusDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.board.BoardStatusMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countByBno(Integer bno) {
        return session.selectOne(namespace + "countByBno", bno);
    }

    public boolean existsBySeq(Integer seq) {
        return session.selectOne(namespace + "existsBySeq", seq);
    }

    public boolean existsBySeqForUpdate(Integer seq) {
        return session.selectOne(namespace + "existsBySeqForUpdate", seq);
    }

    public boolean existsByBno(Integer bno) {
        return session.selectOne(namespace + "existsByBno", bno);
    }

    public boolean existsByBnoForUpdate(Integer bno) {
        return session.selectOne(namespace + "existsByBnoForUpdate", bno);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }


    public int insert(BoardStatusDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public List<BoardStatusDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public List<BoardStatusDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
    }

    public BoardStatusDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    public BoardStatusDto selectByBnoAtPresent(Integer bno) {
        return session.selectOne(namespace + "selectByBnoAtPresent", bno);
    }

    public int update(BoardStatusDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int deleteBySeq(Integer seq) {
        return session.delete(namespace + "deleteBySeq", seq);
    }

    public int deleteByBno(Integer bno) {
        return session.delete(namespace + "deleteByBno", bno);
    }
}
