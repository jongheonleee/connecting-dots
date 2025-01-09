package com.example.demo.application.service;

import com.example.demo.dto.service.ServiceRuleUseDto;
import com.example.demo.dto.service.ServiceRuleUseRequest;
import com.example.demo.dto.service.ServiceRuleUseResponse;
import com.example.demo.repository.mybatis.service.ServiceRuleUseDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ServiceRuleUseServiceImpl {

    private final ServiceRuleUseDaoImpl serviceRuleUseDao;
    private final CustomFormatter formatter;

    public int count() {
        return serviceRuleUseDao.count();
    }

    public int countByCode(String code) {
        return serviceRuleUseDao.countByCode(code);
    }

    public ServiceRuleUseResponse create(ServiceRuleUseRequest request) {
        ServiceRuleUseDto dto = new ServiceRuleUseDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceRuleUseDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-RULE-USE] create() 실패 : {}", dto);
            throw new RuntimeException();
        }

        return serviceRuleUseDao.selectByRuleStat(dto.getRule_stat())
                                .toResponse();
    }

    public ServiceRuleUseResponse readByRuleStat(String rule_stat) {
        return serviceRuleUseDao.selectByRuleStat(rule_stat)
                                .toResponse();
    }


    public List<ServiceRuleUseResponse> readByCode(String code) {
        List<ServiceRuleUseDto> serviceRuleUseDtos = serviceRuleUseDao.selectByCode(code);
        return serviceRuleUseDtos.stream()
                                 .map(ServiceRuleUseResponse::new)
                                 .toList();
    }

    public List<ServiceRuleUseResponse> readAll() {
        List<ServiceRuleUseDto> serviceRuleUseDtos = serviceRuleUseDao.selectAll();
        return serviceRuleUseDtos.stream()
                                 .map(ServiceRuleUseResponse::new)
                                 .toList();
    }

    public void modify(ServiceRuleUseRequest request) {
        ServiceRuleUseDto dto = new ServiceRuleUseDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceRuleUseDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-RULE-USE] modify() 실패 : {}", dto);
            throw new RuntimeException();
        }
    }

    public void modifyUse(ServiceRuleUseRequest request) {
        ServiceRuleUseDto dto = new ServiceRuleUseDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceRuleUseDao.updateUse(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-RULE-USE] modifyUse() 실패 : {}", dto);
            throw new RuntimeException();
        }
    }

    public void removeByRuleStat(String rule_stat) {
        int rowCnt = serviceRuleUseDao.deleteByRuleStat(rule_stat);

        if (rowCnt != 1) {
            log.error("[SERVICE-RULE-USE] removeByRuleStat() 실패 : {}", rule_stat);
            throw new RuntimeException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByCode(String code) {
        int totalCnt = serviceRuleUseDao.countByCode(code);
        int rowCnt = serviceRuleUseDao.deleteByCode(code);

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-RULE-USE] removeByCode() 실패 : {}", code);
            throw new RuntimeException();
        }
    }


    @Transactional
    public void removeAll() {
        int totalCnt = serviceRuleUseDao.count();
        int rowCnt = serviceRuleUseDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-RULE-USE] removeAll() 실패");
            throw new RuntimeException();
        }
    }

}
