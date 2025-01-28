package com.example.demo.repository.service.impl;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceSanctionHistoryDto;
import com.example.demo.repository.service.ServiceSanctionHistoryRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceSanctionHistoryDaoImpl implements ServiceSanctionHistoryRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceSanctionHistoryMapper.";


    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByUserSeq(Integer user_seq) {
        return session.selectOne(namespace + "countByUserSeq", user_seq);
    }

    @Override
    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    @Override
    public boolean existsBySeq(Integer seq) {
        return session.selectOne(namespace + "existsBySeq", seq);
    }

    @Override
    public boolean existsBySeqForUpdate(Integer seq) {
        return session.selectOne(namespace + "existsBySeqForUpdate", seq);
    }

    @Override
    public boolean existsByPoliStat(String poli_stat) {
        return session.selectOne(namespace + "existsByPoliStat", poli_stat);
    }


    @Override
    public ServiceSanctionHistoryDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    @Override
    public List<ServiceSanctionHistoryDto> selectByPoliStat(String poli_stat) {
        return session.selectList(namespace + "selectByPoliStat", poli_stat);
    }


    @Override
    public List<ServiceSanctionHistoryDto> selectByUserSeq(Integer user_seq) {
        return session.selectList(namespace + "selectByUserSeq", user_seq);
    }

    @Override
    public List<ServiceSanctionHistoryDto> selectByUserSeqForNow(Integer user_seq) {
        return session.selectList(namespace + "selectByUserSeqForNow", user_seq);
    }

    @Override
    public List<ServiceSanctionHistoryDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    @Override
    public ServiceSanctionHistoryDto selectBySeqForUpdate(Integer seq) {
        return session.selectOne(namespace + "selectBySeqForUpdate", seq);
    }

    @Override
    public List<ServiceSanctionHistoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int insert(ServiceSanctionHistoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(ServiceSanctionHistoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int updateForApplEnd(ServiceSanctionHistoryDto dto) {
        return session.update(namespace + "updateForApplEnd", dto);
    }

    @Override
    public int deleteBySeq(Integer seq) {
        return session.delete(namespace + "deleteBySeq", seq);
    }

    @Override
    public int deleteByUserSeq(Integer user_seq) {
        return session.delete(namespace + "deleteByUserSeq", user_seq);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
}
