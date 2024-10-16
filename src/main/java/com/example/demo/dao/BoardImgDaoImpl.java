package com.example.demo.dao;

import com.example.demo.dto.BoardImgFormDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Repository;

@Repository
public class BoardImgDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.BoardImgMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }

   public int insert(BoardImgFormDto dto) {
        return session.insert(namespace + "insert", dto);
   }

   public List<BoardImgFormDto> selectAllByBno(int bno) {
        return session.selectList(namespace + "selectAllByBno", bno);
   }

   public BoardImgFormDto selectByIno(int ino) {
        return session.selectOne(namespace + "selectByIno", ino);
   }

   public List<BoardImgFormDto> selectAll() {
        return session.selectList(namespace + "selectAll");
   }

   public int update(BoardImgFormDto dto) {
        return session.update(namespace + "update", dto);
   }

   public int deleteByBno(int bno) {
        return session.delete(namespace + "deleteByBno", bno);
   }

   public int deleteByIno(int ino) {
        return session.delete(namespace + "deleteByIno", ino);
   }

   public int deleteAll() {
        return session.delete(namespace + "deleteAll");
   }


}
