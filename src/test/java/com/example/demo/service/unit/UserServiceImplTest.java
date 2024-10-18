package com.example.demo.service.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.repository.mybatis.user.UserDaoImpl;
import com.example.demo.dto.user.User;
import com.example.demo.dto.user.UserFormDto;
import com.example.demo.dto.user.UserUpdatedFormDto;
import com.example.demo.application.exception.user.UserAlreadyExistsException;
import com.example.demo.application.exception.user.UserFormInvalidException;
import com.example.demo.application.exception.user.UserNotFoundException;
import com.example.demo.application.service.user.UserServiceImpl;
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


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDaoImpl userDao;

    @InjectMocks
    private UserServiceImpl target;

    private List<User> userDtoFixture = new ArrayList<>();
    private List<UserFormDto> userFormDtoFixture = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        userFormDtoFixture.clear();
        userDtoFixture.clear();
        assertNotNull(userDao);
        assertNotNull(target);
    }


    // A. 회원 수 카운트
        // 1. 회원 카운팅시 예외 발생 - x
        // 2. 회원 카운팅 실패 - x
        // 3. 회원 카운팅 성공
            // 3-1. 등록한 회원 수 만큼 카운팅 처리되는지 확인

    @DisplayName("3-1. 등록한 회원 수 만큼 카운팅 처리되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void test1(int cnt) {
        // dao로 카운트 호출하면 cnt만큼 나오게 설정
        // service에서 카운트 호출하면 cnt만큼 나옴
        when(userDao.count()).thenReturn(cnt);
        int actualCnt = target.count();
        assertTrue(actualCnt == cnt);
    }


    // B. 회원 등록
        // 1. 회원 등록시 예외 발생
            // 1-1. 데이터 폼이 null인 경우
            // 1-2. 데이터 폼 중 필수값이 null인 경우
            // 1-3. 데이터의 제약 조건을 위배한 경우 (길이, 형식 등)
            // 1-4. 이미 등록된 아이디로 회원을 등록할 경우

        // 2. 회원 등록 실패

        // 3. 회원 등록 성공
            // 3-1. 회원 여러번 등록시 입력한 데이터와 동일한 데이터가 등록되는지 확인
    @DisplayName("1-1. 데이터 폼이 null인 경우")
    @Test
    public void test2() {
        // 데이터 폼 dto null로 설정
        // dao에서 해당 dto로 insert 호출시 예외 발생 - DataIntegrityViolationException
        // service에서 해당 dto로 insert 호출시 예외 발생 - DataIntegrityViolationException
        when(userDao.insert(null)).thenThrow(new DataIntegrityViolationException(""));
        assertThrows(UserFormInvalidException.class,
                () -> target.create(null)); // 추후에 사용자 정의 예외로 변환해주기
    }

    @DisplayName("1-2. 데이터 폼 중 필수값이 null인 경우")
    @Test
    public void test3() {
        // 필수값이 null인 데이터 폼 dto 설정(id)
        // dao에서 해당 dto로 insert 호출시 예외 발생 - DataIntegrityViolationException
        // service에서 해당 dto로 insert 호출시 예외 발생 - DataIntegrityViolationException
        UserFormDto wrongUserFormDto = createFormDto(0);
        wrongUserFormDto.setId(null);
        when(userDao.insert(wrongUserFormDto)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(UserFormInvalidException.class,
                () -> target.create(wrongUserFormDto)); // 추후에 사용자 정의 예외로 변환해주기
    }

    @DisplayName("1-3. 데이터의 제약 조건을 위배한 경우 (길이, 형식 등)")
    @Test
    public void test4() {
        // 이름의 길이가 varchar(50)을 넘어가는 데이터 폼 dto 설정
        // dao에서 해당 dto로 insert 호출시 예외 발생 - DataIntegrityViolationException
        // service에서 해당 dto로 insert 호출시 예외 발생 - DataIntegrityViolationException
        UserFormDto wrongUserFormDto = createFormDto(0);
        wrongUserFormDto.setName("Over fifty characters are required to store this data without truncation issues.");
        when(userDao.insert(wrongUserFormDto)).thenThrow(new DataIntegrityViolationException(""));
        assertThrows(UserFormInvalidException.class,
                () -> target.create(wrongUserFormDto)); // 추후에 사용자 정의 예외로 변환해주기
    }

    @DisplayName("1-4. 이미 등록된 아이디로 회원을 등록할 경우")
    @Test
    public void test5() {
        // 이름의 길이가 varchar(50)을 넘어가는 데이터 폼 dto 설정
        // dao에서 해당 dto로 insert 호출시 예외 발생 - DataIntegrityViolationException
        // service에서 해당 dto로 insert 호출시 예외 발생 - DataIntegrityViolationException
        UserFormDto duplicatedIdUserFormDto = createFormDto(0);
        when(userDao.insert(duplicatedIdUserFormDto)).thenThrow(new DuplicateKeyException(""));
        assertThrows(UserAlreadyExistsException.class,
                () -> target.create(duplicatedIdUserFormDto)); // 추후에 사용자 정의 예외로 변환해주기
    }

    @DisplayName("3-1. 회원 여러번 등록시 입력한 데이터와 동일한 데이터가 등록되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void test6(int cnt) {
        createFixture(cnt);
        for (var userFormDto : userFormDtoFixture) {
            when(userDao.insert(userFormDto)).thenReturn(1);
            target.create(userFormDto);
        }
    }


    // C. 회원 단건 조회
        // 1. 회원 단건 조회시 예외 발생 - x
        // 2. 회원 단건 조회 실패
            // 2-1. 없는 아이디로 조회시 null 반환. 추후에 사용자 정의 예외로 변환해주기

        // 3. 회원 단건 조회 성공
            // 3-1. 등록한 회원 중 하나를 조회했을 때, 조회한 데이터와 동일한 데이터가 반환되는지 확인
    @DisplayName("2-1. 없는 아이디로 조회시 null 반환. 추후에 사용자 정의 예외로 변환해주기")
    @Test
    public void test7() {
        // 없는 아이디로 조회시 null 반환
        // dao에서 해당 아이디로 selectById 호출시 null 반환
        // service에서 해당 아이디로 selectById 호출시 null 반환
        when(userDao.selectById("nonexistentId")).thenReturn(null);
        assertThrows(UserNotFoundException.class,
                () -> target.findById("nonexistentId"));
    }

    @DisplayName("3-1. 등록한 회원 중 하나를 조회했을 때, 조회한 데이터와 동일한 데이터가 반환되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void test8(int cnt) {
        createFixture(cnt);
        for (var userDto : userDtoFixture) {
            when(userDao.selectById(userDto.getId())).thenReturn(userDto);
            var actualUserDto = target.findById(userDto.getId());
            assertEquals(userDto, actualUserDto);
        }
    }

    // C. 회원 전체 조회
        // 1. 회원 전체 조회시 예외 발생 - x
        // 2. 회원 전체 조회 실패 - x
        // 3. 회원 전체 조회 성공
            // 3-1. 등록한 회원 전체를 조회했을 때, 조회한 데이터와 동일한 데이터가 반환되는지 확인
    @DisplayName("3-1. 등록한 회원 전체를 조회했을 때, 조회한 데이터와 동일한 데이터가 반환되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void test9(int cnt) {
        createFixture(cnt);
        when(userDao.selectAll()).thenReturn(userDtoFixture);
        var actualUserDtoList = target.findAll();

        userDtoFixture.sort((a, b) -> a.getName().compareTo(b.getName()));
        actualUserDtoList.sort((a, b) -> a.getName().compareTo(b.getName()));

        for (int i=0; i<userDtoFixture.size(); i++) {
            assertTrue(isSameUserDto(userDtoFixture.get(i), actualUserDtoList.get(i)));
        }
    }


    // D. 회원 수정
        // 1. 회원 수정시 예외 발생
            // 1-1. 변경할 데이터 폼이 null인 경우
            // 1-2. originId가 null인 경우
            // 1-3. 변경할 데이터 폼 중 필수값이 null인 경우
            // 1-4. 변경할 데이터의 제약 조건을 위배한 경우 (길이, 형식 등)
            // 1-5. 아이디 변경시 이미 등록된 아이디로 변경할 경우

        // 2. 회원 수정 실패 - x

        // 3. 회원 수정 성공
            // 3-1. 등록한 회원 중 하나를 수정했을 때, 수정한 데이터가 조회되는지 확인

    @DisplayName("1-1. 데이터 폼이 null인 경우")
    @Test
    public void test10() {
        // 변경 데이터 폼이 null인 경우, dao에선 DataIntegrityViolationException 발생
        // service에서 해당 dto로 update 호출시 예외 발생 - UserFormInvalidException
        when(userDao.update(null)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(UserFormInvalidException.class,
                () -> target.modify(null));
    }

    @DisplayName("1-2. originId가 null인 경우")
    @Test
    public void test11() {
        var updatedFormDto = createUpdatedFormDto(null, 0);
        when(userDao.update(updatedFormDto)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(UserFormInvalidException.class,
                () -> target.modify(updatedFormDto));
    }

    @DisplayName("1-3. 변경할 데이터 폼 중 필수값이 null인 경우")
    @Test
    public void test12() {
        var updatedFormDto = createUpdatedFormDto("originId", 0);
        updatedFormDto.setId(null);
        when(userDao.update(updatedFormDto)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(UserFormInvalidException.class,
                () -> target.modify(updatedFormDto));
    }

    @DisplayName("1-4. 변경할 데이터의 제약 조건을 위배한 경우 (길이, 형식 등)")
    @Test
    public void test13() {
        var updatedFormDto = createUpdatedFormDto("originId", 0);
        updatedFormDto.setName("Over fifty characters are required to store this data without truncation issues.");
        when(userDao.update(updatedFormDto)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(UserFormInvalidException.class,
                () -> target.modify(updatedFormDto));
    }

    @DisplayName("1-5. 아이디 변경시 이미 등록된 아이디로 변경할 경우")
    @Test
    public void test14() {
        var updatedFormDto = createUpdatedFormDto("originId", 0);
        updatedFormDto.setId("duplicatedId");
        when(userDao.update(updatedFormDto)).thenThrow(DuplicateKeyException.class);
        assertThrows(UserAlreadyExistsException.class,
                () -> target.modify(updatedFormDto));
    }

    @DisplayName("3-1. 등록한 회원 중 하나를 수정했을 때, 수정한 데이터가 조회되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void test15(int cnt) {
        createFixture(cnt);

        int randomIdx = (int)(Math.random() * cnt);
        var userDto = userDtoFixture.get(randomIdx);
        var updatedFormDto = createUpdatedFormDto(userDto.getId(), randomIdx);

        when(userDao.update(updatedFormDto)).thenReturn(1);

        assertDoesNotThrow(() -> target.modify(updatedFormDto));

    }


    // E. 회원 단건 삭제
        // 1. 회원 단건 삭제시 예외 발생
            // 1-1. 없는 아이디로 삭제시 UserNotFoundException 발생

        // 2. 회원 단건 삭제 실패 - x
        // 3. 회원 단건 삭제 성공
            // 3-1. 등록한 회원 중 하나를 삭제했을 때, 삭제한 데이터가 조회되지 않는지 확인

    @DisplayName("1-1. 없는 아이디로 삭제시 UserNotFoundException 발생")
    @Test
    public void test16() {
        // 없는 아이디로 삭제시 UserNotFoundException 발생
        // dao에서 해당 아이디로 deleteById 호출시 0 반환
        // service에서 해당 아이디로 deleteById 호출시 UserNotFoundException 발생
        when(userDao.selectById("nonexistentId")).thenReturn(null);
        assertThrows(UserNotFoundException.class,
                () -> target.remove("nonexistentId"));
    }

    @DisplayName("3-1. 등록한 회원 중 하나를 삭제했을 때, 삭제한 데이터가 조회되지 않는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void test17(int cnt) {
        createFixture(cnt);

        int randomIdx = (int)(Math.random() * cnt);
        var userDto = userDtoFixture.get(randomIdx);

        when(userDao.selectById(userDto.getId())).thenReturn(userDto);
        when(userDao.deleteById(userDto.getId())).thenReturn(1);

        assertDoesNotThrow(() -> target.remove(userDto.getId()));
    }

    // E. 회원 전체 삭제
        // 1. 회원 전체 삭제시 예외 발생 - x
        // 2. 회원 전체 삭제 실패 - x
        // 3. 회원 전체 삭제 성공
            // 3-1. 회원 전체 삭제 후 회원 수가 0인지 확인

    @DisplayName("3-1. 회원 전체 삭제 후 회원 수가 0인지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 15, 20})
    public void test18(int cnt) {
        createFixture(cnt);
        when(userDao.deleteAll()).thenReturn(cnt);
        assertDoesNotThrow(() -> target.removeAll());
    }

    private void createFixture(int cnt) {
        for (int i=0; i<cnt; i++) {
            var dto = new User();
            dto.setId("id" + cnt);
            dto.setName("name" + cnt);
            dto.setEmail("email" + cnt);
            dto.setPwd("pwd" + cnt);
            dto.setBirth("birth" + cnt);
            dto.setSns("sns" + cnt);
            userDtoFixture.add(dto);

            var formDto = new UserFormDto();
            formDto.setId("id" + cnt);
            formDto.setName("name" + cnt);
            formDto.setEmail("email" + cnt);
            formDto.setPwd("pwd" + cnt);
            formDto.setBirth("birth" + cnt);
            formDto.setSns("sns" + cnt);
            userFormDtoFixture.add(formDto);
        }
    }

    private UserFormDto createFormDto(int cnt) {
        var dto = new UserFormDto();
        dto.setId("id" + cnt);
        dto.setName("name" + cnt);
        dto.setEmail("email" + cnt);
        dto.setPwd("pwd" + cnt);
        dto.setBirth("birth" + cnt);
        dto.setSns("sns" + cnt);

        return dto;
    }

    private UserUpdatedFormDto createUpdatedFormDto(String originId, int cnt) {
        var dto = new UserUpdatedFormDto();
        dto.setOriginId("originId" + cnt);
        dto.setId("id" + cnt);
        dto.setName("name" + cnt);
        dto.setEmail("email" + cnt);
        dto.setPwd("pwd" + cnt);
        dto.setBirth("birth" + cnt);
        dto.setSns("sns" + cnt);
        return dto;
    }

    private boolean isSameUserDto(User a, User b) {
        return a.getId().equals(b.getId()) &&
                a.getName().equals(b.getName()) &&
                a.getEmail().equals(b.getEmail()) &&
                a.getPwd().equals(b.getPwd()) &&
                a.getBirth().equals(b.getBirth()) &&
                a.getSns().equals(b.getSns());

    }

}