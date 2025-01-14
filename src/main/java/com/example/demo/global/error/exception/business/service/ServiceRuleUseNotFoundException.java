package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ServiceRuleUseNotFoundException extends EntityNotFoundException {

    public ServiceRuleUseNotFoundException() {
        super(SERVICE_RULE_USE_NOT_FOUND);
    }
}
