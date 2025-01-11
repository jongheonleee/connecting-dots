package com.example.demo.application.service;


import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserConditionsDetailResponse;
import com.example.demo.dto.service.ServiceUserConditionsDto;
import com.example.demo.dto.service.ServiceUserConditionsRequest;
import com.example.demo.dto.service.ServiceUserConditionsResponse;
import com.example.demo.repository.mybatis.service.ServiceUserConditionsDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ServiceUserConditionsServiceImpl {

    private final ServiceUserConditionsDaoImpl serviceUserConditionsDao;
    private final CustomFormatter formatter;


    public int count() {
        return serviceUserConditionsDao.count();
    }

    public int countBySearchCondition(SearchCondition sc) {
        return serviceUserConditionsDao.countBySearchCondition(sc);
    }

    public ServiceUserConditionsResponse create(ServiceUserConditionsRequest request) {
        boolean exists = serviceUserConditionsDao.existsByCondsCode(request.getConds_code());
        if (exists) {
            log.error("[SERVICE-USER-CONDITIONS] create() 실패 - 중복된 키 값(conds_code) : {}", request.getConds_code());
            throw new RuntimeException();
        }

        ServiceUserConditionsDto dto = new ServiceUserConditionsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserConditionsDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITIONS] create() 실패 : {}", dto);
            throw new RuntimeException();
        }

        return serviceUserConditionsDao.selectByCondsCode(dto.getConds_code())
                                       .toResponse();
    }

    public ServiceUserConditionsResponse readByCondsCode(String conds_code) {
        boolean exists = serviceUserConditionsDao.existsByCondsCode(conds_code);
        if (!exists) {
            log.error("[SERVICE-USER-CONDITIONS] readByCondsCode() 해당 conds_code와 관련된 회원 이용 약관 존재하지 않음 : {}", conds_code);
            throw new RuntimeException();
        }

        return serviceUserConditionsDao.selectByCondsCode(conds_code)
                                       .toResponse();
    }

    public PageResponse<ServiceUserConditionsResponse> readBySearchCondition(SearchCondition sc) {
        int totalCnt = serviceUserConditionsDao.countBySearchCondition(sc);
        List<ServiceUserConditionsResponse> responses = serviceUserConditionsDao.selectBySearchCondition(sc)
                                                                                .stream()
                                                                                .map(ServiceUserConditionsResponse::new)
                                                                                .toList();
        return new PageResponse<>(totalCnt, sc, responses);
    }

    public ServiceUserConditionsDetailResponse readByCondsCodeForUserConditions(String conds_code) {
        boolean exists = serviceUserConditionsDao.existsByCondsCode(conds_code);
        if (!exists) {
            log.error("[SERVICE-USER-CONDITIONS] readByCondsCodeForDetail() 해당 conds_code와 관련된 회원 이용 약관 존재하지 않음 : {}", conds_code);
            throw new RuntimeException();
        }

        return serviceUserConditionsDao.selectForUserConditions(conds_code)
                                       .toResponse();
    }

    public void modify(ServiceUserConditionsRequest request) {
        boolean exists = serviceUserConditionsDao.existsByCondsCode(request.getConds_code());
        if (!exists) {
            log.error("[SERVICE-USER-CONDITIONS] update() 실패 - 해당 conds_code와 관련된 회원 이용 약관 존재하지 않음 : {}", request.getConds_code());
            throw new RuntimeException();
        }

        ServiceUserConditionsDto dto = new ServiceUserConditionsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserConditionsDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITIONS] update() 실패 : {}", dto);
            throw new RuntimeException();
        }

    }

    public void modifyChkUse(ServiceUserConditionsRequest request) {
        boolean exists = serviceUserConditionsDao.existsByCondsCode(request.getConds_code());
        if (!exists) {
            log.error("[SERVICE-USER-CONDITIONS] updateChkUse() 실패 - 해당 conds_code와 관련된 회원 이용 약관 존재하지 않음 : {}", request.getConds_code());
            throw new RuntimeException();
        }

        ServiceUserConditionsDto dto = new ServiceUserConditionsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserConditionsDao.updateChkUse(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITIONS] updateChkUse() 실패 : {}", dto);
            throw new RuntimeException();
        }

    }

    public void removeByCondsCode(String conds_code) {
        int rowCnt = serviceUserConditionsDao.deleteByCondsCode(conds_code);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITIONS] remove() 실패 : {}", conds_code);
            throw new RuntimeException();
        }
    }

    public void remove(Integer seq) {
        int rowCnt = serviceUserConditionsDao.delete(seq);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITIONS] remove() 실패 : {}", seq);
            throw new RuntimeException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = serviceUserConditionsDao.count();
        int rowCnt = serviceUserConditionsDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-USER-CONDITIONS] removeAll() 실패");
            throw new RuntimeException();
        }
    }


}
