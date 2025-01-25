package com.example.demo.service.service.impl;

import com.example.demo.service.service.ServiceTermsService;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceTermsConditionDto;
import com.example.demo.dto.service.ServiceTermsDto;
import com.example.demo.dto.service.ServiceTermsRequest;
import com.example.demo.dto.service.ServiceTermsResponse;
import com.example.demo.global.error.exception.business.service.ServiceTermsAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceTermsNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.service.ServiceTermsDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceTermsServiceImpl implements ServiceTermsService {

    private final ServiceTermsDaoImpl serviceTermsDao;
    private final CustomFormatter formatter;

    @Override
    public int count() {
        return serviceTermsDao.count();
    }

    @Override
    public int countBySearchCondition(SearchCondition sc) {
        return serviceTermsDao.countBySearchCondition(sc);
    }


    @Override
    public ServiceTermsResponse create(ServiceTermsRequest request) {
        boolean exists = serviceTermsDao.existsByPoliStat(request.getPoli_stat());
        if (exists) {
            log.error("[SERVICE-TERMS] create() 실패 - 중복된 키 값 : {}", request.getPoli_stat());
            throw new ServiceTermsAlreadyExistsException();
        }

        ServiceTermsDto dto = new ServiceTermsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceTermsDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-TERMS] create() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }

        return serviceTermsDao.select(dto.getPoli_stat())
                              .toResponse();
    }

    @Override
    public ServiceTermsResponse readByPoliStat(String poli_stat) {
        boolean exists = serviceTermsDao.existsByPoliStat(poli_stat);
        if (!exists) {
            log.error("[SERVICE-TERMS] getServiceTermsCondition() 실패 : {}", poli_stat);
            throw new ServiceTermsNotFoundException();
        }

        return serviceTermsDao.select(poli_stat)
                              .toResponse();
    }

    @Override
    public PageResponse<ServiceTermsResponse> readBySearchCondition(SearchCondition sc) {
        int totalCnt = serviceTermsDao.countBySearchCondition(sc);
        List<ServiceTermsResponse> responses = serviceTermsDao.selectBySearchCondition(sc)
                                                              .stream()
                                                              .map(ServiceTermsResponse::new)
                                                              .toList();

        return new PageResponse<>(totalCnt, sc, responses);
    }

    @Override
    public List<ServiceTermsResponse> readAll() {
        return serviceTermsDao.selectAll()
                              .stream()
                              .map(ServiceTermsResponse::new)
                              .toList();
    }

    // 리스폰스 형태로 반환하게 만들기
    @Override
    public String getServiceTermsCondition(String poli_stat) {
        boolean exists = serviceTermsDao.existsByPoliStat(poli_stat);
        if (!exists) {
            log.error("[SERVICE-TERMS] getServiceTermsCondition() 실패 : {}", poli_stat);
            throw new ServiceTermsNotFoundException();
        }

        ServiceTermsConditionDto serviceTermsConditionDto = serviceTermsDao.selectForCondition(poli_stat);
        return serviceTermsConditionDto.getCond();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ServiceTermsRequest request) {
        boolean exists = serviceTermsDao.existsByPoliStatForUpdate(request.getPoli_stat());
        if (!exists) {
            log.error("[SERVICE-TERMS] modify() 실패 - {} 키 값에 대한 정책이 존재하지 않음", request.getPoli_stat());
            throw new ServiceTermsNotFoundException();
        }

        ServiceTermsDto dto = new ServiceTermsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceTermsDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-TERMS] modify() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyChkUse(ServiceTermsRequest request) {
        boolean exists = serviceTermsDao.existsByPoliStatForUpdate(request.getPoli_stat());
        if (!exists) {
            log.error("[SERVICE-TERMS] modifyChkUse() 실패 - {} 키 값에 대한 정책이 존재하지 않음", request.getPoli_stat());
            throw new ServiceTermsNotFoundException();
        }

        ServiceTermsDto dto = new ServiceTermsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceTermsDao.updateChkUse(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-TERMS] modifyChkUse() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }
    }

    @Override
    public void remove(String poli_stat) {
        int rowCnt = serviceTermsDao.delete(poli_stat);

        if (rowCnt != 1) {
            log.error("[SERVICE-TERMS] remove() 실패 : {}", poli_stat);
            throw new NotApplyOnDbmsException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = serviceTermsDao.count();
        int rowCnt = serviceTermsDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-TERMS] removeAll() 실패");
            throw new NotApplyOnDbmsException();
        }
    }

}
