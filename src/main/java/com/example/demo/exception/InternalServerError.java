package com.example.demo.exception;

public class InternalServerError extends RuntimeException {

    public InternalServerError(String message) {
        super(message);
    }

}
