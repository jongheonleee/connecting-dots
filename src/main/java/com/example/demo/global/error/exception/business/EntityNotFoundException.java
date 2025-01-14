package com.example.demo.global.error.exception.business;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;

public class EntityNotFoundException extends BusinessException{

    private ErrorCode errorCode;

    public EntityNotFoundException(String message) {
        super(message, ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
