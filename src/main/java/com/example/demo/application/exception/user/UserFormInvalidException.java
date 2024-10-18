package com.example.demo.application.exception.user;

public class UserFormInvalidException extends RuntimeException {

    public UserFormInvalidException(String message) {
        super(message);
    }

}
