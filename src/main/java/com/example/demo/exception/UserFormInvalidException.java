package com.example.demo.exception;

public class UserFormInvalidException extends RuntimeException {

    public UserFormInvalidException(String message) {
        super(message);
    }

}
