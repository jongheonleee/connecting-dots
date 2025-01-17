package com.example.demo.domain;

import com.example.demo.dto.code.CodeDto;
import com.example.demo.global.error.exception.business.code.CodeNotFoundException;
import java.util.Arrays;
import lombok.Getter;

// 이렇게 할 경우엔 하드코딩이랑 무슨차이가 있나
// 그리고 RDB에 저장하는 것 자체가 의미 없는 것 같음
// 클래스로 변환해서 도메인으로서 활용하는 게 좋을 것 같음


// 코드는 현재 계층구조로 이루어져 있음

public enum Code {
    // 최상위 코드
    CODE(0, "0000", "코드", null),

    // 회원 관련 코드
    USER(1, "1000", "회원", "0000"),
    USER_CREATE(2, "1001", "신규가입", "1000"),
    USER_MODIFY(2, "1002", "정보수정", "1000"),
    USER_REMOVE_WAIT(2, "1003", "탈퇴대기", "1000"),
    USER_REMOVE(2, "1004", "탈퇴처리","1000"),
    USER_REMOVE_CANCEL(2, "1005", "탈퇴취소", "1000"),
    USER_SANCTION_WAIT(2, "1006", "제재대기", "1000"),
    USER_SANCTION(2, "1007", "제재처리", "1000"),
    USER_SANCTION_CANCEL(2, "1008", "제재취소", "1000"),
    USER_HUMAN(2, "1009", "휴먼상태","1000"),
    USER_HUMAN_CANCEL(2, "1010", "휴먼해제","1000"),
    USER_GRADE_CHANGE(2, "1011", "등급변경","1000"),
    USER_GRADE_BRONZE(2, "1012", "브론즈", "1000"),
    USER_GRADE_SILVER(2, "1013", "실버","1000"),
    USER_GRADE_GOLD(2, "1014", "골드", "1000"),
    USER_GRADE_PLATINUM(2, "1015", "플래티넘", "1000"),
    USER_GRADE_DIAMOND(2, "1016", "다이아몬드","1000"),
    USER_GRADE_MASTER(2, "1017", "마스터", "1000"),

    // 서비스 관련 코드
    SERVICE(1, "2000", "서비스", "0000"),
    SERVICE_SANCTION(2, "2001", "제재 관련 정책/규칙", "2000"),
    SERVICE_GRADE(2, "2002", "회원 등급 관련 정책/규칙", "2000"),
    SERVICE_TERMS(2, "2003", "이용 약관 관련 정책/규칙", "2000"),

    // 게시판 관련 코드
    BOARD(1, "3000", "게시판", "0000"),
    BOARD_CREATE(2, "3001", "생성완료", "3000"),
    BOARD_MODIFY(2, "3002", "수정완료", "3000"),
    BOARD_REPORT(2, "3003", "비정상 신고", "3000"),
    BOARD_CHECKING(2, "3004", "분석중", "3000"),
    BOARD_CHECKED(2, "3005", "분석완료", "3000"),
    BOARD_SANCTION(2, "3006", "제재 대상", "3000"),
    BOARD_NORMAL(2, "3007", "정상", "3000"),
    BOARD_NOT_NORMAL(2, "3008", "비정상", "3000"),
    BOARD_NOT_SHOW(2, "3009", "비공개", "3000"),
    BOARD_REMOVE(2, "3010", "삭제", "3000"),


    // 댓글 및 대댓글 관련 코드
    COMMENT(1, "4000", "댓글 및 대댓글", "0000"),
    COMMENT_CREATE(2, "4001", "생성완료", "4000"),
    COMMENT_MODIFY(2, "4002", "수정완료", "4000"),
    COMMENT_REPORT(2, "4003", "비정상 신고", "4000"),
    COMMENT_CHECKING(2, "4004", "분석중", "4000"),
    COMMENT_CHECKED(2, "4005", "분석완료", "4000"),
    COMMENT_SANCTION(2, "4006", "제재 대상", "4000"),
    COMMENT_NORMAL(2, "4007", "정상", "4000"),
    COMMENT_NOT_NORMAL(2, "4008", "비정상", "4000"),
    COMMENT_REMOVE(2, "4009", "삭제", "4000"),

    // 리포트 관련 코드
    REPORT(2, "5000", "리포트", "0000"),
    REPORT_CREATE(2, "5001", "생성완료", "5000"),
    REPORT_MODIFY(2, "5002", "수정완료", "5000"),
    REPORT_WAIT(2, "5003", "처리 대기중", "5000"),
    REPORT_CHECKING(2, "5004", "처리중", "5000"),
    REPORT_CHECKED(2, "5005", "처리완료", "5000"),
    REPORT_CANCEL(2, "5006", "처리보류", "5000"),

    // 관리자 관련 코드
    MANAGER(2, "6000", "관리자", "0000"),

    // 집계 관련 코드
    AGGREGATE(2, "7000", "집계", "0000"),

    // 인공지능 관련 코드
    AI(2, "8000", "인공지능", "0000");

    public static final Integer MAX_LEVEL = 3;

    private final Integer level;
    private final String code;
    private final String name;
    private final String topCode;

    private Code(Integer level, String code, String name, String topCode) {
        this.level = level;
        this.code = code;
        this.name = name;
        this.topCode = topCode;
    }

    public static Code of(String code) {
        return Arrays.stream(Code.values())
                     .filter(c -> c.getCode().equals(code))
                     .findFirst()
                     .orElseThrow(CodeNotFoundException::new);
    }

    public Integer getLevel() {
        return level;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getTopCode() {
        return topCode;
    }

    public CodeDto toDto(String chk_use, String reg_date, Integer reg_user_seq, String up_date, Integer up_user_seq) {
        return new CodeDto(this, chk_use, reg_date, reg_user_seq, up_date, up_user_seq);
    }
}
