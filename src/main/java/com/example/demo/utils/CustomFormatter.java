package com.example.demo.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class CustomFormatter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // 현재 시간 계산 포멧처리
    public String getCurrentDateFormat() {
        LocalDate now = LocalDate.now();
        return now.format(formatter);
    }

    // 관리자 정보 조회
    public Integer getManagerSeq() {
        return 1;
    }
}
