package com.example.demo.global.error.exception.technology;

import com.example.demo.global.error.exception.ErrorCode;

public class InternalServerException extends TechnicalException {

    private ErrorCode errorCode;

    public InternalServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
        this.errorCode = errorCode;
    }

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
