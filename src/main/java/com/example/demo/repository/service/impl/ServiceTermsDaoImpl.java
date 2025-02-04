package com.example.demo.repository.service.impl;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceTermsConditionDto;
import com.example.demo.dto.service.ServiceTermsDto;
import com.example.demo.repository.service.ServiceTermsRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceTermsDaoImpl implements ServiceTermsRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceTermsMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    @Override
    public boolean existsByPoliStat(String poli_stat) {
        return session.selectOne(namespace + "existsByPoliStat", poli_stat);
    }

    @Override
    public boolean existsByPoliStatForUpdate(String poli_stat) {
        return session.selectOne(namespace + "existsByPoliStatForUpdate", poli_stat);
    }

    @Override
    public ServiceTermsDto select(String poli_stat) {
        return session.selectOne(namespace + "select", poli_stat);
    }

    @Override
    public List<ServiceTermsDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    @Override
    public List<ServiceTermsDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public ServiceTermsConditionDto selectForCondition(String poli_stat) {
        return session.selectOne(namespace + "selectForCondition", poli_stat);
    }

    @Override
    public int insert(ServiceTermsDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(ServiceTermsDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int updateChkUse(ServiceTermsDto dto) {
        return session.update(namespace + "updateChkUse", dto);
    }

    @Override
    public int delete(String poli_stat) {
        return session.delete(namespace + "delete", poli_stat);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
