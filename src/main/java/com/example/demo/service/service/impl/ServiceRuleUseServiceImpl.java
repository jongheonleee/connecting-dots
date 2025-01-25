package com.example.demo.service.service.impl;

import com.example.demo.service.service.ServiceRuleUseService;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.dto.service.ServiceRuleUseRequest;
import com.example.demo.dto.service.ServiceRuleUseResponse;
import com.example.demo.dto.PageResponse;
import com.example.demo.global.error.exception.business.service.ServiceRuleUseAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceRuleUseNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.service.ServiceRuleUseDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceRuleUseServiceImpl implements ServiceRuleUseService {

    private final ServiceRuleUseDaoImpl serviceRuleUseDao;
    private final CustomFormatter formatter;

    @Override
    public int count() {
        return serviceRuleUseDao.count();
    }

    @Override
    public int countByCode(String code) {
        return serviceRuleUseDao.countByCode(code);
    }

    @Override
    public ServiceRuleUseResponse create(ServiceRuleUseRequest request) {
        boolean exists = serviceRuleUseDao.existsByRuleStat(request.getRule_stat());
        if (exists) {
            log.error("[SERVICE-RULE-USE] create() 실패 - 중복된 키 값 : {}", request.getRule_stat());
            throw new ServiceRuleUseAlreadyExistsException();
        }


        ServiceRuleUseDto dto = new ServiceRuleUseDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceRuleUseDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-RULE-USE] create() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }

        return serviceRuleUseDao.selectByRuleStat(dto.getRule_stat())
                                .toResponse();
    }

    @Override
    public ServiceRuleUseResponse readByRuleStat(String rule_stat) {
        boolean exists = serviceRuleUseDao.existsByRuleStat(rule_stat);
        if (!exists) {
            log.error("[SERVICE-RULE-USE] readByRuleStat() 해당 rule_stat와 관련된 서비스 이용 규칙 존재하지 않음 : {}", rule_stat);
            throw new ServiceRuleUseNotFoundException();
        }

        return serviceRuleUseDao.selectByRuleStat(rule_stat)
                                .toResponse();
    }


    @Override
    public List<ServiceRuleUseResponse> readByCode(String code) {
        List<ServiceRuleUseDto> serviceRuleUseDtos = serviceRuleUseDao.selectByCode(code);
        return serviceRuleUseDtos.stream()
                                 .map(ServiceRuleUseResponse::new)
                                 .toList();
    }

    @Override
    public PageResponse<ServiceRuleUseResponse> readBySearchCondition(SearchCondition sc) {
        int totalCnt = serviceRuleUseDao.countBySearchCondition(sc);
        List<ServiceRuleUseDto> serviceRuleUseDtos = serviceRuleUseDao.selectBySearchCondition(sc);
        List<ServiceRuleUseResponse> responses = serviceRuleUseDtos.stream()
                                                                  .map(ServiceRuleUseResponse::new)
                                                                  .toList();
        return new PageResponse<>(totalCnt, sc, responses);
    }


    @Override
    public List<ServiceRuleUseResponse> readAll() {
        List<ServiceRuleUseDto> dtos = serviceRuleUseDao.selectAll();
        return dtos.stream()
                   .map(ServiceRuleUseResponse::new)
                   .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ServiceRuleUseRequest request) {
        boolean exists = serviceRuleUseDao.existsByRuleStatForUpdate(request.getRule_stat());
        if (!exists) {
            log.error("[SERVICE-RULE-USE] modify() 해당 rule_stat와 관련된 서비스 이용 규칙 존재하지 않음 : {}", request.getRule_stat());
            throw new ServiceRuleUseNotFoundException();
        }

        ServiceRuleUseDto dto = new ServiceRuleUseDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceRuleUseDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-RULE-USE] modify() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyChkUse(ServiceRuleUseRequest request) {
        boolean exists = serviceRuleUseDao.existsByRuleStatForUpdate(request.getRule_stat());
        if (!exists) {
            log.error("[SERVICE-RULE-USE] modify() 해당 rule_stat와 관련된 서비스 이용 규칙 존재하지 않음 : {}", request.getRule_stat());
            throw new ServiceRuleUseNotFoundException();
        }

        ServiceRuleUseDto dto = new ServiceRuleUseDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceRuleUseDao.updateUse(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-RULE-USE] modifyUse() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }
    }

    @Override
    public void removeByRuleStat(String rule_stat) {
        int rowCnt = serviceRuleUseDao.deleteByRuleStat(rule_stat);

        if (rowCnt != 1) {
            log.error("[SERVICE-RULE-USE] removeByRuleStat() 실패 : {}", rule_stat);
            throw new NotApplyOnDbmsException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByCode(String code) {
        int totalCnt = serviceRuleUseDao.countByCode(code);
        int rowCnt = serviceRuleUseDao.deleteByCode(code);

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-RULE-USE] removeByCode() 실패 : {}", code);
            throw new NotApplyOnDbmsException();
        }
    }


    @Override
    @Transactional
    public void removeAll() {
        int totalCnt = serviceRuleUseDao.count();
        int rowCnt = serviceRuleUseDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-RULE-USE] removeAll() 실패");
            throw new NotApplyOnDbmsException();
        }
    }

}
