package com.example.demo.repository.report.impl;

import com.example.demo.dto.report.ReportCategoryDto;
import com.example.demo.repository.report.ReportCategoryRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportCategoryDaoImpl implements ReportCategoryRepository {

    private static final String namespace = "com.example.demo.mapper.report.ReportCategoryMapper.";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public int count() {
        return sqlSession.selectOne(namespace + "count");
    }

    @Override
    public int countByLevel(Integer level) {
        return sqlSession.selectOne(namespace + "countByLevel", level);
    }

    @Override
    public boolean existsByCateCode(String cate_code) {
        return sqlSession.selectOne(namespace + "existsByCateCode", cate_code);
    }

    @Override
    public boolean existsByCateCodeForUpdate(String cate_code) {
        return sqlSession.selectOne(namespace + "existsByCateCodeForUpdate", cate_code);
    }

    @Override
    public List<ReportCategoryDto> selectAll() {
        return sqlSession.selectList(namespace + "selectAll");
    }

    @Override
    public ReportCategoryDto selectByCateCode(String cate_code) {
        return sqlSession.selectOne(namespace + "selectByCateCode", cate_code);
    }

    @Override
    public List<ReportCategoryDto> selectByTopCate(String top_cate) {
        return sqlSession.selectList(namespace + "selectByTopCate", top_cate);
    }

    @Override
    public int insert(ReportCategoryDto dto) {
        return sqlSession.insert(namespace + "insert", dto);
    }

    @Override
    public int update(ReportCategoryDto dto) {
        return sqlSession.update(namespace + "update", dto);
    }

    @Override
    public int updateChkUseY(String cate_code) {
        return sqlSession.update(namespace + "updateChkUseY", cate_code);
    }

    @Override
    public int updateChkUseN(String cate_code) {
        return sqlSession.update(namespace + "updateChkUseN", cate_code);
    }

    @Override
    public int deleteByCateCode(String cate_code) {
        return sqlSession.delete(namespace + "deleteByCateCode", cate_code);
    }

    @Override
    public int deleteByLevel(Integer level) {
        return sqlSession.delete(namespace + "deleteByLevel", level);
    }
}
