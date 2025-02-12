package com.example.demo.repository.comment.impl;

import com.example.demo.dto.comment.CommentChangeHistoryDto;
import com.example.demo.repository.comment.CommentChangeHistoryRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentChangeHistoryDaoImpl implements CommentChangeHistoryRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.comment.CommentChangeHistoryMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByCno(Integer cno) {
        return session.selectOne(namespace + "countByCno", cno);
    }

    @Override
    public boolean existsByCno(Integer cno) {
        return session.selectOne(namespace + "existsByCno", cno);
    }

    @Override
    public boolean existsByCnoForUpdate(Integer cno) {
        return session.selectOne(namespace + "existsByCnoForUpdate", cno);
    }

    @Override
    public CommentChangeHistoryDto select(Integer seq) {
        return session.selectOne(namespace + "select", seq);
    }

    @Override
    public List<CommentChangeHistoryDto> selectByCno(Integer cno) {
        return session.selectList(namespace + "selectByCno", cno);
    }

    @Override
    public CommentChangeHistoryDto selectLatestByCno(Integer cno) {
        return session.selectOne(namespace + "selectLatestByCno", cno);
    }

    @Override
    public List<CommentChangeHistoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int insert(CommentChangeHistoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(CommentChangeHistoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int delete(Integer seq) {
        return session.delete(namespace + "delete", seq);
    }

    @Override
    public int deleteByCno(Integer cno) {
        return session.delete(namespace + "deleteByCno", cno);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
