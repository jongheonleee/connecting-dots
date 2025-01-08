package com.example.demo.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.code.CodeDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CodeTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");


    @Test
    @DisplayName("CodeDto 변환 테스트")
    void CodeDto_변환_테스트() {
        // given
        Code code = new Code(1, "101", "테스트용", "Y", "100");

        // when
        CodeDto result = code.toDto();

        // then
        assertEquals(code.getLevel(), result.getLevel());
        assertEquals(code.getCode(), result.getCode());
        assertEquals(code.getName(), result.getName());

        LocalDate now = LocalDate.now();
        assertEquals(now.format(formatter), result.getReg_date());
        assertEquals(now.format(formatter), result.getUp_date());

        assertEquals(1, result.getReg_user_seq());
        assertEquals(1, result.getUp_user_seq());
    }
}