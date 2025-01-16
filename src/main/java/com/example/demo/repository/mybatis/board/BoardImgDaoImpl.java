package com.example.demo.repository.mybatis.board;

import com.example.demo.dto.board.BoardImgDto;
import com.example.demo.dto.ord_board.BoardImgFormDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardImgDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.board.BoardImgMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }


   public int insert(BoardImgDto dto) {
        return session.insert(namespace + "insert", dto);
   }

   public int insertAll(List<BoardImgDto> dtos) {
        return session.insert(namespace + "insertAll", dtos);
   }

   public boolean existsByIno(int ino) {
        return session.selectOne(namespace + "existsByIno", ino);
   }

   public boolean existsByInoForUpdate(Integer ino) {
        return session.selectOne(namespace + "existsByInoForUpdate", ino);
   }

   public List<BoardImgDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
   }

   public BoardImgDto selectByIno(int ino) {
        return session.selectOne(namespace + "selectByIno", ino);
   }

   public List<BoardImgDto> selectAll() {
        return session.selectList(namespace + "selectAll");
   }

   public int update(BoardImgDto dto) {
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
