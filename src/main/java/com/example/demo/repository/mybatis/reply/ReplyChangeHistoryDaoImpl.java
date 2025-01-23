package com.example.demo.repository.mybatis.reply;

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
}
