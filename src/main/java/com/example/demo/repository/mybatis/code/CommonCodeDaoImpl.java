package com.example.demo.repository.mybatis.code;

import com.example.demo.dto.code.CodeRequestDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonCodeDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.code.CommonCodeMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int insert(CodeRequestDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public int deleteByLevel(Integer level) {
        return session.delete(namespace + "deleteByLevel", level);
    }

}
