package com.example.demo.repository.mybatis.code;

import com.example.demo.dto.code.request.CodeRequestDto;
import com.example.demo.dto.code.response.CodeResponseDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonCodeDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.code.CommonCodeMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public List<CodeResponseDto> selectByTopCode(String top_code) {
        return session.selectList(namespace + "selectByTopCode", top_code);
    }

    public CodeResponseDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    public CodeResponseDto selectByCode(String code) {
        return session.selectOne(namespace + "selectByCode", code);
    }

    public List<CodeResponseDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int insert(CodeRequestDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public int update(CodeRequestDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int updateUse(CodeRequestDto dto) {
        return session.update(namespace + "updateUse", dto);
    }

    public int deleteByLevel(Integer level) {
        return session.delete(namespace + "deleteByLevel", level);
    }

    public int deleteByCode(String code) {
        return session.delete(namespace + "deleteByCode", code);
    }

    public int delete(Integer seq) {
        return session.delete(namespace + "delete", seq);
    }

}
