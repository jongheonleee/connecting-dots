package com.example.demo.repository.mybatis.board;

import com.example.demo.dto.board.BoardCategoryDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardCategoryDaoImpl {

    @Autowired
    private SqlSession session;

    private final String namespace = "com.example.demo.mapper.category.BoardCategoryMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countByLevel(Integer level) {
        return session.selectOne(namespace + "countByLevel", level);
    }

    public boolean existsByCateCode(String cate_code) {
        return session.selectOne(namespace + "existsByCateCode", cate_code);
    }

    public boolean existsByCateCodeForUpdate(String cate_code) {
        return session.selectOne(namespace + "existsByCateCodeForUpdate", cate_code);
    }

    public int insert(BoardCategoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public BoardCategoryDto selectByCateCode(String cate_code) {
        return session.selectOne(namespace + "selectByCateCode", cate_code);
    }

    public List<BoardCategoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public List<BoardCategoryDto> selectByTopCate(String top_cate) {
        return session.selectList(namespace + "selectByTopCate", top_cate);
    }

    public int update(BoardCategoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int updateChkUseY(String cate_code) {
        return session.update(namespace + "updateChkUseY", cate_code);
    }

    public int updateChkUseN(String cate_code) {
        return session.update(namespace + "updateChkUseN", cate_code);
    }

    public int deleteByCateCode(String cate_code) {
        return session.delete(namespace + "deleteByCateCode", cate_code);
    }

    public int deleteByLevel(int level) {
        return session.delete(namespace + "deleteByLevel", level);
    }
}
