package com.example.demo.application.exception.global;

public class RetryFailedException extends RuntimeException {

    public RetryFailedException(String message) {
        super(message);
    }
}
