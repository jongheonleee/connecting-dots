package com.example.demo.global.error.exception.technology;

import com.example.demo.global.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InternalServerError extends TechnicalException {

    private ErrorCode errorCode;

    public InternalServerError(String message, ErrorCode errorCode) {
        super(message, errorCode);
        this.errorCode = errorCode;
    }

    public InternalServerError(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
