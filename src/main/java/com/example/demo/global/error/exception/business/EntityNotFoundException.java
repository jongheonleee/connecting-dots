package com.example.demo.global.error.exception.business;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;

public class EntityNotFoundException extends BusinessException{

    public EntityNotFoundException(String message) {
        super(message, ENTITY_NOT_FOUND);
    }

}
