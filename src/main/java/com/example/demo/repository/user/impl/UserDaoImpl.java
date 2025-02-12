package com.example.demo.repository.user.impl;

import com.example.demo.repository.user.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.user.UserMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }
}
