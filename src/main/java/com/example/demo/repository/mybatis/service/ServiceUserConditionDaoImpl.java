package com.example.demo.repository.mybatis.service;

import com.example.demo.dto.service.ServiceUserConditionDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceUserConditionDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceUserConditionMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public boolean existsByCondCode(String cond_code) {
        return session.selectOne(namespace + "existsByCondCode", cond_code);
    }

    public ServiceUserConditionDto select(String cond_code) {
        return session.selectOne(namespace + "select", cond_code);
    }

    public List<ServiceUserConditionDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int insert(ServiceUserConditionDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public int update(ServiceUserConditionDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int updateChkUse(ServiceUserConditionDto dto) {
        return session.update(namespace + "updateChkUse", dto);
    }

    public int delete(String cond_code) {
        return session.delete(namespace + "delete", cond_code);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
