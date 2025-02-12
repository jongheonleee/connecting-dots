package com.example.demo.repository.service.impl;

import com.example.demo.dto.service.ServiceUserConditionDto;
import com.example.demo.repository.service.ServiceUserConditionRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceUserConditionDaoImpl implements ServiceUserConditionRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceUserConditionMapper.";

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public boolean existsByCondCode(String cond_code) {
        return session.selectOne(namespace + "existsByCondCode", cond_code);
    }

    @Override
    public boolean existsByCondCodeForUpdate(String cond_code) {
        return session.selectOne(namespace + "existsByCondCodeForUpdate", cond_code);
    }

    @Override
    public ServiceUserConditionDto select(String cond_code) {
        return session.selectOne(namespace + "select", cond_code);
    }

    @Override
    public List<ServiceUserConditionDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int insert(ServiceUserConditionDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(ServiceUserConditionDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int updateChkUse(ServiceUserConditionDto dto) {
        return session.update(namespace + "updateChkUse", dto);
    }

    @Override
    public int delete(String cond_code) {
        return session.delete(namespace + "delete", cond_code);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
