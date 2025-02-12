package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.BusinessException;

public class ServiceUserConditionsAlreadyExistsException extends BusinessException {

    public ServiceUserConditionsAlreadyExistsException() {
        super(SERVICE_USER_CONDITIONS_ALREADY_EXISTS);
    }
}
