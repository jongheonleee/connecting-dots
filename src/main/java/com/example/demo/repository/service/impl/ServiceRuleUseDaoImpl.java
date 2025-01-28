package com.example.demo.repository.service.impl;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.repository.service.ServiceRuleUseRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceRuleUseDaoImpl implements ServiceRuleUseRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceRuleUseMapper.";


    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countByCode(String code) {
        return session.selectOne(namespace + "countByCode", code);
    }

    @Override
    public boolean existsByRuleStatForUpdate(String rule_stat) {
        return session.selectOne(namespace + "existsByRuleStatForUpdate", rule_stat);
    }

    @Override
    public boolean existsByRuleStat(String rule_stat) {
        return session.selectOne(namespace + "existsByRuleStat", rule_stat);
    }

    @Override
    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    @Override
    public int insert(ServiceRuleUseDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public ServiceRuleUseDto selectByRuleStat(String rule_stat) {
        return session.selectOne(namespace + "selectByRuleStat", rule_stat);
    }

    @Override
    public List<ServiceRuleUseDto> selectByCode(String code) {
        return session.selectList(namespace + "selectByCode", code);
    }

    @Override
    public List<ServiceRuleUseDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    @Override
    public List<ServiceRuleUseDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int update(ServiceRuleUseDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int updateUse(ServiceRuleUseDto dto) {
        return session.update(namespace + "updateUse", dto);
    }

    @Override
    public int deleteByRuleStat(String rule_stat) {
        return session.delete(namespace + "deleteByRuleStat", rule_stat);
    }

    @Override
    public int deleteByCode(String code) {
        return session.delete(namespace + "deleteByCode", code);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
}

