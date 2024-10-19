package com.example.demo.application.exception.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        this("해당 사용자를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
