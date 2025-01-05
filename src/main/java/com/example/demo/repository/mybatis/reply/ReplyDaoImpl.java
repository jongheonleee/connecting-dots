package com.example.demo.repository.mybatis.reply;

import com.example.demo.dto.reply.ReplyRequestDto;
import com.example.demo.dto.reply.ReplyResponseDto;
import com.example.demo.dto.reply.ReplyUpdateRequestDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.ReplyMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countByBno(Integer bno) {
        return session.selectOne(namespace + "countByBno", bno);
    }

    public int insert(ReplyRequestDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public List<ReplyResponseDto> selectByBnoAndCno(Map<String, Object> map) {
        return session.selectList(namespace + "selectByBnoAndCno", map);
    }

    public List<ReplyResponseDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
    }

    public List<ReplyResponseDto> selectByCno(Integer cno) {
        return session.selectList(namespace + "selectByCno", cno);
    }

    public ReplyResponseDto select(Integer rcno) {
        return session.selectOne(namespace + "selectByRcno", rcno);
    }

    public List<ReplyResponseDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int update(ReplyUpdateRequestDto dto) {
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
