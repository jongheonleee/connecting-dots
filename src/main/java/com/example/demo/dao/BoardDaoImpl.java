package com.example.demo.dao;

import com.example.demo.dto.BoardDetailDto;
import com.example.demo.dto.BoardFormDto;
import com.example.demo.dto.BoardUpdatedFormDto;
import com.example.demo.dto.CategoryDto;
import java.util.List;
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

    public int insert(BoardFormDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public BoardFormDto select(Integer bno) {
        return session.selectOne(namespace + "select", bno);
    }

    public List<BoardFormDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public List<BoardFormDto> selectAllByCategory(String cate_code) {
        return session.selectList(namespace + "selectByCategory", cate_code);
    }

    public BoardDetailDto selectDetailByBno(Integer bno) {
        return session.selectOne(namespace + "selectByBno", bno);
    }

    public int update(BoardUpdatedFormDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int increaseViewCnt(Integer bno) {
        return session.update(namespace + "increaseViewCnt", bno);
    }

    public int increaseRecoCnt(Integer bno) {
        return session.update(namespace + "increaseRecoCnt", bno);
    }

    public int increaseNotRecoCnt(Integer bno) {
        return session.update(namespace + "increaseNotRecoCnt", bno);
    }

    public int delete(Integer bno) {
        return session.delete(namespace + "delete", bno);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }



}
