package com.example.demo.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // 공통 에러 코드
    INVALID_INPUT_VALUE(400, "EC0001", "잘못된 입력값입니다"),
    METHOD_NOT_ALLOWED(405, "EC0002", "허용되지 않은 메소드입니다"),
    ENTITY_NOT_FOUND(400, "EC0003", "해당 엔티티를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(500, "EC0004", "서버에 오류가 발생하였습니다"),
    INVALID_TYPE_VALUE(400, "EC0005", "잘못된 타입입니다"),
    HANDLE_ACCESS_DENIED(403, "EC0006", "해당 리소스에 접근할 권한이 없습니다");


    // 서비스

    // 회원

    // 게시판

    // 리포트

    // 댓글, 대댓글

    // 관리자

    // 인공지능

    private final String code;
    private final String message;
    private int status;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
