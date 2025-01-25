package com.example.demo.service.service;

import com.example.demo.dto.service.ServiceUserConditionRequest;
import com.example.demo.dto.service.ServiceUserConditionResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceUserConditionService {

    int count();

    ServiceUserConditionResponse create(ServiceUserConditionRequest request);

    ServiceUserConditionResponse readByCondCode(String cond_code);

    List<ServiceUserConditionResponse> readAll();

    @Transactional(rollbackFor = Exception.class)
    void modify(ServiceUserConditionRequest request);

    @Transactional(rollbackFor = Exception.class)
    void modifyChkUse(ServiceUserConditionRequest request);

    void remove(String cond_code);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
