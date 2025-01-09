package com.example.demo.repository.mybatis.service;

import com.example.demo.dto.service.ServiceRuleUseDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceRuleUseDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceRuleUseMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countByCode() {
        return session.selectOne(namespace + "countByCode");
    }

    public int insert(ServiceRuleUseDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public ServiceRuleUseDto selectByRuleStat(String rule_stat) {
        return session.selectOne(namespace + "selectByRuleStat", rule_stat);
    }

    public List<ServiceRuleUseDto> selectByCode(String code) {
        return session.selectList(namespace + "selectByCode", code);
    }

    public List<ServiceRuleUseDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int update(ServiceRuleUseDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int updateUse(ServiceRuleUseDto dto) {
        return session.update(namespace + "updateUse", dto);
    }

    public int deleteByRuleStat(String rule_stat) {
        return session.delete(namespace + "deleteByRuleStat", rule_stat);
    }

    public int deleteByCode(String code) {
        return session.delete(namespace + "deleteByCode", code);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
}

