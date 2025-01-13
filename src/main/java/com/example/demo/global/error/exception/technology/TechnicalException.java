package com.example.demo.global.error.exception.technology;

import com.example.demo.global.error.exception.ErrorCode;

public class TechnicalException extends RuntimeException {

    private ErrorCode errorCode;

    public TechnicalException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TechnicalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
