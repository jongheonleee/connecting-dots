package com.example.demo.repository.mybatis.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserConditionsDetailDto;
import com.example.demo.dto.service.ServiceUserConditionsDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceUserConditionsDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceUserConditionsMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    public boolean existsBySeq(Integer seq) {
        return session.selectOne(namespace + "existsBySeq", seq);
    }

    public boolean existsByCondsCode(String conds_code) {
        return session.selectOne(namespace + "existsByCondsCode", conds_code);
    }

    public boolean existsByCondsCodeForUpdate(String conds_code) {
        return session.selectOne(namespace + "existsByCondsCodeForUpdate", conds_code);
    }

    public ServiceUserConditionsDto select(Integer seq) {
        return session.selectOne(namespace + "select", seq);
    }

    public ServiceUserConditionsDto selectByCondsCode(String conds_code) {
        return session.selectOne(namespace + "selectByCondsCode", conds_code);
    }

    public List<ServiceUserConditionsDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    public List<ServiceUserConditionsDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public ServiceUserConditionsDetailDto selectForUserConditions(String conds_code) {
        return session.selectOne(namespace + "selectForUserConditions", conds_code);
    }

    public int insert(ServiceUserConditionsDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public int update(ServiceUserConditionsDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int updateChkUse(ServiceUserConditionsDto dto) {
        return session.update(namespace + "updateChkUse", dto);
    }

    public int delete(Integer seq) {
        return session.delete(namespace + "delete", seq);
    }

    public int deleteByCondsCode(String conds_code) {
        return session.delete(namespace + "deleteByCondsCode", conds_code);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
