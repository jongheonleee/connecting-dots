package com.example.demo.repository.user.impl;

import com.example.demo.dto.user.UserDto;
import com.example.demo.repository.user.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.user.UserMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public boolean existsByUserId(String id) {
        return session.selectOne(namespace + "existsByUserId", id);
    }

    @Override
    public boolean existsByUserIdForUpdate(String id) {
        return session.selectOne(namespace + "existsByUserIdForUpdate", id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return session.selectOne(namespace + "existsByEmail", email);
    }

    @Override
    public boolean existsByUserSeq(int userSeq) {
        return session.selectOne(namespace + "existsByUserSeq", userSeq);
    }

    @Override
    public boolean existsByUserSeqForUpdate(int userSeq) {
        return session.selectOne(namespace + "existsByUserSeqForUpdate", userSeq);
    }

    @Override
    public UserDto selectByUserId(String id) {
        return session.selectOne(namespace + "selectByUserId", id);
    }

    @Override
    public UserDto selectByUserSeq(int userSeq) {
        return session.selectOne(namespace + "selectByUserSeq", userSeq);
    }

    @Override
    public int insert(UserDto user) {
        return session.insert(namespace + "insert", user);
    }

    @Override
    public int update(UserDto user) {
        return session.update(namespace + "update", user);
    }

    @Override
    public int deleteByUserId(String id) {
        return session.delete(namespace + "deleteById", id);
    }

    @Override
    public int deleteByUserSeq(int userSeq) {
        return session.delete(namespace + "deleteByUserSeq", userSeq);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }


}


