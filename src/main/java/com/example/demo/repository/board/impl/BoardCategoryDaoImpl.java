package com.example.demo.repository.board.impl;

import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.repository.board.BoardCategoryRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardCategoryDaoImpl implements BoardCategoryRepository {

    @Autowired
    private SqlSession session;

    private final String namespace = "com.example.demo.mapper.category.BoardCategoryMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByLevel(Integer level) {
        return session.selectOne(namespace + "countByLevel", level);
    }

    @Override
    public boolean existsByCateCode(String cate_code) {
        return session.selectOne(namespace + "existsByCateCode", cate_code);
    }

    @Override
    public boolean existsByCateCodeForUpdate(String cate_code) {
        return session.selectOne(namespace + "existsByCateCodeForUpdate", cate_code);
    }

    @Override
    public int insert(BoardCategoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public BoardCategoryDto selectByCateCode(String cate_code) {
        return session.selectOne(namespace + "selectByCateCode", cate_code);
    }

    @Override
    public List<BoardCategoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public List<BoardCategoryDto> selectByTopCate(String top_cate) {
        return session.selectList(namespace + "selectByTopCate", top_cate);
    }

    @Override
    public int update(BoardCategoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int updateChkUseY(String cate_code) {
        return session.update(namespace + "updateChkUseY", cate_code);
    }

    @Override
    public int updateChkUseN(String cate_code) {
        return session.update(namespace + "updateChkUseN", cate_code);
    }

    @Override
    public int deleteByCateCode(String cate_code) {
        return session.delete(namespace + "deleteByCateCode", cate_code);
    }

    @Override
    public int deleteByLevel(int level) {
        return session.delete(namespace + "deleteByLevel", level);
    }
}
