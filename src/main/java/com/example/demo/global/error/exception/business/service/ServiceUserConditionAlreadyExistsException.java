package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.SERVICE_USER_CONDITION_ALREADY_EXISTS;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.BusinessException;

public class ServiceUserConditionAlreadyExistsException extends BusinessException {

    public ServiceUserConditionAlreadyExistsException() {
        super(SERVICE_USER_CONDITION_ALREADY_EXISTS);
    }
}
