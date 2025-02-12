package com.example.demo.repository.report.impl;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.report.ReportDto;
import com.example.demo.repository.report.ReportRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDaoImpl implements ReportRepository {

    private static final String namespace = "com.example.demo.mapper.report.ReportMapper.";

    @Autowired
    private SqlSession session;


    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByCateCode(String cate_code) {
        return session.selectOne(namespace + "countByCateCode", cate_code);
    }

    @Override
    public int countByRepoSeq(Integer repo_seq) {
        return session.selectOne(namespace + "countByRepoSeq", repo_seq);
    }

    @Override
    public int countByRespSeq(Integer resp_seq) {
        return session.selectOne(namespace + "countByRespSeq", resp_seq);
    }

    @Override
    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    @Override
    public ReportDto selectByRno(Integer rno) {
        return session.selectOne(namespace + "selectByRno", rno);
    }

    @Override
    public List<ReportDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    @Override
    public List<ReportDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public boolean existsByRno(Integer rno) {
        return session.selectOne(namespace + "existsByRno", rno);
    }

    @Override
    public boolean existsByRnoForUpdate(Integer rno) {
        return session.selectOne(namespace + "existsByRnoForUpdate", rno);
    }

    @Override
    public boolean existsByRepoSeq(Integer repo_seq) {
        return session.selectOne(namespace + "existsByRepoSeq", repo_seq);
    }

    @Override
    public boolean existsByRespSeq(Integer resp_seq) {
        return session.selectOne(namespace + "existsByRespSeq", resp_seq);
    }

    @Override
    public int insert(ReportDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(ReportDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int delete(Integer rno) {
        return session.delete(namespace + "delete", rno);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
}
