package com.example.demo.exception.user;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        this("이미 존재하는 사용자입니다.");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
