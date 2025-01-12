package com.example.demo.repository.mybatis.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceSanctionHistoryDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceSanctionHistoryDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceSanctionHistoryMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countByUserSeq(Integer user_seq) {
        return session.selectOne(namespace + "countByUserSeq", user_seq);
    }

    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    public boolean existsBySeq(Integer seq) {
        return session.selectOne(namespace + "existsBySeq", seq);
    }

    public boolean existsBySeqForUpdate(Integer seq) {
        return session.selectOne(namespace + "existsBySeqForUpdate", seq);
    }


    public ServiceSanctionHistoryDto selectBySeq(Integer seq) {
        return session.selectOne(namespace + "selectBySeq", seq);
    }

    public List<ServiceSanctionHistoryDto> selectByPoliStat(String poli_stat) {
        return session.selectList(namespace + "selectByPoliStat", poli_stat);
    }


    public List<ServiceSanctionHistoryDto> selectByUserSeq(Integer user_seq) {
        return session.selectList(namespace + "selectByUserSeq", user_seq);
    }

    public List<ServiceSanctionHistoryDto> selectByUserSeqForNow(Integer user_seq) {
        return session.selectList(namespace + "selectByUserSeqForNow", user_seq);
    }

    public List<ServiceSanctionHistoryDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    public ServiceSanctionHistoryDto selectBySeqForUpdate(Integer seq) {
        return session.selectOne(namespace + "selectBySeqForUpdate", seq);
    }

    public List<ServiceSanctionHistoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int insert(ServiceSanctionHistoryDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public int update(ServiceSanctionHistoryDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int updateForApplEnd(ServiceSanctionHistoryDto dto) {
        return session.update(namespace + "updateForApplEnd", dto);
    }

    public int deleteBySeq(Integer seq) {
        return session.delete(namespace + "deleteBySeq", seq);
    }

    public int deleteByUserSeq(Integer user_seq) {
        return session.delete(namespace + "deleteByUserSeq", user_seq);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
}
