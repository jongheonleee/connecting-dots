package com.example.demo.application.service.learning;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.repository.mybatis.category.CategoryDaoImpl;
import com.example.demo.application.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplLearningTest {

    @Mock
    private CategoryDaoImpl categoryDao;

    @InjectMocks
    private CategoryServiceImpl target;

    @BeforeEach
    public void setUp() {
        assertNotNull(categoryDao);
        assertNotNull(target);
    }

    // A. 카테고리 수 카운트
    @DisplayName("카테고리 수 카운트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test1(int cnt) {

    }

    // B. 카테고리 등록
        // 1. 카테고리 등록 과정에서 예외가 발생할 경우
            // 1-1.
        // 2. 카테고리 등록 실패
        // 3. 카테고리 등록 성공

    // C-1. 카테고리 단건 조회
        // 1. 카테고리 단건 조회 과정에서 예외가 발생할 경우
        // 2. 카테고리 단건 조회 실패
        // 3. 카테고리 단건 조회 성공

    // C-2. 카테고리 전체 조회
        // 1. 카테고리 전체 조회 과정에서 예외가 발생할 경우
        // 2. 카테고리 전체 조회 실패
        // 3. 카테고리 전체 조회 성공

    // D. 카테고리 수정
        // 1. 카테고리 수정 과정에서 예외가 발생할 경우
        // 2. 카테고리 수정 실패
        // 3. 카테고리 수정 성공

    // E-1. 카테고리 단건 삭제
        // 1. 카테고리 단건 삭제 과정에서 예외가 발생할 경우
        // 2. 카테고리 단건 삭제 실패
        // 3. 카테고리 단건 삭제 성공

    // E-2. 카테고리 전체 삭제
        // 1. 카테고리 전체 삭제 과정에서 예외가 발생할 경우
        // 2. 카테고리 전체 삭제 실패
        // 3. 카테고리 전체 삭제 성공


}