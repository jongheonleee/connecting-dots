package com.example.demo.repository.report.impl;

import com.example.demo.dto.report.ReportProcessDetailsDto;
import com.example.demo.repository.report.ReportProcessDetailsRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportProcessDetailsDaoImpl implements ReportProcessDetailsRepository {

    private static final String namespace = "com.example.demo.mapper.report.ReportProcessDetailsMapper.";

    @Autowired
    private SqlSession session;

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByRno(Integer rno) {
        return session.selectOne(namespace + "countByRno", rno);
    }

    @Override
    public int insert(ReportProcessDetailsDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public boolean existsBySeq(Integer seq) {
        return session.selectOne(namespace + "existsBySeq", seq);
    }

    @Override
    public boolean existsByRno(Integer rno) {
        return session.selectOne(namespace + "existsByRno", rno);
    }

    @Override
    public boolean existsBySeqForUpdate(Integer seq) {
        return session.selectOne(namespace + "existsBySeqForUpdate", seq);
    }

    @Override
    public boolean existsByRnoForUpdate(Integer rno) {
        return session.selectOne(namespace + "existsByRnoForUpdate", rno);
    }

    @Override
    public ReportProcessDetailsDto selectLatestByRno(Integer rno) {
        return session.selectOne(namespace + "selectLatestByRno", rno);
    }

    @Override
    public ReportProcessDetailsDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    @Override
    public List<ReportProcessDetailsDto> selectByRno(Integer rno) {
        return session.selectList(namespace + "selectByRno", rno);
    }

    @Override
    public List<ReportProcessDetailsDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public ReportProcessDetailsDto selectByRnoAtPresent(Integer rno) {
        return session.selectOne(namespace + "selectByRnoAtPresent", rno);
    }

    @Override
    public int update(ReportProcessDetailsDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int deleteBySeq(Integer seq) {
        return session.delete(namespace + "deleteBySeq", seq);
    }

    @Override
    public int deleteByRno(Integer rno) {
        return session.delete(namespace + "deleteByRno", rno);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
}
