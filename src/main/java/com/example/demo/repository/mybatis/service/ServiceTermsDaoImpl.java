package com.example.demo.repository.mybatis.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceTermsConditionDto;
import com.example.demo.dto.service.ServiceTermsDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceTermsDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceTermsMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    public boolean existsByPoliStat(String poli_stat) {
        return session.selectOne(namespace + "existsByPoliStat", poli_stat);
    }

    public ServiceTermsDto select(String poli_stat) {
        return session.selectOne(namespace + "select", poli_stat);
    }

    public List<ServiceTermsDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    public List<ServiceTermsDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public ServiceTermsConditionDto selectForCondition(String poli_stat) {
        return session.selectOne(namespace + "selectForCondition", poli_stat);
    }

    public int insert(ServiceTermsDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public int update(ServiceTermsDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int updateChkUse(ServiceTermsDto dto) {
        return session.update(namespace + "updateChkUse", dto);
    }

    public int delete(String poli_stat) {
        return session.delete(namespace + "delete", poli_stat);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
