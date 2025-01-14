package com.example.demo.global.error.exception.technology.network;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.technology.InternalServerError;

public class RetryFailedException extends InternalServerError {

    private ErrorCode errorCode;

    public RetryFailedException(String message) {
        super(message, INTERNAL_SERVER_ERROR);
    }

    public RetryFailedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
        this.errorCode = errorCode;
    }


    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
