package com.example.demo.application.service;

import com.example.demo.dto.service.ServiceUserConditionDto;
import com.example.demo.dto.service.ServiceUserConditionRequest;
import com.example.demo.dto.service.ServiceUserConditionResponse;
import com.example.demo.repository.mybatis.service.ServiceUserConditionDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceUserConditionServiceImpl {

    private final ServiceUserConditionDaoImpl serviceUserConditionDao;
    private final CustomFormatter formatter;

    public int count() {
        return serviceUserConditionDao.count();
    }

    public ServiceUserConditionResponse create(ServiceUserConditionRequest request) {
        // 이미 존재하는 키인지 확인
        boolean exists = serviceUserConditionDao.existsByCondCode(request.getCond_code());
        // 존재하면 duplicatedKeyException 발생
        if (exists) {
            log.error("[SERVICE-USER-CONDITION] create() 실패 - 중복된 키 값 : {}", request.getCond_code());
            throw new RuntimeException();
        }

        // 그게 아니면 생성 처리
        ServiceUserConditionDto dto = new ServiceUserConditionDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserConditionDao.insert(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITION] create() 실패, DB 상의 문제 혹은 네트워크 연결 문제일 확률이 높음 : {}", dto);
            throw new RuntimeException();
        }

        // 생성 이후에 select로 조회해서 리스폰 형태로 반환
        return serviceUserConditionDao.select(dto.getCond_code())
                                      .toResponse();
    }

    public ServiceUserConditionResponse readByCondCode(String cond_code) {
        // 존재하는 키인지 확인
        boolean exists = serviceUserConditionDao.existsByCondCode(cond_code);
        // 존재하지 않으면 notFoundException 발생
        if (!exists) {
            log.error("[SERVICE-USER-CONDITION] readByCondCode() 해당 cond_code와 관련된 회원 이용 약관 항목 존재하지 않음 : {}", cond_code);
            throw new RuntimeException();
        }

        // 존재하면 select로 조회해서 리스폰 형태로 반환
        return serviceUserConditionDao.select(cond_code)
                                      .toResponse();
    }

    public List<ServiceUserConditionResponse> readAll() {
        // 전체 조회
        List<ServiceUserConditionDto> dtos = serviceUserConditionDao.selectAll();
        // 리스폰 형태로 변환
        return dtos.stream()
                   .map(ServiceUserConditionResponse::new)
                   .toList();
    }

    public void modify(ServiceUserConditionRequest request) {
        // 존재하는 키인지 확인
        boolean exists = serviceUserConditionDao.existsByCondCode(request.getCond_code());
        // 존재하지 않으면 notFoundException 발생
        if (!exists) {
            log.error("[SERVICE-USER-CONDITION] update() 해당 cond_code와 관련된 회원 이용 약관 항목 존재하지 않음 : {}", request.getCond_code());
            throw new RuntimeException();
        }

        // 존재하면 업데이트 처리
        ServiceUserConditionDto dto = new ServiceUserConditionDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserConditionDao.update(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITION] update() 실패, DB 상의 문제 혹은 네트워크 연결 문제일 확률이 높음 : {}", dto);
            throw new RuntimeException();
        }
    }

    public void modifyChkUse(ServiceUserConditionRequest request) {
        // 존재하는 키인지 확인
        boolean exists = serviceUserConditionDao.existsByCondCode(request.getCond_code());
        // 존재하지 않으면 notFoundException 발생
        if (!exists) {
            log.error("[SERVICE-USER-CONDITION] updateChkUse() 해당 cond_code와 관련된 회원 이용 약관 항목 존재하지 않음 : {}", request.getCond_code());
            throw new RuntimeException();
        }

        // 존재하면 업데이트 처리
        ServiceUserConditionDto dto = new ServiceUserConditionDto(request, formatter.getCurrentDateFormat(), formatter.getManagerSeq(), formatter.getCurrentDateFormat(), formatter.getManagerSeq());
        int rowCnt = serviceUserConditionDao.updateChkUse(dto);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITION] updateChkUse() 실패, DB 상의 문제 혹은 네트워크 연결 문제일 확률이 높음 : {}", dto);
            throw new RuntimeException();
        }
    }

    public void remove(String cond_code) {
        // 삭제 처리
        int rowCnt = serviceUserConditionDao.delete(cond_code);

        if (rowCnt != 1) {
            log.error("[SERVICE-USER-CONDITION] remove() 실패 : {}", cond_code);
            throw new RuntimeException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        // 전체 삭제 처리
        int totalCnt = serviceUserConditionDao.count();
        int rowCnt = serviceUserConditionDao.deleteAll();

        if (rowCnt != totalCnt) {
            log.error("[SERVICE-USER-CONDITION] removeAll() 실패 : 전체 개수와 삭제된 항목의 개수가 일치하지 않음");
            throw new RuntimeException();
        }
    }


}
