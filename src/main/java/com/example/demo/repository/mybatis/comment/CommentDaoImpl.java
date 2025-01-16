package com.example.demo.repository.mybatis.comment;

import com.example.demo.dto.ord_comment.CommentRequestDto;
import com.example.demo.dto.ord_comment.CommentResponseDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.comment.CommentMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int count(Integer bno) {
        return session.selectOne(namespace + "countByBno", bno);
    }

    public int insert(CommentRequestDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public List<CommentResponseDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
    }

    public CommentResponseDto selectByCno(Integer cno) {
        return session.selectOne(namespace + "selectByCno", cno);
    }

    public List<CommentResponseDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int update(CommentRequestDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int increaseLikeCnt(Integer cno) {
        return session.update(namespace + "increaseLikeCnt", cno);
    }

    public int increaseDislikeCnt(Integer cno) {
        return session.update(namespace + "increaseDislikeCnt", cno);
    }

    public int deleteByBno(Integer bno) {
        return session.delete(namespace + "deleteByBno", bno);
    }

    public int deleteByCno(Integer cno) {
        return session.delete(namespace + "deleteByCno", cno);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }





}
