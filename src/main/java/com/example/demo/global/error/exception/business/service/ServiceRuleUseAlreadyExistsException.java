package com.example.demo.global.error.exception.business.service;

import com.example.demo.global.error.exception.business.BusinessException;

import com.example.demo.global.error.exception.ErrorCode;

public class ServiceRuleUseAlreadyExistsException extends BusinessException {


    public ServiceRuleUseAlreadyExistsException() {
        super(ErrorCode.SERVICE_RULE_USE_ALREADY_EXISTS);
    }
}
