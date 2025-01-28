package com.example.demo.repository.mybatis.learning;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.repository.user.UserDaoImpl;
import com.example.demo.dto.ord_user.UserFormDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

/**
 * 1차 기능 구현 목록
 *
 * - A. 회원 수 카운트 ✅
 * - B. 회원 등록 ✅
 * - C. 회원 조회 ✅
 * - D. 회원 수정 ✅
 * - E. 회원 삭제 ✅
 *
 */


@SpringBootTest
class UserDaoImplLearningTest {

    @Autowired
    private UserDaoImpl target;

    @BeforeEach
    public void setUp() {
        assertNotNull(target);
        target.deleteAll();
    }


    // A. 회원 수 카운트

    // B. 회원 등록
        // 발생하는 예외 분석
            // not null 제약 조건 위배할 경우 - DataIntegrityViolationException
            // 중복된 아이디로 등록할 경우 - DuplicateKeyException
            // 데이터 길이 제한 초과할 경우 - DataIntegrityViolationException

    @Test
    @DisplayName("not null 제약 조건 위배할 경우 - DataIntegrityViolationException")
    public void learn1() {
        var dto = createDto();
        dto.setId(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(dto)
        );
    }

    @Test
    @DisplayName("중복된 아이디로 등록할 경우 - DuplicateKeyException")
    public void learn2() {
        var dto = createDto();
        target.insert(dto);
        assertThrows(DuplicateKeyException.class,
                () -> target.insert(dto)
        );
    }

    @Test
    @DisplayName("데이터 길이 제한 초과할 경우  - DataIntegrityViolationException")
    public void learn3() {
        var dto = createDto();
        dto.setName("Over fifty characters are required to store this data without truncation issues.");
        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(dto)
        );
    }

    // C. 회원 조회
        // 회원 조회시 예외가 발생할까? - 없음(null 반환)
        // 여러건을 조회하는지 단건을 조회하는지에 따라 달라짐
        // 단건 조회시 예외가 발생할까? id로 찾을 때 해당 값이 null이면 어떻할까?
        // 여러건은 예외 발생 안할듯..
    @Test
    @DisplayName("단건 조회시 예외가 발생할까? id로 찾을 때 해당 값이 null이면 어떻게 할까?")
    public void learn4() {
        var dto = createDto();
        assertTrue(1 == target.insert(dto));
        var found = target.selectById(null);
        assertTrue(found == null);
    }


    // D. 회원 수정

    // E. 회원 삭제
        // 없는 아이디로 삭제할 경우 어떻게 될까? 적용된 로우수가 0이됨

    @DisplayName("2-1. 없는 아이디로 삭제하는 경우 삭제 실패")
    @Test
    public void test11() {
        String notExistId = "notExistId";
        int rowCnt = target.deleteById(notExistId);
    }


    private UserFormDto createDto() {
        UserFormDto dto = new UserFormDto();
        dto.setName("홍길동");
        dto.setEmail("test@gmail.com");
        dto.setId("testId");
        dto.setPwd("1234");
        dto.setSns("naver,facebook");
        return dto;
    }
}