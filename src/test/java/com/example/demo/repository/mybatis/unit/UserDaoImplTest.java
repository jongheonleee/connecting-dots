//package com.example.demo.repository.mybatis.unit;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.example.demo.dto.ord_user.UserDto;
//import com.example.demo.repository.user.UserDaoImpl;
//import com.example.demo.dto.ord_user.UserFormDto;
//import com.example.demo.dto.ord_user.UserUpdatedFormDto;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.dao.DuplicateKeyException;
//
///**
// * 1차 기능 구현 목록
// *
// * - A. 회원 수 카운트 ✅
// * - B. 회원 등록 ✅
// * - C. 회원 조회 ✅
// *  - C-1. 단건 조회 ✅
// *  - C-2. 전체 조회 ✅
// * - D. 회원 수정 ✅
// * - E. 회원 삭제 ✅
// *  - E-1. 단건 삭제 ✅
// *  - E-2. 전체 삭제 ✅
// *
// */
//
//
//@SpringBootTest
//class UserDaoImplTest {
//
//    @Autowired
//    private UserDaoImpl target;
//
//    @BeforeEach
//    public void setUp() {
//        assertNotNull(target);
//        target.deleteAll();
//    }
//
//    // B. 회원 등록
//        // 1. 회원 등록 과정에서 예외 발생
//            // 1-1. 필수값 누락
//            // 1-2. 중복된 아이디 등록
//            // 1-3. 제약 조건 위배(데이터 길이 제한 초과)
//            // 1-4. 회원 데이터 폼이 null인 경우
//
//        // 2. 회원 등록 실패 - x
//        // 3. 회원 등록 처리
//            // 3-1. 회원 등록 여러번 시도해보기
//
//    @DisplayName("1-1. 필수값 누락")
//    @Test
//    public void test1() {
//        var dto = createDto();
//        dto.setId(null);
//        assertThrows(DataIntegrityViolationException.class, () -> target.insert(dto));
//    }
//
//    @DisplayName("1-2. 중복된 아이디 등록")
//    @Test
//    public void test2() {
//        var dto = createDto();
//        target.insert(dto);
//        assertThrows(DuplicateKeyException.class, () -> target.insert(dto));
//    }
//
//    @DisplayName("1-3. 제약 조건 위배(데이터 길이 제한 초과)")
//    @Test
//    public void test3() {
//        var dto = createDto();
//        dto.setName("Over fifty characters are required to store this data without truncation issues.");
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.insert(dto)
//        );
//    }
//
//    @DisplayName("1-4. 회원 데이터 폼이 null인 경우")
//    @Test
//    public void test14() {
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.insert(null)
//        );
//    }
//
//    @DisplayName("3-1. 회원 등록 여러번 시도해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 10, 15, 20})
//    public void test4(int cnt) {
//        List<UserFormDto> dummy = createDto(cnt);
//        for (UserFormDto dto : dummy) {
//            assertTrue(1 == target.insert(dto));
//        }
//
//        // 조회 했을 때 계수가 cnt인지
//        assertTrue(cnt == target.count());
//
//        // 내용 확인
//        // 비교 대상인 리스트 둘을 같은 조건으로 정렬해주기
//        List<UserDto> foundUsers = target.selectAll();
//        dummy.sort((a, b) -> a.getName().compareTo(b.getName()));
//        foundUsers.sort((a, b) -> a.getName().compareTo(b.getName()));
//
//        // 순회하면서 일일이 내용비교하기
//        for (int i=0; i<dummy.size(); i++) {
//            var expectedUserForm = dummy.get(i);
//            var foundUser = foundUsers.get(i);
//            assertTrue(isSameUser(expectedUserForm, foundUser));
//        }
//    }
//
//
//
//    // C. 회원 단건 조회
//        // 1. 회원 단건 조회시 예외 발생하는 경우 - x
//        // 2. 회원 조회시 단건 조회 실패 - x
//        // 3. 회원 단건 조회 처리
//            // 3-1. 회원 단건 조회 여러번 시도해보기
//
//
//    @DisplayName("3-1. 회원 단건 조회 랜덤으로 시도해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 10, 15, 20})
//    public void test5(int cnt) {
//        List<UserFormDto> dummy = createDto(cnt);
//        for (UserFormDto dto : dummy) {
//            assertTrue(1 == target.insert(dto));
//        }
//
//        // 랜덤 인덱스 생성 및 해당 유저 아이디 생성
//        int randomIdx = (int) (Math.random() * cnt);
//        String userId = "testId" + randomIdx;
//
//        // 내용 비교
//        var expectedUserForm = dummy.get(randomIdx);
//        var foundUser = target.selectById(userId);
//        assertTrue(isSameUser(expectedUserForm, foundUser));
//    }
//
//    // C. 회원 전체 조회
//        // 1. 회원 전체 조회시 예외 발생하는 경우 - x
//        // 2. 회원 전체 조회시 실패 - x
//        // 3. 회원 전체 조회 처리
//            // 3-1. 회원 전체 조회 여러번 시도해보기
//
//    @DisplayName("3-1. 회원 전체 조회 여러번 시도해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 10, 15, 20})
//    public void test6(int cnt) {
//        List<UserFormDto> dummy = createDto(cnt);
//        for (UserFormDto dto : dummy) {
//            assertTrue(1 == target.insert(dto));
//        }
//
//        List<UserDto> foundUsers = target.selectAll();
//
//        // 순서 보장을 위한 같은 조건의 정렬 처리
//        dummy.sort((a, b) -> a.getName().compareTo(b.getName()));
//        foundUsers.sort((a, b) -> a.getName().compareTo(b.getName()));
//
//        // 순회하면서 일일이 내용비교하기
//        for (int i=0; i<dummy.size(); i++) {
//            var expectedUserForm = dummy.get(i);
//            var foundUser = foundUsers.get(i);
//            assertTrue(isSameUser(expectedUserForm, foundUser));
//        }
//
//
//    }
//    // D. 회원 수정
//        // 1. 회원 수정시 예외 발생하는 경우
//            // 1-1. 수정된 필드의 값이 null인 경우
//            // 1-2. 아이디를 수정했는데 해당 아이디가 이미 등록된 아이디일 경우
//            // 1-3. 수정된 필드가 제약 조건 위배시(데이터 길이 초과)
//
//        // 2. 회원 수정 실패 - x
//
//        // 3. 회원 수정 처리
//            // 3-1. 회원 수정 여러번 시도해보면서 내용 비교하기
//    @DisplayName("1-1. 수정된 필드의 값이 null인 경우 - DataIntegrityViolationException")
//    @Test
//    public void test7() {
//        // 등록
//        // 수정 데이터 생성(필수값에 null)
//        // 수정 처리
//        // 예외 발생 확인
//        var dto = createDto();
//        assertTrue(1 == target.insert(dto));
//
//        var updateDto = createUpdateDto(dto.getId());
//        updateDto.setId(null);
//
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.update(updateDto));
//    }
//
//    @DisplayName("1-2. 아이디를 수정했는데 해당 아이디가 이미 등록된 아이디일 경우 - DuplicateKeyException")
//    @Test
//    public void test8() {
//        // 등록
//        // 수정 데이터 생성(기존의 등록된 아이디)
//        // 수정 처리
//        // 예외 발생 확인
//        var dto = createDto();
//        assertTrue(1 == target.insert(dto));
//
//        dto.setId("newId");
//        assertTrue(1 == target.insert(dto));
//
//        var updateDto = createUpdateDto(dto.getId());
//        assertThrows(DuplicateKeyException.class,
//                () -> target.update(updateDto));
//    }
//
//    @DisplayName("1-3. 수정된 필드가 제약 조건 위배시(데이터 길이 초과) - DataIntegrityViolationException")
//    @Test
//    public void test9() {
//        // 등록
//        // 수정 데이터 생성(이름이 varchar(50)을 넘어가는 경우)
//        // 수정 처리
//        // 예외 발생 확인
//        var dto = createDto();
//        assertTrue(1 == target.insert(dto));
//
//        var updateDto = createUpdateDto(dto.getId());
//        updateDto.setName("Over fifty characters are required to store this data without truncation issues.");
//
//        assertThrows(DataIntegrityViolationException.class,
//                () -> target.update(updateDto));
//    }
//
//    @DisplayName("3-1. 회원 수정 여러번 시도해보면서 내용 비교하기 ")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 10, 15, 20})
//    public void test10(int cnt) {
//        // 회원 더미 생성
//        // 더미 데이터 등록
//        // 수정 데이터 생성
//        // 수정 처리
//        // 적용된 로우수가 cnt와 동일한지 비교
//        // 내용 비교
//        List<UserFormDto> dummy = createDto(cnt);
//        for (UserFormDto dto : dummy) {
//            assertTrue(1 == target.insert(dto));
//        }
//
//        List<UserUpdatedFormDto> updatedDummy = new ArrayList<>();
//        for (int i=0; i<dummy.size(); i++) {
//            var dto = dummy.get(i);
//            var updateDto = createUpdateDto(dto.getId(), i);
//            updatedDummy.add(updateDto);
//            assertTrue(1 == target.update(updateDto));
//        }
//
//        List<UserDto> foundUsers = target.selectAll();
//
//        // 순서 보장을 위한 같은 조건의 정렬 처리
//        updatedDummy.sort((a, b) -> a.getName().compareTo(b.getName()));
//        foundUsers.sort((a, b) -> a.getName().compareTo(b.getName()));
//
//        for (int i=0; i<dummy.size(); i++) {
//            var expectedUserForm = updatedDummy.get(i);
//            var foundUser = foundUsers.get(i);
//            assertTrue(isSameUser(expectedUserForm, foundUser));
//        }
//    }
//
//
//    // E. 회원 단건 삭제
//        // 1. 회원 단건 삭제시 예외 발생하는 경우 - x
//        // 2. 회원 단건 삭제시 실패
//            // 2-1. 없는 아이디로 삭제하는 경우 삭제 실패
//        // 3. 회원 단건 삭제 처리
//            // 3-1. 여러 회원을 등록하고 특정 회원만 삭제해보기
//    @DisplayName("2-1. 없는 아이디로 삭제하는 경우 삭제 실패")
//    @Test
//    public void test11() {
//        String notExistId = "notExistId";
//        target.deleteById(notExistId);
//    }
//
//    @DisplayName("3-1. 여러 회원을 등록하고 특정 회원만 삭제해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 10, 15, 20})
//    public void test12(int cnt) {
//        // 더미 데이터 생성
//        // 등록
//        // 특정 회원 선택
//        // 해당 회원 삭제
//        // 전체 회원 수 확인
//        // 삭제한 회원 조회시 null인지 확인
//        List<UserFormDto> dummy = createDto(cnt);
//        for (UserFormDto dto : dummy) {
//            assertTrue(1 == target.insert(dto));
//        }
//
//        int randomIdx = (int) (Math.random() * cnt);
//        UserFormDto selectedTarget = dummy.get(randomIdx);
//
//        assertTrue(1 == target.deleteById(selectedTarget.getId()));
//        assertTrue(cnt - 1 == target.count());
//        assertNull(target.selectById(selectedTarget.getId()));
//    }
//
//    // E. 회원 전체 삭제
//        // 1. 회원 전체 삭제시 예외 발생하는 경우 - x
//        // 2. 회원 전체 삭제시 실패 - x
//        // 3. 회원 전체 삭제 처리
//            // 3-1. 여러 회원을 등록하고 전체 회원 삭제해보기
//    @DisplayName("3-1. 여러 회원을 등록하고 전체 회원 삭제해보기")
//    @ParameterizedTest
//    @ValueSource(ints = {1, 10, 15, 20})
//    public void test13(int cnt) {
//        // 더미 데이터 생성
//        // 등록
//        // 전체 삭제
//        // 적용된 로우수가 cnt인지 확인
//        // 카운트시 0인지 확인
//        List<UserFormDto> dummy = createDto(cnt);
//        for (UserFormDto dto : dummy) {
//            assertTrue(1 == target.insert(dto));
//        }
//
//        assertTrue(cnt == target.deleteAll());
//        assertTrue(0 == target.count());
//    }
//
//    private UserFormDto createDto() {
//        UserFormDto dto = new UserFormDto();
//        dto.setName("홍길동");
//        dto.setEmail("test@gmail.com");
//        dto.setId("testId");
//        dto.setPwd("1234");
//        dto.setSns("naver,facebook");
//        return dto;
//    }
//
//    private List<UserFormDto> createDto(int cnt) {
//        List<UserFormDto> list = new ArrayList<>();
//        for (int i = 0; i < cnt; i++) {
//            UserFormDto dto = new UserFormDto();
//            dto.setName("홍길동" + i);
//            dto.setEmail("test" + i + "@gmail.com");
//            dto.setId("testId" + i);
//            dto.setPwd("1234");
//            dto.setSns("naver,facebook");
//            list.add(dto);
//        }
//        return list;
//    }
//    private UserUpdatedFormDto createUpdateDto(String originalId) {
//        UserUpdatedFormDto dto = new UserUpdatedFormDto();
//        dto.setOriginId(originalId);
//        dto.setName("홍길동");
//        dto.setEmail("test@gmail.com");
//        dto.setId("testId");
//        dto.setPwd("1234");
//        dto.setSns("naver,facebook");
//        return dto;
//    }
//
//    private UserUpdatedFormDto createUpdateDto(String originalId, int i) {
//        UserUpdatedFormDto dto = new UserUpdatedFormDto();
//        dto.setOriginId(originalId);
//        dto.setName("홍길동" + i);
//        dto.setEmail("test@gmail.com");
//        dto.setId("testId" + i);
//        dto.setPwd("1234" + i);
//        dto.setSns("naver,facebook");
//        return dto;
//    }
//
//    private boolean isSameUser(UserFormDto expected, UserDto found) {
//        return expected.getName().equals(found.getName())
//                && expected.getEmail().equals(found.getEmail())
//                && expected.getId().equals(found.getId())
//                && expected.getPwd().equals(found.getPwd())
//                && expected.getSns().equals(found.getSns());
//    }
//
//    private boolean isSameUser(UserUpdatedFormDto expected, UserDto found) {
//        return expected.getName().equals(found.getName())
//                && expected.getEmail().equals(found.getEmail())
//                && expected.getId().equals(found.getId())
//                && expected.getPwd().equals(found.getPwd())
//                && expected.getSns().equals(found.getSns());
//    }
//}