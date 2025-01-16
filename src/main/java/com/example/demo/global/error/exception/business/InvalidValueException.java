package com.example.demo.global.error.exception.business;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String message) {
        super(message, INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
