package com.example.demo.application.service.impl;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceSanctionHistoryDto;
import com.example.demo.dto.service.ServiceSanctionHistoryRequest;
import com.example.demo.dto.service.ServiceSanctionHistoryResponse;
import com.example.demo.global.error.exception.business.service.ServiceSanctionHistoryAlreadyExistsException;
import com.example.demo.global.error.exception.business.service.ServiceSanctionHistoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.service.ServiceSanctionHistoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceSanctionHistoryServiceImpl {

    private final ServiceSanctionHistoryDaoImpl serviceSanctionHistoryDao;
    private final CustomFormatter formatter;

    // 추후에 회원 중에서 특정 조건을 만족하는 회원들의 제재 내역을 적용할 때 사용할 오브젝트들
    // private final ServiceTermsDaoImpl serviceTermsDao;
    // private final UserDaoImpl userDao;
    // private final UserActivityDaoImpl userActivityDao;
    // private final ServiceSanctionChecker(JDBC) serviceSanctionChecker;


    public int count() {
        return serviceSanctionHistoryDao.count();
    }

    public int countByUserSeq(Integer user_seq) {
        return serviceSanctionHistoryDao.countByUserSeq(user_seq);
    }

    // 추후에 회원 중에서 특정 조건을 만족하는 회원들의 제재 내역을 적용할 때 사용할 메서드 구현

    // - # 1. 배치 처리? WebFlux? 어떤거 적용할지 고민
    //  - 배치 처리로 구현한다면 이점은?
    //  - WebFlux(비동기)로 구현한다면 이점은?
    // - # 2. 재시도 복구 처리 적용할지 고민
    //  - 재시도 복구 처리를 적용한다면 어떤 정책으로 처리할지 고민

    // - 1. 특정 시간에 실행함, 예를 들어 새벽 04:00
    // - 2. 모든 회원과 활동 내역을 조회함
    // - 3. 서비스 정책 dao에서 조회한 조건문 생성
    // - 4. serviceSanctionChecker을 통해서 회원중에 제재 대상인 회원 식별하고 제재 처리
    //  - 4-1. 제재 대상인 회원은 회원 상태를 제재 상태로 변경
    //  - 4-2. 제재 내역을 추가함

    public ServiceSanctionHistoryResponse create(ServiceSanctionHistoryRequest request) {
        // 이 부분 잘못된 로직임
        // - 체크해야할 부분은 회원이 존재하는지 파악하고 없으면 그때 예외를 발생 시키는 것
        // - 이 부분은 중복된 제재 내역이 존재하는지 파악하는 것이므로 이 부분은 필요 없음
        boolean exists = serviceSanctionHistoryDao.existsByPoliStat(request.getPoli_stat());
        if (exists) {
            log.error("[SERVICE-SANCTION-HISTORY] create() 실패 - 중복된 키 값 : {}", request.getPoli_stat());
            throw new ServiceSanctionHistoryAlreadyExistsException();
        }

//        boolean exists = userDao.existsBySeq(request.getUser_seq());
//        if (exists) {
//            log.error("[SERVICE-SANCTION-HISTORY] create() 실패 - 회원이 존재하지 않음 : {}", request.getUser_seq());
//            throw new UserNotFoundException();
//        }

        ServiceSanctionHistoryDto dto = new ServiceSanctionHistoryDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceSanctionHistoryDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-SANCTION-HISTORY] create() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }

        return serviceSanctionHistoryDao.selectBySeq(dto.getSeq())
                                        .toResponse();
    }

    public ServiceSanctionHistoryResponse readBySeq(Integer seq) {
        boolean exists = serviceSanctionHistoryDao.existsBySeq(seq);

        if (!exists) {
            log.error("[SERVICE-SANCTION-HISTORY] readBySeq() 해당 seq와 관련된 제재 내역 존재하지 않음 : {}", seq);
            throw new ServiceSanctionHistoryNotFoundException();
        }

        return serviceSanctionHistoryDao.selectBySeq(seq)
                                        .toResponse();
    }

    public List<ServiceSanctionHistoryResponse> readByUserSeq(Integer user_seq) {
        return serviceSanctionHistoryDao.selectByUserSeq(user_seq)
                                        .stream()
                                        .map(ServiceSanctionHistoryResponse::new)
                                        .toList();
    }

    public PageResponse<ServiceSanctionHistoryResponse> readBySearchCondition(SearchCondition sc) {
        int totalCnt = serviceSanctionHistoryDao.countBySearchCondition(sc);
        List<ServiceSanctionHistoryResponse> responses = serviceSanctionHistoryDao.selectBySearchCondition(sc)
                                                                                  .stream()
                                                                                  .map(ServiceSanctionHistoryResponse::new)
                                                                                  .toList();
        return new PageResponse<>(totalCnt, sc, responses);
    }

    public List<ServiceSanctionHistoryResponse> readAll() {
        return serviceSanctionHistoryDao.selectAll()
                                        .stream()
                                        .map(ServiceSanctionHistoryResponse::new)
                                        .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(ServiceSanctionHistoryRequest request) {
        boolean exists = serviceSanctionHistoryDao.existsBySeqForUpdate(request.getSeq());

        if (!exists) {
            log.error("[SERVICE-SANCTION-HISTORY] modify() 해당 seq와 관련된 제재 내역 존재하지 않음 : {}", request.getSeq());
            throw new ServiceSanctionHistoryNotFoundException();
        }

        ServiceSanctionHistoryDto dto = new ServiceSanctionHistoryDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceSanctionHistoryDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-SANCTION-HISTORY] modify() 실패 : {}", dto);
            throw new NotApplyOnDbmsException();
        }
    }

    public void remove(Integer seq) {
        boolean exists = serviceSanctionHistoryDao.existsBySeqForUpdate(seq);

        if (!exists) {
            log.error("[SERVICE-SANCTION-HISTORY] remove() 해당 seq와 관련된 제재 내역 존재하지 않음 : {}", seq);
            throw new ServiceSanctionHistoryNotFoundException();
        }

        int rowCnt = serviceSanctionHistoryDao.deleteBySeq(seq);

        if (rowCnt != 1) {
            log.error("[SERVICE-SANCTION-HISTORY] remove() 실패 : {}", seq);
            throw new NotApplyOnDbmsException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByUserSeq(Integer user_seq) {
        int totalCnt = serviceSanctionHistoryDao.countByUserSeq(user_seq);
        int rowCnt = serviceSanctionHistoryDao.deleteByUserSeq(user_seq);

        if (totalCnt != rowCnt) {
            log.error("[SERVICE-SANCTION-HISTORY] removeByUserSeq() 실패 : {}", user_seq);
            throw new NotApplyOnDbmsException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = serviceSanctionHistoryDao.count();
        int rowCnt = serviceSanctionHistoryDao.deleteAll();

        if (totalCnt != rowCnt) {
            log.error("[SERVICE-SANCTION-HISTORY] removeAll() 실패");
            throw new NotApplyOnDbmsException();
        }
    }


}
