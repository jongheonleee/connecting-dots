package com.example.demo.application.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceTermsDto;
import com.example.demo.dto.service.ServiceTermsRequest;
import com.example.demo.dto.service.ServiceTermsResponse;
import com.example.demo.repository.mybatis.service.ServiceTermsDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ServiceTermsServiceImpl {

    private final ServiceTermsDaoImpl serviceTermsDao;
    private final CustomFormatter formatter;

    public int count() {
        return serviceTermsDao.count();
    }

    public int countBySearchCondition(SearchCondition sc) {
        return serviceTermsDao.countBySearchCondition(sc);
    }

    public ServiceTermsResponse create(ServiceTermsRequest request) {
        ServiceTermsDto dto = new ServiceTermsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceTermsDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-TERMS] create() 실패 : {}", dto);
            throw new RuntimeException();
        }

        return serviceTermsDao.select(dto.getPoli_stat())
                             .toResponse();
    }

    public ServiceTermsResponse readByPoliStat(String poli_stat) {
        return serviceTermsDao.select(poli_stat)
                             .toResponse();
    }

    public PageResponse<ServiceTermsResponse> readBySearchCondition(SearchCondition sc) {
        int totalCnt = serviceTermsDao.countBySearchCondition(sc);
        List<ServiceTermsDto> dtos = serviceTermsDao.selectBySearchCondition(sc);
        List<ServiceTermsResponse> responses = dtos.stream()
                                                   .map(ServiceTermsResponse::new)
                                                   .toList();
        return new PageResponse<>(totalCnt, sc, responses);
    }

    public List<ServiceTermsResponse> readAll() {
        List<ServiceTermsDto> dtos = serviceTermsDao.selectAll();
        return dtos.stream()
                   .map(ServiceTermsResponse::new)
                   .toList();
    }

    public void modify(ServiceTermsRequest request) {
        ServiceTermsDto dto = new ServiceTermsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceTermsDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-TERMS] modify() 실패 : {}", dto);
            throw new RuntimeException();
        }
    }

    public void modifyChkUse(ServiceTermsRequest request) {
        ServiceTermsDto dto = new ServiceTermsDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceTermsDao.updateChkUse(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-TERMS] modifyChkUse() 실패 : {}", dto);
            throw new RuntimeException();
        }
    }

    public void remove(String poli_stat) {
        int rowCnt = serviceTermsDao.delete(poli_stat);

        if (rowCnt != 1) {
            log.error("[SERVICE-TERMS] remove() 실패 : {}", poli_stat);
            throw new RuntimeException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = serviceTermsDao.count();
        int rowCnt = serviceTermsDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-TERMS] removeAll() 실패");
            throw new RuntimeException();
        }
    }

}
