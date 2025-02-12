package com.example.demo.repository.board.impl;

import com.example.demo.dto.board.BoardImgDto;
import com.example.demo.repository.board.BoardImgRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardImgDaoImpl implements BoardImgRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.board.BoardImgMapper.";


    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }


   @Override
   public int insert(BoardImgDto dto) {
        return session.insert(namespace + "insert", dto);
   }

   @Override
   public int insertAll(List<BoardImgDto> dtos) {
        return session.insert(namespace + "insertAll", dtos);
   }

   @Override
   public boolean existsByIno(int ino) {
        return session.selectOne(namespace + "existsByIno", ino);
   }

   @Override
   public boolean existsByInoForUpdate(Integer ino) {
        return session.selectOne(namespace + "existsByInoForUpdate", ino);
   }

   @Override
   public List<BoardImgDto> selectByBno(Integer bno) {
        return session.selectList(namespace + "selectByBno", bno);
   }

   @Override
   public BoardImgDto selectByIno(int ino) {
        return session.selectOne(namespace + "selectByIno", ino);
   }

   @Override
   public List<BoardImgDto> selectAll() {
        return session.selectList(namespace + "selectAll");
   }

   @Override
   public int update(BoardImgDto dto) {
        return session.update(namespace + "update", dto);
   }

   @Override
   public int deleteByBno(int bno) {
        return session.delete(namespace + "deleteByBno", bno);
   }

   @Override
   public int deleteByIno(int ino) {
        return session.delete(namespace + "deleteByIno", ino);
   }

   @Override
   public int deleteAll() {
        return session.delete(namespace + "deleteAll");
   }


}
