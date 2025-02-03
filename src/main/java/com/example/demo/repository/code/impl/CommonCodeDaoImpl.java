package com.example.demo.repository.code.impl;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import com.example.demo.repository.code.CommonCodeRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonCodeDaoImpl implements CommonCodeRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.code.CommonCodeMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    @Override
    public boolean existsByCode(String code) {
        return session.selectOne(namespace + "existsByCode", code);
    }

    @Override
    public List<CodeDto> selectByTopCode(String top_code) {
        return session.selectList(namespace + "selectByTopCode", top_code);
    }

    @Override
    public CodeDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    @Override
    public CodeDto selectByCode(String code) {
        return session.selectOne(namespace + "selectByCode", code);
    }

    @Override
    public List<CodeDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    @Override
    public List<CodeDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int insert(CodeDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(CodeDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int updateUse(CodeDto dto) {
        return session.update(namespace + "updateUse", dto);
    }

    @Override
    public int deleteByLevel(Integer level) {
        return session.delete(namespace + "deleteByLevel", level);
    }

    @Override
    public int deleteByCode(String code) {
        return session.delete(namespace + "deleteByCode", code);
    }

    @Override
    public int delete(Integer seq) {
        return session.delete(namespace + "delete", seq);
    }

}
