package com.example.demo.repository.mybatis.reply;


import com.example.demo.dto.reply.ReplyDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.reply.ReplyMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countByBno(Integer bno) {
        return session.selectOne(namespace + "countByBno", bno);
    }

    public int countByCno(Integer cno) {
        return session.selectOne(namespace + "countByCno", cno);
    }

    public boolean existsByRcno(Integer rcno) {
        return session.selectOne(namespace + "existsByRcno", rcno);
    }

    public boolean existsByRcnoForUpdate(Integer rcno) {
        return session.selectOne(namespace + "existsByRcnoForUpdate", rcno);
    }

    public int insert(ReplyDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public List<ReplyDto> selectByBnoAndCno(Map<String, Object> map) {
        return session.selectList(namespace + "selectByBnoAndCno", map);
    }

    public List<ReplyDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
    }

    public List<ReplyDto> selectByCno(Integer cno) {
        return session.selectList(namespace + "selectByCno", cno);
    }

    public ReplyDto select(Integer rcno) {
        return session.selectOne(namespace + "selectByRcno", rcno);
    }

    public List<ReplyDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int update(ReplyDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int increaseRecoCnt(Integer rcno) {
        return session.update(namespace + "increaseRecoCnt", rcno);
    }

    public int increaseNotRecoCnt(Integer rcno) {
        return session.update(namespace + "increaseNotRecoCnt", rcno);
    }

    public int delete(Integer rcno) {
        return session.delete(namespace + "delete", rcno);
    }

    public int deleteByBno(Integer bno) {
        return session.delete(namespace + "deleteByBno", bno);
    }

    public int deleteByCno(Integer cno) {
        return session.delete(namespace + "deleteByCno", cno);
    }

    public int deleteByBnoAndCno(Map<String, Object> map) {
        return session.delete(namespace + "deleteByBnoAndCno", map);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }



}
