package com.example.demo.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public class BoardDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.BoardMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

}
