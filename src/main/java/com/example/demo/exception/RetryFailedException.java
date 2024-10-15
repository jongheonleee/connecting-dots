package com.example.demo.exception;

public class RetryFailedException extends RuntimeException {
    public RetryFailedException(String message) {
        super(message);
    }

}
