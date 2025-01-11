package com.example.demo.repository.mybatis.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceUserConditionsDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.service.ServiceUserConditionsMapper.";


}
