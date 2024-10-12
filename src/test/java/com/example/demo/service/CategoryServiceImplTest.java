package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.dao.CategoryDaoImpl;
import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.CategoryAlreadyExistsException;
import com.example.demo.exception.CategoryFormInvalidException;
import com.example.demo.exception.InternalServerError;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {


    @Mock
    private CategoryDaoImpl categoryDao;

    @InjectMocks
    private CategoryServiceImpl target;

    private List<CategoryDto> fixture = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        fixture.clear();
        assertNotNull(categoryDao);
        assertNotNull(target);
    }

    // A. 카테고리 수 카운트
        // 1. 카테고리 수 카운트 과정에서 예외가 발생할 경우
            // 1-1. dao에서 예기치 못한 예외가 발생
        // 2. 카테고리 수 카운트 실패 - x
        // 3. 카테고리 수 카운트 성공
            // 3-1. 카테고리 여러개 등록해 놓고 카테고리 수 카운트 해보기
    @DisplayName("1-1. 카테고리 수 카운트 과정에서 예외가 발생할 경우 ")
    @Test
    public void test1() {
        // dao에서 예외 발생시키기
        // service에서 해당 에러를 RuntimeException로 변환해서 던지기
        when(categoryDao.count()).thenThrow(new RuntimeException("예기치 못한 에러"));
        assertThrows(RuntimeException.class, () -> target.count());
    }

    @DisplayName("3-1. 카테고리 여러개 등록해 놓고 카테고리 수 카운트 해보기 ")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test2(int cnt) {
        // dao에서 cnt 만큼 카테고리 카운팅하게 설정
        // service에서 카운팅한 결과를 반환
        when(categoryDao.count()).thenReturn(cnt);
        assertEquals(cnt, target.count());
    }

    // B. 카테고리 등록
        // 1. 카테고리 등록 과정에서 예외가 발생할 경우
            // 1-1. dao에서 예기치 못한 예외가 발생
            // 1-2. dao에서 DataIntegrityViolationException 발생
            // 1-3. dao에서 DuplicateKeyException 발생
        // 2. 카테고리 등록 실패 - x
        // 3. 카테고리 등록 성공
            // 3-1. 여러개의 카테고리 등록하고 조회해서 비교해보기

    @DisplayName("1-1. dao에서 예기치 못한 예외가 발생")
    @Test
    public void test3() {
        // dao에서 예외 발생시키기
        // service에서 해당 에러를 RuntimeException 변환해서 던지기
        var dto = createCategoryDto(1);
        when(categoryDao.insert(dto)).thenThrow(new RuntimeException("예기치 못한 에러"));
        assertThrows(RuntimeException.class, () -> target.create(dto));
    }

    @DisplayName("1-2. dao에서 DataIntegrityViolationException 발생")
    @Test
    public void test4() {
        // dao에서 DataIntegrityViolationException 발생시키기
        // service에서 해당 에러를 CategoryFormInvalidException 변환해서 던지기
        var dto = createCategoryDto(1);
        when(categoryDao.insert(dto)).thenThrow(new DataIntegrityViolationException("예기치 못한 에러"));
        assertThrows(CategoryFormInvalidException.class, () -> target.create(dto));
    }

    @DisplayName("1-3. dao에서 DuplicateKeyException 발생")
    @Test
    public void test5() {
        // dao에서 DuplicateKeyException 발생시키기
        // service에서 해당 에러를 CategoryAlreadyExistsException 변환해서 던지기
        var dto = createCategoryDto(1);
        when(categoryDao.insert(dto)).thenThrow(new DuplicateKeyException("예기치 못한 에러"));
        assertThrows(CategoryAlreadyExistsException.class, () -> target.create(dto));
    }

    @DisplayName("1-4. dao의 작업이 DB에 제대로 적용이 안된 경우")
    @Test
    public void test51() {
        // dao에서 적용된 로우수 0 반환하게 설정
        // service에서 해당 에러를 InternalServerError 변환해서 던지기
        var dto = createCategoryDto(1);
        when(categoryDao.insert(dto)).thenReturn(0);
        assertThrows(InternalServerError.class, () -> target.create(dto));
    }

    @DisplayName("3-1. 여러개의 카테고리 등록하고 조회해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test6(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // dao에서 insert 호출시 항상 성공이라고 가정
        // service 호출시 아무 문제 없이 성공하는지 확인
        createFixture(cnt);
        fixture.forEach(dto -> {
            when(categoryDao.insert(dto)).thenReturn(1);
            assertDoesNotThrow(() -> target.create(dto));
        });
    }

    // C-1. 카테고리 단건 조회
        // 1. 카테고리 단건 조회 과정에서 예외가 발생할 경우
            // 1-1. dao에서 예기치 못한 예외가 발생
        // 2. 카테고리 단건 조회 실패
            // 2-1. 없는 코드로 카테고리를 조회하는 경우 null 반환
        // 3. 카테고리 단건 조회 성공
            // 3-1. 여러개의 카테고리 등록하고 각각 조회해서 비교해보기
            // 3-2. 여러개의 카테고리 등록하고 그중에 랜덤으로 하나 뽑아서 조히하기
    @DisplayName("1-1. dao에서 예기치 못한 예외가 발생")
    @Test
    public void test7() {
        // dao에서 예외 발생시키기
        // service에서 해당 에러를 InternalServerError로 변환해서 던지기
    }

    @DisplayName("2-1. 없는 코드로 카테고리를 조회하는 경우 null 반환")
    @Test
    public void test8() {
        // dao에서 null 반환하도록 설정
        // service에서 해당 에러를 CategoryNotFoundException로 변환해서 던지기
    }

    @DisplayName("3-1. 여러개의 카테고리 등록하고 각각 조회해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test9(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // dao에서 select 호출시 항상 특정 dto 반환하게 가정
        // service 호출시 각각의 카테고리를 조회해서 비교
    }

    @DisplayName("3-2. 여러개의 카테고리 등록하고 그중에 랜덤으로 하나 뽑아서 조회해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test10(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // 랜덤 인덱스 설정
        // dao에서 select 호출시 항상 특정 dto 반환하게 가정
        // service 호출시 랜덤으로 하나의 카테고리를 조회해서 비교
    }

    // C-2. 카테고리 전체 조회
        // 1. 카테고리 전체 조회 과정에서 예외가 발생할 경우
            // 1-1. dao에서 예기치 못한 예외가 발생
        // 2. 카테고리 전체 조회 실패 - x
        // 3. 카테고리 전체 조회 성공
            // 3-1. 여러개의 카테고리 등록하고 각각 조회해서 비교해보기
    @DisplayName("1-1. dao에서 예기치 못한 예외가 발생")
    @Test
    public void test11() {
        // dao에서 예외 발생시키기
        // service에서 해당 에러를 InternalServerError로 변환해서 던지기
    }

    @DisplayName("3-1. 여러개의 카테고리 등록하고 각각 조회해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test12(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // dao에서 selectAll 호출시 항상 특정 dto 리스트 반환하게 가정
        // service 호출시 각각의 카테고리를 조회해서 비교
    }



    // D. 카테고리 수정
        // 1. 카테고리 수정 과정에서 예외가 발생할 경우
            // 1-1. dao에서 예기치 못한 예외가 발생
            // 1-2. dao에서 DataIntegrityViolationException 발생
            // 1-3. dao에서 DuplicateKeyException 발생
        // 2. 카테고리 수정 실패
            // 2-1. 없는 코드로 카테고리를 수정하는 경우
        // 3. 카테고리 수정 성공
            // 3-1. 여러개의 카테고리 등록하고 각각 수정해서 비교해보기
            // 3-2. 여러개의 카테고리 등록하고 그중에 랜덤으로 하나 뽑아서 수정해서 비교해보기
    @DisplayName("1-1. dao에서 예기치 못한 예외가 발생")
    @Test
    public void test14() {
        // dao에서 예외 발생시키기
        // service에서 해당 에러를 InternalServerError로 변환해서 던지기
    }

    @DisplayName("1-2. dao에서 DataIntegrityViolationException 발생")
    @Test
    public void test15() {
        // dao에서 DataIntegrityViolationException 발생시키기
        // service에서 해당 에러를 CategoryFormInvalidException 변환해서 던지기
    }

    @DisplayName("1-3. dao에서 DuplicateKeyException 발생")
    @Test
    public void test16() {
        // dao에서 DuplicateKeyException 발생시키기
        // service에서 해당 에러를 CategoryAlreadyExistsException 변환해서 던지기
    }

    @DisplayName("2-1. 없는 코드로 카테고리를 수정하는 경우")
    @Test
    public void test17() {
        // dao에서 null 반환하도록 설정
        // service에서 해당 에러를 CategoryNotFoundException로 변환해서 던지기
    }

    @DisplayName("3-1. 여러개의 카테고리 등록하고 각각 수정해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test18(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // dao에서 update 호출시 항상 성공이라고 가정
        // service 호출시 각각의 카테고리를 수정해서 비교
    }

    @DisplayName("3-2. 여러개의 카테고리 등록하고 그중에 랜덤으로 하나 뽑아서 수정해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test19(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // 랜덤 인덱스 설정
        // dao에서 update 호출시 항상 성공이라고 가정
        // service 호출시 랜덤으로 하나의 카테고리를 수정해서 비교
    }


    // E-1. 카테고리 단건 삭제
        // 1. 카테고리 단건 삭제 과정에서 예외가 발생할 경우
            // 1-1. dao에서 예기치 못한 예외가 발생
        // 2. 카테고리 단건 삭제 실패
            // 2-1. 없는 코드로 삭제를 하는 경우
        // 3. 카테고리 단건 삭제 성공
            // 3-1. 여러개의 카테고리 등록하고 각각 삭제해서 비교해보기
            // 3-2. 여러개의 카테고리 등록하고 그중에 랜덤으로 하나 뽑아서 삭제해서 비교해보기
    @DisplayName("1-1. dao에서 예기치 못한 예외가 발생")
    @Test
    public void test20() {
        // dao에서 예외 발생시키기
        // service에서 해당 에러를 InternalServerError로 변환해서 던지기
    }

    @DisplayName("2-1. 없는 코드로 삭제를 하는 경우")
    @Test
    public void test21() {
        // dao에서 null 반환하도록 설정
        // service에서 해당 에러를 CategoryNotFoundException로 변환해서 던지기
    }

    @DisplayName("3-1. 여러개의 카테고리 등록하고 각각 삭제해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test22(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // dao에서 delete 호출시 항상 성공이라고 가정
        // service 호출시 각각의 카테고리를 삭제해서 비교
    }

    @DisplayName("3-2. 여러개의 카테고리 등록하고 그중에 랜덤으로 하나 뽑아서 삭제해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test23(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // 랜덤 인덱스 설정
        // dao에서 delete 호출시 항상 성공이라고 가정
        // service 호출시 랜덤으로 하나의 카테고리를 삭제해서 비교
    }


    // E-2. 카테고리 전체 삭제
        // 1. 카테고리 전체 삭제 과정에서 예외가 발생할 경우
            // 1-1. dao에서 예기치 못한 예외가 발생, 그래서 전체 롤백 처리
        // 2. 카테고리 전체 삭제 실패 - x
        // 3. 카테고리 전체 삭제 성공
            // 3-1. 여러개의 카테고리 등록하고 전체 삭제해서 비교해보기

    @DisplayName("1-1. dao에서 예기치 못한 예외가 발생, 그래서 전체 롤백 처리")
    @Test
    public void test24() {
        // dao에서 예외 발생시키기
        // service에서 해당 에러를 InternalServerError로 변환해서 던지기
        // 롤백 처리 잘 됐는지 확인
    }

    @DisplayName("3-1. 여러개의 카테고리 등록하고 전체 삭제해서 비교해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test25(int cnt) {
        // cnt 만큼 fixture에 카테고리 추가
        // dao에서 deleteAll 호출시 항상 성공이라고 가정
        // service 호출시 전체 카테고리를 삭제해서 비교
    }

    private CategoryDto createCategoryDto(int i) {
        var dto = new CategoryDto();

        dto.setCate_code("cate_code" + i);
        dto.setTop_cate("top_cate" + i);
        dto.setName("name" + i);
        dto.setComt("comt" + i);
        dto.setReg_date("reg_date" + i);
        dto.setReg_id("reg_id" + i);
        dto.setUp_date("up_date" + i);
        dto.setUp_id("up_id" + i);

        return dto;
    }

    private int chooseRandomIndex(int cnt) {
        return (int) (Math.random() * cnt);
    }

    private void createFixture(int cnt) {
        for (int i = 0; i < cnt; i++) {
            fixture.add(createCategoryDto(i));
        }
    }

}