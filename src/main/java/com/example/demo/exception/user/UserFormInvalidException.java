package com.example.demo.exception.user;

public class UserFormInvalidException extends RuntimeException {

    public UserFormInvalidException() {
        this("입력하신 회원 정보가 애플리케이션의 정책과 규칙에 부합하지 않습니다. 회원 정보를 재검토해주세요.");
    }

    public UserFormInvalidException(String message) {
        super(message);
    }

}
