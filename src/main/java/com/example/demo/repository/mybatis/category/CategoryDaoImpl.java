package com.example.demo.repository.mybatis.category;

import com.example.demo.dto.category.CategoryDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.CategoryMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int insert(CategoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public List<CategoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public List<CategoryDto> selectAllTopCategory() {
        return session.selectList(namespace + "selectAllTopCategory");
    }

    public List<CategoryDto> selectAllByTopCate(String top_cate) {
        return session.selectList(namespace + "selectAllByTopCateCode", top_cate);
    }

    public CategoryDto selectByCode(String cate_code) {
        return session.selectOne(namespace + "selectByCode", cate_code);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

    public int deleteByCode(String cate_code) {
        return session.delete(namespace + "deleteByCode", cate_code);
    }

    public int update(CategoryDto dto) {
        return session.update(namespace + "update", dto);
    }

}
