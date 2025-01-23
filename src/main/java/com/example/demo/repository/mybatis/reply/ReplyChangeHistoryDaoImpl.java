package com.example.demo.repository.mybatis.reply;

import com.example.demo.dto.reply.ReplyChangeHistoryDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyChangeHistoryDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.reply.ReplyChangeHistoryMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }


    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

    public int insert(ReplyChangeHistoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public boolean existsBySeq(Integer seq) {
        return session.selectOne(namespace + "existsBySeq", seq);
    }

    public boolean existsBySeqForUpdate(Integer seq) {
        return session.selectOne(namespace + "existsBySeqForUpdate", seq);
    }

    public List<ReplyChangeHistoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public ReplyChangeHistoryDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    public List<ReplyChangeHistoryDto> selectByRcno(Integer rcno) {
        return session.selectList(namespace + "selectByRcno", rcno);
    }

    public int update(ReplyChangeHistoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int deleteBySeq(Integer seq) {
        return session.delete(namespace + "deleteBySeq", seq);
    }

    public int deleteByRcno(Integer rcno) {
        return session.delete(namespace + "deleteByRcno", rcno);
    }



}
