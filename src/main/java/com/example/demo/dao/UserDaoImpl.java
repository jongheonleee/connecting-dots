package com.example.demo.dao;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserFormDto;
import com.example.demo.dto.UserUpdatedFormDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.UserMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int insert(UserFormDto dto) {
        return session.insert(namespace + "insert", dto);
    }


    public UserDto selectById(String id) {
        return session.selectOne(namespace + "selectById", id);
    }

    public List<UserDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int update(UserUpdatedFormDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int deleteById(String id) {
        return session.delete(namespace + "deleteById", id);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }


}
