package com.example.demo.repository.reply.impl;


import com.example.demo.dto.reply.ReplyDto;
import com.example.demo.repository.reply.ReplyRepository;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDaoImpl implements ReplyRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.reply.ReplyMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByBno(Integer bno) {
        return session.selectOne(namespace + "countByBno", bno);
    }

    @Override
    public int countByCno(Integer cno) {
        return session.selectOne(namespace + "countByCno", cno);
    }

    @Override
    public boolean existsByRcno(Integer rcno) {
        return session.selectOne(namespace + "existsByRcno", rcno);
    }

    @Override
    public boolean existsByRcnoForUpdate(Integer rcno) {
        return session.selectOne(namespace + "existsByRcnoForUpdate", rcno);
    }

    @Override
    public int insert(ReplyDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public List<ReplyDto> selectByBnoAndCno(Map<String, Object> map) {
        return session.selectList(namespace + "selectByBnoAndCno", map);
    }

    @Override
    public List<ReplyDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
    }

    @Override
    public List<ReplyDto> selectByCno(Integer cno) {
        return session.selectList(namespace + "selectByCno", cno);
    }

    @Override
    public ReplyDto select(Integer rcno) {
        return session.selectOne(namespace + "selectByRcno", rcno);
    }

    @Override
    public List<ReplyDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int update(ReplyDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int increaseRecoCnt(Integer rcno) {
        return session.update(namespace + "increaseRecoCnt", rcno);
    }

    @Override
    public int increaseNotRecoCnt(Integer rcno) {
        return session.update(namespace + "increaseNotRecoCnt", rcno);
    }

    @Override
    public int delete(Integer rcno) {
        return session.delete(namespace + "delete", rcno);
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
    public int deleteByBnoAndCno(Map<String, Object> map) {
        return session.delete(namespace + "deleteByBnoAndCno", map);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }



}
