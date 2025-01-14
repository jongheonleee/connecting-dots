package com.example.demo.global.error.exception.business.service;


import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ServiceUserConditionsNotFoundException extends EntityNotFoundException {

    public ServiceUserConditionsNotFoundException() {
        super(SERVICE_USER_CONDITIONS_NOT_FOUND);
    }

}
