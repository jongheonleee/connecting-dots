package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.CategoryDto;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;


/**
 * 1차 기능 구현 목록
 *
 * - A. 카테고리 수 카운트 ✅
 *
 * - B. 카테고리 등록 ✅
 *
 * - C. 카테고리 조회 ✅
 *  - C-1. 단건 조회
 *  - C-2. 전체 조회
 *
 * - D. 카테고리 수정 ✅
 *
 * - E. 카테고리 삭제 ✅
 *  - E-1. 단건 삭제
 *  - E-2. 전체 삭제
 */
@SpringBootTest
class CategoryDaoImplTest {

    @Autowired
    private CategoryDaoImpl target;

    private List<CategoryDto> fixture = new ArrayList<>();
    private List<CategoryDto> updatedFixture = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        assertNotNull(target);
        target.deleteAll();
    }



    // 카테고리 CRUD 테스트
      // C : 카테고리를 등록
      // R : 카테고리를 조회
        // 단건 조회
        // 전체 조회

      // U : 카테고리를 수정
      // D : 카테고리를 삭제
        // 단건 삭제
        // 전체 삭제

    // A. 카테고리 수 카운트 ✅
        // 1. 카테고리 수 카운트 과정에서 예외 발생 - x

        // 2. 카테고리 수 카운트 과정 실패 - x

        // 3. 카테고리 수 카운트 과정 성공
            // 3-1. 카테고리 수 카운트 여러번 시도해보기

    @DisplayName("3-1. 카테고리 수 카운트 여러번 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test1(int cnt) {
        // cnt 만큼 카테고리 등록
        // 카테고리 수 카운트
        // 카테고리 수가 cnt와 같은지 확인
        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        assertEquals(cnt, target.count());
    }


    // B. C : 카테고리를 등록 ✅
        // 1. 카테고리 등록 과정에서 예외 발생
            // 1-1. 필수값 누락
            // 1-2. 중복된 카테고리 등록
            // 1-3. 제약 조건 위배
            // 1-4. 카테고리 데이터 null인 경우

        // 2. 카테고리 등록 과정 실패 - x
        // 3. 카테고리 등록 과정 성공
            // 3-1. 카테고리 등록 여러번 시도해보기

    @DisplayName("1-1. 필수값 누락")
    @Test
    public void test2() {
        // dto 생성
        // 필수값 null 세팅
        // 카테고리 등록
        // DataIntegrityViolationException 발생
        var dto = createCategoryDto(0);
        dto.setCate_code(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(dto));
    }

    @DisplayName("1-2. 중복된 카테고리 등록")
    @Test
    public void test3() {
        // dto 2개 생성
        // 첫 번째 dto 등록
        // 두 번재 dto 첫 번째 dto와 같은 아이디로 세팅
        // 두 번째 dto 등록
        // DuplicateKeyException 발생
        var dto1 = createCategoryDto(0);
        var dto2 = createCategoryDto(0);

        assertTrue(1 == target.insert(dto1));
        dto2.setCate_code(dto1.getCate_code());

        assertThrows(DuplicateKeyException.class,
                () -> target.insert(dto2));
    }

    @DisplayName("1-3. 제약 조건 위배")
    @Test
    public void test4() {
        // dto 생성
        // name의 필드 varchar(20) 넘는 값 세팅
        // 카테고리 등록
        // DataIntegrityViolationException 발생
        var dto = createCategoryDto(0);
        dto.setName("123456789012345678901");
        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(dto));
    }

    @DisplayName("1-4. 카테고리 데이터 null인 경우")
    @Test
    public void test5() {
        // null로 카테고리 등록
        // DataIntegrityViolationException 발생
        assertThrows(DataIntegrityViolationException.class,
                () -> target.insert(null));
    }

    @DisplayName("3-1. 카테고리 등록 여러번 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test6(int cnt) {
        // cnt 만큼 카테고리 dto 생성(더미 데이터 생성)
        // 더미 데이터에서 dto 조회
            // 각 dto마다 등록 처리
            // 이때, 적용된 로우수 1

        // 카테고리 수 카운트했을 때 cnt만큼 나오는지 확인

        // 더미 데이터 정렬, 전체 조회했을 때 카테고리 데이터 정렬
        // 더미 데이터와 카테고리 데이터의 각 dto 내용 비교

        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        assertEquals(cnt, target.count());

        var actual = target.selectAll();

        fixture.sort((a, b) -> a.getCate_code().compareTo(b.getCate_code()));
        actual.sort((a, b) -> a.getCate_code().compareTo(b.getCate_code()));

        for (int i=0; i<cnt; i++) {
            assertTrue(isSameCategoryDto(fixture.get(i), actual.get(i)));
        }

    }


    // C. R : 카테고리를 조회 ✅
        // C-1. 단건 조회
            // 1. 카테고리 조회 과정에서 예외 발생 - x

            // 2. 카테고리 조회 실패
                // 2-1. 존재하지 않는 카테고리 조회

            // 3. 카테고리 조회 성공
                // 3-1. 카테고리 조회 여러번 시도해보기

        // C-2. 전체 조회
            // 1. 카테고리 전체 조회 과정에서 예외 발생 - x

            // 2. 카테고리 전체 조회 실패 - x

            // 3. 카테고리 전체 조회 성공
                // 3-1. 카테고리 전체 조회 시도해보기

    @DisplayName("C-1, 2-1. 존재하지 않는 카테고리 조회")
    @Test
    public void test7() {
        var dto = createCategoryDto(0);
        assertNull(target.selectByCode(dto.getCate_code()));
    }

    @DisplayName("C-1, 3-1. 카테고리 조회 여러번 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test8(int cnt) {
        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        for (var dto : fixture) {
            var actual = target.selectByCode(dto.getCate_code());
            assertTrue(isSameCategoryDto(dto, actual));
        }
    }

    @DisplayName("C-2, 3-1. 카테고리 전체 조회 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test9(int cnt) {
        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        var actual = target.selectAll();

        fixture.sort((a, b) -> a.getCate_code().compareTo(b.getCate_code()));
        actual.sort((a, b) -> a.getCate_code().compareTo(b.getCate_code()));

        for (int i=0; i<cnt; i++) {
            assertTrue(isSameCategoryDto(fixture.get(i), actual.get(i)));
        }
    }


    // D. U : 카테고리를 수정
        // 1. 카테고리 수정 과정에서 예외 발생
            // 1-1. 필수값 누락
            // 1-2. 코드를 수정했을 때 중복된 코드로 수정 시도
            // 1-3. 특정 필드 값 수정했을 때 제약 조건 위배
            // 1-4. 카테고리 데이터 null인 경우

        // 2. 카테고리 수정 실패
            // 2-1. 존재하지 않는 코드의 카테고리 수정해보기

        // 3. 카테고리 수정 성공
            // 3-1. 카테고리 수정 여러번 시도해보기
            // 3-2. 여러개의 카테고리에서 특정 카테고리만 수정 시도해보기

    @DisplayName("1-1. 필수값 누락")
    @Test
    public void test10() {
        // dto 생성
        // 해당 dto 등록
        // 해당 dto 필수값 null 세팅
        // 카테고리 수정 시도
        // DataIntegrityViolationException 발생
        var dto = createCategoryDto(0);
        assertTrue(1 == target.insert(dto));

        dto.setTop_cate(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> target.update(dto));
    }

//    @DisplayName("1-2. 코드를 수정했을 때 중복된 코드로 수정 시도")
//    @Test
//    public void test11() {
//        // dto 2개 생성
//        // 두개의 dto 모두 등록
//        // 두번째 dto를 첫번째 dto와 같은 코드로 설정
//        // 두번째 dto 수정 시도
//        // DuplicateKeyException 발생
//    }

    @DisplayName("1-3. 특정 필드 값 수정했을 때 제약 조건 위배")
    @Test
    public void test12() {
        // dto 생성
        // 해당 dto 등록
        // 해당 dto 이름 필드를 varchar(20) 넘는 값으로 수정
        // 카테고리 수정 시도
        // DataIntegrityViolationException 발생
        var dto = createCategoryDto(0);
        assertTrue(1 == target.insert(dto));

        dto.setName("123456789012345678901");
        assertThrows(DataIntegrityViolationException.class,
                () -> target.update(dto));
    }

    @DisplayName("1-4. 카테고리 데이터 null인 경우")
    @Test
    public void test13() {
        // null로 카테고리 수정
        // DataIntegrityViolationException 발생
        assertTrue(0 == target.update(null));
    }

    @DisplayName("2-1. 존재하지 않는 코드의 카테고리 수정해보기")
    @Test
    public void test14() {
        // 존재하지 않는 코드의 dto 생성
        // 카데고리 수정 시도
        // 적용된 로우수 0
        var dto = createCategoryDto(0);
        assertTrue(0 == target.update(dto));
    }

    @DisplayName("3-1. 카테고리 수정 여러번 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test15(int cnt) {
        // cnt 만큼 카테고리 dto 생성(더미 데이터 생성)
        // 해당 더미 데이터 등록
        // 해당 더미 데이터의 수정 필드값 설정
        // 해당 더미 데이터 각각 수정 시도
            // 적용된 로우수 1

        // 전체 카테고리 조회, 더미 데이터 같은 조건으로 정렬
        // 더미 데이터와 카테고리 데이터의 각 dto 내용 비교

        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        for (int i=0; i<cnt; i++) {
            var updatedDto = createUpdatedCategoryDto(fixture.get(i).getCate_code(), i);
            updatedFixture.add(updatedDto);
            assertTrue(1 == target.update(updatedDto));
        }

        var actual = target.selectAll();
        updatedFixture.sort((a, b) -> a.getCate_code().compareTo(b.getCate_code()));
        actual.sort((a, b) -> a.getCate_code().compareTo(b.getCate_code()));

        for (int i=0; i<cnt; i++) {
            assertTrue(isSameCategoryDtoForUpdate(updatedFixture.get(i), actual.get(i)));
        }
    }

    @DisplayName("3-2. 여러개의 카테고리에서 특정 카테고리만 수정 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test16(int cnt) {
        // cnt 만큼 카테고리 dto 생성(더미 데이터 생성)
        // 해당 더미 데이터 등록
        // 랜덤으로 특정 dto 선택
        // 해당 dto만 수정 시도
            // 적용된 로우수 1
            // 내용비교

        // 다른 dto들은 그대로인지 확인

        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        int randomIdx = (int)(Math.random() * cnt);
        var selectedDto = fixture.get(randomIdx);
        var updatedDto = createUpdatedCategoryDto(selectedDto.getCate_code(), randomIdx);

        assertTrue(1 == target.update(updatedDto));
        var actualDto = target.selectByCode(updatedDto.getCate_code());
        System.out.println("updatedDto = " + updatedDto);
        System.out.println("actualDto = " + actualDto);
        assertTrue(isSameCategoryDtoForUpdate(updatedDto, actualDto));

        for (int i=0; i<cnt; i++) {
            if (i == randomIdx) continue;

            var notUpdatedDto = target.selectByCode(fixture.get(i).getCate_code());
            assertFalse(isSameCategoryDto(updatedDto, notUpdatedDto));
        }
    }

    // E. D : 카테고리를 삭제
        // E-1. 단건 삭제
            // 1. 카테고리 삭제 과정에서 예외 발생 - x
            // 2. 카테고리 삭제 실패
                // 2-1. 존재하지 않는 카테고리 삭제

            // 3. 카테고리 삭제 성공
                // 3-1. 카테고리 삭제 여러번 시도해보기
                // 3-2. 여러개의 카테고리에서 특정 카테고리만 삭제 시도해보기

        // E-2. 전체 삭제
            // 1. 카테고리 전체 삭제 과정에서 예외 발생 - x
            // 2. 카테고리 전체 삭제 실패 - x
            // 3. 카테고리 전체 삭제 성공
                // 3-1. 카테고리 전체 삭제 시도해보기

    @DisplayName("E-1, 2-1. 존재하지 않는 카테고리 삭제")
    @Test
    public void test17() {
        var dto = createCategoryDto(0);
        assertTrue(0 == target.deleteByCode(dto.getCate_code()));
    }

    @DisplayName("E-1, 3-1. 카테고리 삭제 여러번 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test18(int cnt) {
        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        for (var dto : fixture) {
            assertTrue(1 == target.deleteByCode(dto.getCate_code()));
        }

        assertTrue(0 == target.count());
    }

    @DisplayName("E-1, 3-2. 여러개의 카테고리에서 특정 카테고리만 삭제 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test19(int cnt) {
        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        int randomIdx = (int)(Math.random() * cnt);
        var selectedDto = fixture.get(randomIdx);

        assertTrue(1 == target.deleteByCode(selectedDto.getCate_code()));
        assertNull(target.selectByCode(selectedDto.getCate_code()));
    }

    @DisplayName("E-2, 3-1. 카테고리 전체 삭제 시도해보기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    public void test20(int cnt) {
        createCategoryFixture(cnt);
        for (var dto : fixture) {
            assertTrue(1 == target.insert(dto));
        }

        assertTrue(cnt == target.count());
        assertTrue(cnt == target.deleteAll());
        assertTrue(0 == target.count());
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

    private CategoryDto createUpdatedCategoryDto(String cate_code, int i) {
        var dto = new CategoryDto();

        dto.setCate_code(cate_code);
        dto.setTop_cate("updated_top_cate" + i);
        dto.setName("updated_name" + i);
        dto.setComt("updated_comt" + i);
        dto.setReg_date("updated_reg_date" + i);
        dto.setReg_id("updated_reg_id" + i);
        dto.setUp_date("updated_up_date" + i);
        dto.setUp_id("updated_up_id" + i);

        return dto;
    }

    private void createCategoryFixture(int cnt) {
        for (int i=0; i<cnt; i++) {
            var dto = createCategoryDto(i);
            fixture.add(dto);
        }
    }

    private boolean isSameCategoryDto(CategoryDto dto1, CategoryDto dto2) {
        return dto1.getCate_code().equals(dto2.getCate_code())
            && dto1.getTop_cate().equals(dto2.getTop_cate())
            && dto1.getName().equals(dto2.getName())
            && dto1.getComt().equals(dto2.getComt())
            && dto1.getReg_id().equals(dto2.getReg_id())
            && dto1.getUp_id().equals(dto2.getUp_id());
    }

    private boolean isSameCategoryDtoForUpdate(CategoryDto dto1, CategoryDto dto2) {
        return dto1.getCate_code().equals(dto2.getCate_code())
            && dto1.getTop_cate().equals(dto2.getTop_cate())
            && dto1.getName().equals(dto2.getName())
            && dto1.getComt().equals(dto2.getComt())
            && dto1.getUp_id().equals(dto2.getUp_id());
    }
}