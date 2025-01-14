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
    HANDLE_ACCESS_DENIED(403, "EC0006", "해당 리소스에 접근할 권한이 없습니다"),


    // > 비즈니스
    // - 서비스
    SERVICE_USER_GRADE_ALREADY_EXISTS(409, "EC1001", "해당 사용자 등급이 이미 존재합니다"),
    SERVICE_USER_GRADE_NOT_FOUND(404, "EC1002", "해당 사용자 등급을 찾을 수 없습니다"),

    SERVICE_RULE_USE_ALREADY_EXISTS(409, "EC1003", "해당 이용 규칙 존재합니다"),
    SERVICE_RULE_USE_NOT_FOUND(404, "EC1004", "해당 이용 규칙을 찾을 수 없습니다"),

    SERVICE_SANCTION_HISTORY_ALREADY_EXISTS(409, "EC1005", "해당 제재 내역이 이미 존재합니다"),
    SERVICE_SANCTION_HISTORY_NOT_FOUND(404, "EC1005", "해당 제재 내역을 찾을 수 없습니다"),

    SERVICE_TERMS_ALREADY_EXISTS(409, "EC1006", "해당 정책이 이미 존재합니다"),
    SERVICE_TERMS_NOT_FOUND(404, "EC1007", "해당 정책을 찾을 수 없습니다"),

    SERVICE_USER_CONDITION_ALREADY_EXISTS(409, "EC1008", "해당 회원 약관 항목이 이미 존재합니다"),
    SERVICE_USER_CONDITION_NOT_FOUND(404, "EC1009", "해당 회원 약관 항목을 찾을 수 없습니다"),

    SERVICE_USER_CONDITIONS_ALREADY_EXISTS(409, "EC1010", "해당 회원 약관이 이미 존재합니다"),
    SERVICE_USER_CONDITIONS_NOT_FOUND(404, "EC1011", "해당 회원 약관을 찾을 수 없습니다"),

    // 회원

    // 게시판

    // 리포트

    // 댓글, 대댓글

    // 관리자

    // 인공지능

    // > 기술
    // - 네트워크
    RETRY_FAILED(500, "EC2001", "재시도에 실패하였습니다"),

    // - 데이터베이스
    NOT_APPLY_ON_DBMS(500, "EC2001", "해당 기능은 DBMS에 적용되지 않았습니다.");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
