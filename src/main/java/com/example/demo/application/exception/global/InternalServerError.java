package com.example.demo.application.exception.global;

public class InternalServerError extends RuntimeException {

    public InternalServerError(String message) {
        super(message);
    }

}
