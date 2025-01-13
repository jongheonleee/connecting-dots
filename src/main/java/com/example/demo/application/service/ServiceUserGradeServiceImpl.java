package com.example.demo.application.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserGradeDto;
import com.example.demo.dto.service.ServiceUserGradeRequest;
import com.example.demo.dto.service.ServiceUserGradeResponse;
import com.example.demo.repository.mybatis.service.ServiceUserGradeDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceUserGradeServiceImpl {

    private final ServiceUserGradeDaoImpl serviceUserGradeDao;
    private final CustomFormatter formatter;


    public int count() {
        return serviceUserGradeDao.count();
    }

    public ServiceUserGradeResponse create(ServiceUserGradeRequest request) {
        boolean exists = serviceUserGradeDao.existsByStatCode(request.getStat_code());
        if (exists) {
            log.error("[SERVICE-USER-GRADE] create() 실패 - 중복된 키 값 : {}", request.getStat_code());
            throw new RuntimeException();
        }

        ServiceUserGradeDto dto = new ServiceUserGradeDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserGradeDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-GRADE] create() 실패 : {}", dto);
            throw new RuntimeException();
        }

        return serviceUserGradeDao.selectByStatCode(dto.getStat_code())
                                  .toResponse();
    }

    public List<ServiceUserGradeResponse> readAll() {
        return serviceUserGradeDao.selectAll()
                                  .stream()
                                  .map(ServiceUserGradeResponse::new)
                                  .toList();
    }

    public PageResponse<ServiceUserGradeResponse> readBySearchCondition(SearchCondition sc) {
        int totalCnt = serviceUserGradeDao.countBySearchCondition(sc);
        List<ServiceUserGradeResponse> response = serviceUserGradeDao.selectBySearchCondition(sc)
                                                                    .stream()
                                                                    .map(ServiceUserGradeResponse::new)
                                                                    .toList();

        return new PageResponse<>(totalCnt, sc, response); // 이 부분 추후에 빌더 패턴으로 적용하기
    }

    public ServiceUserGradeResponse readByStatCode(String stat_code) {
        boolean exists = serviceUserGradeDao.existsByStatCode(stat_code);
        if (!exists) {
            log.error("[SERVICE-USER-GRADE] readByStatCode() 실패 : {}", stat_code);
            throw new RuntimeException();
        }

        return serviceUserGradeDao.selectByStatCode(stat_code)
                                  .toResponse();
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(ServiceUserGradeRequest request) {
        boolean exists = serviceUserGradeDao.existsByStatCodeForUpdate(request.getStat_code());
        if (!exists) {
            log.error("[SERVICE-USER-GRADE] modify() 실패, 해당 키와 관련된 데이터가 없음 : {}", request.getStat_code());
            throw new RuntimeException();
        }

        ServiceUserGradeDto dto = new ServiceUserGradeDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserGradeDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-GRADE] modify() 실패 : {}", dto);
            throw new RuntimeException();
        }
    }

    public void removeByStatCode(String stat_code) {
        int rowCnt = serviceUserGradeDao.deleteByStatCode(stat_code);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-GRADE] removeByStatCode() 실패 : {}", stat_code);
            throw new RuntimeException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = serviceUserGradeDao.count();
        int rowCnt = serviceUserGradeDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-USER-GRADE] removeAll() 실패");
            throw new RuntimeException();
        }
    }




}
