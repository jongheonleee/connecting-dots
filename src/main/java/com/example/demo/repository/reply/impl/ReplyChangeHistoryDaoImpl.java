package com.example.demo.repository.reply.impl;

import com.example.demo.dto.reply.ReplyChangeHistoryDto;
import com.example.demo.repository.reply.ReplyChangeHistoryRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyChangeHistoryDaoImpl implements ReplyChangeHistoryRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.reply.ReplyChangeHistoryMapper.";


    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByRcno(Integer rcno) {
        return session.selectOne(namespace + "countByRcno", rcno);
    }


    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

    @Override
    public int insert(ReplyChangeHistoryDto dto) {
        return session.insert(namespace + "insert", dto);
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
    public List<ReplyChangeHistoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public ReplyChangeHistoryDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    @Override
    public List<ReplyChangeHistoryDto> selectByRcno(Integer rcno) {
        return session.selectList(namespace + "selectByRcno", rcno);
    }

    @Override
    public int update(ReplyChangeHistoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int deleteBySeq(Integer seq) {
        return session.delete(namespace + "deleteBySeq", seq);
    }

    @Override
    public int deleteByRcno(Integer rcno) {
        return session.delete(namespace + "deleteByRcno", rcno);
    }



}
