package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ServiceUserConditionNotFoundException extends EntityNotFoundException {

    public ServiceUserConditionNotFoundException() {
        super(SERVICE_USER_CONDITION_NOT_FOUND);
    }

}
