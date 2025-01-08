package com.example.demo.domain;

import com.example.demo.dto.code.CodeDto;
import com.example.demo.dto.code.CodeRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Getter;


@Getter
public class Code {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private Integer level;
    private String code;
    private String name;
    private String chkUse;
    private String topCode;

    // 요청 dto -> domain 오브젝트로 변경
    public Code(CodeRequest request) {
        this.level = request.getLevel(); // 0~3까지 범위만 허용
        this.code = request.getCode(); // 4자리 숫자 형태로 구성
        this.name = request.getName(); // 최대 25자까지만 허용
        this.chkUse = request.getChk_use(); // Y, N만 허용
        this.topCode = request.getTop_code(); // 4자리 숫자 형태로 구성
    }


    // dao에 요구되는 데이터 형태로 변환
    public CodeDto toCodeDto() {
        String formatedNow = getCurrentDateFormat();
        Integer regUserSeq = getRegUserSeq();
        return new CodeDto(this, formatedNow, regUserSeq, formatedNow, regUserSeq);
    }

    // 현재 시간 계산 포멧처리
    private String getCurrentDateFormat() {
        LocalDate now = LocalDate.now();
        return now.format(formatter);
    }

    // 관리자 정보 조회
    private Integer getRegUserSeq() {
        return 1;
    }



}
