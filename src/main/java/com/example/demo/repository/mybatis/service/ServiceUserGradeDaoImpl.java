package com.example.demo.repository.mybatis.service;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserGradeDto;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceUserGradeDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceUserGradeMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    public boolean existsByStatCode(String stat_code) {
        return session.selectOne(namespace + "existsByStatCode", stat_code);
    }

    public boolean existsByStatCodeForUpdate(String stat_code) {
        return session.selectOne(namespace + "existsByStatCodeForUpdate", stat_code);
    }

    public ServiceUserGradeDto selectByStatCode(String stat_code) {
        return session.selectOne(namespace + "selectByStatCode", stat_code);
    }

    public List<ServiceUserGradeDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    public List<ServiceUserGradeDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public int insert(ServiceUserGradeDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public int update(ServiceUserGradeDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int deleteByStatCode(String stat_code) {
        return session.delete(namespace + "deleteByStatCode", stat_code);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

}
