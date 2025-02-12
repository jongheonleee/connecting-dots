package com.example.demo.repository.service.impl;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserConditionsDetailDto;
import com.example.demo.dto.service.ServiceUserConditionsDto;
import com.example.demo.repository.service.ServiceUserConditionsRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceUserConditionsDaoImpl implements ServiceUserConditionsRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceUserConditionsMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
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
    public boolean existsByCondsCode(String conds_code) {
        return session.selectOne(namespace + "existsByCondsCode", conds_code);
    }

    @Override
    public boolean existsByCondsCodeForUpdate(String conds_code) {
        return session.selectOne(namespace + "existsByCondsCodeForUpdate", conds_code);
    }

    @Override
    public ServiceUserConditionsDto select(Integer seq) {
        return session.selectOne(namespace + "select", seq);
    }

    @Override
    public ServiceUserConditionsDto selectByCondsCode(String conds_code) {
        return session.selectOne(namespace + "selectByCondsCode", conds_code);
    }

    @Override
    public List<ServiceUserConditionsDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    @Override
    public List<ServiceUserConditionsDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public ServiceUserConditionsDetailDto selectForUserConditions(String conds_code) {
        return session.selectOne(namespace + "selectForUserConditions", conds_code);
    }

    @Override
    public int insert(ServiceUserConditionsDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(ServiceUserConditionsDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int updateChkUse(ServiceUserConditionsDto dto) {
        return session.update(namespace + "updateChkUse", dto);
    }

    @Override
    public int delete(Integer seq) {
        return session.delete(namespace + "delete", seq);
    }

    @Override
    public int deleteByCondsCode(String conds_code) {
        return session.delete(namespace + "deleteByCondsCode", conds_code);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
