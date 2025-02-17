package com.example.demo.repository.user.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.user.UserDto;
import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserDaoImplTest {

    @Autowired
    private UserDaoImpl sut;

    @BeforeEach
    void setUp() {
        assertNotNull(sut);
        sut.deleteAll();
        assertEquals(0, sut.count());
    }

    @AfterEach
    void clean() {
        sut.deleteAll();
        assertEquals(0, sut.count());
    }


    @Nested
    @DisplayName("카운트 및 존재 여부 확인 관련 테스트")
    class sut_count_exists_test {

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        @DisplayName("사용자를 여러명 등록하고 카운팅하여 확인한다.")
        void it_correctly_work_when_count(int cnt) {
            for (int i=0; i<cnt; i++) {
                var user = createUserDto(i);
                assertEquals(1, sut.insert(user));
            }

            assertEquals(cnt, sut.count());
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        @DisplayName("사용자를 여러명 등록하고 각각 아이디로 존재하는지 확인 여부를 확인한다.")
        void it_correctly_work_when_existsByUserId(int cnt) {
            List<UserDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var user = createUserDto(i);
                assertEquals(1, sut.insert(user));
                dummy.add(user);
            }

            for (UserDto dto : dummy) {
                assertTrue(sut.existsByUserId(dto.getId()));
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        @DisplayName("사용자를 여러명 등록하고 각각 아이디로 존재하는지 확인 여부를 select for update 로 확인한다.")
        void it_correctly_work_when_existsByUserIdForUpdate(int cnt) {
            List<UserDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var user = createUserDto(i);
                assertEquals(1, sut.insert(user));
                dummy.add(user);
            }

            for (UserDto dto : dummy) {
                assertTrue(sut.existsByUserIdForUpdate(dto.getId()));
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        @DisplayName("사용자를 여러명 등록하고 각각 이메일로 존재하는지 확인 여부를 확인한다.")
        void it_correctly_work_when_existsByEmail(int cnt) {
            List<UserDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var user = createUserDto(i);
                assertEquals(1, sut.insert(user));
                dummy.add(user);
            }

            for (UserDto dto : dummy) {
                assertTrue(sut.existsByEmail(dto.getEmail()));
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        @DisplayName("사용자를 여러명 등록하고 각각 사용자 시퀀스로 존재하는지 확인 여부를 확인한다.")
        void it_correctly_work_when_existsByUserSeq(int cnt) {
            List<UserDto> dummy = new ArrayList<>();
            for (int i=0; i<cnt; i++) {
                var user = createUserDto(i);
                assertEquals(1, sut.insert(user));
                dummy.add(user);
            }

            for (UserDto dto : dummy) {
                assertTrue(sut.existsByUserSeq(dto.getUser_seq()));
            }
        }
    }

    @Nested
    @DisplayName("사용자 조회 관련 테스트")
    class sut_select_test {

        @DisplayName("등록된 사용자의 아이디로 조회에 성공한다.")
        @Test
        void it_correctly_work_when_selectByUserId() {
            var expected = createUserDto(1);
            assertEquals(1, sut.insert(expected));

            var actual = sut.selectByUserId(expected.getId());
            assertNotNull(actual);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getPwd(), actual.getPwd());
            assertEquals(expected.getEmail(), actual.getEmail());
            assertEquals(expected.getBirth(), actual.getBirth());
            assertEquals(expected.getSns(), actual.getSns());
        }

        @DisplayName("등록되지 않은 사용자의 아이디로 조회할 경우 null이 반환된다.")
        @Test
        void it_return_null_when_selectByUserId() {
            var actual = sut.selectByUserId("not-exist-id");
            assertNull(actual);
        }

        @DisplayName("등록된 사용자의 시퀀스로 조회에 성공한다.")
        @Test
        void it_correctly_work_when_selectByUserSeq() {
            var expected = createUserDto(1);
            assertEquals(1, sut.insert(expected));

            var actual = sut.selectByUserSeq(expected.getUser_seq());
            assertNotNull(actual);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getPwd(), actual.getPwd());
            assertEquals(expected.getEmail(), actual.getEmail());
            assertEquals(expected.getBirth(), actual.getBirth());
            assertEquals(expected.getSns(), actual.getSns());
        }

        @DisplayName("등록되지 않은 사용자의 시퀀스로 조회할 경우 null이 반환된다.")
        @Test
        void it_return_null_when_selectByUserSeq() {
            var actual = sut.selectByUserSeq(9999);
            assertNull(actual);
        }


    }

    @Nested
    @DisplayName("사용자 등록 관련 테스트")
    class sut_insert_test {

        @DisplayName("여러명의 사용자를 등록한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 15, 20})
        void it_correctly_work_when_insert(int cnt) {
            for (int i=0; i<cnt; i++) {
                var user = createUserDto(i);
                assertEquals(1, sut.insert(user));
            }

            assertEquals(cnt, sut.count());
        }
    }

    @Nested
    @DisplayName("사용자 수정 관련 테스트")
    class sut_update_test {

    }

    @Nested
    @DisplayName("사용자 삭제 관련 테스트")
    class sut_delete_test {

    }

    private UserDto createUserDto(int i) {
        return new UserDto(i, "id" + i, "name" + i,
                     "pwd" + i, "email" + i, "birth" + i,
                      "sns" + i, "20250101", i, "20250218", i);
    }
}