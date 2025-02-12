package com.example.demo.repository.service.impl;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserGradeDto;
import com.example.demo.repository.service.ServiceUserGradeRepository;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceUserGradeDaoImpl implements ServiceUserGradeRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceUserGradeMapper.";


    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    @Override
    public boolean existsByStatCode(String stat_code) {
        return session.selectOne(namespace + "existsByStatCode", stat_code);
    }

    @Override
    public boolean existsByStatCodeForUpdate(String stat_code) {
        return session.selectOne(namespace + "existsByStatCodeForUpdate", stat_code);
    }

    @Override
    public ServiceUserGradeDto selectByStatCode(String stat_code) {
        return session.selectOne(namespace + "selectByStatCode", stat_code);
    }

    @Override
    public List<ServiceUserGradeDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    @Override
    public List<ServiceUserGradeDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int insert(ServiceUserGradeDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public int update(ServiceUserGradeDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int deleteByStatCode(String stat_code) {
        return session.delete(namespace + "deleteByStatCode", stat_code);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
