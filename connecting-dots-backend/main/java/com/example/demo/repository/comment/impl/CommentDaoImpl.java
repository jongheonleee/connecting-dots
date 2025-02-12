package com.example.demo.repository.comment.impl;

import com.example.demo.dto.comment.CommentDto;
import com.example.demo.repository.comment.CommentRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDaoImpl implements CommentRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.comment.CommentMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByBno(Integer bno) {
        return session.selectOne(namespace + "countByBno", bno);
    }

    @Override
    public boolean existsByCno(Integer cno) {
        return session.selectOne(namespace + "existsByCno", cno) != null;
    }

    @Override
    public boolean existsByCnoForUpdate(Integer cno) {
        return session.selectOne(namespace + "existsByCnoForUpdate", cno) != null;
    }

    @Override
    public int insert(CommentDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public List<CommentDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
    }

    @Override
    public CommentDto selectByCno(Integer cno) {
        return session.selectOne(namespace + "selectByCno", cno);
    }

    @Override
    public List<CommentDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int update(CommentDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int increaseLikeCnt(Integer cno) {
        return session.update(namespace + "increaseLikeCnt", cno);
    }

    @Override
    public int increaseDislikeCnt(Integer cno) {
        return session.update(namespace + "increaseDislikeCnt", cno);
    }

    @Override
    public int deleteByBno(Integer bno) {
        return session.delete(namespace + "deleteByBno", bno);
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
