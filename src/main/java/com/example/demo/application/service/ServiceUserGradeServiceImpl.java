package com.example.demo.application.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserGradeDto;
import com.example.demo.dto.service.ServiceUserGradeRequest;
import com.example.demo.dto.service.ServiceUserGradeResponse;
import com.example.demo.global.error.exception.business.service.ServiceUserGradeAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceUserGradeNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
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

    // 추후에 회원 중에서 회원들 중에서 특정 조건을 만족하는 회원들의 등급을 적용할 때 사용할 오브젝트들
    // private final ServiceTermsDaoImpl serviceTermsDao;
    // private final UserServiceDaoImpl userServiceDao;
    // private final UserActivityDaoImpl userActivityDao;
    // private final ServiceUserGradeChecker(JDBC) serviceUserGradeChecker;

    // - # 1. 배치 처리? WebFlux? 어떤거 적용할지 고민
    //  - 배치 처리로 구현한다면 이점은?
    //  - WebFlux(비동기)로 구현한다면 이점은?
    // - # 2. 재시도 복구 처리 적용할지 고민
    //  - 재시도 복구 처리를 적용한다면 어떤 정책으로 처리할지 고민

    // - 1. 특정 시간에 실행함, 예를 들어 새벽 04:00
    // - 2. 모든 회원과 활동 내역을 조회함
    // - 3. 서비스 정책 dao에서 조회한 조건문 생성
    // - 4. serviceUserGradeChecker 통해서 회원중에 등급 업그레이드 대상을 식별하고 업그레이드 처리
    //  - 4-1. 회원 상태를 등급변경 상태



    public int count() {
        return serviceUserGradeDao.count();
    }

    public ServiceUserGradeResponse create(ServiceUserGradeRequest request) {
        boolean exists = serviceUserGradeDao.existsByStatCode(request.getStat_code());
        if (exists) {
            log.error("[SERVICE-USER-GRADE] create() 실패 - 중복된 키 값 : {}", request.getStat_code());
            throw new ServiceUserGradeAlreadyExistsException();
        }

        ServiceUserGradeDto dto = new ServiceUserGradeDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserGradeDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-GRADE] create() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
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
            throw new ServiceUserGradeNotFoundException();
        }

        return serviceUserGradeDao.selectByStatCode(stat_code)
                                  .toResponse();
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(ServiceUserGradeRequest request) {
        boolean exists = serviceUserGradeDao.existsByStatCodeForUpdate(request.getStat_code());
        if (!exists) {
            log.error("[SERVICE-USER-GRADE] modify() 실패, 해당 키와 관련된 데이터가 없음 : {}", request.getStat_code());
            throw new ServiceUserGradeNotFoundException();
        }

        ServiceUserGradeDto dto = new ServiceUserGradeDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserGradeDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-GRADE] modify() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }
    }

    public void removeByStatCode(String stat_code) {
        int rowCnt = serviceUserGradeDao.deleteByStatCode(stat_code);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-GRADE] removeByStatCode() 실패 : {}", stat_code);
            throw new NotApplyOnDbmsException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = serviceUserGradeDao.count();
        int rowCnt = serviceUserGradeDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-USER-GRADE] removeAll() 실패");
            throw new NotApplyOnDbmsException();
        }
    }




}
