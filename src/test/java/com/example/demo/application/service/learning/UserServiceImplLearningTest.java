package com.example.demo.application.service.learning;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.repository.mybatis.user.UserDaoImpl;
import com.example.demo.application.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 1차 기능 구현 목록
 *
 * - A. 회원 수 카운트
 * - B. 회원 등록
 * - C. 회원 조회
 * - D. 회원 수정
 * - E. 회원 삭제
 *
 */


@ExtendWith(MockitoExtension.class)
class UserServiceImplLearningTest {

    @Mock
    private UserDaoImpl userDao;


    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    public void setUp() {
        assertNotNull(userDao);
        assertNotNull(userService);
    }


    // A. 회원 수 카운트

    // B. 회원 등록

    // C. 회원 단건 조회

    // C. 회원 전체 조회

    // D. 회원 수정

    // E. 회원 단건 삭제

    // E. 회원 전체 삭제



}