package com.example.demo.repository.mybatis.comment;

import com.example.demo.dto.comment.CommentChangeHistoryDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentChangeHistoryDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.comment.CommentChangeHistoryMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countByCno(Integer cno) {
        return session.selectOne(namespace + "countByCno", cno);
    }

    public boolean existsByCno(Integer cno) {
        return session.selectOne(namespace + "existsByCno", cno) != null;
    }

    public CommentChangeHistoryDto select(Integer seq) {
        return session.selectOne(namespace + "select", seq);
    }

    public List<CommentChangeHistoryDto> selectByCno(Integer cno) {
        return session.selectList(namespace + "selectByCno", cno);
    }

    public CommentChangeHistoryDto selectLatestByCno(Integer cno) {
        return session.selectOne(namespace + "selectLatestByCno", cno);
    }

    public List<CommentChangeHistoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int insert(CommentChangeHistoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public int update(CommentChangeHistoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int delete(Integer seq) {
        return session.delete(namespace + "delete", seq);
    }

    public int deleteByCno(Integer cno) {
        return session.delete(namespace + "deleteByCno", cno);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
